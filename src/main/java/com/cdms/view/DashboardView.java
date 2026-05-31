// ============================================================
// File: DashboardView.java
// Package: com.cdms.view
// Description: Giao diện bảng điều khiển trung tâm (Dashboard)
//              hệ thống CDMS hiển thị thống kê thời gian thực.
//              Thiết kế bởi Senior UI Designer.
// ============================================================
package com.cdms.view;

import com.cdms.model.DeliveryStaff;
import com.cdms.model.Invoice;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.ParcelRepository;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.DeliveryStaffRepository;
import com.cdms.repository.InvoiceRepository;

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
                // 1. Tính toán các chỉ số thời gian thực từ các Repositories (BUG-09)
                int totalCustomers = CustomerRepository.findAll().size();
                int totalParcels = ParcelRepository.findAll().size();
                int totalOrders = DeliveryOrderRepository.findAll().size();

                // Đếm số lượng shipper đang hoạt động
                long activeShippersCount = DeliveryStaffRepository.findAll().stream()
                                .filter(s -> "Active".equalsIgnoreCase(s.getStatus()))
                                .count();
                int totalShippers = DeliveryStaffRepository.findAll().size();

                // Tính toán doanh thu thực tế đã thanh toán (Paid Invoices)
                double totalRevenue = InvoiceRepository.findPaidInvoices().stream()
                                .mapToDouble(Invoice::getTotalAmount)
                                .sum();

                // Đếm số lượng đơn theo các trạng thái (UX-12)
                long deliveredOrders = DeliveryOrderRepository.countByStatus("Delivered");
                long inTransitOrders = DeliveryOrderRepository.countByStatus("In Transit");
                long failedOrders = DeliveryOrderRepository.countByStatus("Failed");
                long cancelledOrders = DeliveryOrderRepository.countByStatus("Cancelled");
                long assignedOrders = DeliveryOrderRepository.countByStatus("Assigned")
                                + DeliveryOrderRepository.countByStatus("Picked Up");

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
                                + "╠══════════════════════════════════════╬═══════════════════════════════════╣"
                                + RESET);

                System.out.printf(
                                BOLD_YELLOW + "║" + RESET + "  ⚡ Đang vận chuyển: " + BOLD_CYAN + "%-16d" + RESET
                                                + BOLD_YELLOW + "║" + RESET + "  📋 Đơn đã phân công: " + BOLD_WHITE
                                                + "%-12d" + RESET + BOLD_YELLOW + "║%n" + RESET,
                                inTransitOrders, assignedOrders);

                System.out.printf(
                                BOLD_YELLOW + "║" + RESET + "  ❌ Giao thất bại  : " + BOLD_RED + "%-16d" + RESET
                                                + BOLD_YELLOW + "║" + RESET + "  🚫 Đơn hàng đã hủy : " + BOLD_RED
                                                + "%-12d" + RESET + BOLD_YELLOW + "║%n" + RESET,
                                failedOrders, cancelledOrders);

                System.out.println(BOLD_YELLOW
                                + "╚══════════════════════════════════════╩═══════════════════════════════════╝"
                                + RESET);
                System.out.println();
        }
}
