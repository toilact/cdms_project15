// ============================================================
// File: ParcelOrderView.java
// Package: com.cdms.view
// Description: Giao diện Console bổ sung cho phân hệ Kiện hàng
//              & Đơn giao hàng (dành cho các chức năng mà
//              Reception chưa phủ hết hoặc cần xem riêng).
// Phân công: Trương Đan Huy (Developer B - Thành viên 3)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.Parcel;
import com.cdms.service.OrderService;
import com.cdms.service.ParcelService;

import java.util.List;

public class ParcelOrderView {

    // ANSI Colors for premium visual presentation
    private static final String RESET = "\u001B[0m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String BOLD_RED = "\u001B[1;31m";
    private static final String BOLD_WHITE = "\u001B[1;37m";
    private static final String WHITE = "\u001B[37m";

    // Ngăn khởi tạo đối tượng
    private ParcelOrderView() {
    }

    // ==========================================================
    // SUBMENU: QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG (Chi tiết)
    // ==========================================================

    /**
     * Menu quản lý chi tiết kiện hàng và đơn hàng.
     */
    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println(
                    BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE
                    + "Thêm kiện hàng mới           (B4)                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE
                    + "Xem danh sách kiện hàng      (B5)                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE
                    + "Tạo đơn giao hàng mới        (B6)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE
                    + "Cập nhật đơn giao hàng        (B7)                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE
                    + "Xem chi tiết đơn giao hàng    (B8)                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE
                    + "Tìm kiếm đơn theo KH                               " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE
                    + "Hủy đơn giao hàng                                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE
                    + "Quay lại Menu chính                                " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-7): " + RESET, 0, 7);

            switch (choice) {
                case 1:
                    executeCreateParcel();
                    break;
                case 2:
                    executeViewParcels();
                    break;
                case 3:
                    executeCreateOrder();
                    break;
                case 4:
                    executeUpdateOrderStatus();
                    break;
                case 5:
                    executeViewOrderDetail();
                    break;
                case 6:
                    executeSearchOrdersByCustomer();
                    break;
                case 7:
                    executeCancelOrder();
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

    // ==========================================================
    // CÁC HÀM THỰC THI (EXECUTE)
    // ==========================================================

    public static void executeCreateParcel() {
        System.out.println(BOLD_CYAN + "\n=== THÊM KIỆN HÀNG MỚI ===" + RESET);
        String id = InputHelper.getStringInput("Mã kiện hàng: ");
        String senderId = InputHelper.getStringInput("Mã khách hàng gửi: ");
        String receiverName = InputHelper.getStringInput("Tên người nhận: ");
        String receiverPhone = InputHelper.getStringInput("SĐT người nhận: ");
        String pickupAddress = InputHelper.getStringInput("Địa chỉ lấy hàng: ");
        String deliveryAddress = InputHelper.getStringInput("Địa chỉ giao hàng: ");
        double weight = InputHelper.getDoubleInput("Trọng lượng (kg): ", 0.1);
        String type = InputHelper.getStringInput("Loại kiện (Document/Goods): ");

        try {
            Parcel parcel = ParcelService.createParcel(id, senderId, receiverName,
                    receiverPhone, pickupAddress, deliveryAddress, weight, type);

            System.out.println(BOLD_GREEN + "✅ Tạo kiện hàng thành công: " + id + RESET);
            System.out.println(parcel);
        } catch (IllegalArgumentException e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
    }

    public static void executeViewParcels() {
        System.out.println(BOLD_CYAN + "\n=== DANH SÁCH KIỆN HÀNG ===" + RESET);
        List<Parcel> parcels = JSONDataManager.parcels;

        if (parcels.isEmpty()) {
            System.out.println("Chưa có kiện hàng nào.");
            return;
        }

        for (Parcel p : parcels) {
            System.out.println(p);
        }
        System.out.println(BOLD_GREEN + "Tổng số kiện: " + parcels.size() + RESET);
    }

    public static void executeCreateOrder() {
        System.out.println(BOLD_CYAN + "\n=== TẠO ĐƠN GIAO HÀNG ===" + RESET);
        String orderId = InputHelper.getStringInput("Mã đơn hàng: ");
        String parcelId = InputHelper.getStringInput("Mã kiện hàng cần chuyển: ");
        String staffId = InputHelper.getStringInput("Mã shipper (Staff ID): ");
        String deliveryType = InputHelper.getStringInput("Loại đơn hàng (Standard/Urgent): ");

        try {
            DeliveryOrder order = OrderService.convertParcelToOrder(orderId, parcelId, staffId, deliveryType);

            System.out.println(BOLD_GREEN + "✅ Chuyển đổi kiện hàng thành đơn hàng thành công: " + orderId + RESET);
            System.out.println(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
    }

    public static void executeUpdateOrderStatus() {
        System.out.println(BOLD_CYAN + "\n=== CẬP NHẬT TRẠNG THÁI ĐƠN ===" + RESET);
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");
        String status = InputHelper
                .getStringInput("Nhập trạng thái mới (Assigned/In Transit/Delivered/Cancelled/Failed): ");

        try {
            DeliveryOrder order = OrderService.updateOrderStatus(orderId, status);
            System.out.println(BOLD_GREEN + "✅ Cập nhật trạng thái thành công!" + RESET);
            System.out.println(order);
        } catch (Exception e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
    }

    public static void executeViewOrderDetail() {
        System.out.println(BOLD_CYAN + "\n=== XEM CHI TIẾT ĐƠN HÀNG ===" + RESET);
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");

        try {
            DeliveryOrder order = OrderService.getOrderDetail(orderId);
            System.out.println(BOLD_GREEN + "✅ Thông tin đơn hàng:" + RESET);
            System.out.println(order);
        } catch (Exception e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
    }

    public static void executeSearchOrdersByCustomer() {
        System.out.println(BOLD_CYAN + "\n=== TÌM KIẾM ĐƠN THEO KHÁCH HÀNG ===" + RESET);
        String customerId = InputHelper.getStringInput("Nhập mã khách hàng: ");

        try {
            List<DeliveryOrder> orders = OrderService.searchOrdersByCustomer(customerId);

            if (orders.isEmpty()) {
                System.out.println("Không tìm thấy đơn hàng nào của khách hàng " + customerId);
                return;
            }

            System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng:" + RESET);
            for (DeliveryOrder o : orders) {
                System.out.println(o);
            }
        } catch (Exception e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
    }

    public static void executeCancelOrder() {
        System.out.println(BOLD_CYAN + "\n=== HỦY ĐƠN GIAO HÀNG ===" + RESET);
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng cần hủy: ");

        try {
            DeliveryOrder order = OrderService.cancelOrder(orderId);
            System.out.println(BOLD_GREEN + "✅ Hủy đơn hàng thành công!" + RESET);
            System.out.println(order);
        } catch (Exception e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        }
    }
}
