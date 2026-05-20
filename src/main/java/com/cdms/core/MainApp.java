// ============================================================
// File: MainApp.java
// Package: com.cdms.core
// Description: Điểm khởi chạy chính (Entry Point) của ứng dụng
//              CDMS. Hiển thị Main Menu phân quyền theo vai trò
//              và điều hướng tới các Submenu tương ứng.
// ============================================================
package com.cdms.core;

import com.cdms.view.BillingReportView;
import com.cdms.view.CustomerStaffView;
import com.cdms.view.ParcelOrderView;
import com.cdms.view.TrackingView;

public class MainApp {

    public static void main(String[] args) {

        // ============================
        // BƯỚC 1: Nạp dữ liệu từ JSON
        // ============================
        System.out.println();
        JSONDataManager.loadAllData();

        // ============================
        // BƯỚC 2: Vòng lặp Main Menu
        // ============================
        boolean running = true;

        while (running) {
            System.out.println("=========================================");
            System.out.println(" COURIER DELIVERY MANAGEMENT SYSTEM (CDMS)");
            System.out.println("=========================================");
            System.out.println(" 1. Reception Staff      (Nhân viên lễ tân)");
            System.out.println(" 2. Dispatcher           (Điều phối viên)");
            System.out.println(" 3. Delivery Staff       (Shipper)");
            System.out.println(" 4. Manager              (Quản lý)");
            System.out.println(" 5. Thoát & Lưu dữ liệu");
            System.out.println("-----------------------------------------");

            int choice = InputHelper.getIntInput("Chọn vai trò (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    System.out.println("\n>>> Đăng nhập với vai trò: RECEPTION STAFF <<<\n");
                    CustomerStaffView.showReceptionMenu();
                    break;

                case 2:
                    System.out.println("\n>>> Đăng nhập với vai trò: DISPATCHER <<<\n");
                    TrackingView.showDispatcherMenu();
                    break;

                case 3:
                    System.out.println("\n>>> Đăng nhập với vai trò: DELIVERY STAFF (SHIPPER) <<<\n");
                    TrackingView.showShipperMenu();
                    break;

                case 4:
                    System.out.println("\n>>> Đăng nhập với vai trò: MANAGER <<<\n");
                    BillingReportView.showMenu();
                    break;

                case 5:
                    // ============================
                    // BƯỚC 3: Lưu & Thoát
                    // ============================
                    System.out.println("\n💾 Đang lưu toàn bộ dữ liệu...");
                    JSONDataManager.saveAllData();
                    System.out.println("✅ Dữ liệu đã được lưu thành công!");
                    System.out.println("👋 Cảm ơn bạn đã sử dụng CDMS. Tạm biệt!\n");
                    running = false;
                    break;

                default:
                    break;
            }
        }
    }
}
