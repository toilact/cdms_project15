// ============================================================
// File: DeliveryStaff.java
// Package: com.cdms.model
// Description: Thực thể Nhân viên giao hàng (Shipper) — người
//              được phân công vận chuyển và giao bưu kiện.
// ============================================================
package com.cdms.model;

public class DeliveryStaff {

    // --- Thuộc tính ---
    private String id;
    private String name;
    private String phone;
    private String vehicleType;          // "Motorbike", "Truck", ...
    private String status;               // "Active" hoặc "Inactive"
    private int deliveredOrdersCount;    // Số đơn đã giao thành công

    // --- Constructors ---

    /** Constructor rỗng — Gson cần để deserialize từ JSON. */
    public DeliveryStaff() {
        this.status = "Active";
        this.deliveredOrdersCount = 0;
    }

    /** Constructor đầy đủ. */
    public DeliveryStaff(String id, String name, String phone,
                         String vehicleType, String status,
                         int deliveredOrdersCount) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.status = status;
        this.deliveredOrdersCount = deliveredOrdersCount;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public int getDeliveredOrdersCount() {
        return deliveredOrdersCount;
    }

    // --- Setters ---

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeliveredOrdersCount(int deliveredOrdersCount) {
        this.deliveredOrdersCount = deliveredOrdersCount;
    }

    // --- toString ---

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-15s | %-12s | %-8s | Đã giao: %5d đơn |",
                id, name, phone, vehicleType, status, deliveredOrdersCount);
    }
}
