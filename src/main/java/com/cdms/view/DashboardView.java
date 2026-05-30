// ============================================================
// File: DashboardView.java
// Package: com.cdms.view
// Description: Giao diện bảng điều khiển trung tâm (Dashboard)
//              hệ thống CDMS hiển thị thống kê thời gian thực.
//              Thiết kế bởi Senior UI Designer.
// ============================================================
package com.cdms.view;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.Invoice;

public class DashboardView {

        // ANSI Colors for premium visual presentation
        private static final String RESET = "\u001B[0m";
        private static final String BOLD_CYAN = "\u001B[1;36m";
        private static final String BOLD_YELLOW = "\u001B[1;33m";
        private static final String BOLD_GREEN = "\u001B[1;32m";
        private static final String BOLD_RED = "\u001B[1;31m";
        private static final String BOLD_WHITE = "\u001B[1;37m";
        private static final String BOLD_PURPLE = "\u001B[1;35m";
        private static final String WHITE = "\u001B[37m";

        // Ngăn khởi tạo đối tượng
        private DashboardView() {
        }

        /**
         * Hiển thị bảng điều khiển thống kê thời gian thực (Real-time Live Stats)
         * với phong cách thiết kế UI bảng Console tối giản và sang trọng bậc nhất.
         */
        public static void showDashboard() {
                // 1. Tính toán các chỉ số thời gian thực từ JSONDataManager
                int totalCustomers = JSONDataManager.customers.size();
                int totalParcels = JSONDataManager.parcels.size();
                int totalOrders = JSONDataManager.orders.size();

                // Đếm số lượng shipper đang hoạt động
                long activeShippersCount = JSONDataManager.staffs.stream()
                                .filter(s -> "Active".equalsIgnoreCase(s.getStatus()))
                                .count();
                int totalShippers = JSONDataManager.staffs.size();

                // Tính toán doanh thu thực tế đã thanh toán (Paid Invoices)
                double totalRevenue = JSONDataManager.invoices.stream()
                                .filter(i -> "Paid".equalsIgnoreCase(i.getPaymentStatus()))
                                .mapToDouble(Invoice::getTotalAmount)
                                .sum();

                // Đếm số lượng đơn giao thành công (Delivered)
                long deliveredOrders = JSONDataManager.orders.stream()
                                .filter(o -> "Delivered".equalsIgnoreCase(o.getStatus()))
                                .count();

                // 2. In Banner nghệ thuật CDMS phối màu mượt mà
                System.out.println(BOLD_CYAN + "   ▄████████ ▀█████████▄   ▄▄▄▄███▄▄▄▄    ▄████████ " + RESET);
                System.out.println(BOLD_CYAN + "  ███    ███   ███    ███ ▄██▀▀███▀▀██▄  ███    ███ " + RESET);
                System.out.println(BOLD_CYAN + "  ███    █▀    ███    ███ ███  ███  ███  ███    █▀  " + RESET);
                System.out.println(BOLD_YELLOW + "  ███          ███    ███ ███  ███  ███  ███        " + RESET);
                System.out.println(BOLD_YELLOW + "  ███          ███    ███ ███  ███  ███  ▀█████████▄" + RESET);
                System.out.println(BOLD_PURPLE + "  ███    █▄    ███    ███ ███  ███  ███           ███" + RESET);
                System.out.println(BOLD_PURPLE + "  ███    ███   ███    ███ ███  ███  ███     ▄█    ███" + RESET);
                System.out.println(BOLD_CYAN + "  ████████▀  ▄█████████▀   ▀█  ███  █▀    ▄████████▀ " + RESET);
                System.out.println(BOLD_WHITE + "   🚚 GIẢI PHÁP QUẢN LÝ GIAO HÀNG CHUYÊN NGHIỆP  " + RESET);
                System.out.println();

                // 3. Thiết kế khung thẻ thông tin (Dynamic Data Cards) cực kỳ sang trọng
                System.out.println(BOLD_YELLOW
                                + "╔══════════════════════════════════════════════════════════════════════════╗"
                                + RESET);
                System.out.println(BOLD_YELLOW + "║" + BOLD_YELLOW
                                + "                    📊 DỰ ÁN CDMS - GIÁM SÁT THỜI GIAN THỰC               "
                                + BOLD_YELLOW + "║" + RESET);
                System.out.println(BOLD_YELLOW
                                + "╠══════════════════════════════════════╦═══════════════════════════════════╣"
                                + RESET);

                System.out.printf(
                                BOLD_YELLOW + "║" + RESET + "  👥 Khách hàng    : " + BOLD_WHITE + "%-17d" + RESET
                                                + BOLD_YELLOW + "║" + RESET + "  📦 Tổng kiện hàng : " + BOLD_WHITE
                                                + "%-13d" + RESET + BOLD_YELLOW + "║%n" + RESET,
                                totalCustomers, totalParcels);

                System.out.printf(BOLD_YELLOW + "║" + RESET + "  🚚 Hoạt động/Tổng: " + BOLD_GREEN + "%-2d" + RESET
                                + "/" + BOLD_WHITE + "%-14d" + RESET + BOLD_YELLOW + "║" + RESET
                                + "  📝 Tổng đơn hàng  : " + BOLD_WHITE + "%-13d" + RESET + BOLD_YELLOW + "║%n" + RESET,
                                activeShippersCount, totalShippers, totalOrders);

                System.out.printf(
                                BOLD_YELLOW + "║" + RESET + "  ✅ Đơn giao xong : " + BOLD_GREEN + "%-17d" + RESET
                                                + BOLD_YELLOW + "║" + RESET + "  💰 Thực thu (VND) : " + BOLD_GREEN
                                                + "%,-13.0f" + RESET + BOLD_YELLOW + "║%n" + RESET,
                                deliveredOrders, totalRevenue);

                System.out.println(BOLD_YELLOW
                                + "╚══════════════════════════════════════╩═══════════════════════════════════╝"
                                + RESET);
                System.out.println();
        }
}
