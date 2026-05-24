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

    // ANSI Colors for beautiful UI
    private static final String RESET = "\u001B[0m";
    private static final String BLUE_BG = "\u001B[44m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD_WHITE = "\u001B[1;37m";
    private static final String PURPLE = "\u001B[35m";

    private BillingReportView() {
    }

    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_CYAN + "╔═══════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_CYAN + "║" + BOLD_YELLOW + "        MANAGER - MENU CHÍNH           " + RESET
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╠═══════════════════════════════════════╣" + RESET);
            System.out.println(
                    BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ THANH TOÁN]                 " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "1. " + WHITE + "Tính hóa đơn cho đơn hàng (B16)   "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "2. " + WHITE + "Xem chi tiết hóa đơn      (B17)   "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "3. " + WHITE + "Ghi nhận thanh toán        (B18)  "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(
                    BOLD_CYAN + "║  " + BOLD_GREEN + "[BÁO CÁO & THỐNG KÊ]                 " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "4. " + WHITE + "Báo cáo doanh thu theo ngày(B19)  "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "5. " + WHITE + "Báo cáo doanh thu theo tháng(B20) "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "6. " + WHITE + "Shipper tích cực nhất      (B21)  "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "7. " + WHITE + "Thống kê đơn giao thành công(B22) "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(
                    BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ DỮ LIỆU]                    " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "8. " + WHITE + "Thêm dữ liệu (CRUD)               "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "9. " + WHITE + "Sửa dữ liệu  (CRUD)               "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "10." + WHITE + " Xóa dữ liệu (CRUD)               "
                    + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "0. " + BOLD_WHITE
                    + "Quay lại Menu chính               " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╚═══════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput("Chọn chức năng (0-10): ", 0, 10);

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
                case 8:
                    handleAddInvoice();
                    break;
                case 9:
                    handleUpdateInvoice();
                    break;
                case 10:
                    handleDeleteInvoice();
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

    private static void handleAddInvoice() {
        System.out.println("\n" + PURPLE + "--- Thêm dữ liệu (Hóa đơn) ---" + RESET);
        String id = InputHelper.getStringInput("Nhập mã hóa đơn (VD: INV-001): ");
        if (BillingReportService.findInvoiceById(id) != null) {
            System.out.println(BOLD_YELLOW + "⚠️  Mã hóa đơn này đã tồn tại.\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng tương ứng: ");
        double baseFee = InputHelper.getDoubleInput("Nhập phí cơ bản (VND): ", 0, Double.MAX_VALUE);
        double urgentCharge = InputHelper.getDoubleInput("Nhập phụ phí hỏa tốc (VND): ", 0, Double.MAX_VALUE);
        double totalAmount = baseFee + urgentCharge;

        System.out.println("Nhập trạng thái thanh toán:");
        System.out.println("1. Unpaid");
        System.out.println("2. Paid");
        int statusChoice = InputHelper.getIntInput("Chọn trạng thái (1-2): ", 1, 2);
        String status = (statusChoice == 1) ? "Unpaid" : "Paid";

        String paymentMethod = null;
        LocalDate paymentDate = null;
        if ("Paid".equals(status)) {
            paymentMethod = InputHelper.getStringInput("Nhập phương thức thanh toán (Cash/Banking): ");
            paymentDate = InputHelper.getDateInput("Nhập ngày thanh toán (DD/MM/YYYY): ", false);
        }

        Invoice newInvoice = new Invoice(id, orderId, baseFee, urgentCharge, totalAmount, status, paymentMethod,
                paymentDate);
        String result = BillingReportService.addInvoice(newInvoice);
        System.out.println(result + "\n");
        InputHelper.pressEnterToContinue();
    }

    private static void handleUpdateInvoice() {
        System.out.println("\n" + PURPLE + "--- Sửa dữ liệu (Hóa đơn) ---" + RESET);
        String id = InputHelper.getStringInput("Nhập mã hóa đơn cần sửa: ");
        Invoice existing = BillingReportService.findInvoiceById(id);

        if (existing == null) {
            System.out.println(BOLD_YELLOW + "⚠️  Không tìm thấy hóa đơn.\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("\nThông tin hiện tại:");
        System.out.println(existing);
        System.out.println("\nNhập thông tin mới (nhấn Enter để giữ nguyên giá trị cũ nếu không phải là số):");

        System.out.println("Trạng thái thanh toán hiện tại: " + existing.getPaymentStatus());
        String newStatus = InputHelper.getStringInput("Nhập trạng thái mới (Unpaid/Paid) [Enter để bỏ qua]: ");
        if (newStatus.isEmpty()) {
            newStatus = existing.getPaymentStatus();
        }

        String newMethod = existing.getPaymentMethod();
        LocalDate newDate = existing.getPaymentDate();

        if ("Paid".equalsIgnoreCase(newStatus)) {
            newMethod = InputHelper.getStringInput("Nhập phương thức thanh toán (Cash/Banking) [Enter để giữ cũ]: ");
            if (newMethod.isEmpty())
                newMethod = existing.getPaymentMethod();

            System.out.println("Nhập ngày thanh toán (DD/MM/YYYY) [Nhập 'skip' để giữ ngày cũ]: ");
            String dateStr = InputHelper.getStringInput("");
            if (!dateStr.equalsIgnoreCase("skip") && !dateStr.isEmpty()) {
                try {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                            .ofPattern("dd/MM/yyyy");
                    newDate = LocalDate.parse(dateStr, formatter);
                } catch (Exception e) {
                    System.out.println("Sai định dạng ngày, giữ nguyên ngày cũ.");
                }
            }
        } else {
            newMethod = null;
            newDate = null;
        }

        existing.setPaymentStatus(newStatus);
        existing.setPaymentMethod(newMethod);
        existing.setPaymentDate(newDate);

        String result = BillingReportService.updateInvoice(existing);
        System.out.println(BOLD_GREEN + result + "\n" + RESET);
        InputHelper.pressEnterToContinue();
    }

    private static void handleDeleteInvoice() {
        System.out.println("\n" + PURPLE + "--- Xóa dữ liệu (Hóa đơn) ---" + RESET);
        String id = InputHelper.getStringInput("Nhập mã hóa đơn cần xóa: ");
        Invoice existing = BillingReportService.findInvoiceById(id);

        if (existing == null) {
            System.out.println(BOLD_YELLOW + "⚠️  Không tìm thấy hóa đơn.\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("\nThông tin hóa đơn:");
        System.out.println(existing);
        String confirm = InputHelper.getStringInput("Bạn có chắc chắn muốn xóa hóa đơn này? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            String result = BillingReportService.deleteInvoice(id);
            System.out.println(BOLD_GREEN + result + "\n" + RESET);
        } else {
            System.out.println(BOLD_YELLOW + "Đã hủy thao tác xóa.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleCreateInvoice() {
        System.out.println("\n" + PURPLE + "--- Tạo hóa đơn cho đơn hàng đã giao ---" + RESET);
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");
        DeliveryOrder order = BillingReportService.findOrderById(orderId);

        if (order == null) {
            System.out.println(BOLD_YELLOW + "⚠️  Không tìm thấy đơn hàng với mã: " + orderId + "\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
            System.out.println(
                    BOLD_YELLOW + "⚠️  Chỉ có thể tạo hóa đơn cho đơn hàng đã giao (Delivered). Trạng thái hiện tại: "
                            + order.getStatus() + "\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        if (BillingReportService.invoiceExistsForOrder(orderId)) {
            Invoice existing = BillingReportService.findInvoiceByOrderId(orderId);
            System.out.println(BOLD_YELLOW + "⚠️  Hóa đơn cho đơn hàng này đã tồn tại: " + existing.getId() + RESET);
            System.out.println(existing);
            System.out.println();
            InputHelper.pressEnterToContinue();
            return;
        }

        Invoice invoice = BillingReportService.createInvoiceForDeliveredOrder(orderId);
        if (invoice == null) {
            System.out.println(BOLD_RED + "❌ Không thể tạo hóa đơn cho đơn hàng này.\n" + RESET);
        } else {
            System.out.println(BOLD_GREEN + "✅ Hóa đơn đã được tạo thành công:" + RESET);
            System.out.println(invoice);
            System.out.println();
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleViewInvoiceDetails() {
        System.out.println("\n" + PURPLE + "--- Xem chi tiết hóa đơn ---" + RESET);
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
            System.out.println(BOLD_YELLOW + "⚠️  Không tìm thấy hóa đơn với mã: " + invoiceId + "\n" + RESET);
        } else {
            System.out.println("\nChi tiết hóa đơn:");
            System.out.println("Mã hóa đơn   : " + invoice.getId());
            System.out.println("Mã đơn hàng  : " + invoice.getOrderId());
            System.out.println("Phí cơ bản   : " + String.format("%,.0f VND", invoice.getBaseFee()));
            System.out.println("Phụ phí hỏa tốc: " + String.format("%,.0f VND", invoice.getUrgentCharge()));
            System.out.println("Tổng phí      : " + String.format("%,.0f VND", invoice.getTotalAmount()));
            System.out.println("Trạng thái TT : " + invoice.getPaymentStatus());
            System.out.println(
                    "Phương thức TT: " + (invoice.getPaymentMethod() != null ? invoice.getPaymentMethod() : "Chưa TT"));
            System.out.println(
                    "Ngày TT      : " + (invoice.getPaymentDate() != null ? invoice.getPaymentDate() : "Chưa TT"));
            System.out.println();
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleRecordPayment() {
        System.out.println("\n" + PURPLE + "--- Ghi nhận thanh toán ---" + RESET);
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
            System.out.println(BOLD_YELLOW + "⚠️  Không tìm thấy hóa đơn với mã: " + invoiceId + "\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        if (invoice.getPaymentStatus() != null && invoice.getPaymentStatus().equalsIgnoreCase("Paid")) {
            System.out.println(BOLD_YELLOW + "⚠️  Hóa đơn này đã được thanh toán trước đó.\n" + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        String paymentMethod = InputHelper.getStringInput("Nhập phương thức thanh toán (Cash/Banking/...): ");
        LocalDate paymentDate = InputHelper.getDateInput("Nhập ngày thanh toán (DD/MM/YYYY): ", false);
        boolean success = BillingReportService.recordPayment(invoiceId, paymentMethod, paymentDate);

        if (success) {
            System.out
                    .println(BOLD_GREEN + "✅ Ghi nhận thanh toán thành công cho hóa đơn " + invoiceId + ".\n" + RESET);
        } else {
            System.out.println(BOLD_RED + "❌ Ghi nhận thanh toán thất bại. Vui lòng thử lại.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleTopShippers() {
        System.out.println("\n" + PURPLE + "--- Shipper tích cực nhất ---" + RESET);
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
        System.out.println("\n" + PURPLE + "--- Thống kê đơn giao thành công ---" + RESET);
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
