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
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║      RECEPTION STAFF - MENU CHÍNH     ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  [QUẢN LÝ KHÁCH HÀNG]                 ║");
            System.out.println("║  1. Thêm khách hàng mới       (B1)    ║");
            System.out.println("║  2. Cập nhật thông tin KH      (B2)   ║");
            System.out.println("║  3. Hiển thị danh sách KH      (B3)   ║");
            System.out.println("║                                       ║");
            System.out.println("║  [QUẢN LÝ KIỆN HÀNG]                  ║");
            System.out.println("║  4. Thêm kiện hàng mới         (B4)   ║");
            System.out.println("║  5. Xem danh sách kiện hàng    (B5)   ║");
            System.out.println("║                                       ║");
            System.out.println("║  [QUẢN LÝ ĐƠN GIAO HÀNG]              ║");
            System.out.println("║  6. Tạo đơn giao hàng mới      (B6)   ║");
            System.out.println("║  7. Cập nhật đơn giao hàng     (B7)   ║");
            System.out.println("║  8. Xem chi tiết đơn giao hàng (B8)   ║");
            System.out.println("║                                       ║");
            System.out.println("║  0. Quay lại Menu chính               ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-8): ", 0, 8);

            switch (choice) {
                case 1:
                    // TODO: Thành viên 2 (Nguyên Quốc Cường) - Gọi CustomerService.addCustomer()
                    System.out
                            .println("  🔧 [B1] Chức năng 'Thêm khách hàng' đang được phát triển bởi Thành viên 2 (Nguyên Quốc Cường).\n");
                    break;
                case 2:
                    // TODO: Thành viên 2 (Nguyên Quốc Cường) - Gọi CustomerService.updateCustomer()
                    System.out.println("  🔧 [B2] Chức năng 'Cập nhật KH' đang được phát triển bởi Thành viên 2 (Nguyên Quốc Cường).\n");
                    break;
                case 3:
                    // TODO: Thành viên 2 (Nguyên Quốc Cường) - Gọi CustomerService.displayAll()
                    System.out.println("  🔧 [B3] Chức năng 'Hiển thị DS KH' đang được phát triển bởi Thành viên 2 (Nguyên Quốc Cường).\n");
                    break;
               case 4:  // B4 - Thêm kiện hàng mới
                    ParcelOrderView.executeCreateParcel();
                    break;

                case 5:  // B5 - Xem danh sách kiện hàng
                    ParcelOrderView.executeViewParcels();
                    break;

                case 6:  // B6 - Tạo đơn giao hàng mới
                    ParcelOrderView.executeCreateOrder();
                    break;

                case 7:  // B7 - Cập nhật đơn giao hàng
                    ParcelOrderView.executeUpdateOrderStatus();
                    break;

                case 8:  // B8 - Xem chi tiết đơn giao hàng
                    ParcelOrderView.executeViewOrderDetail();
                    break;
                // ===============================================
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
