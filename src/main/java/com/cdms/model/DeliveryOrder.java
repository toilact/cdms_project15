// ============================================================
// File: DeliveryOrder.java
// Package: com.cdms.model
// Description: Thực thể Đơn giao hàng — liên kết một Parcel với
//              một Shipper, theo dõi trạng thái và ghi chú hành trình.
// ============================================================
package com.cdms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryOrder {

    // --- Thuộc tính ---
    private String id;
    private String parcelId;
    private String staffId;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private LocalDate pickupDate;
    private LocalDate deliveryDate;
    private String deliveryType;     // "Standard" hoặc "Urgent"
    private String status;           // "Pending", "Assigned", "Picked Up", "In Transit", "Delivered", "Failed", "Cancelled"
    private List<String> notes;      // Danh sách ghi chú giao hàng
    private String paymentTerms;     // "Sender Pay" (Người gửi trả) hoặc "Receiver Pay" (Người nhận trả - COD)

    // --- Constructors ---

    /** Constructor rỗng — Gson cần để deserialize từ JSON. */
    public DeliveryOrder() {
        this.status = "Pending";
        this.notes = new ArrayList<>();
    }

    /** Constructor đầy đủ (không có paymentTerms — tự đặt null). */
    public DeliveryOrder(String id, String parcelId, String staffId,
                         LocalDate orderDate, LocalDate expectedDeliveryDate,
                         LocalDate pickupDate, LocalDate deliveryDate,
                         String deliveryType, String status, List<String> notes) {
        this(id, parcelId, staffId, null, orderDate, expectedDeliveryDate, pickupDate, deliveryDate, deliveryType, status, notes);
    }

    /** Constructor đầy đủ bao gồm cả paymentTerms. */
    public DeliveryOrder(String id, String parcelId, String staffId, String paymentTerms,
                         LocalDate orderDate, LocalDate expectedDeliveryDate,
                         LocalDate pickupDate, LocalDate deliveryDate,
                         String deliveryType, String status, List<String> notes) {
        this.id = id;
        this.parcelId = parcelId;
        this.staffId = staffId;
        this.paymentTerms = paymentTerms;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.pickupDate = pickupDate;
        this.deliveryDate = deliveryDate;
        this.deliveryType = deliveryType;
        this.status = status;
        this.notes = (notes != null) ? notes : new ArrayList<>();
    }

    // --- Getters ---

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public String getId() {
        return id;
    }

    public String getParcelId() {
        return parcelId;
    }

    public String getStaffId() {
        return staffId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getNotes() {
        return notes;
    }

    // --- Setters ---

    public void setId(String id) {
        this.id = id;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    // --- Helper ---

    /** Thêm một ghi chú hành trình vào danh sách, bỏ qua nếu rỗng. */
    public void addNote(String note) {
        if (note != null && !note.trim().isEmpty()) {
            this.notes.add(note);
        }
    }

    // --- toString ---

    @Override
    public String toString() {
        return String.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %-12s |",
                id, parcelId,
                (staffId != null ? staffId : "Chưa phân"),
                deliveryType, status,
                (orderDate != null ? orderDate.toString() : "N/A"));
    }
}
