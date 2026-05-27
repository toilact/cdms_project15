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
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║       DISPATCHER - MENU CHÍNH         ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  [QUẢN LÝ SHIPPER]                    ║");
            System.out.println("║  1. Thêm nhân viên giao hàng   (B9)   ║");
            System.out.println("║  2. Phân công đơn cho shipper  (B10)  ║");
            System.out.println("║  3. Xem đơn đã giao của shipper(B11)  ║");
            System.out.println("║                                       ║");
            System.out.println("║  [THEO DÕI GIAO HÀNG]                 ║");
            System.out.println("║  4. Cập nhật trạng thái đơn    (B12)  ║");
            System.out.println("║  5. Hiển thị đơn đang giao     (B13)  ║");
            System.out.println("║  6. Hiển thị đơn giao thất bại (B14)  ║");
            System.out.println("║                                       ║");
            System.out.println("║  0. Quay lại Menu chính               ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-6): ", 0, 6);

            switch (choice) {
                case 1:
                    // TODO: Nguyên Quốc Cường (Thành viên 2) - Gọi StaffService.addStaff()
                    System.out.println("  🔧 [B9] Chức năng 'Thêm shipper' đang được phát triển bởi Nguyên Quốc Cường (Thành viên 2).\n");
                    break;
                case 2:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.assignStaff()
                    System.out.println("  🔧 [B10] Chức năng 'Phân công đơn' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n");
                    break;
                case 3:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.viewDeliveredByStaff()
                    System.out.println("  🔧 [B11] Chức năng 'Xem đơn đã giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n");
                    break;
                case 4:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.updateStatus()
                    System.out.println("  🔧 [B12] Chức năng 'Cập nhật trạng thái' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n");
                    break;
                case 5:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.showInTransit()
                    System.out.println("  🔧 [B13] Chức năng 'Đơn đang giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n");
                    break;
                case 6:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.showFailed()
                    System.out.println("  🔧 [B14] Chức năng 'Đơn giao thất bại' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n");
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
