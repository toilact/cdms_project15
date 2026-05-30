// ============================================================
// File: BillingReportView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Thanh toán &
//              Báo cáo thống kê. Phục vụ vai trò Manager.
<<<<<<< HEAD
=======
//              Phân công: Huỳnh Lê Quốc Cường (Developer D - Thành viên 5)
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
// ============================================================
package com.cdms.view;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.cdms.core.InputHelper;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Invoice;
import com.cdms.service.BillingReportService;

public class BillingReportView {

    // ANSI Colors
    private static final String RESET       = "\u001B[0m";
    private static final String BOLD_CYAN   = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN  = "\u001B[1;32m";
    private static final String BOLD_RED    = "\u001B[1;31m";
    private static final String WHITE       = "\u001B[37m";
    private static final String BOLD_WHITE  = "\u001B[1;37m";
    private static final String PURPLE      = "\u001B[35m";

    private BillingReportView() {}

    // ==========================================================
    //  MENU CHÍNH — MANAGER
    // ==========================================================

    public static void showMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputHelper.getIntInput("Chọn chức năng (0-10): ", 0, 10);
            switch (choice) {
<<<<<<< HEAD
                case 1:  handleCreateInvoice();       break; // B16
                case 2:  handleViewInvoiceDetails();  break; // B17
                case 3:  handleRecordPayment();       break; // B18
                case 4:  handleDailyRevenueReport();  break; // B19
                case 5:  handleMonthlyRevenueReport();break; // B20
                case 6:  handleTopShippers();         break; // B21
                case 7:  handleDeliveryStatistics();  break; // B22
                case 8:  handleAddInvoice();          break; // CRUD Add
                case 9:  handleUpdateInvoice();       break; // CRUD Update
                case 10: handleDeleteInvoice();       break; // CRUD Delete
=======
                case 1:  handleCreateInvoice();        break; // B16
                case 2:  handleViewInvoiceDetails();   break; // B17
                case 3:  handleRecordPayment();        break; // B18
                case 4:  handleDailyRevenueReport();   break; // B19
                case 5:  handleMonthlyRevenueReport(); break; // B20
                case 6:  handleTopShippers();          break; // B21
                case 7:  handleDeliveryStatistics();   break; // B22
                case 8:  handleAddInvoice();           break; // CRUD Add
                case 9:  handleUpdateInvoice();        break; // CRUD Update
                case 10: handleDeleteInvoice();        break; // CRUD Delete
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
                case 0:
                    running = false;
                    System.out.println("  ↩ Quay lại Menu chính...\n");
                    break;
                default: break;
            }
        }
    }

    private static void printMenu() {
<<<<<<< HEAD
        System.out.println(BOLD_CYAN + "╔═══════════════════════════════════════╗" + RESET);
        System.out.println(BOLD_CYAN + "║" + BOLD_YELLOW + "        MANAGER - MENU CHÍNH           " + RESET + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "╠═══════════════════════════════════════╣" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ THANH TOÁN]                 " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "1. " + WHITE + "Tính hóa đơn cho đơn hàng (B16)   " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "2. " + WHITE + "Xem chi tiết hóa đơn      (B17)   " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "3. " + WHITE + "Ghi nhận thanh toán        (B18)   " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[BÁO CÁO & THỐNG KÊ]                 " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "4. " + WHITE + "Báo cáo doanh thu theo ngày(B19)   " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "5. " + WHITE + "Báo cáo doanh thu theo tháng(B20)  " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "6. " + WHITE + "Shipper tích cực nhất      (B21)   " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "7. " + WHITE + "Thống kê đơn giao thành công(B22)  " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ DỮ LIỆU - CRUD]             " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "8. " + WHITE + "Thêm hóa đơn                       " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "9. " + WHITE + "Sửa hóa đơn                        " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "10." + WHITE + " Xóa hóa đơn                       " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
        System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "0. " + BOLD_WHITE + "Quay lại Menu chính               " + BOLD_CYAN + "║" + RESET);
        System.out.println(BOLD_CYAN + "╚═══════════════════════════════════════╝" + RESET);
=======
        System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "MANAGER - MENU CHÍNH          " + RESET);
        System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ THANH TOÁN]                                 " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE + "Tính hóa đơn cho đơn hàng                         " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE + "Xem chi tiết hóa đơn                               " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE + "Ghi nhận thanh toán                                " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [BÁO CÁO & THỐNG KÊ]                                 " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE + "Báo cáo doanh thu theo ngày                         " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE + "Báo cáo doanh thu theo tháng                        " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE + "Shipper tích cực nhất                               " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE + "Thống kê đơn giao thành công                        " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ DỮ LIỆU - CRUD]                              " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  8. " + WHITE + "Thêm hóa đơn thủ công                              " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  9. " + WHITE + "Sửa trạng thái hóa đơn                             " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  10." + WHITE + " Xóa hóa đơn                                       " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính                                " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
    }

    // ==========================================================
    //  B16 — Tính hóa đơn cho đơn hàng đã giao
    // ==========================================================

    private static void handleCreateInvoice() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B16] Tính hóa đơn cho đơn hàng đã giao ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Tính hóa đơn cho đơn hàng đã giao ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");

        // Kiểm tra đơn hàng tồn tại
        var order = BillingReportService.findOrderById(orderId);
        if (order == null) {
            System.out.println(BOLD_YELLOW + "⚠  Không tìm thấy đơn hàng: " + orderId + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        // Chỉ tạo hóa đơn cho đơn đã Delivered
        if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
            System.out.println(BOLD_YELLOW + "⚠  Chỉ tạo hóa đơn được khi đơn hàng ở trạng thái \"Delivered\"."
                    + "\n   Trạng thái hiện tại: " + order.getStatus() + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        // Kiểm tra hóa đơn đã tồn tại chưa
        if (BillingReportService.invoiceExistsForOrder(orderId)) {
            Invoice existing = BillingReportService.findInvoiceByOrderId(orderId);
            System.out.println(BOLD_YELLOW + "⚠  Hóa đơn đã tồn tại: " + existing.getId() + RESET);
            printInvoiceDetail(existing);
            InputHelper.pressEnterToContinue();
            return;
        }

        Invoice invoice = BillingReportService.createInvoiceForDeliveredOrder(orderId);
        if (invoice == null) {
            System.out.println(BOLD_RED + "❌ Không thể tạo hóa đơn (kiện hàng không tồn tại?)." + RESET);
        } else {
            System.out.println(BOLD_GREEN + "✅ Hóa đơn đã được tạo thành công:" + RESET);
            printInvoiceDetail(invoice);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B17 — Xem chi tiết hóa đơn
    // ==========================================================

    private static void handleViewInvoiceDetails() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B17] Xem chi tiết hóa đơn ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Xem chi tiết hóa đơn ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf

        List<Invoice> invoices = BillingReportService.getAllInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào trong hệ thống.");
            InputHelper.pressEnterToContinue();
            return;
        }

        printInvoiceList(invoices);

        String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn muốn xem: ");
        Invoice invoice = BillingReportService.findInvoiceById(invoiceId);
        if (invoice == null) {
            System.out.println(BOLD_YELLOW + "⚠  Không tìm thấy hóa đơn: " + invoiceId + RESET);
        } else {
            printInvoiceDetail(invoice);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B18 — Ghi nhận thanh toán
    // ==========================================================

    private static void handleRecordPayment() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B18] Ghi nhận thanh toán ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Ghi nhận thanh toán ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf

        // Chỉ hiển thị hóa đơn chưa thanh toán
        List<Invoice> unpaidInvoices = BillingReportService.getUnpaidInvoices();
        if (unpaidInvoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào chưa thanh toán.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("Danh sách hóa đơn chưa thanh toán:");
        printInvoiceList(unpaidInvoices);

        String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn cần ghi nhận thanh toán: ");
        Invoice invoice = BillingReportService.findInvoiceById(invoiceId);
        if (invoice == null) {
            System.out.println(BOLD_YELLOW + "⚠  Không tìm thấy hóa đơn: " + invoiceId + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        // Guard: đã thanh toán rồi
        if ("Paid".equalsIgnoreCase(invoice.getPaymentStatus())) {
            System.out.println(BOLD_YELLOW + "⚠  Hóa đơn này đã được thanh toán trước đó." + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        String paymentMethod = InputHelper.getStringInput("Phương thức thanh toán (Cash/Banking/...): ");
        LocalDate paymentDate = InputHelper.getDateInput("Ngày thanh toán (DD/MM/YYYY)", false);

        boolean success = BillingReportService.recordPayment(invoiceId, paymentMethod, paymentDate);
        if (success) {
            System.out.println(BOLD_GREEN + "✅ Ghi nhận thanh toán thành công cho hóa đơn " + invoiceId + "." + RESET);
        } else {
            System.out.println(BOLD_RED + "❌ Ghi nhận thanh toán thất bại. Vui lòng thử lại." + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B19 — Báo cáo doanh thu theo ngày
    // ==========================================================

    private static void handleDailyRevenueReport() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B19] Báo cáo doanh thu theo ngày ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Báo cáo doanh thu theo ngày ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf

        Map<LocalDate, Double> dailyRevenue = BillingReportService.calculateDailyRevenue();

        System.out.println(BOLD_CYAN + "\n══════════ BÁO CÁO DOANH THU THEO NGÀY ══════════" + RESET);
        System.out.printf("%-15s │ %20s%n", "Ngày thanh toán", "Doanh thu (VND)");
        System.out.println("─".repeat(40));

        if (dailyRevenue.isEmpty()) {
            System.out.println("  Không có hóa đơn đã thanh toán để báo cáo.");
        } else {
            double total = 0.0;
            for (Map.Entry<LocalDate, Double> e : dailyRevenue.entrySet()) {
                System.out.printf("%-15s │ %,20.0f%n", e.getKey(), e.getValue());
                total += e.getValue();
            }
            System.out.println("─".repeat(40));
            System.out.printf("%-15s │ %,20.0f VND%n", "TỔNG CỘNG", total);
        }
        System.out.println("═".repeat(40) + "\n");
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B20 — Báo cáo doanh thu theo tháng
    // ==========================================================

    private static void handleMonthlyRevenueReport() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B20] Báo cáo doanh thu theo tháng ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Báo cáo doanh thu theo tháng ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        System.out.println("Chọn hình thức xem:");
        System.out.println("  1. Tất cả các tháng");
        System.out.println("  2. Theo năm cụ thể");
        int opt = InputHelper.getIntInput("Chọn (1-2): ", 1, 2);

        Map<YearMonth, Double> monthlyRevenue;
        String title;

        if (opt == 2) {
            int year = InputHelper.getIntInput("Nhập năm (VD: 2026): ", 2000, 2100);
            monthlyRevenue = BillingReportService.calculateMonthlyRevenueByYear(year);
            title = "BÁO CÁO DOANH THU THEO THÁNG NĂM " + year;
        } else {
            monthlyRevenue = BillingReportService.calculateMonthlyRevenue();
            title = "BÁO CÁO DOANH THU THEO THÁNG";
        }

        System.out.println(BOLD_CYAN + "\n══════════ " + title + " ══════════" + RESET);
        System.out.printf("%-10s │ %20s%n", "Tháng", "Doanh thu (VND)");
        System.out.println("─".repeat(35));

        if (monthlyRevenue.isEmpty()) {
            System.out.println("  Không có hóa đơn đã thanh toán để báo cáo.");
        } else {
            double total = 0.0;
            for (Map.Entry<YearMonth, Double> e : monthlyRevenue.entrySet()) {
                System.out.printf("%-10s │ %,20.0f%n", e.getKey(), e.getValue());
                total += e.getValue();
            }
            System.out.println("─".repeat(35));
            System.out.printf("%-10s │ %,20.0f VND%n", "TỔNG CỘNG", total);
        }
        System.out.println("═".repeat(35) + "\n");
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B21 — Top Shipper tích cực nhất
    // ==========================================================

    private static void handleTopShippers() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B21] Shipper tích cực nhất ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Shipper tích cực nhất ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf

        List<DeliveryStaff> shippers = BillingReportService.getTopShippers(5);
        if (shippers.isEmpty()) {
            System.out.println("Không có dữ liệu shipper.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println(BOLD_CYAN + "\nBảng xếp hạng Shipper (theo số đơn giao thành công):" + RESET);
        System.out.println("─".repeat(50));
        int rank = 1;
        for (DeliveryStaff s : shippers) {
            System.out.printf("  %d. %-20s (%s) — Đã giao: %d đơn%n",
                    rank++, s.getName(), s.getId(), s.getDeliveredOrdersCount());
        }
        System.out.println("─".repeat(50) + "\n");
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B22 — Thống kê đơn giao thành công
    // ==========================================================

    private static void handleDeliveryStatistics() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── [B22] Thống kê đơn giao hàng ───" + RESET);

        Map<String, Object> stats = BillingReportService.getDeliveryStatistics();

        System.out.println(BOLD_CYAN + "\n══════════ THỐNG KÊ ĐƠN HÀNG ══════════" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Thống kê đơn giao hàng ───" + RESET);

        Map<String, Object> stats = BillingReportService.getDeliveryStatistics();

        System.out.println(BOLD_CYAN + "\n══════════ THỐNG KÊ TỔNG HỢP ĐƠN HÀNG ══════════" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        System.out.printf("  %-28s: %s%n", "Tổng số đơn hàng",       stats.get("totalOrders"));
        System.out.printf("  %-28s: %s%n", "Đã giao thành công",      stats.get("delivered"));
        System.out.printf("  %-28s: %s%n", "Đang giao (In Transit)",  stats.get("inTransit"));
        System.out.printf("  %-28s: %s%n", "Đã phân công (Assigned)", stats.get("assigned"));
        System.out.printf("  %-28s: %s%n", "Chờ xử lý (Pending)",     stats.get("pending"));
        System.out.printf("  %-28s: %s%n", "Giao thất bại (Failed)",  stats.get("failed"));
        System.out.printf("  %-28s: %.1f%%%n", "Tỷ lệ giao thành công", stats.get("successRate"));
<<<<<<< HEAD
        System.out.println("═".repeat(42) + "\n");
=======
        System.out.println("═".repeat(42));

        System.out.println("\nXem danh sách đơn hàng chi tiết:");
        System.out.println("  1. Xem danh sách đơn giao thành công (Delivered)");
        System.out.println("  2. Xem danh sách đơn giao thất bại (Failed)");
        System.out.println("  0. Bỏ qua");
        int detailChoice = InputHelper.getIntInput("Lựa chọn (0-2): ", 0, 2);

        if (detailChoice == 1) {
            List<com.cdms.model.DeliveryOrder> deliveredOrders = com.cdms.repository.DeliveryOrderRepository.findByStatus("Delivered");
            System.out.println(BOLD_CYAN + "\n----------- SUCCESSFULLY DELIVERED ORDERS -----------" + RESET);
            if (deliveredOrders.isEmpty()) {
                System.out.println("  Không có đơn hàng nào giao thành công.");
            } else {
                for (com.cdms.model.DeliveryOrder o : deliveredOrders) {
                    String receiverInfo = "";
                    com.cdms.model.Parcel p = com.cdms.repository.ParcelRepository.findById(o.getParcelId());
                    if (p != null) {
                        receiverInfo = p.getReceiverName();
                    }
                    System.out.printf("  %-5s - %-20s - %s%n", o.getId(), receiverInfo, o.getDeliveryDate() != null ? o.getDeliveryDate() : "Chưa cập nhật");
                }
            }
            System.out.println("-".repeat(50) + "\n");
        } else if (detailChoice == 2) {
            List<com.cdms.model.DeliveryOrder> failedOrders = com.cdms.repository.DeliveryOrderRepository.findByStatus("Failed");
            System.out.println(BOLD_RED + "\n----------- FAILED DELIVERY ORDERS -----------" + RESET);
            if (failedOrders.isEmpty()) {
                System.out.println("  Không có đơn hàng nào giao thất bại.");
            } else {
                for (com.cdms.model.DeliveryOrder o : failedOrders) {
                    String reason = "Không rõ lý do";
                    if (o.getNotes() != null && !o.getNotes().isEmpty()) {
                        reason = o.getNotes().get(o.getNotes().size() - 1);
                    }
                    System.out.printf("  %-5s - %-30s - %s%n", o.getId(), reason, o.getDeliveryDate() != null ? o.getDeliveryDate() : "Chưa cập nhật");
                }
            }
            System.out.println("-".repeat(50) + "\n");
        }
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
<<<<<<< HEAD
    //  CRUD — Thêm / Sửa / Xóa hóa đơn
    // ==========================================================

    private static void handleAddInvoice() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "--- Thêm dữ liệu (Hóa đơn) ---" + RESET);
        String id = InputHelper.getStringInput("Nhập mã hóa đơn (VD: INV-001 hoặc Enter để sinh tự động): ");
=======
    //  CRUD — Thêm hóa đơn thủ công
    //  FIX: Dùng getOptionalStringInput để cho phép tự sinh mã ID.
    //  FIX: Phí được tính TỰ ĐỘNG từ Parcel.calculateFee() — không nhập tay tùy ý.
    // ==========================================================

    private static void handleAddInvoice() {
        System.out.println("\n" + PURPLE + "─── Thêm hóa đơn thủ công ───" + RESET);

        // FIX: Dùng getOptionalStringInput để người dùng CÓ THỂ nhấn Enter để sinh mã tự động
        String id = InputHelper.getOptionalStringInput("Nhập mã hóa đơn (Enter để tự sinh, VD: INV001): ");
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        if (id.isEmpty()) {
            id = BillingReportService.generateNextInvoiceId();
            System.out.println("  ➜ Mã hóa đơn tự sinh: " + id);
        } else if (BillingReportService.findInvoiceById(id) != null) {
<<<<<<< HEAD
            System.out.println(BOLD_YELLOW + "⚠️  Mã hóa đơn này đã tồn tại.\n" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Thêm hóa đơn ───" + RESET);

        String id = InputHelper.getStringInput("Nhập mã hóa đơn (VD: INV-001): ");
        if (BillingReportService.findInvoiceById(id) != null) {
            System.out.println(BOLD_YELLOW + "⚠  Mã hóa đơn \"" + id + "\" đã tồn tại." + RESET);
>>>>>>> 5518f1d (tiep tuc la fix lai code)
=======
            System.out.println(BOLD_YELLOW + "⚠  Mã hóa đơn \"" + id + "\" đã tồn tại." + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
            InputHelper.pressEnterToContinue();
            return;
        }

<<<<<<< HEAD
<<<<<<< HEAD
=======
        // FIX: Bắt buộc lấy mã đơn hàng đã giao để tính phí tự động từ Parcel.calculateFee()
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng tương ứng: ");
        var order = BillingReportService.findOrderById(orderId);
        if (order == null) {
            System.out.println(BOLD_YELLOW + "⚠  Không tìm thấy đơn hàng: " + orderId + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        if (!"Delivered".equalsIgnoreCase(order.getStatus())) {
            System.out.println(BOLD_YELLOW + "⚠  Chỉ lập hóa đơn cho đơn hàng đã giao (Delivered)."
                    + "\n   Trạng thái hiện tại: " + order.getStatus() + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        if (BillingReportService.invoiceExistsForOrder(orderId)) {
            Invoice existing = BillingReportService.findInvoiceByOrderId(orderId);
            System.out.println(BOLD_YELLOW + "⚠  Đơn hàng '" + orderId + "' đã có hóa đơn: " + existing.getId() + RESET);
            printInvoiceDetail(existing);
            InputHelper.pressEnterToContinue();
            return;
        }

        // FIX: Phí được tính TỰ ĐỘNG từ hệ thống (Parcel.calculateFee() + urgentCharge nếu Urgent)
        // Không cho nhập tay để đảm bảo tính nhất quán tài chính
        Invoice suggested = BillingReportService.createInvoice(order);
        if (suggested == null) {
            System.out.println(BOLD_RED + "❌ Không thể tính phí (kiện hàng không tồn tại?)." + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        double baseFee     = suggested.getBaseFee();
        double urgentCharge = suggested.getUrgentCharge();
        double totalAmount  = baseFee + urgentCharge;

        System.out.printf("💡 Phí tự động: Phí cơ bản = %,.0f VND | Phụ phí hỏa tốc = %,.0f VND | Tổng = %,.0f VND%n",
                baseFee, urgentCharge, totalAmount);

<<<<<<< HEAD
        double urgentCharge = InputHelper.getDoubleInput("Nhập phụ phí hỏa tốc (VND): ", 0, Double.MAX_VALUE);
        if (urgentCharge == 0) {
            urgentCharge = suggestedUrgent;
        }

        double totalAmount = baseFee + urgentCharge;
=======
        String orderId      = InputHelper.getStringInput("Nhập mã đơn hàng tương ứng: ");
        double baseFee      = InputHelper.getDoubleInput("Phí cơ bản (VND, >= 0): ", 0, Double.MAX_VALUE);
        double urgentCharge = InputHelper.getDoubleInput("Phụ phí hỏa tốc (VND, >= 0): ", 0, Double.MAX_VALUE);
        double totalAmount  = baseFee + urgentCharge;
>>>>>>> 5518f1d (tiep tuc la fix lai code)

        System.out.println("Trạng thái thanh toán:  1. Unpaid   2. Paid");
        int statusChoice    = InputHelper.getIntInput("Chọn (1-2): ", 1, 2);
        String status       = (statusChoice == 1) ? "Unpaid" : "Paid";
=======
        System.out.println("Trạng thái thanh toán:  1. Unpaid   2. Paid");
        int statusChoice     = InputHelper.getIntInput("Chọn (1-2): ", 1, 2);
        String status        = (statusChoice == 1) ? "Unpaid" : "Paid";
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf

        String paymentMethod = null;
        LocalDate paymentDate = null;
        if ("Paid".equals(status)) {
            paymentMethod = InputHelper.getStringInput("Phương thức thanh toán (Cash/Banking/...): ");
            paymentDate   = InputHelper.getDateInput("Ngày thanh toán (DD/MM/YYYY)", false);
        }

        Invoice newInvoice = new Invoice(id, orderId, baseFee, urgentCharge, totalAmount,
                status, paymentMethod, paymentDate);
<<<<<<< HEAD
        System.out.println(BillingReportService.addInvoice(newInvoice));
=======
        System.out.println(BOLD_GREEN + BillingReportService.addInvoice(newInvoice) + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  CRUD — Sửa trạng thái hóa đơn
    // ==========================================================

    private static void handleUpdateInvoice() {
<<<<<<< HEAD
        System.out.println("\n" + PURPLE + "─── Sửa hóa đơn ───" + RESET);
=======
        System.out.println("\n" + PURPLE + "─── Sửa trạng thái hóa đơn ───" + RESET);
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf

        String id = InputHelper.getStringInput("Nhập mã hóa đơn cần sửa: ");
        Invoice existing = BillingReportService.findInvoiceById(id);
        if (existing == null) {
            System.out.println(BOLD_YELLOW + "⚠  Không tìm thấy hóa đơn: " + id + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("\nThông tin hiện tại:");
        printInvoiceDetail(existing);
        System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)\n");

        // Cập nhật trạng thái — chỉ chấp nhận Unpaid hoặc Paid
        String newStatus;
        while (true) {
            newStatus = InputHelper.getOptionalStringInput(
                    "Trạng thái thanh toán [" + existing.getPaymentStatus() + "] (Unpaid/Paid): ");
            if (newStatus.isEmpty()) {
                newStatus = existing.getPaymentStatus();
                break;
            }
            if ("Unpaid".equalsIgnoreCase(newStatus) || "Paid".equalsIgnoreCase(newStatus)) {
                break;
            }
            System.out.println("  ⚠  Giá trị không hợp lệ. Vui lòng nhập 'Unpaid' hoặc 'Paid'.");
        }

        String paymentMethod = existing.getPaymentMethod();
        LocalDate paymentDate = existing.getPaymentDate();

        if ("Paid".equalsIgnoreCase(newStatus)) {
            // Cập nhật phương thức thanh toán
            String inputMethod = InputHelper.getOptionalStringInput(
                    "Phương thức thanh toán [" + (paymentMethod != null ? paymentMethod : "chưa có") + "]: ");
            if (!inputMethod.isEmpty()) {
                paymentMethod = inputMethod;
            }
            // Cập nhật ngày thanh toán qua InputHelper (an toàn, chuẩn format)
            System.out.println("Ngày thanh toán ["
                    + (paymentDate != null ? paymentDate : "chưa có")
                    + "] — nhấn Enter để bỏ qua:");
            paymentDate = InputHelper.getDateInput("Ngày mới (DD/MM/YYYY)", true);
            if (paymentDate == null) {
                paymentDate = existing.getPaymentDate(); // giữ nguyên nếu bỏ qua
            }
        } else {
            // Khi chuyển sang Unpaid: xóa thông tin thanh toán
            paymentMethod = null;
            paymentDate   = null;
        }

        existing.setPaymentStatus(newStatus);
        existing.setPaymentMethod(paymentMethod);
        existing.setPaymentDate(paymentDate);

        System.out.println(BOLD_GREEN + BillingReportService.updateInvoice(existing) + RESET);
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  CRUD — Xóa hóa đơn
    // ==========================================================

    private static void handleDeleteInvoice() {
        System.out.println("\n" + PURPLE + "─── Xóa hóa đơn ───" + RESET);

        String id = InputHelper.getStringInput("Nhập mã hóa đơn cần xóa: ");
        Invoice existing = BillingReportService.findInvoiceById(id);
        if (existing == null) {
            System.out.println(BOLD_YELLOW + "⚠  Không tìm thấy hóa đơn: " + id + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("\nThông tin hóa đơn:");
        printInvoiceDetail(existing);

        String confirm = InputHelper.getStringInput("Xác nhận xóa hóa đơn \"" + id + "\"? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println(BOLD_GREEN + BillingReportService.deleteInvoice(id) + RESET);
        } else {
            System.out.println(BOLD_YELLOW + "Đã hủy thao tác xóa." + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  PRIVATE HELPER — In danh sách & chi tiết hóa đơn
    // ==========================================================

    /** In danh sách hóa đơn dạng bảng tóm tắt */
    private static void printInvoiceList(List<Invoice> invoices) {
        System.out.println("─".repeat(75));
        System.out.printf("  %-12s %-12s %-16s %15s%n",
                "Mã HĐ", "Mã Đơn", "Trạng thái TT", "Tổng (VND)");
        System.out.println("─".repeat(75));
        for (Invoice inv : invoices) {
            System.out.printf("  %-12s %-12s %-16s %,15.0f%n",
                    inv.getId(), inv.getOrderId(),
                    inv.getPaymentStatus(), inv.getTotalAmount());
        }
        System.out.println("─".repeat(75) + "\n");
    }

    /** In chi tiết một hóa đơn */
    private static void printInvoiceDetail(Invoice inv) {
        System.out.println("─".repeat(45));
<<<<<<< HEAD
        System.out.printf("  %-22s: %s%n",  "Mã hóa đơn",       inv.getId());
        System.out.printf("  %-22s: %s%n",  "Mã đơn hàng",      inv.getOrderId());
        System.out.printf("  %-22s: %,.0f VND%n", "Phí cơ bản", inv.getBaseFee());
        System.out.printf("  %-22s: %,.0f VND%n", "Phụ phí hỏa tốc", inv.getUrgentCharge());
        System.out.printf("  %-22s: %,.0f VND%n", "Tổng phí",   inv.getTotalAmount());
        System.out.printf("  %-22s: %s%n",  "Trạng thái TT",    inv.getPaymentStatus());
=======
        System.out.printf("  %-22s: %s%n",  "Mã hóa đơn",        inv.getId());
        System.out.printf("  %-22s: %s%n",  "Mã đơn hàng",       inv.getOrderId());
        System.out.printf("  %-22s: %,.0f VND%n", "Phí cơ bản",  inv.getBaseFee());
        System.out.printf("  %-22s: %,.0f VND%n", "Phụ phí hỏa tốc", inv.getUrgentCharge());
        System.out.printf("  %-22s: %,.0f VND%n", "Tổng phí",    inv.getTotalAmount());
        System.out.printf("  %-22s: %s%n",  "Trạng thái TT",     inv.getPaymentStatus());
>>>>>>> 2e7296aa199894929ca8beb2f896d33934ddeecf
        System.out.printf("  %-22s: %s%n",  "Phương thức TT",
                inv.getPaymentMethod() != null ? inv.getPaymentMethod() : "Chưa TT");
        System.out.printf("  %-22s: %s%n",  "Ngày TT",
                inv.getPaymentDate() != null ? inv.getPaymentDate() : "Chưa TT");
        System.out.println("─".repeat(45));
    }
}
