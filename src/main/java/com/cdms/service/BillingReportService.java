// ============================================================
// File: BillingReportService.java
// Package: com.cdms.service
// Description: Phân hệ Thanh toán và Báo cáo cho vai trò Manager.
//              Cung cấp tất cả chức năng B16-B22.
// Phân công: Huỳnh Lê Quốc Cường (Developer D - Thành viên 5)
// ============================================================
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

    private static final double URGENT_CHARGE = 20_000.0;

    private BillingReportService() {
    }

    // ============================================================
    // TÌM KIẾM / LOOKUP
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

    /** Trả về danh sách hóa đơn chưa thanh toán (paymentStatus = "Unpaid"). */
    public static List<Invoice> getUnpaidInvoices() {
        return InvoiceRepository.findAll().stream()
                .filter(inv -> !"Paid".equalsIgnoreCase(inv.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    public static List<DeliveryStaff> getTopShippers(int limit) {
        return DeliveryStaffRepository.findTopShippers(limit);
    }

    /**
     * B21 / BR19 — Lấy danh sách Top Shipper tích cực nhất trong một khoảng thời gian được chọn.
     */
    public static List<DeliveryStaff> getTopShippersInPeriod(LocalDate start, LocalDate end, int limit) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Khoảng thời gian không được để trống!");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc!");
        }

        // Thống kê số đơn hàng hoàn thành của shipper trong khoảng [start, end]
        Map<String, Long> counts = DeliveryOrderRepository.findAll().stream()
                .filter(o -> "Delivered".equalsIgnoreCase(o.getStatus()) && o.getDeliveryDate() != null)
                .filter(o -> !o.getDeliveryDate().isBefore(start) && !o.getDeliveryDate().isAfter(end))
                .filter(o -> o.getStaffId() != null)
                .collect(Collectors.groupingBy(DeliveryOrder::getStaffId, Collectors.counting()));

        return counts.entrySet().stream()
                .map(entry -> {
                    DeliveryStaff original = DeliveryStaffRepository.findById(entry.getKey());
                    if (original == null) return null;
                    // Tạo bản sao tạm thời để chứa số lượng đơn đã giao trong giai đoạn này
                    return new DeliveryStaff(
                            original.getId(),
                            original.getName(),
                            original.getPhone(),
                            original.getVehicleType(),
                            original.getStatus(),
                            entry.getValue().intValue()
                    );
                })
                .filter(java.util.Objects::nonNull)
                .sorted((s1, s2) -> Integer.compare(s2.getDeliveredOrdersCount(), s1.getDeliveredOrdersCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static boolean invoiceExistsForOrder(String orderId) {
        return InvoiceRepository.existsByOrderId(orderId);
    }

    // ============================================================
    // TẠO / GHI NHẬN HÓA ĐƠN
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

    /**
     * Tạo Invoice từ DeliveryOrder (chưa lưu vào repository).
     * Tính phí từ Parcel.calculateFee() + phụ phí hỏa tốc nếu Urgent.
     */
    public static Invoice createInvoice(DeliveryOrder order) {
        if (order == null)
            return null;
        Parcel parcel = ParcelRepository.findById(order.getParcelId());
        if (parcel == null)
            return null;

        double baseFee = parcel.calculateFee();
        double urgent = "Urgent".equalsIgnoreCase(order.getDeliveryType()) ? URGENT_CHARGE : 0.0;
        double totalAmount = baseFee + urgent;

        return new Invoice(
                generateNextInvoiceId(),
                order.getId(),
                baseFee, urgent, totalAmount,
                "Unpaid", null, null);
    }

    /**
     * B16 — Tạo và lưu hóa đơn cho đơn hàng đã giao.
     * Trả về null nếu đơn không tồn tại hoặc kiện hàng không hợp lệ.
     * Nếu hóa đơn đã tồn tại, trả về hóa đơn cũ (không tạo lại).
     */
    public static Invoice createInvoiceForDeliveredOrder(String orderId) {
        DeliveryOrder order = findOrderById(orderId);
        if (order == null || !"Delivered".equalsIgnoreCase(order.getStatus()))
            return null;

        // Nếu đã có hóa đơn, trả về hóa đơn cũ
        if (invoiceExistsForOrder(orderId)) {
            return findInvoiceByOrderId(orderId);
        }

        Invoice invoice = createInvoice(order);
        if (invoice != null) {
            InvoiceRepository.add(invoice);
        }
        return invoice;
    }

    /**
     * B18 — Ghi nhận thanh toán cho hóa đơn.
     * Trả về false nếu hóa đơn không tồn tại hoặc đã được thanh toán.
     */
    public static boolean recordPayment(String invoiceId, String paymentMethod, LocalDate paymentDate) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (invoice == null)
            return false;
        if ("Paid".equalsIgnoreCase(invoice.getPaymentStatus()))
            return false;

        // BUG-12: Ràng buộc ngày thanh toán không được trước ngày giao đơn hàng thực tế
        DeliveryOrder order = DeliveryOrderRepository.findById(invoice.getOrderId());
        if (order != null && order.getDeliveryDate() != null && paymentDate.isBefore(order.getDeliveryDate())) {
            throw new IllegalArgumentException("Ngày thanh toán không được trước ngày giao đơn hàng thực tế (" + order.getDeliveryDate() + ")!");
        }

        invoice.setPaymentStatus("Paid");
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(paymentDate);
        return InvoiceRepository.update(invoice);
    }

    // ============================================================
    // CRUD HÓA ĐƠN
    // ============================================================

    public static String addInvoice(Invoice invoice) {
        if (InvoiceRepository.findById(invoice.getId()) != null) {
            return "❌ Mã hóa đơn '" + invoice.getId() + "' đã tồn tại!";
        }
        InvoiceRepository.add(invoice);
        return "✅ Đã thêm hóa đơn: " + invoice.getId();
    }

    public static String updateInvoice(Invoice updated) {
        boolean success = InvoiceRepository.update(updated);
        return success
                ? "✅ Đã cập nhật hóa đơn: " + updated.getId()
                : "❌ Không tìm thấy hóa đơn '" + updated.getId() + "'!";
    }

    public static String deleteInvoice(String invoiceId) {
        boolean success = InvoiceRepository.delete(invoiceId);
        return success
                ? "✅ Đã xóa hóa đơn: " + invoiceId
                : "❌ Không tìm thấy hóa đơn '" + invoiceId + "'!";
    }

    // ============================================================
    // THỐNG KÊ ĐƠN HÀNG (B22)
    // ============================================================

    /**
     * B22 — Thống kê đơn hàng theo trạng thái.
     * Bao gồm: Pending, Assigned, In Transit, Delivered, Failed.
     */
    public static Map<String, Object> getDeliveryStatistics() {
        long totalOrders = DeliveryOrderRepository.findAll().size();
        long delivered = DeliveryOrderRepository.countByStatus("Delivered");
        long inTransit = DeliveryOrderRepository.countByStatus("In Transit");
        long assigned = DeliveryOrderRepository.countByStatus("Assigned");
        long pending = DeliveryOrderRepository.countByStatus("Pending");
        long failed = DeliveryOrderRepository.countByStatus("Failed");

        double successRate = (totalOrders > 0) ? (delivered * 100.0 / totalOrders) : 0.0;

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("delivered", delivered);
        stats.put("inTransit", inTransit);
        stats.put("assigned", assigned);
        stats.put("pending", pending);
        stats.put("failed", failed);
        stats.put("successRate", successRate);
        return stats;
    }

    // ============================================================
    // DOANH THU — tính từ Invoice đã Paid (B19, B20)
    // ============================================================

    /**
     * B19 — Doanh thu theo từng ngày thanh toán.
     * Chỉ tính hóa đơn Paid có paymentDate != null, group by paymentDate.
     */
    public static Map<LocalDate, Double> calculateDailyRevenue() {
        Map<LocalDate, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            result.merge(inv.getPaymentDate(), inv.getTotalAmount(), Double::sum);
        }
        return result;
    }

    /**
     * B20 — Doanh thu theo từng tháng (tất cả năm).
     * Chỉ tính hóa đơn Paid có paymentDate != null, group by YearMonth.
     */
    public static Map<YearMonth, Double> calculateMonthlyRevenue() {
        Map<YearMonth, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            result.merge(YearMonth.from(inv.getPaymentDate()), inv.getTotalAmount(), Double::sum);
        }
        return result;
    }

    /**
     * B20 — Doanh thu theo từng tháng trong một năm cụ thể.
     */
    public static Map<YearMonth, Double> calculateMonthlyRevenueByYear(int year) {
        Map<YearMonth, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findByPaymentYear(year)) {
            result.merge(YearMonth.from(inv.getPaymentDate()), inv.getTotalAmount(), Double::sum);
        }
        return result;
    }

    /**
     * Doanh thu theo từng năm.
     * Chỉ tính hóa đơn Paid có paymentDate != null, group by year.
     */
    public static Map<Integer, Double> calculateAnnualRevenue() {
        Map<Integer, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            result.merge(inv.getPaymentDate().getYear(), inv.getTotalAmount(), Double::sum);
        }
        return result;
    }
}
