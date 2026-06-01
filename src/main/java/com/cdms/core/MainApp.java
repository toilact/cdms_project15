// ============================================================
// File: MainApp.java
// Package: com.cdms.core
// Description: Điểm khởi chạy chính (Entry Point) của ứng dụng
//              CDMS. Hiển thị Main Menu phân quyền theo vai trò
//              và điều hướng tới các Submenu tương ứng.
// Phân công: Đỗ Chí Thành (Leader - Thành viên 1)
// ============================================================
package com.cdms.core;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.cdms.view.BillingReportView;
import com.cdms.view.CustomerStaffView;
import com.cdms.view.DispatcherView;
import com.cdms.view.ShipperView;
import com.cdms.view.DashboardView;

public class MainApp {

    // ANSI Colors for beautiful UI
    private static final String RESET = "\u001B[0m";
    private static final String BLUE_BG = "\u001B[44m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String BOLD_RED = "\u001B[1;31m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD_WHITE = "\u001B[1;37m";

    private static int getVisualWidth(String str) {
        if (str == null) return 0;
        // Strip ANSI escape sequences
        String stripped = str.replaceAll("\\u001B\\[[;\\d]*[a-zA-Z]", "");
        int width = 0;
        for (int i = 0; i < stripped.length(); ) {
            int cp = stripped.codePointAt(i);
            width += getCodePointWidth(cp);
            i += Character.charCount(cp);
        }
        return width;
    }

    private static int getCodePointWidth(int cp) {
        // Emojis and other wide symbols
        if (cp >= 0x1F000 && cp <= 0x1FFFF) {
            return 2;
        }
        if (cp >= 0x2600 && cp <= 0x27BF) {
            return 2; // Miscellaneous Symbols & Dingbats
        }
        // CJK Unified Ideographs
        if (cp >= 0x4E00 && cp <= 0x9FFF) {
            return 2;
        }
        // Hangul, Hiragana, Katakana, Fullwidth forms
        if (cp >= 0x3000 && cp <= 0x30FF || cp >= 0xFF00 && cp <= 0xFFEF) {
            return 2;
        }
        return 1;
    }

    private static String padRight(String text, int targetVisualWidth) {
        int currentWidth = getVisualWidth(text);
        if (currentWidth >= targetVisualWidth) {
            return text;
        }
        int neededSpaces = targetVisualWidth - currentWidth;
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < neededSpaces; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

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
            DashboardView.showDashboard();
            
            System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "XIN CHỌN VAI TRÒ HỆ THỐNG   " + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            
            String lineHeader = BOLD_CYAN + "  [DANH SÁCH VAI TRÒ]";
            String line1 = BOLD_WHITE + "  1. " + WHITE + "Reception Staff      (Nhân viên lễ tân)";
            String line2 = BOLD_WHITE + "  2. " + WHITE + "Dispatcher           (Điều phối viên)";
            String line3 = BOLD_WHITE + "  3. " + WHITE + "Delivery Staff       (Shipper)";
            String line4 = BOLD_WHITE + "  4. " + WHITE + "Manager              (Quản lý)";
            String lineEmpty = " ";
            String line5 = BOLD_RED + "  5. " + BOLD_WHITE + "Thoát & Lưu dữ liệu";
            
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineHeader, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line1, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line2, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line3, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line4, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineEmpty, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line5, 55) + BOLD_YELLOW + "║" + RESET);
            
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn vai trò (1-5): " + RESET, 1, 5);

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
