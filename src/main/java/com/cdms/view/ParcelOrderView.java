// ============================================================
// File: ParcelOrderView.java
// Package: com.cdms.view
// Description: Giao diện Console bổ sung cho phân hệ Kiện hàng
//              & Đơn giao hàng (dành cho các chức năng mà
//              Reception chưa phủ hết hoặc cần xem riêng).
//              🔧 BÀN GIAO CHO: Trương Đan Huy (Developer B - Thành viên 3)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class ParcelOrderView {

    // ANSI Colors for premium visual presentation
    private static final String RESET        = "\u001B[0m";
    private static final String BOLD_CYAN    = "\u001B[1;36m";
    private static final String BOLD_YELLOW  = "\u001B[1;33m";
    private static final String BOLD_GREEN   = "\u001B[1;32m";
    private static final String BOLD_RED     = "\u001B[1;31m";
    private static final String BOLD_WHITE   = "\u001B[1;37m";
    private static final String WHITE        = "\u001B[37m";

    // Ngăn khởi tạo đối tượng
    private ParcelOrderView() {
    }

    // ==========================================================
    //  SUBMENU: QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG (Chi tiết)
    // ==========================================================

    /**
     * Menu quản lý chi tiết kiện hàng và đơn hàng.
     * Trương Đan Huy (Thành viên 3) có thể mở rộng menu này để thêm các tính năng
     * nâng cao như: tìm kiếm đơn, hủy đơn, xem lịch sử KH...
     */
    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE + "Thêm kiện hàng mới           (B4)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE + "Xem danh sách kiện hàng      (B5)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE + "Tạo đơn giao hàng mới        (B6)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE + "Cập nhật đơn giao hàng        (B7)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE + "Xem chi tiết đơn giao hàng    (B8)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE + "Tìm kiếm đơn theo KH                               " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE + "Hủy đơn giao hàng                                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính                                " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-7): " + RESET, 0, 7);

            switch (choice) {
                case 1:
                    System.out.println("  🔧 [B4] Chức năng 'Thêm kiện hàng' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
                    break;
                case 2:
                    System.out.println("  🔧 [B5] Chức năng 'Xem DS kiện hàng' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
                    break;
                case 3:
                    System.out.println("  🔧 [B6] Chức năng 'Tạo đơn giao hàng' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
                    break;
                case 4:
                    System.out.println("  🔧 [B7] Chức năng 'Cập nhật đơn' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
                    break;
                case 5:
                    System.out.println("  🔧 [B8] Chức năng 'Xem chi tiết đơn' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
                    break;
                case 6:
                    System.out.println("  🔧 Chức năng 'Tìm kiếm đơn theo KH' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
                    break;
                case 7:
                    System.out.println("  🔧 Chức năng 'Hủy đơn giao hàng' đang được phát triển bởi Trương Đan Huy (Thành viên 3).\n");
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
}
