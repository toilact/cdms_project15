
// ============================================================
// File: ParcelService.java
// Package: com.cdms.service
// Description: Lớp xử lý nghiệp vụ liên quan đến Kiện hàng (Parcel).
//              Kiểm tra tính hợp lệ của khách hàng và tạo đối tượng con.
// ============================================================
package com.cdms.service;

import com.cdms.core.JSONDataManager;
import com.cdms.model.Parcel;
import com.cdms.model.DocumentParcel;
import com.cdms.model.GoodsParcel;

public class ParcelService {

    private final JSONDataManager dataManager;

    public ParcelService(JSONDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Parcel createParcel(String id, String senderId, String receiverName,
                               String receiverPhone, String pickupAddress,
                               String deliveryAddress, double weight, String type) {

        // 1. Kiểm tra ID người gửi có tồn tại
        boolean isCustomerExist = JSONDataManager.customers.stream()
                .anyMatch(customer -> customer.getId().equalsIgnoreCase(senderId));

        if (!isCustomerExist) {
            System.out.println("❌ Lỗi: ID người gửi (Customer ID) không tồn tại trong hệ thống!");
            return null;
        }

        // 2. Kiểm tra đầu vào cơ bản
        if (id == null || id.trim().isEmpty()) {
            System.out.println("❌ Lỗi: Mã kiện hàng không được để trống!");
            return null;
        }
        if (weight <= 0) {
            System.out.println("❌ Lỗi: Trọng lượng phải lớn hơn 0!");
            return null;
        }

        // 3. Tạo kiện hàng theo loại (Polymorphism)
        Parcel newParcel;
        String typeUpper = type != null ? type.trim().toUpperCase() : "";

        if ("DOCUMENT".equals(typeUpper)) {
            newParcel = new DocumentParcel(id, senderId, receiverName, receiverPhone,
                    pickupAddress, deliveryAddress, weight);
        } else if ("GOODS".equals(typeUpper)) {
            newParcel = new GoodsParcel(id, senderId, receiverName, receiverPhone,
                    pickupAddress, deliveryAddress, weight);
        } else {
            System.out.println("❌ Lỗi: Loại kiện hàng không hợp lệ! Chỉ chấp nhận 'Document' hoặc 'Goods'.");
            return null;
        }

        // 4. Thêm vào danh sách và lưu
        JSONDataManager.parcels.add(newParcel);
        JSONDataManager.saveAllData();

        System.out.println("✅ Tạo kiện hàng thành công: " + id);
        return newParcel;
    }
}