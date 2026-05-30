// ============================================================
// File: ParcelService.java
// Package: com.cdms.service
// ============================================================
package com.cdms.service;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DocumentParcel;
import com.cdms.model.GoodsParcel;
import com.cdms.model.Parcel;

public class ParcelService {

    public Parcel createParcel(String id, String senderId, String receiverName,
                               String receiverPhone, String pickupAddress,
                               String deliveryAddress, double weight, String type) {

        // 1. Kiểm tra khách hàng tồn tại
        boolean isCustomerExist = JSONDataManager.customers.stream()
                .anyMatch(c -> c.getId().equalsIgnoreCase(senderId));

        if (!isCustomerExist) {
            throw new IllegalArgumentException("ID người gửi không tồn tại trong hệ thống!");
        }

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã kiện hàng không được để trống!");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Trọng lượng phải lớn hơn 0!");
        }

        // 2. Tạo kiện theo loại
        Parcel newParcel;
        String typeUpper = type != null ? type.trim().toUpperCase() : "";

        if ("DOCUMENT".equals(typeUpper)) {
            newParcel = new DocumentParcel(id, senderId, receiverName, receiverPhone,
                    pickupAddress, deliveryAddress, weight);
        } else if ("GOODS".equals(typeUpper)) {
            newParcel = new GoodsParcel(id, senderId, receiverName, receiverPhone,
                    pickupAddress, deliveryAddress, weight);
        } else {
            throw new IllegalArgumentException("Loại kiện hàng không hợp lệ! Chỉ chấp nhận 'Document' hoặc 'Goods'.");
        }

        JSONDataManager.parcels.add(newParcel);
        JSONDataManager.saveAllData();

        return newParcel;
    }
}