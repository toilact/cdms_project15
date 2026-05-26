// ============================================================
// File: BillingReportService.java
// Package: com.cdms.service
// Description: Phân hệ Thanh toán và Báo cáo cho vai trò Manager.
//              Cung cấp tất cả chức năng B16-B22.
// ============================================================
//HUỲNH LÊ QUỐC CƯỜNG
package com.cdms.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Invoice;
import com.cdms.model.Parcel;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.DeliveryStaffRepository;
import com.cdms.repository.InvoiceRepository;
import com.cdms.repository.ParcelRepository;

public class BillingReportService {

    private static final double URGENT_CHARGE = 20000.0;

    private BillingReportService() {
    }

    // ============================================================
    //  TÌM KIẾM / LOOKUP
    // ============================================================

    public static DeliveryOrder findOrderById(String orderId) {
        return DeliveryOrderRepository.findById(orderId);
    }

    public static Invoice findInvoiceById(String invoiceId) {
        return InvoiceRepository.findById(invoiceId);
    }

    public static Invoice findInvoiceByOrderId(String orderId) {
        return InvoiceRepository.findByOrderId(orderId);
    }

    public static List<Invoice> getAllInvoices() {
        return InvoiceRepository.findAll();
    }

    public static List<DeliveryOrder> getDeliveredOrders() {
        return DeliveryOrderRepository.findDeliveredOrders();
    }

    public static List<DeliveryStaff> getTopShippers(int limit) {
        return DeliveryStaffRepository.findTopShippers(limit);
    }

    public static boolean invoiceExistsForOrder(String orderId) {
        return InvoiceRepository.existsByOrderId(orderId);
    }

    // ============================================================
    //  TẠO / GHI NHẬN HÓA ĐƠN
    // ============================================================

    public static String generateNextInvoiceId() {
        List<Invoice> invoices = InvoiceRepository.findAll();
        int maxNum = 0;
        for (Invoice inv : invoices) {
            String id = inv.getId();
            if (id != null && id.startsWith("INV") && id.length() > 3) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua mã không chuẩn
                }
            }
        }
        return "INV" + String.format("%03d", maxNum + 1);
    }

    public static Invoice createInvoice(DeliveryOrder order) {
        if (order == null) {
            return null;
        }
        Parcel parcel = ParcelRepository.findById(order.getParcelId());
        if (parcel == null) {
            return null;
        }
        double baseFee = parcel.calculateFee();
        double urgentCharge = "Urgent".equalsIgnoreCase(order.getDeliveryType()) ? URGENT_CHARGE : 0.0;
        double totalAmount = baseFee + urgentCharge;

        return new Invoice(
                generateNextInvoiceId(),
                order.getId(),
                baseFee,
                urgentCharge,
                totalAmount,
                "Unpaid",
                null,
                null
        );
    }

    public static Invoice createInvoiceForDeliveredOrder(String orderId) {
        DeliveryOrder order = findOrderById(orderId);
        if (order == null || !"Delivered".equalsIgnoreCase(order.getStatus())) {
            return null;
        }
        if (invoiceExistsForOrder(orderId)) {
            return findInvoiceByOrderId(orderId);
        }
        Invoice invoice = createInvoice(order);
        if (invoice != null) {
            InvoiceRepository.add(invoice);
        }
        return invoice;
    }

    public static boolean recordPayment(String invoiceId, String paymentMethod, LocalDate paymentDate) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (invoice == null) {
            return false;
        }
        if (invoice.getPaymentStatus() != null && invoice.getPaymentStatus().equalsIgnoreCase("Paid")) {
            return false;
        }
        invoice.setPaymentStatus("Paid");
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(paymentDate);
        return InvoiceRepository.update(invoice);
    }

    // ============================================================
    //  CRUD HÓA ĐƠN
    // ============================================================

    public static String addInvoice(Invoice invoice) {
        if (InvoiceRepository.findById(invoice.getId()) != null) {
            return "❌ Lỗi: Mã hóa đơn '" + invoice.getId() + "' đã tồn tại!";
        }
        InvoiceRepository.add(invoice);
        return "✅ Đã thêm hóa đơn thành công: " + invoice.getId();
    }

    public static String updateInvoice(Invoice updated) {
        boolean success = InvoiceRepository.update(updated);
        if (success) {
            return "✅ Đã cập nhật hóa đơn: " + updated.getId();
        }
        return "❌ Lỗi: Không tìm thấy hóa đơn với mã '" + updated.getId() + "'!";
    }

    public static String deleteInvoice(String invoiceId) {
        boolean success = InvoiceRepository.delete(invoiceId);
        if (success) {
            return "✅ Đã xóa hóa đơn: " + invoiceId;
        }
        return "❌ Lỗi: Không tìm thấy hóa đơn với mã '" + invoiceId + "'!";
    }

    // ============================================================
    //  THỐNG KÊ ĐƠN HÀNG
    // ============================================================

    public static Map<String, Object> getDeliveryStatistics() {
        long totalOrders = DeliveryOrderRepository.findAll().size();
        long delivered   = DeliveryOrderRepository.countByStatus("Delivered");
        long failed      = DeliveryOrderRepository.countByStatus("Failed");
        long inTransit   = DeliveryOrderRepository.countByStatus("In Transit");
        long pending     = DeliveryOrderRepository.countByStatus("Pending");

        double successRate = totalOrders > 0 ? (delivered * 100.0 / totalOrders) : 0.0;

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("delivered",   delivered);
        stats.put("inTransit",   inTransit);
        stats.put("pending",     pending);
        stats.put("failed",      failed);
        stats.put("successRate", successRate);
        return stats;
    }

    // ============================================================
    //  DOANH THU — tính từ Invoice đã Paid (paymentDate)
    // ============================================================

    /**
     * Tổng doanh thu từ danh sách hóa đơn truyền vào.
     */
    public static double calculateTotalRevenue(List<Invoice> invoices) {
        return invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();
    }

    /**
     * Doanh thu theo từng ngày.
     * Chỉ tính các hóa đơn có paymentStatus = "Paid" và paymentDate != null.
     * Group by paymentDate, cộng dồn totalAmount.
     */
    public static Map<LocalDate, Double> calculateDailyRevenue() {
        List<Invoice> paidInvoices = InvoiceRepository.findPaidInvoices();
        Map<LocalDate, Double> dailyRevenue = new TreeMap<>();

        for (Invoice inv : paidInvoices) {
            dailyRevenue.merge(inv.getPaymentDate(), inv.getTotalAmount(), Double::sum);
        }
        return dailyRevenue;
    }

    /**
     * Doanh thu theo từng tháng.
     * Chỉ tính các hóa đơn có paymentStatus = "Paid" và paymentDate != null.
     * Group by YearMonth, cộng dồn totalAmount.
     */
    public static Map<YearMonth, Double> calculateMonthlyRevenue() {
        List<Invoice> paidInvoices = InvoiceRepository.findPaidInvoices();
        Map<YearMonth, Double> monthlyRevenue = new TreeMap<>();

        for (Invoice inv : paidInvoices) {
            YearMonth ym = YearMonth.from(inv.getPaymentDate());
            monthlyRevenue.merge(ym, inv.getTotalAmount(), Double::sum);
        }
        return monthlyRevenue;
    }

    /**
     * Doanh thu theo từng năm.
     * Chỉ tính các hóa đơn có paymentStatus = "Paid" và paymentDate != null.
     * Group by Year, cộng dồn totalAmount.
     */
    public static Map<Integer, Double> calculateAnnualRevenue() {
        List<Invoice> paidInvoices = InvoiceRepository.findPaidInvoices();
        Map<Integer, Double> annualRevenue = new TreeMap<>();

        for (Invoice inv : paidInvoices) {
            int year = inv.getPaymentDate().getYear();
            annualRevenue.merge(year, inv.getTotalAmount(), Double::sum);
        }
        return annualRevenue;
    }

    /**
     * Doanh thu theo tháng trong một năm cụ thể.
     */
    public static Map<YearMonth, Double> calculateMonthlyRevenueByYear(int year) {
        List<Invoice> paidInYear = InvoiceRepository.findByPaymentYear(year);
        Map<YearMonth, Double> result = new TreeMap<>();

        for (Invoice inv : paidInYear) {
            YearMonth ym = YearMonth.from(inv.getPaymentDate());
            result.merge(ym, inv.getTotalAmount(), Double::sum);
        }
        return result;
    }
}
