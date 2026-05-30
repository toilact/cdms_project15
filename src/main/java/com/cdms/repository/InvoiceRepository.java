// ============================================================
// File: InvoiceRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý CRUD cho Invoice.
// ============================================================
package com.cdms.repository;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.cdms.core.JSONDataManager;
import com.cdms.model.Invoice;

public class InvoiceRepository {

    private InvoiceRepository() {
    }

    public static List<Invoice> findAll() {
        return new java.util.ArrayList<>(JSONDataManager.invoices);
    }

    public static Invoice findById(String invoiceId) {
        if (invoiceId == null) {
            return null;
        }
        for (Invoice invoice : JSONDataManager.invoices) {
            if (invoiceId.equalsIgnoreCase(invoice.getId())) {
                return invoice;
            }
        }
        return null;
    }

    public static Invoice findByOrderId(String orderId) {
        if (orderId == null) {
            return null;
        }
        for (Invoice invoice : JSONDataManager.invoices) {
            if (orderId.equalsIgnoreCase(invoice.getOrderId())) {
                return invoice;
            }
        }
        return null;
    }

    public static boolean existsByOrderId(String orderId) {
        return findByOrderId(orderId) != null;
    }

    /**
     * Trả về danh sách hóa đơn đã thanh toán (paymentStatus = "Paid").
     */
    public static List<Invoice> findPaidInvoices() {
        return JSONDataManager.invoices.stream()
                .filter(inv -> "Paid".equalsIgnoreCase(inv.getPaymentStatus())
                        && inv.getPaymentDate() != null)
                .collect(Collectors.toList());
    }

    /**
     * Trả về danh sách hóa đơn đã thanh toán trong một tháng/năm cụ thể.
     */
    public static List<Invoice> findByPaymentYearMonth(YearMonth yearMonth) {
        if (yearMonth == null) {
            return List.of();
        }
        return findPaidInvoices().stream()
                .filter(inv -> YearMonth.from(inv.getPaymentDate()).equals(yearMonth))
                .collect(Collectors.toList());
    }

    /**
     * Trả về danh sách hóa đơn đã thanh toán trong một năm cụ thể.
     */
    public static List<Invoice> findByPaymentYear(int year) {
        return findPaidInvoices().stream()
                .filter(inv -> inv.getPaymentDate().getYear() == year)
                .collect(Collectors.toList());
    }

    public static void add(Invoice invoice) {
        if (invoice == null) {
            return;
        }
        JSONDataManager.invoices.add(invoice);
        JSONDataManager.saveAllData();
    }

    public static boolean update(Invoice updated) {
        if (updated == null || updated.getId() == null) {
            return false;
        }
        for (int i = 0; i < JSONDataManager.invoices.size(); i++) {
            if (JSONDataManager.invoices.get(i).getId().equalsIgnoreCase(updated.getId())) {
                JSONDataManager.invoices.set(i, updated);
                JSONDataManager.saveAllData();
                return true;
            }
        }
        return false;
    }

    public static boolean delete(String invoiceId) {
        if (invoiceId == null) {
            return false;
        }
        boolean removed = JSONDataManager.invoices.removeIf(invoice -> invoice.getId().equalsIgnoreCase(invoiceId));
        if (removed) {
            JSONDataManager.saveAllData();
        }
        return removed;
    }
}
