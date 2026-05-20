// ============================================================
// File: Customer.java
// Package: com.cdms.model
// Description: Lớp thực thể đại diện cho Khách hàng gửi hàng
// ============================================================
package com.cdms.model;

public class Customer {

    // ===== PRIVATE FIELDS (Encapsulation) =====
    private String id;
    private String name;
    private String phone;
    private String address;

    // ===== CONSTRUCTORS =====

    /** Constructor không tham số (cần cho Gson deserialization) */
    public Customer() {
    }

    /** Constructor đầy đủ tham số */
    public Customer(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // ===== GETTERS =====

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

    // ===== SETTERS =====

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

    // ===== toString() =====

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-15s | %-30s |",
                id, name, phone, address);
    }
}
