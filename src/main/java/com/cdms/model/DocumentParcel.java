// ============================================================
// File: DocumentParcel.java
// Package: com.cdms.model
// Description: Kiện hàng loại Tài liệu. Kế thừa từ Parcel.
//              Override calculateFee() trả về phí cố định.
// ============================================================
package com.cdms.model;

public class DocumentParcel extends Parcel {

    /** Mức phí cố định cho kiện hàng loại tài liệu (VND) */
    private static final double DOCUMENT_FIXED_FEE = 15000.0;

    // ===== CONSTRUCTORS =====

    /** Constructor không tham số */
    public DocumentParcel() {
        super();
        setType("Document");
    }

    /** Constructor đầy đủ tham số */
    public DocumentParcel(String id, String senderId, String receiverName,
                          String receiverPhone, String pickupAddress,
                          String deliveryAddress, double weight) {
        super(id, senderId, receiverName, receiverPhone,
              pickupAddress, deliveryAddress, weight, "Document");
    }

    // ===== POLYMORPHISM: Override calculateFee() =====

    /**
     * Tính phí giao hàng cho kiện tài liệu.
     * Công thức: Phí cố định = 15,000 VND (không phụ thuộc cân nặng).
     * @return 15000.0
     */
    @Override
    public double calculateFee() {
        return DOCUMENT_FIXED_FEE;
    }
}
