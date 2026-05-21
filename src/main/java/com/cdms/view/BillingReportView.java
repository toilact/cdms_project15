// ============================================================
// File: BillingReportView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Thanh toán &
//              Báo cáo thống kê. Phục vụ vai trò Manager.
//              🔧 BÀN GIAO CHO: THÀNH VIÊN 5 (Developer D)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;

public class BillingReportView {

    // Ngăn khởi tạo đối tượng
    private BillingReportView() {
    }

    // ==========================================================
    //  SUBMENU: MANAGER (Quản lý)
    //  Các tính năng: B16 - B22 (Thanh toán + Báo cáo)
    // ==========================================================

    /**
     * Menu chính cho vai trò Manager.
     * Manager có quyền: tính hóa đơn, ghi nhận thanh toán,
     * xem báo cáo doanh thu, đánh giá hiệu suất shipper.
     */
    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║        MANAGER - MENU CHÍNH           ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  [QUẢN LÝ THANH TOÁN]                 ║");
            System.out.println("║  1. Tính hóa đơn cho đơn hàng (B16)   ║");
            System.out.println("║  2. Xem chi tiết hóa đơn      (B17)   ║");
            System.out.println("║  3. Ghi nhận thanh toán        (B18)  ║");
            System.out.println("║                                       ║");
            System.out.println("║  [BÁO CÁO & THỐNG KÊ]                 ║");
            System.out.println("║  4. Báo cáo doanh thu theo ngày(B19)  ║");
            System.out.println("║  5. Báo cáo doanh thu theo tháng(B20) ║");
            System.out.println("║  6. Shipper tích cực nhất      (B21)  ║");
            System.out.println("║  7. Thống kê đơn giao thành công(B22) ║");
            System.out.println("║                                       ║");
            System.out.println("║  0. Quay lại Menu chính               ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-7): ", 0, 7);

            switch (choice) {
                case 1:
                    // TODO: Thành viên 5 - Gọi BillingService.generateInvoice()
                    System.out.println("  🔧 [B16] Chức năng 'Tính hóa đơn' đang được phát triển bởi Thành viên 5.\n");
                    break;
                case 2:
                    // TODO: Thành viên 5 - Gọi BillingService.viewInvoiceDetail()
                    System.out.println("  🔧 [B17] Chức năng 'Xem chi tiết HĐ' đang được phát triển bởi Thành viên 5.\n");
                    break;
                case 3:
                    // TODO: Thành viên 5 - Gọi BillingService.recordPayment()
                    System.out.println("  🔧 [B18] Chức năng 'Ghi nhận thanh toán' đang được phát triển bởi Thành viên 5.\n");
                    break;
                case 4:
                    // TODO: Thành viên 5 - Gọi ReportService.dailyRevenue()
                    System.out.println("  🔧 [B19] Chức năng 'Doanh thu ngày' đang được phát triển bởi Thành viên 5.\n");
                    break;
                case 5:
                    // TODO: Thành viên 5 - Gọi ReportService.monthlyRevenue()
                    System.out.println("  🔧 [B20] Chức năng 'Doanh thu tháng' đang được phát triển bởi Thành viên 5.\n");
                    break;
                case 6:
                    // TODO: Thành viên 5 - Gọi ReportService.topShipper()
                    System.out.println("  🔧 [B21] Chức năng 'Shipper tích cực nhất' đang được phát triển bởi Thành viên 5.\n");
                    break;
                case 7:
                    // TODO: Thành viên 5 - Gọi ReportService.successfulDeliveries()
                    System.out.println("  🔧 [B22] Chức năng 'Đơn giao thành công' đang được phát triển bởi Thành viên 5.\n");
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
