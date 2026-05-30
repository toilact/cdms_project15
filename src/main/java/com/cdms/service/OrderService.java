// ============================================================
// File: OrderService.java
// Package: com.cdms.service
// Description: Lớp xử lý nghiệp vụ cho Đơn hàng (DeliveryOrder)
//              (Refactored to use Repository Layer)
// Phân công: Trương Đan Huy (Developer B - Thành viên 3)
// ============================================================
package com.cdms.service;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Parcel;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.DeliveryStaffRepository;
import com.cdms.repository.ParcelRepository;

import java.time.LocalDate;
import java.util.List;

public class OrderService {

    // Private constructor để ngăn khởi tạo đối tượng (Static Utility Class)
    private OrderService() {
    }

    /**
     * Định dạng trạng thái đơn hàng về dạng Mixed Case chuẩn của hệ thống
     */
    public static String formatStatus(String status) {
        if (status == null) return null;
        String s = status.trim();
        if ("ASSIGNED".equalsIgnoreCase(s)) return "Assigned";
        if ("PICKED_UP".equalsIgnoreCase(s) || "PICKED UP".equalsIgnoreCase(s)) return "Picked Up";
        if ("IN_TRANSIT".equalsIgnoreCase(s) || "IN TRANSIT".equalsIgnoreCase(s)) return "In Transit";
        if ("DELIVERED".equalsIgnoreCase(s)) return "Delivered";
        if ("CANCELLED".equalsIgnoreCase(s)) return "Cancelled";
        if ("FAILED".equalsIgnoreCase(s)) return "Failed";
        throw new IllegalArgumentException("Trạng thái không hợp lệ! (Chỉ chấp nhận: Assigned, Picked Up, In Transit, Delivered, Cancelled, Failed)");
    }

    /**
     * Chuyển kiện hàng thành đơn hàng
     */
    public static DeliveryOrder convertParcelToOrder(String orderId, String parcelId,
                                                     String staffId, String deliveryType) {

        if (orderId == null || orderId.trim().isEmpty() ||
            parcelId == null || parcelId.trim().isEmpty() ||
            staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID, Parcel ID và Staff ID không được để trống!");
        }

        // 1. Kiểm tra trùng lặp Order ID (Sử dụng Repository Layer)
        if (DeliveryOrderRepository.existsById(orderId.trim())) {
            throw new IllegalArgumentException("Mã đơn giao hàng đã tồn tại trong hệ thống!");
        }

        // 2. Kiểm tra kiện hàng (Sử dụng Repository Layer)
        Parcel parcel = ParcelRepository.findById(parcelId.trim());
        if (parcel == null) {
            throw new IllegalArgumentException("Không tìm thấy kiện hàng với mã: " + parcelId);
        }

        if (!"Pending".equalsIgnoreCase(parcel.getStatus())) {
            throw new IllegalStateException("Kiện hàng này đã được xử lý hoặc đang trong đơn hàng khác!");
        }

        // 3. Kiểm tra shipper tồn tại và hoạt động tích cực (Active) (Sử dụng Repository Layer)
        DeliveryStaff staff = DeliveryStaffRepository.findById(staffId.trim());
        if (staff == null) {
            throw new IllegalArgumentException("Mã shipper không tồn tại trong hệ thống!");
        }

        if (!"Active".equalsIgnoreCase(staff.getStatus())) {
            throw new IllegalStateException("Shipper này hiện không hoạt động (không phải trạng thái Active)!");
        }

        // 4. Kiểm tra deliveryType hợp lệ (Ném ngoại lệ thay vì tự gán mặc định)
        if (deliveryType == null || deliveryType.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại giao hàng không được để trống!");
        }
        String type = deliveryType.trim();
        String typeFormatted;
        if ("Standard".equalsIgnoreCase(type)) {
            typeFormatted = "Standard";
        } else if ("Urgent".equalsIgnoreCase(type)) {
            typeFormatted = "Urgent";
        } else {
            throw new IllegalArgumentException("Loại giao hàng không hợp lệ! Chỉ chấp nhận 'Standard' hoặc 'Urgent'.");
        }

        // 5. Tạo đơn hàng
        DeliveryOrder newOrder = new DeliveryOrder();
        newOrder.setId(orderId.trim());
        newOrder.setParcelId(parcel.getId());
        newOrder.setStaffId(staff.getId());
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setExpectedDeliveryDate(LocalDate.now().plusDays("Urgent".equals(typeFormatted) ? 1 : 3));
        newOrder.setDeliveryType(typeFormatted);
        newOrder.setStatus("Assigned");
        newOrder.addNote("Đơn hàng được tạo từ kiện hàng: " + parcelId);

        // Cập nhật trạng thái kiện sang Assigned
        parcel.setStatus("Assigned");

        // Lưu dữ liệu qua Repository
        DeliveryOrderRepository.add(newOrder);
        return newOrder;
    }

    /**
     * Cập nhật trạng thái đơn hàng (Sử dụng Repository Layer)
     */
    public static DeliveryOrder updateOrderStatus(String orderId, String newStatus) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái mới không được để trống!");
        }

        DeliveryOrder order = DeliveryOrderRepository.findById(orderId.trim());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId);
        }

        // Format và kiểm tra trạng thái hợp lệ
        String formattedStatus = formatStatus(newStatus);
        
        // Delegate tới TrackingService để sử dụng State Machine validation và cập nhật đồng bộ
        return TrackingService.updateStatus(orderId, formattedStatus);
    }

    /**
     * Xem chi tiết đơn hàng (Sử dụng Repository Layer)
     */
    public static DeliveryOrder getOrderDetail(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }
        DeliveryOrder order = DeliveryOrderRepository.findById(orderId.trim());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId);
        }
        return order;
    }

    /**
     * Tìm kiếm đơn hàng theo khách hàng (Sử dụng Repository Layer)
     */
    public static List<DeliveryOrder> searchOrdersByCustomer(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống!");
        }
        
        // Tìm tất cả kiện hàng của khách hàng gửi trước từ Repository
        List<String> parcelIds = ParcelRepository.findAll().stream()
                .filter(p -> p.getSenderId().equalsIgnoreCase(customerId.trim()))
                .map(Parcel::getId)
                .toList();

        return DeliveryOrderRepository.findAll().stream()
                .filter(o -> parcelIds.contains(o.getParcelId()))
                .toList();
    }

    /**
     * Hủy đơn hàng (Sử dụng Repository Layer)
     */
    public static DeliveryOrder cancelOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }

        // Hủy đơn hàng thực chất là cập nhật trạng thái sang "Cancelled" đi kèm với các kiểm tra logic
        return TrackingService.updateStatus(orderId, "Cancelled");
    }
}
