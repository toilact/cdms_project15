// ============================================================
// File: ParcelOrderView.java
// Package: com.cdms.view
// Description: Giao diện Console bổ sung cho phân hệ Kiện hàng
//              & Đơn giao hàng (dành cho các chức năng mà
//              Reception chưa phủ hết hoặc cần xem riêng).
//              🔧 BÀN GIAO CHO: THÀNH VIÊN 3 (Developer B)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class ParcelOrderView {

    // Ngăn khởi tạo đối tượng
    private ParcelOrderView() {
    }

    // ==========================================================
    //  SUBMENU: QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG (Chi tiết)
    // ==========================================================

    /**
     * Menu quản lý chi tiết kiện hàng và đơn hàng.
     * Thành viên 3 có thể mở rộng menu này để thêm các tính năng
     * nâng cao như: tìm kiếm đơn, hủy đơn, xem lịch sử KH...
     */
    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║   QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG     ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Thêm kiện hàng mới         (B4)  ║");
            System.out.println("║  2. Xem danh sách kiện hàng    (B5)  ║");
            System.out.println("║  3. Tạo đơn giao hàng mới      (B6)  ║");
            System.out.println("║  4. Cập nhật đơn giao hàng     (B7)  ║");
            System.out.println("║  5. Xem chi tiết đơn giao hàng (B8)  ║");
            System.out.println("║  6. Tìm kiếm đơn theo KH             ║");
            System.out.println("║  7. Hủy đơn giao hàng                ║");
            System.out.println("║                                      ║");
            System.out.println("║  0. Quay lại Menu chính              ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-7): ", 0, 7);

            switch (choice) {
                case 1:
                    System.out.println("  🔧 [B4] Chức năng 'Thêm kiện hàng' đang được phát triển bởi Thành viên 3.\n");
                    break;
                case 2:
                    System.out.println("  🔧 [B5] Chức năng 'Xem DS kiện hàng' đang được phát triển bởi Thành viên 3.\n");
                    break;
                case 3:
                    System.out.println("  🔧 [B6] Chức năng 'Tạo đơn giao hàng' đang được phát triển bởi Thành viên 3.\n");
                    break;
                case 4:
                    System.out.println("  🔧 [B7] Chức năng 'Cập nhật đơn' đang được phát triển bởi Thành viên 3.\n");
                    break;
                case 5:
                    System.out.println("  🔧 [B8] Chức năng 'Xem chi tiết đơn' đang được phát triển bởi Thành viên 3.\n");
                    break;
                case 6:
                    System.out.println("  🔧 Chức năng 'Tìm kiếm đơn theo KH' đang được phát triển bởi Thành viên 3.\n");
                    break;
                case 7:
                    System.out.println("  🔧 Chức năng 'Hủy đơn giao hàng' đang được phát triển bởi Thành viên 3.\n");
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
