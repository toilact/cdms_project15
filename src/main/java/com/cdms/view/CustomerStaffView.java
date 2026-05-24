// ============================================================
// File: CustomerStaffView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Khách hàng &
//              Nhân viên giao hàng.
//              🔧 BÀN GIAO CHO: THÀNH VIÊN 2 (Nguyên Quốc Cường - Developer A)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class CustomerStaffView {

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
    private CustomerStaffView() {
    }

    // ==========================================================
    // SUBMENU: RECEPTION STAFF (Nhân viên lễ tân)
    // Các tính năng: B1, B2, B3 (Khách hàng) + B4, B5 (Kiện hàng)
    // + B6, B7, B8 (Đơn hàng)
    // ==========================================================

    /**
     * Menu chính cho vai trò Reception Staff.
     * Reception có quyền quản lý khách hàng, tạo kiện hàng và tạo đơn.
     */
    public static void showReceptionMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_CYAN + "╔═══════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_CYAN + "║" + BOLD_YELLOW + "      RECEPTION STAFF - MENU CHÍNH     " + RESET + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╠═══════════════════════════════════════╣" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ KHÁCH HÀNG]                 " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "1. " + WHITE + "Thêm khách hàng mới       (B1)    " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "2. " + WHITE + "Cập nhật thông tin KH      (B2)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "3. " + WHITE + "Hiển thị danh sách KH      (B3)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ KIỆN HÀNG]                  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "4. " + WHITE + "Thêm kiện hàng mới         (B4)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "5. " + WHITE + "Xem danh sách kiện hàng    (B5)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ ĐƠN GIAO HÀNG]              " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "6. " + WHITE + "Tạo đơn giao hàng mới      (B6)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "7. " + WHITE + "Cập nhật đơn giao hàng     (B7)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "8. " + WHITE + "Xem chi tiết đơn giao hàng (B8)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "0. " + BOLD_WHITE + "Quay lại Menu chính               " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╚═══════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput("Chọn chức năng (0-8): ", 0, 8);

            switch (choice) {
                case 1:
                    // TODO: Thành viên 2 (Nguyên Quốc Cường) - Gọi CustomerService.addCustomer()
                    System.out.println(PURPLE + "  🔧 [B1] Chức năng 'Thêm khách hàng' đang được phát triển bởi Thành viên 2 (Nguyên Quốc Cường).\n" + RESET);
                    break;
                case 2:
                    // TODO: Thành viên 2 (Nguyên Quốc Cường) - Gọi CustomerService.updateCustomer()
                    System.out.println(PURPLE + "  🔧 [B2] Chức năng 'Cập nhật KH' đang được phát triển bởi Thành viên 2 (Nguyên Quốc Cường).\n" + RESET);
                    break;
                case 3:
                    // TODO: Thành viên 2 (Nguyên Quốc Cường) - Gọi CustomerService.displayAll()
                    System.out.println(PURPLE + "  🔧 [B3] Chức năng 'Hiển thị DS KH' đang được phát triển bởi Thành viên 2 (Nguyên Quốc Cường).\n" + RESET);
                    break;
                case 4:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi ParcelService.addParcel()
                    System.out.println(PURPLE + "  🔧 [B4] Chức năng 'Thêm kiện hàng' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n" + RESET);
                    break;
                case 5:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi ParcelService.displayAll()
                    System.out.println(PURPLE + "  🔧 [B5] Chức năng 'Xem DS kiện hàng' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n" + RESET);
                    break;
                case 6:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi OrderService.createOrder()
                    System.out.println(PURPLE + "  🔧 [B6] Chức năng 'Tạo đơn giao hàng' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n" + RESET);
                    break;
                case 7:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi OrderService.updateOrder()
                    System.out.println(PURPLE + "  🔧 [B7] Chức năng 'Cập nhật đơn' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n" + RESET);
                    break;
                case 8:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi OrderService.viewOrderDetail()
                    System.out.println(PURPLE + "  🔧 [B8] Chức năng 'Xem chi tiết đơn' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n" + RESET);
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
