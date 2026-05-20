// ============================================================
// File: Parcel.java
// Package: com.cdms.model
// Description: Lớp cha TRỪU TƯỢNG đại diện cho Kiện hàng.
//              Áp dụng Inheritance & Polymorphism qua hàm
//              abstract calculateFee().
// ============================================================
package com.cdms.model;

public abstract class Parcel {

    // ===== PRIVATE FIELDS (Encapsulation) =====
    private String id;
    private String senderId;
    private String receiverName;
    private String receiverPhone;
    private String pickupAddress;
    private String deliveryAddress;
    private double weight;
    private String type;       // "Document" hoặc "Goods"
    private String status;     // Mặc định "Pending"

    // ===== CONSTRUCTORS =====

    /** Constructor không tham số (cần cho Gson deserialization) */
    public Parcel() {
        this.status = "Pending";
    }

    /** Constructor đầy đủ tham số */
    public Parcel(String id, String senderId, String receiverName,
                  String receiverPhone, String pickupAddress,
                  String deliveryAddress, double weight, String type) {
        this.id = id;
        this.senderId = senderId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.weight = weight;
        this.type = type;
        this.status = "Pending";
    }

    // ===== PHƯƠNG THỨC TRỪU TƯỢNG (Polymorphism) =====

    /**
     * Tính phí giao hàng. Mỗi lớp con (DocumentParcel, GoodsParcel)
     * sẽ override phương thức này với công thức tính phí riêng.
     * @return Phí giao hàng (VND)
     */
    public abstract double calculateFee();

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public double getWeight() {
        return weight;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    // ===== SETTERS =====

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ===== toString() =====

    @Override
    public String toString() {
        return String.format("| %-10s | %-10s | %-15s | %-10s | %8.1f kg | %-10s | Phí: %,.0f VND |",
                id, senderId, receiverName, type, weight, status, calculateFee());
    }
}
