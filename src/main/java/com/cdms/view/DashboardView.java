// ============================================================
// File: DashboardView.java
// Package: com.cdms.view
// Description: Màn hình Dashboard hiển thị thống kê toàn hệ thống
//              theo thời gian thực khi khởi động Main Menu.
// ============================================================
package com.cdms.view;

import com.cdms.model.Invoice;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.ParcelRepository;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.DeliveryStaffRepository;
import com.cdms.repository.InvoiceRepository;

public class DashboardView {

    // ANSI Colors for premium visual presentation
    private static final String RESET        = "\u001B[0m";
    private static final String BOLD_CYAN    = "\u001B[1;36m";
    private static final String BOLD_YELLOW  = "\u001B[1;33m";
    private static final String BOLD_GREEN   = "\u001B[1;32m";
    private static final String BOLD_RED     = "\u001B[1;31m";
    private static final String BOLD_WHITE   = "\u001B[1;37m";
    private static final String BOLD_PURPLE  = "\u001B[1;35m";

    // Ngăn khởi tạo đối tượng
    private DashboardView() {
    }

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
            return 2; // Miscellaneous Symbols & Dingbats (contains ⚡, ✅, ❌, etc.)
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

    /**
     * Hiển thị bảng điều khiển thống kê thời gian thực (Real-time Live Stats)
     * với phong cách thiết kế UI bảng Console tối giản và sang trọng bậc nhất.
     */
    public static void showDashboard() {
        // Lấy số liệu thực tế từ các Repository
        int totalCustomers = CustomerRepository.findAll().size();
        int totalParcels   = ParcelRepository.findAll().size();
        int totalOrders    = DeliveryOrderRepository.findAll().size();
        
        // Đếm số shipper đang Active
        int activeShippersCount = 0;
        for (com.cdms.model.DeliveryStaff s : DeliveryStaffRepository.findAll()) {
            if ("Active".equalsIgnoreCase(s.getStatus())) {
                activeShippersCount++;
            }
        }
        int totalShippers = DeliveryStaffRepository.findAll().size();

        // Tính tổng doanh thu từ các hóa đơn đã Paid
        double totalRevenue = 0;
        for (Invoice inv : InvoiceRepository.findPaidInvoices()) {
            totalRevenue += inv.getTotalAmount();
        }

        // Đếm số đơn theo từng trạng thái
        long deliveredOrders = DeliveryOrderRepository.countByStatus("Delivered");
        long inTransitOrders = DeliveryOrderRepository.countByStatus("In Transit");
        long failedOrders    = DeliveryOrderRepository.countByStatus("Failed");
        long cancelledOrders = DeliveryOrderRepository.countByStatus("Cancelled");
        long assignedOrders  = DeliveryOrderRepository.countByStatus("Assigned") + DeliveryOrderRepository.countByStatus("Picked Up");

        // In banner ASCII art CDMS
        System.out.println(BOLD_CYAN + "   ▄████████ ▀█████████▄   ▄▄▄▄███▄▄▄▄    ▄████████ " + RESET);
        System.out.println(BOLD_CYAN + "  ███    ███   ███    ███ ▄██▀▀███▀▀██▄  ███    ███ " + RESET);
        System.out.println(BOLD_CYAN + "  ███    █▀    ███    ███ ███  ███  ███  ███    █▀  " + RESET);
        System.out.println(BOLD_YELLOW + "  ███          ███    ███ ███  ███  ███  ███        " + RESET);
        System.out.println(BOLD_YELLOW + "  ███          ███    ███ ███  ███  ███  ▀█████████▄" + RESET);
        System.out.println(BOLD_PURPLE + "  ███    █▄    ███    ███ ███  ███  ███           ███" + RESET);
        System.out.println(BOLD_PURPLE + "  ███    ███   ███    ███ ███  ███  ███     ▄█    ███" + RESET);
        System.out.println(BOLD_CYAN + "  ████████▀  ▄█████████▀   ▀█  ███  █▀    ▄████████▀ " + RESET);
        System.out.println(BOLD_WHITE + "   🚚 GIẢI PHÁP QUẢN LÝ GIAO HÀNG CHUYÊN NGHIỆP  " + RESET);
        System.out.println();

        // In bảng thống kê dạng card
        System.out.println(BOLD_YELLOW + "╔═════════════════════════════════════════════════════════════════════════╗" + RESET);
        String title = BOLD_YELLOW + "                    📊 DỰ ÁN CDMS - GIÁM SÁT THỜI GIAN THỰC";
        System.out.println(BOLD_YELLOW + "║" + RESET + padRight(title, 73) + BOLD_YELLOW + "║" + RESET);
        System.out.println(BOLD_YELLOW + "╠════════════════════════════════════╦════════════════════════════════════╣" + RESET);
        
        String left1 = "  👥 Khách hàng    : " + BOLD_WHITE + totalCustomers + RESET;
        String right1 = "  📦 Tổng kiện hàng : " + BOLD_WHITE + totalParcels + RESET;
        System.out.println(BOLD_YELLOW + "║" + RESET + padRight(left1, 36) + BOLD_YELLOW + "║" + RESET + padRight(right1, 36) + BOLD_YELLOW + "║" + RESET);
        
        String left2 = "  🚚 Hoạt động/Tổng: " + BOLD_GREEN + activeShippersCount + RESET + "/" + BOLD_WHITE + totalShippers + RESET;
        String right2 = "  📝 Tổng đơn hàng  : " + BOLD_WHITE + totalOrders + RESET;
        System.out.println(BOLD_YELLOW + "║" + RESET + padRight(left2, 36) + BOLD_YELLOW + "║" + RESET + padRight(right2, 36) + BOLD_YELLOW + "║" + RESET);
        
        String left3 = "  ✅ Đơn giao xong : " + BOLD_GREEN + deliveredOrders + RESET;
        String right3 = "  💰 Thực thu (VND) : " + BOLD_GREEN + String.format("%,.0f", totalRevenue) + RESET;
        System.out.println(BOLD_YELLOW + "║" + RESET + padRight(left3, 36) + BOLD_YELLOW + "║" + RESET + padRight(right3, 36) + BOLD_YELLOW + "║" + RESET);

        System.out.println(BOLD_YELLOW + "╠════════════════════════════════════╬════════════════════════════════════╣" + RESET);

        String left4 = "  ⚡ Đang vận chuyển: " + BOLD_CYAN + inTransitOrders + RESET;
        String right4 = "  📋 Đơn đã phân công: " + BOLD_WHITE + assignedOrders + RESET;
        System.out.println(BOLD_YELLOW + "║" + RESET + padRight(left4, 36) + BOLD_YELLOW + "║" + RESET + padRight(right4, 36) + BOLD_YELLOW + "║" + RESET);

        String left5 = "  ❌ Giao thất bại  : " + BOLD_RED + failedOrders + RESET;
        String right5 = "  🚫 Đơn hàng đã hủy : " + BOLD_RED + cancelledOrders + RESET;
        System.out.println(BOLD_YELLOW + "║" + RESET + padRight(left5, 36) + BOLD_YELLOW + "║" + RESET + padRight(right5, 36) + BOLD_YELLOW + "║" + RESET);
        
        System.out.println(BOLD_YELLOW + "╚════════════════════════════════════╩════════════════════════════════════╝" + RESET);
        System.out.println();
    }
}
