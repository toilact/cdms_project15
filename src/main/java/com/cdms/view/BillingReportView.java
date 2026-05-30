// ============================================================
// File: BillingReportView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Thanh toán &
//              Báo cáo thống kê. Phục vụ vai trò Manager.
//              Phân công: Huỳnh Lê Quốc Cường (Developer D - Thành viên 5)
// ============================================================
package com.cdms.view;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.cdms.core.InputHelper;
import com.cdms.core.FormCancelledException;
import com.cdms.model.DeliveryOrder;
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
            int choice = InputHelper.getIntInput("Chọn chức năng (0-12): ", 0, 12);
            switch (choice) {
                case 1:  handleCreateInvoice();        break; // B16
                case 2:  handleViewInvoiceDetails();   break; // B17
                case 3:  handleRecordPayment();        break; // B18
                case 4:  handleReconcileCOD();         break; // COD Reconciliation (Stage 3)
                case 5:  handleDailyRevenueReport();   break; // B19
                case 6:  handleMonthlyRevenueReport(); break; // B20
                case 7:  handleTopShippers();          break; // B21
                case 8:  handleDeliveryStatistics();   break; // B22
                case 9:  handleAddInvoice();           break; // CRUD Add
                case 10: handleUpdateInvoice();        break; // CRUD Update
                case 11: handleDeleteInvoice();        break; // CRUD Delete
                case 12: handleListAllInvoices();      break; // UX-10 List all
                case 0:
                    running = false;
                    System.out.println("  ↩ Quay lại Menu chính...\n");
                    break;
                default: break;
            }
        }
    }

    private static void printMenu() {
        System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "MANAGER - MENU CHÍNH          " + RESET);
        System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ THANH TOÁN]                                 " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE + "Tính hóa đơn cho đơn hàng                         " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE + "Xem chi tiết một hóa đơn (B17)                     " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE + "Ghi nhận thanh toán trực tiếp                      " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE + "Đối soát tiền COD từ Shipper                       " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [BÁO CÁO & THỐNG KÊ]                                 " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE + "Báo cáo doanh thu theo ngày                         " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE + "Báo cáo doanh thu theo tháng                        " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE + "Shipper tích cực nhất                               " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  8. " + WHITE + "Thống kê đơn giao thành công                        " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ DỮ LIỆU - CRUD & TIỆN ÍCH]                   " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  9. " + WHITE + "Thêm hóa đơn thủ công                              " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  10." + WHITE + " Sửa trạng thái hóa đơn                             " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  11." + WHITE + " Xóa hóa đơn                                       " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  12." + WHITE + " Xem danh sách tất cả hóa đơn (UX-10)              " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
        System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính                                " + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);
    }

    // ==========================================================
    //  B16 — Tính hóa đơn cho đơn hàng đã giao
    // ==========================================================

    private static void handleCreateInvoice() {
        System.out.println("\n" + PURPLE + "─── Tính hóa đơn cho đơn hàng đã giao ───" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ",
                    val -> {
                        var o = BillingReportService.findOrderById(val);
                        return o != null && "Delivered".equalsIgnoreCase(o.getStatus()) 
                                && !BillingReportService.invoiceExistsForOrder(val);
                    },
                    "Đơn hàng không tồn tại, chưa ở trạng thái 'Delivered', hoặc đã có hóa đơn!");

            System.out.println("\nXác nhận tạo hóa đơn cho đơn hàng này?");
            System.out.println("  1. Tạo hóa đơn");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                Invoice invoice = BillingReportService.createInvoiceForDeliveredOrder(orderId);
                if (invoice == null) {
                    System.out.println(BOLD_RED + "❌ Không thể tạo hóa đơn." + RESET);
                } else {
                    System.out.println(BOLD_GREEN + "✅ Hóa đơn đã được tạo thành công:" + RESET);
                    printInvoiceDetail(invoice);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B17 — Xem chi tiết hóa đơn
    // ==========================================================

    private static void handleViewInvoiceDetails() {
        System.out.println("\n" + PURPLE + "─── Xem chi tiết hóa đơn ───" + RESET);

        List<Invoice> invoices = BillingReportService.getAllInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào trong hệ thống.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn muốn xem: ",
                    val -> BillingReportService.findInvoiceById(val) != null,
                    "Không tìm thấy hóa đơn này!");
            Invoice invoice = BillingReportService.findInvoiceById(invoiceId);
            printInvoiceDetail(invoice);
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    private static void handleListAllInvoices() {
        System.out.println("\n" + PURPLE + "─── Xem danh sách hóa đơn ───" + RESET);
        List<Invoice> invoices = BillingReportService.getAllInvoices();
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào trong hệ thống.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("+------------+------------+-----------------+--------------------+");
        System.out.printf("| %-10s | %-10s | %-15s | %-18s |%n",
                "Mã HĐ", "Mã Đơn", "Trạng thái TT", "Tổng (VND)");
        System.out.println("+------------+------------+-----------------+--------------------+");

        List<String> formatted = invoices.stream()
                .map(inv -> String.format("| %-10s | %-10s | %-15s | %,18.0f |",
                        inv.getId(), inv.getOrderId(),
                        inv.getPaymentStatus(), inv.getTotalAmount()))
                .toList();

        InputHelper.printPaginatedList(formatted, 10, "+------------+------------+-----------------+--------------------+");
        System.out.println(BOLD_GREEN + "Tổng số hóa đơn: " + invoices.size() + RESET);
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B18 — Ghi nhận thanh toán
    // ==========================================================

    private static void handleRecordPayment() {
        System.out.println("\n" + PURPLE + "─── Ghi nhận thanh toán ───" + RESET);

        List<Invoice> unpaidInvoices = BillingReportService.getUnpaidInvoices();
        if (unpaidInvoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào chưa thanh toán.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("Danh sách hóa đơn chưa thanh toán:");
        printInvoiceList(unpaidInvoices);

        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn cần ghi nhận thanh toán: ",
                    val -> {
                        Invoice inv = BillingReportService.findInvoiceById(val);
                        return inv != null && "Unpaid".equalsIgnoreCase(inv.getPaymentStatus());
                    },
                    "Mã hóa đơn không tồn tại hoặc đã được thanh toán!");

            String paymentMethod = InputHelper.getStringInput("Phương thức thanh toán (Cash/Banking/...): ");
            LocalDate paymentDate = InputHelper.getDateInput("Ngày thanh toán (DD/MM/YYYY)", false);

            System.out.println("\nXác nhận ghi nhận thanh toán?");
            System.out.println("  1. Xác nhận");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                boolean success = BillingReportService.recordPayment(invoiceId, paymentMethod, paymentDate);
                if (success) {
                    System.out.println(BOLD_GREEN + "✅ Ghi nhận thanh toán thành công cho hóa đơn " + invoiceId + "." + RESET);
                } else {
                    System.out.println(BOLD_RED + "❌ Ghi nhận thanh toán thất bại. Vui lòng thử lại." + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy ghi nhận thanh toán." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  Đối soát tiền COD (Chuyển trạng thái Collected sang Paid)
    // ==========================================================

    private static void handleReconcileCOD() {
        System.out.println("\n" + PURPLE + "─── Đối soát tiền COD từ Shipper ───" + RESET);

        List<Invoice> collectedInvoices = BillingReportService.getCollectedInvoices();
        if (collectedInvoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào đang ở trạng thái 'Collected' (Đã thu COD bởi Shipper) cần đối soát.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("Danh sách hóa đơn đã thu hộ COD chưa đối soát:");
        printInvoiceList(collectedInvoices);

        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String invoiceId = InputHelper.getStringInput("Nhập mã hóa đơn cần đối soát: ",
                    val -> {
                        Invoice inv = BillingReportService.findInvoiceById(val);
                        return inv != null && "Collected".equalsIgnoreCase(inv.getPaymentStatus());
                    },
                    "Mã hóa đơn không tồn tại hoặc không ở trạng thái 'Collected'!");

            String paymentMethod = InputHelper.getStringInput("Phương thức thanh toán bàn giao (Cash/Banking/...): ");
            LocalDate paymentDate = InputHelper.getDateInput("Ngày đối soát nhận tiền (DD/MM/YYYY)", false);

            System.out.println("\nXác nhận hoàn tất đối soát cho hóa đơn này? (Chuyển sang trạng thái 'Paid')");
            System.out.println("  1. Xác nhận đối soát");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                boolean success = BillingReportService.reconcileInvoice(invoiceId, paymentMethod, paymentDate);
                if (success) {
                    System.out.println(BOLD_GREEN + "✅ Đối soát thành công! Hóa đơn " + invoiceId + " đã được chuyển sang trạng thái Paid." + RESET);
                } else {
                    System.out.println(BOLD_RED + "❌ Đối soát thất bại. Vui lòng thử lại." + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác đối soát." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B19 — Báo cáo doanh thu theo ngày
    // ==========================================================

    private static void handleDailyRevenueReport() {
        System.out.println("\n" + PURPLE + "─── Báo cáo doanh thu theo ngày ───" + RESET);

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
        System.out.println("\n" + PURPLE + "─── Báo cáo doanh thu theo tháng ───" + RESET);
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
        System.out.println("\n" + PURPLE + "─── Shipper tích cực nhất ───" + RESET);
        System.out.println("Hình thức xem xếp hạng:");
        System.out.println("  1. Tất cả thời gian (Lũy kế)");
        System.out.println("  2. Lọc theo khoảng thời gian (BR19 - Selected Period)");
        int opt = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

        List<DeliveryStaff> shippers;
        String tableTitle;

        if (opt == 2) {
            System.out.println("\n(Nhập 'cancel' để hủy)");
            try {
                LocalDate start = InputHelper.getDateInput("Ngày bắt đầu (DD/MM/YYYY)", false);
                LocalDate end = InputHelper.getDateInput("Ngày kết thúc (DD/MM/YYYY)", false);
                shippers = BillingReportService.getTopShippersInPeriod(start, end, 5);
                tableTitle = String.format("Bảng xếp hạng Shipper tích cực từ %s đến %s:", start, end);
            } catch (FormCancelledException e) {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác lọc." + RESET);
                InputHelper.pressEnterToContinue();
                return;
            } catch (Exception e) {
                System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                InputHelper.pressEnterToContinue();
                return;
            }
        } else {
            shippers = BillingReportService.getTopShippers(5);
            tableTitle = "Bảng xếp hạng Shipper tích cực (Mọi thời đại):";
        }

        if (shippers.isEmpty()) {
            System.out.println("Không có dữ liệu shipper nào hoàn thành giao đơn trong giai đoạn này.");
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println(BOLD_CYAN + "\n" + tableTitle + RESET);
        System.out.println("─".repeat(55));
        int rank = 1;
        for (DeliveryStaff s : shippers) {
            System.out.printf("  %d. %-20s (%s) — Đã giao: %d đơn%n",
                    rank++, s.getName(), s.getId(), s.getDeliveredOrdersCount());
        }
        System.out.println("─".repeat(55) + "\n");
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  B22 — Thống kê đơn giao thành công
    // ==========================================================

    private static void handleDeliveryStatistics() {
        System.out.println("\n" + PURPLE + "─── Thống kê đơn giao hàng ───" + RESET);

        Map<String, Object> stats = BillingReportService.getDeliveryStatistics();

        System.out.println(BOLD_CYAN + "\n══════════ THỐNG KÊ TỔNG HỢP ĐƠN HÀNG ══════════" + RESET);
        System.out.printf("  %-28s: %s%n", "Tổng số đơn hàng",       stats.get("totalOrders"));
        System.out.printf("  %-28s: %s%n", "Đã giao thành công",      stats.get("delivered"));
        System.out.printf("  %-28s: %s%n", "Đang giao (In Transit)",  stats.get("inTransit"));
        System.out.printf("  %-28s: %s%n", "Đã phân công (Assigned)", stats.get("assigned"));
        System.out.printf("  %-28s: %s%n", "Chờ xử lý (Pending)",     stats.get("pending"));
        System.out.printf("  %-28s: %s%n", "Giao thất bại (Failed)",  stats.get("failed"));
        System.out.printf("  %-28s: %.1f%%%n", "Tỷ lệ giao thành công", stats.get("successRate"));
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
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  CRUD — Thêm hóa đơn thủ công
    // ==========================================================

    private static void handleAddInvoice() {
        System.out.println("\n" + PURPLE + "─── Thêm hóa đơn thủ công ───" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getOptionalStringInput("Nhập mã hóa đơn (Enter để tự sinh, VD: INV001): ");
            if (id.isEmpty()) {
                id = BillingReportService.generateNextInvoiceId();
                System.out.println("  ➜ Mã hóa đơn tự sinh: " + id);
            } else if (BillingReportService.findInvoiceById(id) != null) {
                System.out.println(BOLD_YELLOW + "⚠  Mã hóa đơn \"" + id + "\" đã tồn tại." + RESET);
                InputHelper.pressEnterToContinue();
                return;
            }

            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng tương ứng: ",
                    val -> {
                        var o = BillingReportService.findOrderById(val);
                        return o != null && "Delivered".equalsIgnoreCase(o.getStatus()) 
                                && !BillingReportService.invoiceExistsForOrder(val);
                    },
                    "Đơn hàng không tồn tại, chưa 'Delivered' hoặc đã có hóa đơn!");

            var order = BillingReportService.findOrderById(orderId);
            Invoice suggested = BillingReportService.createInvoice(order);
            if (suggested == null) {
                System.out.println(BOLD_RED + "❌ Không thể tính phí." + RESET);
                InputHelper.pressEnterToContinue();
                return;
            }

            double baseFee     = suggested.getBaseFee();
            double urgentCharge = suggested.getUrgentCharge();
            double totalAmount  = baseFee + urgentCharge;

            System.out.printf("💡 Phí tự động: Phí cơ bản = %,.0f VND | Phụ phí hỏa tốc = %,.0f VND | Tổng = %,.0f VND%n",
                    baseFee, urgentCharge, totalAmount);

            System.out.println("Trạng thái thanh toán:  1. Unpaid   2. Partially Paid   3. Paid");
            int statusChoice     = InputHelper.getIntInput("Chọn (1-3): ", 1, 3);
            String status;
            if (statusChoice == 1) {
                status = "Unpaid";
            } else if (statusChoice == 2) {
                status = "Partially Paid";
            } else {
                status = "Paid";
            }

            String paymentMethod = null;
            LocalDate paymentDate = null;
            if ("Paid".equals(status) || "Partially Paid".equals(status)) {
                paymentMethod = InputHelper.getStringInput("Phương thức thanh toán (Cash/Banking/...): ");
                
                // BUG-12: Ràng buộc ngày thanh toán không được trước ngày giao đơn hàng thực tế
                while (true) {
                    paymentDate = InputHelper.getDateInput("Ngày thanh toán (DD/MM/YYYY)", false);
                    if (order.getDeliveryDate() != null && paymentDate.isBefore(order.getDeliveryDate())) {
                        System.out.println(BOLD_RED + "  ⚠ Lỗi: Ngày thanh toán không được trước ngày giao đơn hàng thực tế (" + order.getDeliveryDate() + ")! Vui lòng nhập lại." + RESET);
                        continue;
                    }
                    break;
                }
            }

            System.out.println("\nXác nhận thêm hóa đơn mới này?");
            System.out.println("  1. Đồng ý");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                Invoice newInvoice = new Invoice(id, orderId, baseFee, urgentCharge, totalAmount,
                        status, paymentMethod, paymentDate);
                System.out.println(BOLD_GREEN + BillingReportService.addInvoice(newInvoice) + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác thêm hóa đơn thủ công." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  CRUD — Sửa trạng thái hóa đơn
    // ==========================================================

    private static void handleUpdateInvoice() {
        System.out.println("\n" + PURPLE + "─── Sửa trạng thái hóa đơn ───" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Nhập mã hóa đơn cần sửa: ",
                    val -> BillingReportService.findInvoiceById(val) != null,
                    "Mã hóa đơn không tồn tại!");
            Invoice existing = BillingReportService.findInvoiceById(id);

            System.out.println("\nThông tin hiện tại:");
            printInvoiceDetail(existing);
            System.out.println("(Nhấn Enter để giữ nguyên giá trị cũ)\n");

            String newStatus = InputHelper.getOptionalValidatedStringInput(
                    "Trạng thái thanh toán [" + existing.getPaymentStatus() + "] (Unpaid/Partially Paid/Paid): ",
                    val -> val.equalsIgnoreCase("Unpaid") || val.equalsIgnoreCase("Partially Paid") || val.equalsIgnoreCase("Paid"),
                    "Vui lòng nhập 'Unpaid', 'Partially Paid' hoặc 'Paid'!");
            if (newStatus.isEmpty()) {
                newStatus = existing.getPaymentStatus();
            }

            String paymentMethod = existing.getPaymentMethod();
            LocalDate paymentDate = existing.getPaymentDate();

            if ("Paid".equalsIgnoreCase(newStatus) || "Partially Paid".equalsIgnoreCase(newStatus)) {
                String inputMethod = InputHelper.getOptionalStringInput(
                        "Phương thức thanh toán [" + (paymentMethod != null ? paymentMethod : "chưa có") + "]: ");
                if (!inputMethod.isEmpty()) {
                    paymentMethod = inputMethod;
                }
                
                DeliveryOrder order = BillingReportService.findOrderById(existing.getOrderId());
                while (true) {
                    System.out.println("Ngày thanh toán ["
                            + (paymentDate != null ? paymentDate : "chưa có")
                            + "] — nhấn Enter để giữ nguyên:");
                    LocalDate inputDate = InputHelper.getDateInput("Ngày mới (DD/MM/YYYY)", true);
                    if (inputDate != null) {
                        if (order != null && order.getDeliveryDate() != null && inputDate.isBefore(order.getDeliveryDate())) {
                            System.out.println(BOLD_RED + "  ⚠ Lỗi: Ngày thanh toán không được trước ngày giao đơn hàng thực tế (" + order.getDeliveryDate() + ")! Vui lòng nhập lại." + RESET);
                            continue;
                        }
                        paymentDate = inputDate;
                    }
                    break;
                }
            } else {
                paymentMethod = null;
                paymentDate   = null;
            }

            System.out.println("\nXác nhận cập nhật thông tin hóa đơn?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                existing.setPaymentStatus(newStatus);
                existing.setPaymentMethod(paymentMethod);
                existing.setPaymentDate(paymentDate);

                System.out.println(BOLD_GREEN + BillingReportService.updateInvoice(existing) + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác sửa hóa đơn." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  CRUD — Xóa hóa đơn
    // ==========================================================

    private static void handleDeleteInvoice() {
        System.out.println("\n" + PURPLE + "─── Xóa hóa đơn ───" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Nhập mã hóa đơn cần xóa: ",
                    val -> BillingReportService.findInvoiceById(val) != null,
                    "Không tìm thấy hóa đơn!");
            Invoice existing = BillingReportService.findInvoiceById(id);

            System.out.println("\nThông tin hóa đơn:");
            printInvoiceDetail(existing);

            System.out.println("\nXác nhận xóa hóa đơn này?");
            System.out.println("  1. Xóa");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                System.out.println(BOLD_GREEN + BillingReportService.deleteInvoice(id) + RESET);
            } else {
                System.out.println(BOLD_YELLOW + "Đã hủy thao tác xóa." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    // ==========================================================
    //  PRIVATE HELPER — In danh sách & chi tiết hóa đơn
    // ==========================================================

    private static void printInvoiceList(List<Invoice> invoices) {
        System.out.println("+------------+------------+-----------------+--------------------+");
        System.out.printf("| %-10s | %-10s | %-15s | %-18s |%n",
                "Mã HĐ", "Mã Đơn", "Trạng thái TT", "Tổng (VND)");
        System.out.println("+------------+------------+-----------------+--------------------+");
        for (Invoice inv : invoices) {
            System.out.printf("| %-10s | %-10s | %-15s | %,18.0f |%n",
                    inv.getId(), inv.getOrderId(),
                    inv.getPaymentStatus(), inv.getTotalAmount());
        }
        System.out.println("+------------+------------+-----------------+--------------------+\n");
    }

    private static void printInvoiceDetail(Invoice inv) {
        System.out.println("─".repeat(45));
        System.out.printf("  %-22s: %s%n",  "Mã hóa đơn",        inv.getId());
        System.out.printf("  %-22s: %s%n",  "Mã đơn hàng",       inv.getOrderId());
        System.out.printf("  %-22s: %,.0f VND%n", "Phí cơ bản",  inv.getBaseFee());
        System.out.printf("  %-22s: %,.0f VND%n", "Phụ phí hỏa tốc", inv.getUrgentCharge());
        System.out.printf("  %-22s: %,.0f VND%n", "Tổng phí",    inv.getTotalAmount());
        System.out.printf("  %-22s: %s%n",  "Trạng thái TT",     inv.getPaymentStatus());
        System.out.printf("  %-22s: %s%n",  "Phương thức TT",
                inv.getPaymentMethod() != null ? inv.getPaymentMethod() : "Chưa TT");
        System.out.printf("  %-22s: %s%n",  "Ngày TT",
                inv.getPaymentDate() != null ? inv.getPaymentDate() : "Chưa TT");
        System.out.println("─".repeat(45));
    }
}
