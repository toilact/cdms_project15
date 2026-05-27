// ============================================================
// File: Invoice.java
// Package: com.cdms.model
// Description: Lớp thực thể đại diện cho Hóa đơn thanh toán.
//              Tổng phí = baseFee + urgentCharge.
// ============================================================
package com.cdms.model;

import java.time.LocalDate;

public class Invoice {

    // ===== PRIVATE FIELDS (Encapsulation) =====
    private String id;
    private String orderId;
    private double baseFee;            // Phí cơ bản (từ Parcel.calculateFee())
    private double urgentCharge;       // Phụ phí hỏa tốc (nếu deliveryType = "Urgent")
    private double totalAmount;        // Tổng phí = baseFee + urgentCharge
    private String paymentStatus;      // "Unpaid", "Partially Paid", "Paid"
    private String paymentMethod;      // "Cash", "Banking", ...
    private LocalDate paymentDate;

    // ===== CONSTRUCTORS =====

    /** Constructor không tham số */
    public Invoice() {
        this.paymentStatus = "Unpaid";
    }

    /** Constructor đầy đủ tham số */
    public Invoice(String id, String orderId, double baseFee,
                   double urgentCharge, double totalAmount,
                   String paymentStatus, String paymentMethod,
                   LocalDate paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.baseFee = baseFee;
        this.urgentCharge = urgentCharge;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getBaseFee() {
        return baseFee;
    }

    public double getUrgentCharge() {
        return urgentCharge;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    // ===== SETTERS =====

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setBaseFee(double baseFee) {
        this.baseFee = baseFee;
    }

    public void setUrgentCharge(double urgentCharge) {
        this.urgentCharge = urgentCharge;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    // ===== toString() =====

    @Override
    public String toString() {
        return String.format(
                "| %-10s | Order: %-10s | Cơ bản: %,.0f | Hỏa tốc: %,.0f | Tổng: %,.0f VND | %-15s | %-10s |",
                id, orderId, baseFee, urgentCharge, totalAmount, paymentStatus,
                (paymentDate != null ? paymentDate.toString() : "Chưa TT"));
    }
}
