// ============================================================
// File: BillingReportView.java
// Package: com.cdms.view
// Description: Giao diện console cho phân hệ Báo cáo & Quản lý hóa đơn.
// ============================================================
// HUỲNH LÊ QUỐC CƯỜNG
package com.cdms.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.cdms.core.InputHelper;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Invoice;
import com.cdms.service.BillingReportService;

public class BillingReportView {

    private BillingReportView() {
    }

    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║        MANAGER - MENU CHÍNH           ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  [QUẢN LÝ THANH TOÁN]                 ║");
            System.out.println("║  1. Tính hóa đơn cho đơn hàng (B16)   ║");
            System.out.println("║  2. Xem chi tiết hóa đơn      (B17)   ║");
            System.out.println("║  3. Ghi nhận thanh toán        (B18)  ║");
            System.out.println("║                                       ║");
            System.out.println("║  [BÁO CÁO & THỐNG KÊ]                 ║");
            System.out.println("║  4. Báo cáo doanh thu theo ngày(B19)  ║");
            System.out.println("║  5. Báo cáo doanh thu theo tháng(B20) ║");
            System.out.println("║  6. Shipper tích cực nhất      (B21)  ║");
            System.out.println("║  7. Thống kê đơn giao thành công(B22) ║");
            System.out.println("║                                       ║");
            System.out.println("║  0. Quay lại Menu chính               ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-7): ", 0, 7);

            switch (choice) {
                case 1:
                    handleCreateInvoice();
                    break;
                case 2:
                    handleViewInvoiceDetails();
                    break;
                case 3:
                    handleRecordPayment();
                    break;
                case 4:
                    BillingReportService.printDailyRevenueReport();
                    InputHelper.pressEnterToContinue();
                    break;
                case 5:
                    BillingReportService.printMonthlyRevenueReport();
                    InputHelper.pressEnterToContinue();
                    break;
                case 6:
                    handleTopShippers();
                    break;
                case 7:
                    handleDeliveryStatistics();
                    break;
                case 0:
                    running = false;
                    System.out.println("  ↩ Quay lại Menu chính...\n");
                    break;
                default:
                    break;
            }
        }
    }

    private static void handleCreateInvoice() {
        System.out.println("\n--- Tạo hóa đơn cho đơn hàng đã giao ---");
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");
        DeliveryOrder order = BillingReportService.findOrderById(orderId);

        if (order == null) {
            System.out.println("⚠️  Không tìm thấy đơn hàng với mã: " + orderId + "\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
            System.out.println("⚠️  Chỉ có thể tạo hóa đơn cho đơn hàng đã giao (Delivered). Trạng thái hiện tại: " + order.getStatus() + "\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        if (BillingReportService.invoiceExistsForOrder(orderId)) {
            Invoice existing = BillingReportService.findInvoiceByOrderId(orderId);
            System.out.println("⚠️  Hóa đơn cho đơn hàng này đã tồn tại: " + existing.getId());
            System.out.println(existing);
            System.out.println();
            InputHelper.pressEnterToContinue();
            return;
        }

        Invoice invoice = BillingReportService.createInvoiceForDeliveredOrder(orderId);
        if (invoice == null) {
            System.out.println("❌ Không thể tạo hóa đơn cho đơn hàng này.\n");
        } else {
            System.out.println("✅ Hóa đơn đã được tạo thành công:");
            System.out.println(invoice);
            System.out.println();
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleViewInvoiceDetails() {
        System.out.println("\n--- Xem chi tiết hóa đơn ---");
        List<Invoice> invoices = BillingReportService.getAllInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào trong hệ thống.\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("Danh sách hóa đơn hiện có:");
        for (Invoice invoice : invoices) {
            System.out.printf("  - %s (Đơn: %s, Trạng thái: %s, Tổng: %,.0f VND)%n",
                    invoice.getId(), invoice.getOrderId(), invoice.getPaymentStatus(), invoice.getTotalAmount());
        }
        System.out.println();

        String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn muốn xem: ");
        Invoice invoice = BillingReportService.findInvoiceById(invoiceId);
        if (invoice == null) {
            System.out.println("⚠️  Không tìm thấy hóa đơn với mã: " + invoiceId + "\n");
        } else {
            System.out.println("\nChi tiết hóa đơn:");
            System.out.println("Mã hóa đơn   : " + invoice.getId());
            System.out.println("Mã đơn hàng  : " + invoice.getOrderId());
            System.out.println("Phí cơ bản   : " + String.format("%,.0f VND", invoice.getBaseFee()));
            System.out.println("Phụ phí hỏa tốc: " + String.format("%,.0f VND", invoice.getUrgentCharge()));
            System.out.println("Tổng phí      : " + String.format("%,.0f VND", invoice.getTotalAmount()));
            System.out.println("Trạng thái TT : " + invoice.getPaymentStatus());
            System.out.println("Phương thức TT: " + (invoice.getPaymentMethod() != null ? invoice.getPaymentMethod() : "Chưa TT"));
            System.out.println("Ngày TT      : " + (invoice.getPaymentDate() != null ? invoice.getPaymentDate() : "Chưa TT"));
            System.out.println();
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleRecordPayment() {
        System.out.println("\n--- Ghi nhận thanh toán ---");
        List<Invoice> invoices = BillingReportService.getAllInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào để thanh toán.\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        for (Invoice invoice : invoices) {
            System.out.printf("  - %s (Đơn %s, Trạng thái: %s, Tổng: %,.0f VND)%n",
                    invoice.getId(), invoice.getOrderId(), invoice.getPaymentStatus(), invoice.getTotalAmount());
        }
        System.out.println();

        String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn cần ghi nhận thanh toán: ");
        Invoice invoice = BillingReportService.findInvoiceById(invoiceId);
        if (invoice == null) {
            System.out.println("⚠️  Không tìm thấy hóa đơn với mã: " + invoiceId + "\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        if (invoice.getPaymentStatus() != null && invoice.getPaymentStatus().equalsIgnoreCase("Paid")) {
            System.out.println("⚠️  Hóa đơn này đã được thanh toán trước đó.\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        String paymentMethod = InputHelper.getStringInput("Nhập phương thức thanh toán (Cash/Banking/...): ");
        LocalDate paymentDate = InputHelper.getDateInput("Nhập ngày thanh toán (DD/MM/YYYY): ", false);
        boolean success = BillingReportService.recordPayment(invoiceId, paymentMethod, paymentDate);

        if (success) {
            System.out.println("✅ Ghi nhận thanh toán thành công cho hóa đơn " + invoiceId + ".\n");
        } else {
            System.out.println("❌ Ghi nhận thanh toán thất bại. Vui lòng thử lại.\n");
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleTopShippers() {
        System.out.println("\n--- Shipper tích cực nhất ---");
        List<DeliveryStaff> shippers = BillingReportService.getTopShippers(5);
        if (shippers.isEmpty()) {
            System.out.println("Không có dữ liệu shipper.\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("Danh sách shipper theo số đơn giao thành công:");
        int rank = 1;
        for (DeliveryStaff shipper : shippers) {
            System.out.printf("%d. %s (%s) - Đã giao: %d đơn%n",
                    rank++, shipper.getName(), shipper.getId(), shipper.getDeliveredOrdersCount());
        }
        System.out.println();
        InputHelper.pressEnterToContinue();
    }

    private static void handleDeliveryStatistics() {
        System.out.println("\n--- Thống kê đơn giao thành công ---");
        Map<String, Object> stats = BillingReportService.getDeliveryStatistics();

        System.out.println("Tổng số đơn hàng      : " + stats.get("totalOrders"));
        System.out.println("Số đơn đã giao       : " + stats.get("delivered"));
        System.out.println("Số đơn đang giao      : " + stats.get("inTransit"));
        System.out.println("Số đơn chờ xử lý     : " + stats.get("pending"));
        System.out.println("Số đơn giao thất bại : " + stats.get("failed"));
        System.out.printf("Tỷ lệ giao thành công: %.1f%% %n", stats.get("successRate"));
        System.out.println();
        InputHelper.pressEnterToContinue();
    }
}
