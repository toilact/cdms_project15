// ============================================================
// File: CustomerStaffView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Khách hàng &
//              Nhân viên giao hàng.
//              🔧 BÀN GIAO CHO: THÀNH VIÊN 2 (Nguyên Quốc Cường - Developer A)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.model.Customer;
import com.cdms.service.CustomerStaffService;

import java.util.List;

public class CustomerStaffView {

    // ANSI Colors for premium visual presentation
    private static final String RESET        = "\u001B[0m";
    private static final String BOLD_CYAN    = "\u001B[1;36m";
    private static final String BOLD_YELLOW  = "\u001B[1;33m";
    private static final String BOLD_GREEN   = "\u001B[1;32m";
    private static final String BOLD_RED     = "\u001B[1;31m";
    private static final String BOLD_WHITE   = "\u001B[1;37m";
    private static final String WHITE        = "\u001B[37m";

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
            System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "RECEPTION STAFF - MENU CHÍNH" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ KHÁCH HÀNG]                                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE + "Thêm khách hàng mới          (B1)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE + "Cập nhật thông tin KH        (B2)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE + "Hiển thị danh sách KH        (B3)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ KIỆN HÀNG]                                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE + "Thêm kiện hàng mới           (B4)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE + "Xem danh sách kiện hàng      (B5)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ ĐƠN GIAO HÀNG]                              " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE + "Tạo đơn giao hàng mới        (B6)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE + "Cập nhật đơn giao hàng        (B7)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  8. " + WHITE + "Xem chi tiết đơn giao hàng    (B8)                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính                                " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-8): " + RESET, 0, 8);

            switch (choice) {
                case 1:
                    handleAddCustomer();
                    break;
                case 2:
                    handleUpdateCustomer();
                    break;
                case 3:
                    handleShowCustomerList();
                    break;
                case 4:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi ParcelService.addParcel()
                    System.out.println("  🔧 [B4] Chức năng 'Thêm kiện hàng' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n");
                    break;
                case 5:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi ParcelService.displayAll()
                    System.out.println("  🔧 [B5] Chức năng 'Xem DS kiện hàng' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n");
                    break;
                case 6:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi OrderService.createOrder()
                    System.out.println("  🔧 [B6] Chức năng 'Tạo đơn giao hàng' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n");
                    break;
                case 7:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi OrderService.updateOrder()
                    System.out.println("  🔧 [B7] Chức năng 'Cập nhật đơn' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n");
                    break;
                case 8:
                    // TODO: Thành viên 3 (Trương Đan Huy) - Gọi OrderService.viewOrderDetail()
                    System.out.println("  🔧 [B8] Chức năng 'Xem chi tiết đơn' đang được phát triển bởi Thành viên 3 (Trương Đan Huy).\n");
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
    //  [B1] Thêm khách hàng mới
    // ----------------------------------------------------------
    private static void handleAddCustomer() {
        System.out.println("\n===== THÊM KHÁCH HÀNG MỚI =====");
        String id      = InputHelper.getStringInput("Mã khách hàng (VD: KH001): ");
        String name    = InputHelper.getStringInput("Họ tên: ");
        String phone   = InputHelper.getStringInput("Số điện thoại: ");
        String address = InputHelper.getStringInput("Địa chỉ: ");

        Customer customer = new Customer(id, name, phone, address);
        System.out.println(CustomerStaffService.addCustomer(customer));
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [B2] Cập nhật thông tin khách hàng
    //  FIX: Dùng getOptionalStringInput — nhấn Enter để giữ giá trị cũ
    // ----------------------------------------------------------
    private static void handleUpdateCustomer() {
        System.out.println("\n===== CẬP NHẬT KHÁCH HÀNG =====");
        String id = InputHelper.getStringInput("Mã khách hàng cần cập nhật: ");
        Customer existing = CustomerStaffService.findCustomer(id);
        if (existing == null) {
            System.out.println("❌ Không tìm thấy khách hàng với mã '" + id + "'.\n");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println(existing);
        System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

        String newName = InputHelper.getOptionalStringInput("Tên mới [" + existing.getName() + "]: ");
        if (newName.isEmpty()) newName = existing.getName();

        String newPhone = InputHelper.getOptionalStringInput("Số điện thoại mới [" + existing.getPhone() + "]: ");
        if (newPhone.isEmpty()) newPhone = existing.getPhone();

        String newAddress = InputHelper.getOptionalStringInput("Địa chỉ mới [" + existing.getAddress() + "]: ");
        if (newAddress.isEmpty()) newAddress = existing.getAddress();

        Customer updated = new Customer(id, newName, newPhone, newAddress);
        System.out.println(CustomerStaffService.updateCustomer(updated));
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [B3] Hiển thị danh sách khách hàng
    // ----------------------------------------------------------
    private static void handleShowCustomerList() {
        System.out.println("\n===== DANH SÁCH KHÁCH HÀNG =====");
        List<Customer> customers = CustomerStaffService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("  (Chưa có khách hàng nào.)\n");
            return;
        }

        System.out.println("+------------+----------------------+-----------------+--------------------------------+");
        System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
        System.out.println("+------------+----------------------+-----------------+--------------------------------+");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
        System.out.println("+------------+----------------------+-----------------+--------------------------------+\n");
    }
}
