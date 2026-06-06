// ============================================================
// File: DocumentParcel.java
// Package: com.cdms.model
// Description: Kiện hàng loại Tài liệu, kế thừa từ Parcel.
//              Phí giao cố định 15.000 VND, không phụ thuộc cân.
// ============================================================
package com.cdms.model;

public class DocumentParcel extends Parcel {

    /** Mức phí cố định cho kiện hàng loại tài liệu (VND) */
    private static final double DOCUMENT_FIXED_FEE = 15000.0;

    // --- Constructors ---

    /** Constructor rỗng — Gson cần để deserialize từ JSON. */
    public DocumentParcel() {
        super();
        setType("Document");
    }

    /** Constructor đầy đủ. */
    public DocumentParcel(String id, String senderId, String receiverName,
                          String receiverPhone, String pickupAddress,
                          String deliveryAddress, double weight) {
        super(id, senderId, receiverName, receiverPhone,
              pickupAddress, deliveryAddress, weight, "Document");
    }

    // --- Tính phí (Đa hình) ---

    /** Phí cố định 15.000 VND, không phụ thuộc cân nặng. */
    @Override
    public double calculateFee() {
        return DOCUMENT_FIXED_FEE;
    }
}
