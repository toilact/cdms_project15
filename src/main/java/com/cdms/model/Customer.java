// ============================================================
// File: Customer.java
// Package: com.cdms.model
// Description: Thực thể Khách hàng — người tạo bưu kiện gửi đi.
// ============================================================
package com.cdms.model;

public class Customer {

    // --- Thuộc tính ---
    private String id;
    private String name;
    private String phone;
    private String address;

    // --- Constructors ---

    /** Constructor rỗng — Gson cần để deserialize từ JSON. */
    public Customer() {
    }

    /** Constructor đầy đủ. */
    public Customer(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
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

    public String getAddress() {
        return address;
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

    public void setAddress(String address) {
        this.address = address;
    }

    // --- toString ---

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-15s | %-30s |",
                id, name, phone, address);
    }
}
