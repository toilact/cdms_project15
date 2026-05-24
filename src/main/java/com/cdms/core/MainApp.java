// ============================================================
// File: MainApp.java
// Package: com.cdms.core
// Description: Điểm khởi chạy chính (Entry Point) của ứng dụng
//              CDMS. Hiển thị Main Menu phân quyền theo vai trò
//              và điều hướng tới các Submenu tương ứng.
// ============================================================
package com.cdms.core;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.cdms.view.BillingReportView;
import com.cdms.view.CustomerStaffView;
import com.cdms.view.DispatcherView;
import com.cdms.view.ShipperView;

public class MainApp {

    // ANSI Colors for beautiful UI
    private static final String RESET = "\u001B[0m";
    private static final String BLUE_BG = "\u001B[44m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD_WHITE = "\u001B[1;37m";

    public static void main(String[] args) throws Exception {

        // ============================
        // Fix encoding UTF-8 cho terminal Windows
        // ============================
        if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
            try {
                // Đổi code page của console hiện tại sang UTF-8 (shared với parent process)
                new ProcessBuilder("cmd", "/c", "chcp", "65001")
                        .inheritIO().start().waitFor();
            } catch (Exception ignored) {}
        }
        System.setOut(new PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.err), true, StandardCharsets.UTF_8));

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
            System.out.println(BOLD_CYAN + "=========================================" + RESET);
            System.out.println(BLUE_BG + BOLD_WHITE + " COURIER DELIVERY MANAGEMENT SYSTEM (CDMS) " + RESET);
            System.out.println(BOLD_CYAN + "=========================================" + RESET);
            System.out.println(BOLD_YELLOW + " 1. " + WHITE + "Reception Staff      (Nhân viên lễ tân)" + RESET);
            System.out.println(BOLD_YELLOW + " 2. " + WHITE + "Dispatcher           (Điều phối viên)" + RESET);
            System.out.println(BOLD_YELLOW + " 3. " + WHITE + "Delivery Staff       (Shipper)" + RESET);
            System.out.println(BOLD_YELLOW + " 4. " + WHITE + "Manager              (Quản lý)" + RESET);
            System.out.println(BOLD_YELLOW + " 5. " + BOLD_WHITE + "Thoát & Lưu dữ liệu" + RESET);
            System.out.println(BOLD_CYAN + "-----------------------------------------" + RESET);

            int choice = InputHelper.getIntInput("Chọn vai trò (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    System.out.println("\n" + BOLD_GREEN + ">>> Đăng nhập với vai trò: RECEPTION STAFF <<<" + RESET + "\n");
                    CustomerStaffView.showReceptionMenu();
                    break;

                case 2:
                    System.out.println("\n" + BOLD_GREEN + ">>> Đăng nhập với vai trò: DISPATCHER <<<" + RESET + "\n");
                    DispatcherView.showDispatcherMenu();
                    break;

                case 3:
                    System.out.println("\n" + BOLD_GREEN + ">>> Đăng nhập với vai trò: DELIVERY STAFF (SHIPPER) <<<" + RESET + "\n");
                    ShipperView.showShipperMenu();
                    break;

                case 4:
                    System.out.println("\n" + BOLD_GREEN + ">>> Đăng nhập với vai trò: MANAGER <<<" + RESET + "\n");
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
