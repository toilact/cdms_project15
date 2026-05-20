// ============================================================
// File: TrackingView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Theo dõi giao hàng,
//              Phân công shipper, và Ghi chú giao nhận.
//              Phục vụ 2 vai trò: Dispatcher + Delivery Staff.
//              🔧 BÀN GIAO CHO: THÀNH VIÊN 4 (Developer C)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class TrackingView {

    // Ngăn khởi tạo đối tượng
    private TrackingView() {
    }

    // ==========================================================
    //  SUBMENU 1: DISPATCHER (Điều phối viên)
    //  Các tính năng: B9, B10, B11, B12, B13
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
            System.out.println("║       DISPATCHER - MENU CHÍNH        ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  [QUẢN LÝ SHIPPER]                   ║");
            System.out.println("║  1. Thêm nhân viên giao hàng   (B9)  ║");
            System.out.println("║  2. Phân công đơn cho shipper  (B10)  ║");
            System.out.println("║  3. Xem đơn đã giao của shipper(B11)  ║");
            System.out.println("║                                      ║");
            System.out.println("║  [THEO DÕI GIAO HÀNG]               ║");
            System.out.println("║  4. Cập nhật trạng thái đơn    (B12)  ║");
            System.out.println("║  5. Hiển thị đơn đang giao     (B13)  ║");
            System.out.println("║  6. Hiển thị đơn giao thất bại (B14)  ║");
            System.out.println("║                                      ║");
            System.out.println("║  0. Quay lại Menu chính              ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-6): ", 0, 6);

            switch (choice) {
                case 1:
                    // TODO: Thành viên 2 - Gọi StaffService.addStaff()
                    System.out.println("  🔧 [B9] Chức năng 'Thêm shipper' đang được phát triển bởi Thành viên 2.\n");
                    break;
                case 2:
                    // TODO: Thành viên 4 - Gọi TrackingService.assignStaff()
                    System.out.println("  🔧 [B10] Chức năng 'Phân công đơn' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 3:
                    // TODO: Thành viên 4 - Gọi TrackingService.viewDeliveredByStaff()
                    System.out.println("  🔧 [B11] Chức năng 'Xem đơn đã giao' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 4:
                    // TODO: Thành viên 4 - Gọi TrackingService.updateStatus()
                    System.out.println("  🔧 [B12] Chức năng 'Cập nhật trạng thái' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 5:
                    // TODO: Thành viên 4 - Gọi TrackingService.showInTransit()
                    System.out.println("  🔧 [B13] Chức năng 'Đơn đang giao' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 6:
                    // TODO: Thành viên 4 - Gọi TrackingService.showFailed()
                    System.out.println("  🔧 [B14] Chức năng 'Đơn giao thất bại' đang được phát triển bởi Thành viên 4.\n");
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

    // ==========================================================
    //  SUBMENU 2: DELIVERY STAFF / SHIPPER
    //  Các tính năng: B15 (Thêm ghi chú), xem đơn được giao
    // ==========================================================

    /**
     * Menu chính cho vai trò Delivery Staff (Shipper).
     * Shipper có quyền: xem đơn được phân công, cập nhật trạng thái
     * thực tế (pickupDate, deliveryDate), thêm ghi chú.
     */
    public static void showShipperMenu() {
        boolean running = true;

        while (running) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║    DELIVERY STAFF (SHIPPER) - MENU   ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Xem danh sách đơn được giao      ║");
            System.out.println("║  2. Cập nhật ngày nhận hàng          ║");
            System.out.println("║  3. Cập nhật ngày giao hàng          ║");
            System.out.println("║  4. Thêm ghi chú giao hàng    (B15)  ║");
            System.out.println("║  5. Cập nhật trạng thái đơn          ║");
            System.out.println("║                                      ║");
            System.out.println("║  0. Quay lại Menu chính              ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-5): ", 0, 5);

            switch (choice) {
                case 1:
                    // TODO: Thành viên 4 - Gọi TrackingService.viewAssignedOrders()
                    System.out.println("  🔧 Chức năng 'Xem đơn được giao' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 2:
                    // TODO: Thành viên 4 - Gọi TrackingService.updatePickupDate()
                    System.out.println("  🔧 Chức năng 'Cập nhật ngày nhận' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 3:
                    // TODO: Thành viên 4 - Gọi TrackingService.updateDeliveryDate()
                    System.out.println("  🔧 Chức năng 'Cập nhật ngày giao' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 4:
                    // TODO: Thành viên 4 - Gọi TrackingService.addDeliveryNote()
                    System.out.println("  🔧 [B15] Chức năng 'Thêm ghi chú' đang được phát triển bởi Thành viên 4.\n");
                    break;
                case 5:
                    // TODO: Thành viên 4 - Gọi TrackingService.updateOrderStatus()
                    System.out.println("  🔧 Chức năng 'Cập nhật trạng thái' đang được phát triển bởi Thành viên 4.\n");
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
