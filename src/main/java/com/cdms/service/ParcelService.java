// ============================================================
// File: ParcelService.java
// Package: com.cdms.service
// Description: Xử lý nghiệp vụ tạo và xóa Kiện hàng (Parcel).
//              Kiểm tra ràng buộc trước khi lưu qua Repository.
// Phân công: Trương Đan Huy (Developer B - Thành viên 3)
// ============================================================
package com.cdms.service;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DocumentParcel;
import com.cdms.model.GoodsParcel;
import com.cdms.model.Parcel;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.ParcelRepository;

public class ParcelService {

    // Private constructor để ngăn khởi tạo đối tượng (Static Utility Class)
    private ParcelService() {
    }

    /**
     * Tạo một kiện hàng mới
     */
    public static Parcel createParcel(String id, String senderId, String receiverName,
                                      String receiverPhone, String pickupAddress,
                                      String deliveryAddress, double weight, String type) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã kiện hàng không được để trống!");
        }

        // 1. Kiểm tra trùng lặp Parcel ID (Sử dụng Repository Layer)
        if (ParcelRepository.existsById(id.trim())) {
            throw new IllegalArgumentException("Mã kiện hàng đã tồn tại trong hệ thống!");
        }

        // 2. Kiểm tra khách hàng gửi tồn tại (Sử dụng Repository Layer)
        if (CustomerRepository.findById(senderId.trim()) == null) {
            throw new IllegalArgumentException("ID người gửi không tồn tại trong hệ thống!");
        }

        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người nhận không được để trống!");
        }
        if (receiverPhone == null || receiverPhone.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại người nhận không được để trống!");
        }
        if (pickupAddress == null || pickupAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ lấy hàng không được để trống!");
        }
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ giao hàng không được để trống!");
        }

        if (weight <= 0) {
            throw new IllegalArgumentException("Trọng lượng phải lớn hơn 0!");
        }

        // 3. Tạo kiện theo loại
        Parcel newParcel;
        String typeUpper = type != null ? type.trim().toUpperCase() : "";

        if ("DOCUMENT".equals(typeUpper)) {
            newParcel = new DocumentParcel(id.trim(), senderId.trim(), receiverName.trim(), receiverPhone.trim(),
                    pickupAddress.trim(), deliveryAddress.trim(), weight);
        } else if ("GOODS".equals(typeUpper)) {
            newParcel = new GoodsParcel(id.trim(), senderId.trim(), receiverName.trim(), receiverPhone.trim(),
                    pickupAddress.trim(), deliveryAddress.trim(), weight);
        } else {
            throw new IllegalArgumentException("Loại kiện hàng không hợp lệ! Chỉ chấp nhận 'Document' hoặc 'Goods'.");
        }

        // Mặc định trạng thái là Pending
        newParcel.setStatus("Pending");

        // Lưu qua Repository và đồng bộ
        ParcelRepository.add(newParcel);

        return newParcel;
    }

    /**
     * Xóa bưu kiện theo ID (Có kiểm tra ràng buộc nghiệp vụ)
     */
    public static String deleteParcel(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "❌ Lỗi: Mã bưu kiện không được để trống.";
        }
        final String finalId = id.trim();
        Parcel parcel = ParcelRepository.findById(finalId);
        if (parcel == null) {
            return "❌ Lỗi: Không tìm thấy bưu kiện với mã '" + finalId + "'!";
        }

        // B1: Nếu bưu kiện đã được gán đơn hàng hoặc vận chuyển (trạng thái khác Pending)
        if (!"Pending".equalsIgnoreCase(parcel.getStatus())) {
            return "❌ Lỗi: Không thể xóa bưu kiện vì nó đã được tạo đơn giao hàng (trạng thái hiện tại: " + parcel.getStatus() + ")!";
        }

        // B2: Kiểm tra xem có đơn hàng nào liên kết với kiện hàng này trong dữ liệu hay không
        boolean hasOrder = false;
        for (com.cdms.model.DeliveryOrder o : com.cdms.repository.DeliveryOrderRepository.findAll()) {
            if (finalId.equalsIgnoreCase(o.getParcelId())) {
                hasOrder = true;
                break;
            }
        }
        if (hasOrder) {
            return "❌ Lỗi: Không thể xóa bưu kiện vì đang liên kết với đơn giao hàng trong hệ thống!";
        }

        boolean success = ParcelRepository.delete(id);
        if (success) {
            return "✅ Đã xóa bưu kiện: " + id;
        }
        return "❌ Lỗi: Xóa bưu kiện thất bại.";
    }
}
