// ============================================================
// File: DispatcherView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Điều phối viên (Dispatcher).
//              Quản lý việc gán shipper, cập nhật hành trình,
//              và theo dõi trạng thái giao nhận.
//              🔧 BÀN GIAO CHO: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class DispatcherView {

    // ANSI Colors for beautiful UI
    private static final String RESET = "\u001B[0m";
    private static final String BLUE_BG = "\u001B[44m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD_WHITE = "\u001B[1;37m";
    private static final String PURPLE = "\u001B[35m";

    // Ngăn khởi tạo đối tượng
    private DispatcherView() {
    }

    // ==========================================================
    //  SUBMENU: DISPATCHER (Điều phối viên)
    //  Các tính năng: B9, B10, B11, B12, B13, B14
    // ==========================================================

    /**
     * Menu chính cho vai trò Dispatcher.
     * Dispatcher có quyền: thêm shipper, phân công đơn,
     * cập nhật trạng thái, xem đơn đang giao/thất bại.
     */
    public static void showDispatcherMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_CYAN + "╔═══════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_CYAN + "║" + BOLD_YELLOW + "       DISPATCHER - MENU CHÍNH         " + RESET + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╠═══════════════════════════════════════╣" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ SHIPPER]                    " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "1. " + WHITE + "Thêm nhân viên giao hàng   (B9)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "2. " + WHITE + "Phân công đơn cho shipper  (B10)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "3. " + WHITE + "Xem đơn đã giao của shipper(B11)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[THEO DÕI GIAO HÀNG]                 " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "4. " + WHITE + "Cập nhật trạng thái đơn    (B12)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "5. " + WHITE + "Hiển thị đơn đang giao     (B13)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "6. " + WHITE + "Hiển thị đơn giao thất bại (B14)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "0. " + BOLD_WHITE + "Quay lại Menu chính               " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╚═══════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput("Chọn chức năng (0-6): ", 0, 6);

            switch (choice) {
                case 1:
                    // TODO: Nguyên Quốc Cường (Thành viên 2) - Gọi StaffService.addStaff()
                    System.out.println(PURPLE + "  🔧 [B9] Chức năng 'Thêm shipper' đang được phát triển bởi Nguyên Quốc Cường (Thành viên 2).\n" + RESET);
                    break;
                case 2:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.assignStaff()
                    System.out.println(PURPLE + "  🔧 [B10] Chức năng 'Phân công đơn' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 3:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.viewDeliveredByStaff()
                    System.out.println(PURPLE + "  🔧 [B11] Chức năng 'Xem đơn đã giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 4:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.updateStatus()
                    System.out.println(PURPLE + "  🔧 [B12] Chức năng 'Cập nhật trạng thái' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 5:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.showInTransit()
                    System.out.println(PURPLE + "  🔧 [B13] Chức năng 'Đơn đang giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 6:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.showFailed()
                    System.out.println(PURPLE + "  🔧 [B14] Chức năng 'Đơn giao thất bại' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
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
