// ============================================================
// File: OrderService.java
// Package: com.cdms.service
// Description: Xử lý nghiệp vụ Đơn giao hàng: tạo đơn từ bưu kiện,
//              cập nhật trạng thái, tìm kiếm, hủy đơn.
// Phân công: Trương Đan Huy (Developer B - Thành viên 3)
// ============================================================
package com.cdms.service;

import com.cdms.model.DeliveryOrder;
import com.cdms.model.Parcel;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.ParcelRepository;

import java.time.LocalDate;
import java.util.List;

public class OrderService {

    // Utility class — không cho tạo đối tượng
    private OrderService() {
    }

    /** Chuẩn hóa tên trạng thái đơn hàng về dạng Mixed Case (vd: "In Transit"). */
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
     * Tạo đơn giao hàng từ một bưu kiện có sẵn.
     * Kiểm tra trùng mã, bưu kiện tồn tại, trạng thái Pending, loại giao và hình thức thanh toán.
     */
    public static DeliveryOrder convertParcelToOrder(String orderId, String parcelId,
                                                     String deliveryType, String paymentTerms) {

        if (orderId == null || orderId.trim().isEmpty() ||
            parcelId == null || parcelId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID và Parcel ID không được để trống!");
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

        // 3. Kiểm tra deliveryType hợp lệ (Ném ngoại lệ thay vì tự gán mặc định)
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

        // 4. Kiểm tra paymentTerms hợp lệ
        if (paymentTerms == null || paymentTerms.trim().isEmpty()) {
            throw new IllegalArgumentException("Hình thức thanh toán không được để trống!");
        }
        String pt = paymentTerms.trim();
        String paymentTermsFormatted;
        if ("Sender Pay".equalsIgnoreCase(pt) || "Người gửi trả".equalsIgnoreCase(pt)) {
            paymentTermsFormatted = "Sender Pay";
        } else if ("Receiver Pay".equalsIgnoreCase(pt) || "Người nhận trả".equalsIgnoreCase(pt) || "COD".equalsIgnoreCase(pt)) {
            paymentTermsFormatted = "Receiver Pay";
        } else {
            throw new IllegalArgumentException("Hình thức thanh toán không hợp lệ! Chỉ chấp nhận 'Sender Pay' hoặc 'Receiver Pay'.");
        }

        // 5. Tạo đơn hàng
        DeliveryOrder newOrder = new DeliveryOrder();
        newOrder.setId(orderId.trim());
        newOrder.setParcelId(parcel.getId());
        newOrder.setStaffId(null); // Chưa phân công shipper
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setExpectedDeliveryDate(LocalDate.now().plusDays("Urgent".equals(typeFormatted) ? 1 : 3));
        newOrder.setDeliveryType(typeFormatted);
        newOrder.setStatus("Pending"); // Trạng thái ban đầu là Pending
        newOrder.setPaymentTerms(paymentTermsFormatted);
        newOrder.addNote("Đơn hàng được tạo từ kiện hàng: " + parcelId);

        // Đánh dấu bưu kiện đã có đơn, tránh tạo đơn trùng
        parcel.setStatus("Pending Order");

        // Lưu đơn mới vào Repository (tự động ghi file JSON)
        DeliveryOrderRepository.add(newOrder);

        return newOrder;
    }

    /** Cập nhật trạng thái đơn hàng, ủy quyền kiểm tra state-machine cho TrackingService. */
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

        // Chuẩn hóa và kiểm tra trạng thái hợp lệ
        String formattedStatus = formatStatus(newStatus);
        
        // Ủy quyền cho TrackingService để xác thực state-machine và cập nhật đồng bộ
        return TrackingService.updateStatus(orderId, formattedStatus);
    }

    /** Lấy chi tiết đơn hàng theo mã, ném ngoại lệ nếu không tìm thấy. */
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

    /** Tìm tất cả đơn hàng liên quan đến một khách hàng (qua các bưu kiện của họ). */
    public static List<DeliveryOrder> searchOrdersByCustomer(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống!");
        }
        
        // Tìm tất cả kiện hàng của khách hàng gửi trước từ Repository
        List<String> parcelIds = new java.util.ArrayList<>();
        for (Parcel p : ParcelRepository.findAll()) {
            if (p.getSenderId().equalsIgnoreCase(customerId.trim())) {
                parcelIds.add(p.getId());
            }
        }

        // Tìm tất cả đơn hàng chứa những kiện hàng đó
        List<DeliveryOrder> result = new java.util.ArrayList<>();
        for (DeliveryOrder o : DeliveryOrderRepository.findAll()) {
            if (parcelIds.contains(o.getParcelId())) {
                result.add(o);
            }
        }
        return result;
    }

    /** Hủy đơn hàng — thực chất là cập nhật trạng thái sang "Cancelled". */
    public static DeliveryOrder cancelOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không được để trống!");
        }

        // Hủy đơn = cập nhật trạng thái "Cancelled" kèm kiểm tra logic
        return TrackingService.updateStatus(orderId, "Cancelled");
    }
}
