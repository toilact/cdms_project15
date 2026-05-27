// ============================================================
// File: OrderService.java
// Package: com.cdms.service
// Description: Lớp xử lý nghiệp vụ chuyển Kiện hàng thành Đơn hàng.
//              Kết nối Khách hàng, Kiện hàng và Shipper với nhau.
// ============================================================

package com.cdms.service;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.Parcel;

import java.time.LocalDate;

public class OrderService {

    private final JSONDataManager dataManager;

    public OrderService(JSONDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public DeliveryOrder convertParcelToOrder(String orderId, String parcelId, String staffId) {

        if (orderId == null || orderId.trim().isEmpty() ||
            parcelId == null || parcelId.trim().isEmpty() ||
            staffId == null || staffId.trim().isEmpty()) {
            
            System.out.println("❌ Lỗi: Order ID, Parcel ID và Staff ID không được để trống!");
            return null;
        }

        // Tìm kiện hàng
        Parcel targetParcel = JSONDataManager.parcels.stream()
                .filter(p -> p.getId() != null && p.getId().equalsIgnoreCase(parcelId))
                .findFirst()
                .orElse(null);

        if (targetParcel == null) {
            System.out.println("❌ Lỗi: Không tìm thấy mã Kiện hàng (Parcel ID) trong hệ thống!");
            return null;
        }

        if (!"Pending".equalsIgnoreCase(targetParcel.getStatus())) {
            System.out.println("❌ Lỗi: Kiện hàng này đã được xử lý hoặc đang trong một đơn hàng khác!");
            return null;
        }

        // Kiểm tra Staff
        boolean isStaffExist = JSONDataManager.staffs.stream()
                .anyMatch(s -> s.getId() != null && s.getId().equalsIgnoreCase(staffId));

        if (!isStaffExist) {
            System.out.println("❌ Lỗi: Mã Staff/Shipper không tồn tại trong hệ thống!");
            return null;
        }

        // Tính phí
        double finalFee = targetParcel.calculateFee();

        // Tạo đơn hàng
        DeliveryOrder newOrder = new DeliveryOrder();
        newOrder.setId(orderId);
        newOrder.setParcelId(targetParcel.getId());
        newOrder.setStaffId(staffId);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setExpectedDeliveryDate(LocalDate.now().plusDays(3));
        newOrder.setDeliveryType("Standard");
        newOrder.setStatus("Assigned");
        newOrder.addNote("Đơn hàng được tạo từ kiện hàng: " + parcelId);

        // Cập nhật trạng thái kiện hàng
        targetParcel.setStatus("Assigned");

        // Lưu dữ liệu
        JSONDataManager.orders.add(newOrder);
        JSONDataManager.saveAllData();

        System.out.println("✅ Chuyển đổi kiện hàng thành đơn hàng thành công: " + orderId);
        return newOrder;
    }
}