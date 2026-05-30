// ============================================================
// File: ShipperView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Shipper (Nhân viên giao hàng).
//              Xem đơn được phân công, cập nhật ngày nhận/giao hàng thực tế,
//              và thêm ghi chú giao hàng.
// Phân công: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.core.FormCancelledException;
import com.cdms.model.DeliveryOrder;
import com.cdms.service.CustomerStaffService;
import com.cdms.repository.DeliveryOrderRepository;

import java.util.List;

public class ShipperView {

    // ANSI Colors for beautiful UI
    private static final String RESET = "\u001B[0m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String BOLD_RED = "\u001B[1;31m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD_WHITE = "\u001B[1;37m";

    // Ngăn khởi tạo đối tượng
    private ShipperView() {
    }

    // ==========================================================
    // SUBMENU: DELIVERY STAFF / SHIPPER
    // Các tính năng: B15 (Thêm ghi chú), xem đơn được giao
    // ==========================================================

    /**
     * Menu chính cho vai trò Delivery Staff (Shipper).
     * Shipper có quyền: xem đơn được phân công, cập nhật trạng thái
     * thực tế (pickupDate, deliveryDate), thêm ghi chú.
     */
    public static void showShipperMenu() {
        boolean running = true;

        while (running) {
            System.out.println(
                    BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "DELIVERY STAFF (SHIPPER) - MENU" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [TÁC VỤ GIAO HÀNG]                                   "
                    + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE
                    + "Xem danh sách đơn được giao                       " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE
                    + "Cập nhật ngày nhận hàng                           " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE
                    + "Cập nhật ngày giao hàng                           " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE
                    + "Thêm ghi chú giao hàng                            " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE
                    + "Cập nhật trạng thái đơn                           " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE
                    + "Quay lại Menu chính                            " + BOLD_YELLOW + "   ║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-5): " + RESET, 0, 5);

            switch (choice) {
                case 1:
                    handleViewAssignedOrders();
                    break;
                case 2:
                    handleUpdatePickupDate();
                    break;
                case 3:
                    handleUpdateDeliveryDate();
                    break;
                case 4:
                    handleRegisterTrackingNote();
                    break;
                case 5:
                    handleUpdateOrderStatus();
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

    // ----------------------------------------------------------
    // [B10] Xem đơn được giao
    // ----------------------------------------------------------
    private static void handleViewAssignedOrders() {
        System.out.println(BOLD_CYAN + "\n===== DANH SÁCH ĐƠN HÀNG ĐƯỢC GIAO =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String staffId = InputHelper.getStringInput("Mã shipper (Staff ID): ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Mã shipper không tồn tại!");
            try {
                List<DeliveryOrder> orders = com.cdms.service.TrackingService.getAssignedOrdersByStaff(staffId);
                if (orders.isEmpty()) {
                    System.out.println("Không có đơn hàng nào được phân công cho shipper " + staffId);
                    return;
                }
                System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng được phân công:" + RESET);
                for (DeliveryOrder o : orders) {
                    System.out.println(o);
                }
            } catch (Exception e) {
                System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    // ----------------------------------------------------------
    // Cập nhật ngày nhận hàng
    // ----------------------------------------------------------
    private static void handleUpdatePickupDate() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT NGÀY NHẬN HÀNG THỰC TẾ =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại!");
            java.time.LocalDate pickupDate = InputHelper.getDateInput("Ngày nhận hàng (DD/MM/YYYY): ", false);

            System.out.println("\nXác nhận cập nhật ngày nhận hàng?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    com.cdms.service.TrackingService.updatePickupDate(orderId, pickupDate);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật ngày nhận hàng thực tế thành công!" + RESET);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy cập nhật ngày nhận." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    // ----------------------------------------------------------
    // Cập nhật ngày giao hàng
    // ----------------------------------------------------------
    private static void handleUpdateDeliveryDate() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT NGÀY GIAO HÀNG THỰC TẾ =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại!");
            java.time.LocalDate deliveryDate = InputHelper.getDateInput("Ngày giao hàng (DD/MM/YYYY): ", false);

            System.out.println("\nXác nhận cập nhật ngày giao hàng?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    com.cdms.service.TrackingService.updateDeliveryDate(orderId, deliveryDate);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật ngày giao hàng thực tế thành công!" + RESET);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy cập nhật ngày giao." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    // ----------------------------------------------------------
    // [B15] Thêm ghi chú giao hàng
    // ----------------------------------------------------------
    private static void handleRegisterTrackingNote() {
        System.out.println(BOLD_CYAN + "\n===== THÊM GHI CHÚ HÀNH TRÌNH =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại!");
            String note = InputHelper.getStringInput("Nội dung ghi chú: ");

            System.out.println("\nXác nhận thêm ghi chú?");
            System.out.println("  1. Đồng ý");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    com.cdms.service.TrackingService.addTrackingNote(orderId, note);
                    System.out.println(BOLD_GREEN + "✅ Thêm ghi chú thành công!" + RESET);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println("  Đã hủy thao tác.");
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    // ----------------------------------------------------------
    // Cập nhật trạng thái đơn
    // ----------------------------------------------------------
    private static void handleUpdateOrderStatus() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại!");
            String status = InputHelper.getStringInput("Trạng thái mới (In Transit/Delivered/Failed): ",
                    val -> val.equalsIgnoreCase("In Transit") || val.equalsIgnoreCase("Delivered") || val.equalsIgnoreCase("Failed"),
                    "Trạng thái không hợp lệ!");

            System.out.println("\nXác nhận cập nhật trạng thái đơn hàng?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    com.cdms.model.DeliveryOrder o = com.cdms.service.TrackingService.updateStatus(orderId, status);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật trạng thái đơn hàng thành công!" + RESET);
                    System.out.println(o);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy cập nhật trạng thái." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }
}
