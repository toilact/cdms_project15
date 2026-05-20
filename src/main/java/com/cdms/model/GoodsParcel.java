// ============================================================
// File: GoodsParcel.java
// Package: com.cdms.model
// Description: Kiện hàng loại Hàng hóa. Kế thừa từ Parcel.
//              Override calculateFee() tính phí theo cân nặng.
// ============================================================
package com.cdms.model;

public class GoodsParcel extends Parcel {

    /** Đơn giá cơ bản trên mỗi kg (VND/kg) */
    private static final double RATE_PER_KG = 10000.0;

    // ===== CONSTRUCTORS =====

    /** Constructor không tham số */
    public GoodsParcel() {
        super();
        setType("Goods");
    }

    /** Constructor đầy đủ tham số */
    public GoodsParcel(String id, String senderId, String receiverName,
                       String receiverPhone, String pickupAddress,
                       String deliveryAddress, double weight) {
        super(id, senderId, receiverName, receiverPhone,
              pickupAddress, deliveryAddress, weight, "Goods");
    }

    // ===== POLYMORPHISM: Override calculateFee() =====

    /**
     * Tính phí giao hàng cho kiện hàng hóa.
     * Công thức: weight (kg) × 10,000 VND/kg.
     * @return Phí giao hàng tính theo cân nặng
     */
    @Override
    public double calculateFee() {
        return getWeight() * RATE_PER_KG;
    }
}
