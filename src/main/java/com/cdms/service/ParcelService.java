// ============================================================
// File: ParcelService.java
// Package: com.cdms.service
// Description: Lớp xử lý nghiệp vụ cho Kiện hàng (Parcel)
//              (Refactored to use Repository Layer)
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
        JSONDataManager.saveAllData();

        return newParcel;
    }
}
