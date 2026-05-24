// ============================================================
// File: ShipperView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Shipper (Nhân viên giao hàng).
//              Xem đơn được phân công, cập nhật ngày nhận/giao hàng thực tế,
//              và thêm ghi chú giao hàng.
//              🔧 BÀN GIAO CHO: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class ShipperView {

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
    private ShipperView() {
    }

    // ==========================================================
    //  SUBMENU: DELIVERY STAFF / SHIPPER
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
            System.out.println(BOLD_CYAN + "╔═══════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_CYAN + "║" + BLUE_BG + BOLD_WHITE + "    DELIVERY STAFF (SHIPPER) - MENU    " + RESET + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╠═══════════════════════════════════════╣" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "1. " + WHITE + "Xem danh sách đơn được giao       " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "2. " + WHITE + "Cập nhật ngày nhận hàng           " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "3. " + WHITE + "Cập nhật ngày giao hàng           " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "4. " + WHITE + "Thêm ghi chú giao hàng    (B15).  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "5. " + WHITE + "Cập nhật trạng thái đơn           " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "0. " + BOLD_WHITE + "Quay lại Menu chính               " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╚═══════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput("Chọn chức năng (0-5): ", 0, 5);

            switch (choice) {
                case 1:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.viewAssignedOrders()
                    System.out.println(PURPLE + "  🔧 Chức năng 'Xem đơn được giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 2:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.updatePickupDate()
                    System.out.println(PURPLE + "  🔧 Chức năng 'Cập nhật ngày nhận' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 3:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.updateDeliveryDate()
                    System.out.println(PURPLE + "  🔧 Chức năng 'Cập nhật ngày giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 4:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.addDeliveryNote()
                    System.out.println(PURPLE + "  🔧 [B15] Chức năng 'Thêm ghi chú' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 5:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.updateOrderStatus()
                    System.out.println(PURPLE + "  🔧 Chức năng 'Cập nhật trạng thái' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
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
