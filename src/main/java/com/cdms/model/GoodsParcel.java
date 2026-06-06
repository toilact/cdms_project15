// ============================================================
// File: GoodsParcel.java
// Package: com.cdms.model
// Description: Kiện hàng loại Hàng hóa, kế thừa từ Parcel.
//              Phí giao = cân nặng (kg) × 10.000 VND/kg.
// ============================================================
package com.cdms.model;

public class GoodsParcel extends Parcel {

    /** Đơn giá cơ bản trên mỗi kg (VND/kg) */
    private static final double RATE_PER_KG = 10000.0;

    // --- Constructors ---

    /** Constructor rỗng — Gson cần để deserialize từ JSON. */
    public GoodsParcel() {
        super();
        setType("Goods");
    }

    /** Constructor đầy đủ. */
    public GoodsParcel(String id, String senderId, String receiverName,
                       String receiverPhone, String pickupAddress,
                       String deliveryAddress, double weight) {
        super(id, senderId, receiverName, receiverPhone,
              pickupAddress, deliveryAddress, weight, "Goods");
    }

    // --- Tính phí (Đa hình) ---

    /** Phí = cân nặng (kg) × 10.000 VND/kg. */
    @Override
    public double calculateFee() {
        return getWeight() * RATE_PER_KG;
    }
}
