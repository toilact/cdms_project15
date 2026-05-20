// ============================================================
// File: ShipperView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Shipper (Nhân viên giao hàng).
//              Xem đơn được phân công, cập nhật ngày nhận/giao hàng thực tế,
//              và thêm ghi chú giao hàng.
//              🔧 BÀN GIAO CHO: THÀNH VIÊN 4 (Developer C)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class ShipperView {

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
