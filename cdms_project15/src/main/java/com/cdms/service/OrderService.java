// ============================================================
// File: OrderService.java
// Package: com.cdms.service
// ============================================================
package com.cdms.service;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Parcel;

import java.time.LocalDate;
import java.util.List;

public class OrderService {

    /**
     * Chuyển kiện hàng thành đơn hàng
     */
    public DeliveryOrder convertParcelToOrder(String orderId, String parcelId,
                                              String staffId, String deliveryType) {

        if (orderId == null || orderId.trim().isEmpty() ||
            parcelId == null || parcelId.trim().isEmpty() ||
            staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID, Parcel ID và Staff ID không được để trống!");
        }

        // Kiểm tra kiện hàng
        Parcel parcel = JSONDataManager.parcels.stream()
                .filter(p -> p.getId().equalsIgnoreCase(parcelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kiện hàng!"));

        if (!"Pending".equalsIgnoreCase(parcel.getStatus())) {
            throw new IllegalStateException("Kiện hàng này đã được xử lý hoặc đang trong đơn hàng khác!");
        }

        // Kiểm tra shipper tồn tại và đang Active
        DeliveryStaff staff = JSONDataManager.staffs.stream()
                .filter(s -> s.getId().equalsIgnoreCase(staffId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mã shipper không tồn tại!"));

        if (!"Active".equalsIgnoreCase(staff.getStatus())) {
            throw new IllegalStateException("Shipper này hiện không hoạt động (không phải trạng thái Active)!");
        }

        // Kiểm tra deliveryType hợp lệ
        String type = deliveryType != null ? deliveryType.trim() : "Standard";
        if (!"Standard".equalsIgnoreCase(type) && !"Urgent".equalsIgnoreCase(type)) {
            type = "Standard";
        }

        // Tạo đơn hàng
        DeliveryOrder newOrder = new DeliveryOrder();
        newOrder.setId(orderId);
        newOrder.setParcelId(parcel.getId());
        newOrder.setStaffId(staffId);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setExpectedDeliveryDate(LocalDate.now().plusDays("Urgent".equalsIgnoreCase(type) ? 1 : 3));
        newOrder.setDeliveryType(type);
        newOrder.setStatus("Assigned");
        newOrder.addNote("Đơn hàng được tạo từ kiện hàng: " + parcelId);

        // Cập nhật trạng thái kiện
        parcel.setStatus("Assigned");

        // Lưu dữ liệu
        JSONDataManager.orders.add(newOrder);
        JSONDataManager.saveAllData();
        return newOrder;
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public DeliveryOrder updateOrderStatus(String orderId, String newStatus) {
        DeliveryOrder order = JSONDataManager.orders.stream()
                .filter(o -> o.getId().equalsIgnoreCase(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId));

        // Kiểm tra trạng thái hợp lệ
        String statusUpper = newStatus.trim().toUpperCase();
        if (!List.of("ASSIGNED", "IN_TRANSIT", "DELIVERED", "CANCELLED", "FAILED").contains(statusUpper)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ! (Assigned, In Transit, Delivered, Cancelled, Failed)");
        }

        order.setStatus(statusUpper);
        JSONDataManager.saveAllData();

        return order;
    }

    /**
     * Xem chi tiết đơn hàng
     */
    public DeliveryOrder getOrderDetail(String orderId) {
        return JSONDataManager.orders.stream()
                .filter(o -> o.getId().equalsIgnoreCase(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId));
    }

    /**
     * Tìm kiếm đơn hàng theo khách hàng
     */
    public List<DeliveryOrder> searchOrdersByCustomer(String customerId) {
        // Tìm tất cả kiện hàng của khách hàng đó trước
        List<String> parcelIds = JSONDataManager.parcels.stream()
                .filter(p -> p.getSenderId().equalsIgnoreCase(customerId))
                .map(Parcel::getId)
                .toList();

        return JSONDataManager.orders.stream()
                .filter(o -> parcelIds.contains(o.getParcelId()))
                .toList();
    }

    /**
     * Hủy đơn hàng
     */
    public DeliveryOrder cancelOrder(String orderId) {
        DeliveryOrder order = getOrderDetail(orderId); // reuse method above

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Không thể hủy đơn hàng đã giao thành công!");
        }

        order.setStatus("CANCELLED");
        // Có thể cập nhật lại trạng thái kiện hàng về Pending nếu cần
        JSONDataManager.parcels.stream()
                .filter(p -> p.getId().equalsIgnoreCase(order.getParcelId()))
                .findFirst()
                .ifPresent(p -> p.setStatus("Pending"));

        JSONDataManager.saveAllData();
        return order;
    }
}
