// ============================================================
// File: DashboardView.java
// Package: com.cdms.view
// Description: Giao diện bảng điều khiển trung tâm (Dashboard)
//              hệ thống CDMS hiển thị thống kê thời gian thực.
//              Thiết kế bởi Senior UI Designer.
// ============================================================
package com.cdms.view;

import com.cdms.model.DeliveryStaff;
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
    private static final String WHITE        = "\u001B[37m";

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
        // 1. Tính toán các chỉ số thời gian thực từ các Repositories (BUG-09)
        int totalCustomers = CustomerRepository.findAll().size();
        int totalParcels   = ParcelRepository.findAll().size();
        int totalOrders    = DeliveryOrderRepository.findAll().size();
        
        // Đếm số lượng shipper đang hoạt động
        long activeShippersCount = DeliveryStaffRepository.findAll().stream()
                .filter(s -> "Active".equalsIgnoreCase(s.getStatus()))
                .count();
        int totalShippers = DeliveryStaffRepository.findAll().size();

        // Tính toán doanh thu thực tế đã thanh toán (Paid Invoices)
        double totalRevenue = InvoiceRepository.findPaidInvoices().stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();

        // Đếm số lượng đơn theo các trạng thái (UX-12)
        long deliveredOrders = DeliveryOrderRepository.countByStatus("Delivered");
        long inTransitOrders = DeliveryOrderRepository.countByStatus("In Transit");
        long failedOrders    = DeliveryOrderRepository.countByStatus("Failed");
        long cancelledOrders = DeliveryOrderRepository.countByStatus("Cancelled");
        long assignedOrders  = DeliveryOrderRepository.countByStatus("Assigned") + DeliveryOrderRepository.countByStatus("Picked Up");

        // 2. In Banner nghệ thuật CDMS phối màu mượt mà
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

        // 3. Thiết kế khung thẻ thông tin (Dynamic Data Cards) cực kỳ sang trọng
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
