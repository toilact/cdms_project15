// ============================================================
// File: TrackingService.java
// Package: com.cdms.service
// Description: Theo dõi hành trình đơn hàng: phân công Shipper,
//              cập nhật trạng thái theo state-machine, ghi chú.
// Phân công: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.service;

import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Parcel;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.DeliveryStaffRepository;
import com.cdms.repository.ParcelRepository;

import java.time.LocalDate;
import java.util.List;

public class TrackingService {

    // Utility class — không cho tạo đối tượng
    private TrackingService() {
    }

    /** B10 — Phân công đơn giao hàng cho một Shipper đang Active. */
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

        // Không phân công đơn đã hoàn thành hoặc đã hủy
        if ("Delivered".equalsIgnoreCase(order.getStatus()) || "Cancelled".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Không thể phân công cho đơn hàng đã hoàn thành hoặc đã hủy!");
        }

        // Không phân công lại nếu đơn đã có Shipper
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

        // Đồng bộ trạng thái bưu kiện theo đơn hàng
        Parcel parcel = ParcelRepository.findById(order.getParcelId());
        if (parcel != null) {
            parcel.setStatus("Assigned");
        }

        DeliveryOrderRepository.update(order);
    }

    /** B11 — Lấy danh sách đơn đã giao thành công của một Shipper. */
    public static List<DeliveryOrder> getDeliveredOrdersByStaff(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống!");
        }

        // Kiểm tra xem nhân viên tồn tại
        if (DeliveryStaffRepository.findById(staffId.trim()) == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên với mã: " + staffId);
        }

        List<DeliveryOrder> result = new java.util.ArrayList<>();
        for (DeliveryOrder o : DeliveryOrderRepository.findByStaffId(staffId.trim())) {
            if ("Delivered".equalsIgnoreCase(o.getStatus())) {
                result.add(o);
            }
        }
        return result;
    }

    /**
     * B12 — Cập nhật trạng thái đơn hàng theo state-machine.
     * Chuyển trạng thái không hợp lệ sẽ ném ngoại lệ.
     * Khi chuyển sang Delivered: tăng deliveredOrdersCount của Shipper.
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
        // Chuẩn hóa trạng thái mới về dạng Mixed Case
        String formattedNewStatus = OrderService.formatStatus(newStatus);

        // Kiểm tra luật chuyển tiếp trạng thái (state-machine)
        boolean validTransition = false;

        if ("Pending".equalsIgnoreCase(currentStatus) && "Assigned".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Pending".equalsIgnoreCase(currentStatus) && "Picked Up".equalsIgnoreCase(formattedNewStatus)) {
            if (order.getPickupDate() == null) {
                throw new IllegalStateException(
                        "Không thể chuyển sang trạng thái Picked Up vì chưa cập nhật ngày nhận thực tế!");
            }
            validTransition = true;
        } else if ("Assigned".equalsIgnoreCase(currentStatus) && "Picked Up".equalsIgnoreCase(formattedNewStatus)) {
            if (order.getPickupDate() == null) {
                throw new IllegalStateException(
                        "Không thể chuyển sang trạng thái Picked Up vì chưa cập nhật ngày nhận thực tế!");
            }
            validTransition = true;
        } else if ("Picked Up".equalsIgnoreCase(currentStatus) && "In Transit".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Pending".equalsIgnoreCase(currentStatus) && "In Transit".equalsIgnoreCase(formattedNewStatus)) {
            if (order.getPickupDate() == null) {
                throw new IllegalStateException(
                        "Không thể chuyển sang trạng thái In Transit vì chưa cập nhật ngày nhận thực tế!");
            }
            validTransition = true;
        } else if ("Assigned".equalsIgnoreCase(currentStatus) && "In Transit".equalsIgnoreCase(formattedNewStatus)) {
            if (order.getPickupDate() == null) {
                throw new IllegalStateException(
                        "Không thể chuyển sang trạng thái In Transit vì chưa cập nhật ngày nhận thực tế!");
            }
            validTransition = true;
        } else if ("In Transit".equalsIgnoreCase(currentStatus) && "Delivered".equalsIgnoreCase(formattedNewStatus)) {
            if (order.getDeliveryDate() == null) {
                throw new IllegalStateException(
                        "Không thể chuyển sang trạng thái Delivered vì chưa có ngày giao thực tế từ Shipper!");
            }
            validTransition = true;

            // Tăng số đơn đã giao thành công của Shipper
            if (order.getStaffId() != null) {
                DeliveryStaff staff = DeliveryStaffRepository.findById(order.getStaffId());
                if (staff != null) {
                    staff.setDeliveredOrdersCount(staff.getDeliveredOrdersCount() + 1);
                    DeliveryStaffRepository.update(staff);
                }
            }
        } else if ("In Transit".equalsIgnoreCase(currentStatus) && "Failed".equalsIgnoreCase(formattedNewStatus)) {
            validTransition = true;
        } else if ("Cancelled".equalsIgnoreCase(formattedNewStatus)) {
            if ("Delivered".equalsIgnoreCase(currentStatus)) {
                throw new IllegalStateException("Không thể hủy đơn hàng đã giao thành công!");
            }
            if ("Cancelled".equalsIgnoreCase(currentStatus)) {
                throw new IllegalStateException("Đơn hàng này đã được hủy trước đó!");
            }
            if ("Failed".equalsIgnoreCase(currentStatus)) {
                throw new IllegalStateException("Không thể hủy đơn hàng đã giao thất bại!");
            }
            validTransition = true;
        }

        if (!validTransition) {
            throw new IllegalStateException("Quy trình chuyển tiếp trạng thái không hợp lệ! Không thể chuyển từ '"
                    + currentStatus + "' sang '" + formattedNewStatus + "'.");
        }

        order.setStatus(formattedNewStatus);

        // Đồng bộ trạng thái bưu kiện: nếu hủy đơn thì bưu kiện về lại "Pending"
        Parcel parcel = ParcelRepository.findById(order.getParcelId());
        if (parcel != null) {
            if ("Cancelled".equalsIgnoreCase(formattedNewStatus)) {
                parcel.setStatus("Pending");
            } else {
                parcel.setStatus(formattedNewStatus);
            }
            ParcelRepository.update(parcel);
        }

        DeliveryOrderRepository.update(order);
        return order;
    }

    /** B13 — Lấy danh sách đơn đang vận chuyển (trạng thái "In Transit"). */
    public static List<DeliveryOrder> getInTransitOrders() {
        return DeliveryOrderRepository.findByStatus("In Transit");
    }

    /** B14 — Lấy danh sách đơn giao thất bại (Failed) hoặc đã hủy (Cancelled). */
    public static List<DeliveryOrder> getFailedAndCancelledOrders() {
        List<DeliveryOrder> result = new java.util.ArrayList<>();
        for (DeliveryOrder o : DeliveryOrderRepository.findAll()) {
            if ("Failed".equalsIgnoreCase(o.getStatus()) || "Cancelled".equalsIgnoreCase(o.getStatus())) {
                result.add(o);
            }
        }
        return result;
    }

    /** Lấy danh sách đơn đang ở trạng thái "Assigned" của một Shipper. */
    public static List<DeliveryOrder> getAssignedOrdersByStaff(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống!");
        }
        List<DeliveryOrder> result = new java.util.ArrayList<>();
        for (DeliveryOrder o : DeliveryOrderRepository.findByStaffId(staffId.trim())) {
            if ("Assigned".equalsIgnoreCase(o.getStatus())) {
                result.add(o);
            }
        }
        return result;
    }

    /** Cập nhật ngày Shipper thực tế lấy hàng. Phải >= ngày tạo đơn và <= ngày giao. */
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

        // Không cập nhật nếu đơn đã hoàn thành hoặc đã hủy
        if ("Delivered".equalsIgnoreCase(order.getStatus()) || "Cancelled".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Không thể cập nhật ngày lấy hàng cho đơn hàng đã hoàn thành hoặc đã hủy!");
        }

        // Không cho nhập ngày ở tương lai
        if (pickupDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày lấy hàng thực tế không được vượt quá ngày hiện tại!");
        }

        // Ngày lấy hàng phải >= ngày tạo đơn
        if (pickupDate.isBefore(order.getOrderDate())) {
            throw new IllegalArgumentException(
                    "Ngày lấy hàng thực tế không được trước ngày tạo đơn (" + order.getOrderDate() + ")!");
        }

        // Ngày lấy hàng không được sau ngày giao (nếu đã có)
        if (order.getDeliveryDate() != null && pickupDate.isAfter(order.getDeliveryDate())) {
            throw new IllegalArgumentException(
                    "Ngày lấy hàng thực tế không được sau ngày giao hàng thực tế (" + order.getDeliveryDate() + ")!");
        }

        order.setPickupDate(pickupDate);
        DeliveryOrderRepository.update(order);
    }

    /** Cập nhật ngày Shipper thực tế giao hàng. Phải >= ngày tạo đơn và >= ngày lấy hàng. */
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

        // Không cập nhật nếu đơn đã hoàn thành hoặc đã hủy
        if ("Delivered".equalsIgnoreCase(order.getStatus()) || "Cancelled".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Không thể cập nhật ngày giao hàng cho đơn hàng đã hoàn thành hoặc đã hủy!");
        }

        // Chặn ngày giao ở tương lai
        if (deliveryDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày giao hàng thực tế không được vượt quá ngày hiện tại!");
        }

        // Ngày giao hàng phải >= ngày tạo đơn
        if (deliveryDate.isBefore(order.getOrderDate())) {
            throw new IllegalArgumentException(
                    "Ngày giao hàng thực tế không được trước ngày tạo đơn (" + order.getOrderDate() + ")!");
        }

        // Ngày giao hàng không được trước ngày lấy hàng (nếu đã có)
        if (order.getPickupDate() != null && deliveryDate.isBefore(order.getPickupDate())) {
            throw new IllegalArgumentException(
                    "Ngày giao hàng thực tế không được trước ngày lấy hàng thực tế (" + order.getPickupDate() + ")!");
        }

        order.setDeliveryDate(deliveryDate);
        DeliveryOrderRepository.update(order);
    }

    /** B15 — Thêm ghi chú hành trình vào đơn hàng. */
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
