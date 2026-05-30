// ============================================================
// File: TrackingService.java
// Package: com.cdms.service
// Description: Lớp xử lý nghiệp vụ theo dõi hành trình đơn hàng
//              và điều phối giao hàng (Tĩnh, sử dụng Repository)
// Phân công: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
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

public class TrackingService {

    // Private constructor để ngăn khởi tạo đối tượng (Static Utility Class)
    private TrackingService() {
    }

    /**
     * B10: Phân công đơn giao hàng cho nhân viên
     */
    public static void assignStaff(String orderId, String staffId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên giao hàng không được để trống!");
        }

        DeliveryOrder order = DeliveryOrderRepository.findById(orderId.trim());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId);
        }

        // BR: Không cho phân công nếu đơn hoàn thành/hủy
        if ("Delivered".equalsIgnoreCase(order.getStatus()) || "Cancelled".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Không thể phân công cho đơn hàng đã hoàn thành hoặc đã hủy!");
        }

        // BR: Không phân công nếu đã phân công rồi
        if (order.getStaffId() != null && !order.getStaffId().trim().isEmpty()) {
            throw new IllegalStateException("Đơn hàng này đã được phân công trước đó!");
        }

        DeliveryStaff staff = DeliveryStaffRepository.findById(staffId.trim());
        if (staff == null) {
            throw new IllegalArgumentException("Không tìm thấy shipper với mã: " + staffId);
        }

        if (!"Active".equalsIgnoreCase(staff.getStatus())) {
            throw new IllegalStateException("Shipper này hiện đang không hoạt động (Trạng thái khác Active)!");
        }

        order.setStaffId(staff.getId());
        order.setStatus("Assigned");

        // Cập nhật trạng thái kiện hàng tương ứng qua Repository
        Parcel parcel = ParcelRepository.findById(order.getParcelId());
        if (parcel != null) {
            parcel.setStatus("Assigned");
        }

        DeliveryOrderRepository.update(order);
    }

    /**
     * B11: Xem đơn đã giao của một Shipper
     */
    public static List<DeliveryOrder> getDeliveredOrdersByStaff(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống!");
        }
        
        // Kiểm tra xem nhân viên tồn tại
        if (DeliveryStaffRepository.findById(staffId.trim()) == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên với mã: " + staffId);
        }

        return DeliveryOrderRepository.findByStaffId(staffId.trim()).stream()
                .filter(o -> "Delivered".equalsIgnoreCase(o.getStatus()))
                .toList();
    }

    /**
     * B12: Cập nhật trạng thái đơn hàng (Có chuyển tiếp trạng thái hợp lệ, cập nhật Delivered count, lưu file)
     */
    public static DeliveryOrder updateStatus(String orderId, String newStatus) {
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

        String currentStatus = order.getStatus();
        // Định dạng trạng thái mới để đồng nhất Mixed Case
        String formattedNewStatus = OrderService.formatStatus(newStatus);

        // Kiểm tra chuyển tiếp trạng thái hợp lệ (Luật chuyển tiếp trạng thái)
        boolean validTransition = false;
        
        if ("Pending".equalsIgnoreCase(currentStatus) && "Assigned".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Pending".equalsIgnoreCase(currentStatus) && "Picked Up".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Assigned".equalsIgnoreCase(currentStatus) && "Picked Up".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Picked Up".equalsIgnoreCase(currentStatus) && "In Transit".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Pending".equalsIgnoreCase(currentStatus) && "In Transit".equalsIgnoreCase(formattedNewStatus)) {
            // Trường hợp đi thẳng In Transit nếu bỏ qua Picked Up
            validTransition = true;
        } else if ("Assigned".equalsIgnoreCase(currentStatus) && "In Transit".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("In Transit".equalsIgnoreCase(currentStatus) && "Delivered".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
            
            // Tăng số lượng đơn đã giao thành công của Shipper qua Repository
            if (order.getStaffId() != null) {
                DeliveryStaff staff = DeliveryStaffRepository.findById(order.getStaffId());
                if (staff != null) {
                    staff.setDeliveredOrdersCount(staff.getDeliveredOrdersCount() + 1);
                    DeliveryStaffRepository.update(staff);
                }
            }
        } else if ("In Transit".equalsIgnoreCase(currentStatus) && "Failed".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        }

        if (!validTransition) {
            throw new IllegalStateException("Quy trình chuyển tiếp trạng thái không hợp lệ! Không thể chuyển từ '" 
                    + currentStatus + "' sang '" + formattedNewStatus + "'.");
        }

        order.setStatus(formattedNewStatus);

        // Cập nhật trạng thái kiện hàng tương ứng qua Repository
        Parcel parcel = ParcelRepository.findById(order.getParcelId());
        if (parcel != null) {
            parcel.setStatus(formattedNewStatus);
        }

        DeliveryOrderRepository.update(order);
        return order;
    }

    /**
     * B13: Xem danh sách các đơn hàng đang vận chuyển (In Transit)
     */
    public static List<DeliveryOrder> getInTransitOrders() {
        return DeliveryOrderRepository.findByStatus("In Transit");
    }

    /**
     * B14: Xem danh sách các đơn giao hàng thất bại (Failed)
     */
    public static List<DeliveryOrder> getFailedOrders() {
        return DeliveryOrderRepository.findByStatus("Failed");
    }

    /**
     * Xem các đơn hàng được phân công cho Shipper (Assigned)
     */
    public static List<DeliveryOrder> getAssignedOrdersByStaff(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống!");
        }
        return DeliveryOrderRepository.findByStaffId(staffId.trim()).stream()
                .filter(o -> "Assigned".equalsIgnoreCase(o.getStatus()))
                .toList();
    }

    /**
     * Cập nhật ngày lấy hàng
     */
    public static void updatePickupDate(String orderId, LocalDate pickupDate) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }
        if (pickupDate == null) {
            throw new IllegalArgumentException("Ngày lấy hàng không được để trống!");
        }

        DeliveryOrder order = DeliveryOrderRepository.findById(orderId.trim());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId);
        }

        // BR: Không cập nhật nếu hoàn thành/hủy
        if ("Delivered".equalsIgnoreCase(order.getStatus()) || "Cancelled".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Không thể cập nhật ngày lấy hàng cho đơn hàng đã hoàn thành hoặc đã hủy!");
        }

        order.setPickupDate(pickupDate);
        DeliveryOrderRepository.update(order);
    }

    /**
     * Cập nhật ngày giao hàng thực tế
     */
    public static void updateDeliveryDate(String orderId, LocalDate deliveryDate) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }
        if (deliveryDate == null) {
            throw new IllegalArgumentException("Ngày giao hàng không được để trống!");
        }

        DeliveryOrder order = DeliveryOrderRepository.findById(orderId.trim());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId);
        }

        order.setDeliveryDate(deliveryDate);
        DeliveryOrderRepository.update(order);
    }

    /**
     * B15: Thêm ghi chú hành trình (Tracking note)
     */
    public static void addTrackingNote(String orderId, String note) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }
        if (note == null || note.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung ghi chú không được để trống!");
        }

        DeliveryOrder order = DeliveryOrderRepository.findById(orderId.trim());
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderId);
        }

        order.addNote(note.trim());
        DeliveryOrderRepository.update(order);
    }
}
