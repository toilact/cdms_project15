// ============================================================
// File: BillingReportService.java
// Package: com.cdms.service
// Description: Nghiệp vụ Thanh toán & Báo cáo cho Manager: tạo
//              hóa đơn, đối soát COD, thống kê doanh thu, top shipper.
// Phân công: Huỳnh Lê Quốc Cường (Developer D - Thành viên 5)
// ============================================================
package com.cdms.service;

import java.time.LocalDate;
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

    public static final double URGENT_CHARGE = 20_000.0;

    // Utility class — không cho tạo đối tượng
    private BillingReportService() {
    }

    // ----------------------------------------------------------
    // TÌM KIẾM / LOOKUP
    // ----------------------------------------------------------

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
        List<Invoice> result = new java.util.ArrayList<>();
        for (Invoice inv : InvoiceRepository.findAll()) {
            if ("Unpaid".equalsIgnoreCase(inv.getPaymentStatus())) {
                result.add(inv);
            }
        }
        return result;
    }

    public static List<DeliveryStaff> getTopShippers(int limit) {
        return DeliveryStaffRepository.findTopShippers(limit);
    }

    /** B21 — Top Shipper theo số đơn đã giao trong khoảng thời gian [start, end]. */
    public static List<DeliveryStaff> getTopShippersInPeriod(LocalDate start, LocalDate end, int limit) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Khoảng thời gian không được để trống!");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc!");
        }

        // Đếm số đơn hoàn thành của từng shipper trong khoảng [start, end]
        Map<String, Integer> counts = new java.util.HashMap<>();
        for (DeliveryOrder o : DeliveryOrderRepository.findAll()) {
            if (!"Delivered".equalsIgnoreCase(o.getStatus())) continue;
            if (o.getDeliveryDate() == null) continue;
            if (o.getDeliveryDate().isBefore(start) || o.getDeliveryDate().isAfter(end)) continue;
            if (o.getStaffId() == null) continue;

            String sid = o.getStaffId();
            if (counts.containsKey(sid)) {
                counts.put(sid, counts.get(sid) + 1);
            } else {
                counts.put(sid, 1);
            }
        }

        // Tạo bản sao DeliveryStaff với số đơn trong kỳ (không sửa dữ liệu gốc)
        List<DeliveryStaff> staffInPeriod = new java.util.ArrayList<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            DeliveryStaff original = DeliveryStaffRepository.findById(entry.getKey());
            if (original == null) continue;
            staffInPeriod.add(new DeliveryStaff(
                    original.getId(),
                    original.getName(),
                    original.getPhone(),
                    original.getVehicleType(),
                    original.getStatus(),
                    entry.getValue()
            ));
        }

        // Sắp xếp giảm dần theo số đơn đã giao trong kỳ
        for (int i = 0; i < staffInPeriod.size() - 1; i++) {
            for (int j = 0; j < staffInPeriod.size() - 1 - i; j++) {
                if (staffInPeriod.get(j).getDeliveredOrdersCount() < staffInPeriod.get(j + 1).getDeliveredOrdersCount()) {
                    DeliveryStaff temp = staffInPeriod.get(j);
                    staffInPeriod.set(j, staffInPeriod.get(j + 1));
                    staffInPeriod.set(j + 1, temp);
                }
            }
        }

        // Lấy tối đa [limit] người đứng đầu
        List<DeliveryStaff> result = new java.util.ArrayList<>();
        for (int i = 0; i < staffInPeriod.size() && i < limit; i++) {
            result.add(staffInPeriod.get(i));
        }
        return result;
    }

    public static boolean invoiceExistsForOrder(String orderId) {
        return InvoiceRepository.existsByOrderId(orderId);
    }

    // ----------------------------------------------------------
    // TẠO / GHI NHẬN HÓA ĐƠN
    // ----------------------------------------------------------

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

    /** Tạo đối tượng Invoice từ đơn hàng (chưa lưu). Phí = Parcel.calculateFee() + phụ phí hỏa tốc nếu Urgent. */
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
     * B16 — Tạo và lưu hóa đơn cho đơn hàng đã giao thành công.
     * Trả về hóa đơn cũ nếu đã tồn tại; trả về null nếu đơn chưa Delivered.
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
            if (order.getPaymentTerms() != null && "Sender Pay".equalsIgnoreCase(order.getPaymentTerms())) {
                invoice.setPaymentStatus("Paid");
                invoice.setPaymentMethod("Cash"); // Mặc định đã thanh toán tại quầy
                invoice.setPaymentDate(order.getOrderDate() != null ? order.getOrderDate() : LocalDate.now());
            } else {
                // Đơn COD: Shipper đã thu tiền mặt khi giao hàng thành công → trạng thái "Collected"
                invoice.setPaymentStatus("Collected");
                invoice.setPaymentDate(order.getDeliveryDate() != null ? order.getDeliveryDate() : LocalDate.now());
            }
            InvoiceRepository.add(invoice);
        }
        return invoice;
    }

    /** B18 — Ghi nhận thanh toán cho hóa đơn. Trả về false nếu không tìm thấy hoặc đã Paid. */
    public static boolean recordPayment(String invoiceId, String paymentMethod, LocalDate paymentDate) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (invoice == null)
            return false;
        if ("Paid".equalsIgnoreCase(invoice.getPaymentStatus()))
            return false;

        // Ngày thanh toán không được trước ngày giao hàng thực tế
        DeliveryOrder order = DeliveryOrderRepository.findById(invoice.getOrderId());
        if (order != null && order.getDeliveryDate() != null && paymentDate.isBefore(order.getDeliveryDate())) {
            throw new IllegalArgumentException("Ngày thanh toán không được trước ngày giao đơn hàng thực tế (" + order.getDeliveryDate() + ")!");
        }

        invoice.setPaymentStatus("Paid");
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(paymentDate);
        return InvoiceRepository.update(invoice);
    }

    /**
     * Lấy danh sách hóa đơn đã được Shipper thu hộ COD nhưng chưa đối soát (paymentStatus = "Collected").
     */
    public static List<Invoice> getCollectedInvoices() {
        List<Invoice> result = new java.util.ArrayList<>();
        for (Invoice inv : InvoiceRepository.findAll()) {
            if ("Collected".equalsIgnoreCase(inv.getPaymentStatus())) {
                result.add(inv);
            }
        }
        return result;
    }

    /** Đối soát COD — chuyển trạng thái từ "Collected" sang "Paid". Trả về false nếu không hợp lệ. */
    public static boolean reconcileInvoice(String invoiceId, String paymentMethod, LocalDate paymentDate) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (invoice == null)
            return false;
        if (!"Collected".equalsIgnoreCase(invoice.getPaymentStatus()))
            return false;

        // Ràng buộc ngày đối soát không được trước ngày giao đơn hàng thực tế
        DeliveryOrder order = DeliveryOrderRepository.findById(invoice.getOrderId());
        if (order != null && order.getDeliveryDate() != null && paymentDate.isBefore(order.getDeliveryDate())) {
            throw new IllegalArgumentException("Ngày đối soát không được trước ngày giao đơn hàng thực tế (" + order.getDeliveryDate() + ")!");
        }

        invoice.setPaymentStatus("Paid");
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(paymentDate);
        return InvoiceRepository.update(invoice);
    }

    // ----------------------------------------------------------
    // CRUD HÓA ĐƠN
    // ----------------------------------------------------------

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

    // ----------------------------------------------------------
    // THỐNG KÊ & BÁO CÁO
    // ----------------------------------------------------------

    /** B22 — Thống kê số đơn hàng theo từng trạng thái và tỷ lệ giao thành công. */
    public static Map<String, Object> getDeliveryStatistics() {
        long totalOrders = DeliveryOrderRepository.findAll().size();
        long delivered = DeliveryOrderRepository.countByStatus("Delivered");
        long inTransit = DeliveryOrderRepository.countByStatus("In Transit");
        long assigned = DeliveryOrderRepository.countByStatus("Assigned");
        long pickedUp = DeliveryOrderRepository.countByStatus("Picked Up");
        long pending = DeliveryOrderRepository.countByStatus("Pending");
        long failed = DeliveryOrderRepository.countByStatus("Failed");
        long cancelled = DeliveryOrderRepository.countByStatus("Cancelled");

        double successRate = (totalOrders > 0) ? (delivered * 100.0 / totalOrders) : 0.0;

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("delivered", delivered);
        stats.put("inTransit", inTransit);
        stats.put("assigned", assigned);
        stats.put("pickedUp", pickedUp);
        stats.put("pending", pending);
        stats.put("failed", failed);
        stats.put("cancelled", cancelled);
        stats.put("successRate", successRate);
        return stats;
    }

    /**
     * Lấy danh sách đơn hàng đã bị hủy hoặc giao thất bại phục vụ hoàn tiền.
     */
    public static List<DeliveryOrder> getCancelledOrFailedOrders() {
        List<DeliveryOrder> result = new java.util.ArrayList<>();
        for (DeliveryOrder o : DeliveryOrderRepository.findAll()) {
            if ("Cancelled".equalsIgnoreCase(o.getStatus()) || "Failed".equalsIgnoreCase(o.getStatus())) {
                result.add(o);
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    // DOANH THU (chỉ tính hóa đơn đã Paid)
    // ----------------------------------------------------------

    /** B19 — Doanh thu theo ngày thanh toán (Map: ngày → tổng tiền). */
    public static Map<LocalDate, Double> calculateDailyRevenue() {
        Map<LocalDate, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            LocalDate date = inv.getPaymentDate();
            if (result.containsKey(date)) {
                result.put(date, result.get(date) + inv.getTotalAmount());
            } else {
                result.put(date, inv.getTotalAmount());
            }
        }
        return result;
    }

    /** B20 — Doanh thu theo tháng, tất cả các năm (Map: tháng/năm → tổng tiền). */
    public static Map<YearMonth, Double> calculateMonthlyRevenue() {
        Map<YearMonth, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            YearMonth ym = YearMonth.from(inv.getPaymentDate());
            if (result.containsKey(ym)) {
                result.put(ym, result.get(ym) + inv.getTotalAmount());
            } else {
                result.put(ym, inv.getTotalAmount());
            }
        }
        return result;
    }

    /** B20 — Doanh thu theo từng tháng trong một năm cụ thể (Map: tháng/năm → tổng tiền). */
    public static Map<YearMonth, Double> calculateMonthlyRevenueByYear(int year) {
        Map<YearMonth, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findByPaymentYear(year)) {
            YearMonth ym = YearMonth.from(inv.getPaymentDate());
            if (result.containsKey(ym)) {
                result.put(ym, result.get(ym) + inv.getTotalAmount());
            } else {
                result.put(ym, inv.getTotalAmount());
            }
        }
        return result;
    }

    /** Doanh thu theo từng năm (Map: năm → tổng tiền). */
    public static Map<Integer, Double> calculateAnnualRevenue() {
        Map<Integer, Double> result = new TreeMap<>();
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            int year = inv.getPaymentDate().getYear();
            if (result.containsKey(year)) {
                result.put(year, result.get(year) + inv.getTotalAmount());
            } else {
                result.put(year, inv.getTotalAmount());
            }
        }
        return result;
    }
}
