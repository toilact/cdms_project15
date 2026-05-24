// ============================================================
// File: BillingReportService.java
// Package: com.cdms.service
// Description: Phân hệ Thanh toán và Báo cáo cho vai trò Manager.
//              Cung cấp tất cả chức năng B16-B22.
// ============================================================
//HUỲNH LÊ QUỐC CƯỜNG
package com.cdms.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
                "INV-" + order.getId(),
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

    public static Map<String, Object> getDeliveryStatistics() {
        long totalOrders = DeliveryOrderRepository.findAll().size();
        long delivered = DeliveryOrderRepository.countByStatus("Delivered");
        long failed = DeliveryOrderRepository.countByStatus("Failed");
        long inTransit = DeliveryOrderRepository.countByStatus("In Transit");
        long pending = DeliveryOrderRepository.countByStatus("Pending");

        double successRate = totalOrders > 0 ? (delivered * 100.0 / totalOrders) : 0.0;

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("delivered", delivered);
        stats.put("inTransit", inTransit);
        stats.put("pending", pending);
        stats.put("failed", failed);
        stats.put("successRate", successRate);
        return stats;
    }

    public static double calculateTotalRevenue(List<Invoice> invoices) {
        return invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();
    }

    public static Map<YearMonth, Double> calculateMonthlyRevenue() {
        Map<YearMonth, Double> monthlyRevenue = new TreeMap<>();
        Map<LocalDate, Double> dailyRevenue = calculateDailyRevenue();
        
        for (Map.Entry<LocalDate, Double> entry : dailyRevenue.entrySet()) {
            monthlyRevenue.merge(YearMonth.from(entry.getKey()), entry.getValue(), Double::sum);
        }

        return monthlyRevenue;
    }

    public static Map<LocalDate, Double> calculateDailyRevenue() {
        List<DeliveryOrder> deliveredOrders = getDeliveredOrders();
        Map<LocalDate, Double> dailyRevenue = new TreeMap<>();

        for (DeliveryOrder order : deliveredOrders) {
            LocalDate deliveryDate = order.getDeliveryDate();
            if (deliveryDate == null) {
                continue;
            }
            Parcel parcel = ParcelRepository.findById(order.getParcelId());
            if (parcel == null) {
                continue;
            }
            double baseFee = parcel.calculateFee();
            double urgentCharge = "Urgent".equalsIgnoreCase(order.getDeliveryType()) ? URGENT_CHARGE : 0.0;
            double totalAmount = baseFee + urgentCharge;
            dailyRevenue.merge(deliveryDate, totalAmount, Double::sum);
        }

        return dailyRevenue;
    }

    public static void printDailyRevenueReport() {
        Map<LocalDate, Double> dailyRevenue = calculateDailyRevenue();

        System.out.println("\n========== BÁO CÁO DOANH THU THEO NGÀY ==========");
        System.out.println(String.format("%-15s | %20s", "Ngày", "Doanh thu (VND)"));
        System.out.println("-".repeat(40));

        if (dailyRevenue.isEmpty()) {
            System.out.println("Không có đơn hàng đã giao để báo cáo.");
        } else {
            double totalRevenue = 0.0;
            for (Map.Entry<LocalDate, Double> entry : dailyRevenue.entrySet()) {
                System.out.printf("%-15s | %20,.0f%n", entry.getKey(), entry.getValue());
                totalRevenue += entry.getValue();
            }
            System.out.println("-".repeat(40));
            System.out.printf("%-15s | %20,.0f VND%n", "TỔNG CỘNG", totalRevenue);
        }

        System.out.println("=".repeat(40) + "\n");
    }

    public static void printMonthlyRevenueReport() {
        Map<YearMonth, Double> monthlyRevenue = calculateMonthlyRevenue();

        System.out.println("\n========== BÁO CÁO DOANH THU THEO THÁNG ==========");
        System.out.println(String.format("%-15s | %20s", "Tháng", "Doanh thu (VND)"));
        System.out.println("-".repeat(40));

        if (monthlyRevenue.isEmpty()) {
            System.out.println("Không có đơn hàng đã giao để báo cáo.");
        } else {
            double totalRevenue = 0.0;
            for (Map.Entry<YearMonth, Double> entry : monthlyRevenue.entrySet()) {
                System.out.printf("%-15s | %20,.0f%n", entry.getKey(), entry.getValue());
                totalRevenue += entry.getValue();
            }
            System.out.println("-".repeat(40));
            System.out.printf("%-15s | %20,.0f VND%n", "TỔNG CỘNG", totalRevenue);
        }

        System.out.println("=".repeat(40) + "\n");
    }
}
