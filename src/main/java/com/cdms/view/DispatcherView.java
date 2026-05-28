// ============================================================
// File: DispatcherView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Điều phối viên (Dispatcher).
//              Quản lý việc gán shipper, cập nhật hành trình,
//              và theo dõi trạng thái giao nhận.
//              🔧 BÀN GIAO CHO: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.model.DeliveryStaff;
import com.cdms.service.CustomerStaffService;

import java.util.List;

public class DispatcherView {

    // ANSI Colors for beautiful UI
    private static final String RESET       = "\u001B[0m";
    private static final String BOLD_CYAN   = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN  = "\u001B[1;32m";
    private static final String WHITE       = "\u001B[37m";
    private static final String BOLD_WHITE  = "\u001B[1;37m";
    private static final String PURPLE      = "\u001B[35m";

    // Ngăn khởi tạo đối tượng
    private DispatcherView() {
    }

    // ==========================================================
    //  SUBMENU: DISPATCHER (Điều phối viên)
    //  Các tính năng: B9, B10, B11, B12, B13, B14
    // ==========================================================

    /**
     * Menu chính cho vai trò Dispatcher.
     * Dispatcher có quyền: thêm/xem/sửa/xóa shipper, phân công đơn,
     * cập nhật trạng thái, xem đơn đang giao/thất bại.
     */
    public static void showDispatcherMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_CYAN + "╔═══════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_CYAN + "║" + BOLD_YELLOW + "       DISPATCHER - MENU CHÍNH         " + RESET + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╠═══════════════════════════════════════╣" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[QUẢN LÝ SHIPPER]                    " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "1. " + WHITE + "Thêm nhân viên giao hàng   (B9)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "2. " + WHITE + "Phân công đơn cho shipper  (B10)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "3. " + WHITE + "Xem đơn đã giao của shipper(B11)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "4. " + WHITE + "Xem danh sách shipper      (DS)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "5. " + WHITE + "Cập nhật thông tin shipper (CP)   " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "6. " + WHITE + "Xóa nhân viên giao hàng    (X)    " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_GREEN + "[THEO DÕI GIAO HÀNG]                 " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "7. " + WHITE + "Cập nhật trạng thái đơn    (B12)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "8. " + WHITE + "Hiển thị đơn đang giao     (B13)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "9. " + WHITE + "Hiển thị đơn giao thất bại (B14)  " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "║                                       ║" + RESET);
            System.out.println(BOLD_CYAN + "║  " + BOLD_YELLOW + "0. " + BOLD_WHITE + "Quay lại Menu chính               " + BOLD_CYAN + "║" + RESET);
            System.out.println(BOLD_CYAN + "╚═══════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput("Chọn chức năng (0-9): ", 0, 9);

            switch (choice) {
                case 1:
                    handleAddDeliveryStaff();
                    break;
                case 2:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.assignStaff()
                    System.out.println(PURPLE + "  🔧 [B10] Chức năng 'Phân công đơn' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 3:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.viewDeliveredByStaff()
                    System.out.println(PURPLE + "  🔧 [B11] Chức năng 'Xem đơn đã giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 4:
                    handleShowStaffList();
                    break;
                case 5:
                    handleUpdateDeliveryStaff();
                    break;
                case 6:
                    handleDeleteDeliveryStaff();
                    break;
                case 7:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.updateStatus()
                    System.out.println(PURPLE + "  🔧 [B12] Chức năng 'Cập nhật trạng thái' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 8:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.showInTransit()
                    System.out.println(PURPLE + "  🔧 [B13] Chức năng 'Đơn đang giao' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
                    break;
                case 9:
                    // TODO: Nguyễn Thanh Tùng (Thành viên 4) - Gọi TrackingService.showFailed()
                    System.out.println(PURPLE + "  🔧 [B14] Chức năng 'Đơn giao thất bại' đang được phát triển bởi Nguyễn Thanh Tùng (Thành viên 4).\n" + RESET);
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
    //  [B9] Thêm nhân viên giao hàng mới
    // ----------------------------------------------------------
    private static void handleAddDeliveryStaff() {
        System.out.println("\n===== THÊM NHÂN VIÊN GIAO HÀNG MỚI =====");
        String id          = InputHelper.getStringInput("Mã shipper (VD: NV001): ");
        String name        = InputHelper.getStringInput("Họ tên: ");
        String phone       = InputHelper.getStringInput("Số điện thoại: ");
        String vehicleType = InputHelper.getStringInput("Loại phương tiện (Motorbike/Truck/...): ");

        // Mặc định thêm shipper với trạng thái Active và 0 đơn giao
        DeliveryStaff staff = new DeliveryStaff(id, name, phone, vehicleType, "Active", 0);
        System.out.println(CustomerStaffService.addStaff(staff));
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [DS] Hiển thị danh sách shipper
    // ----------------------------------------------------------
    private static void handleShowStaffList() {
        System.out.println("\n===== DANH SÁCH NHÂN VIÊN GIAO HÀNG =====");
        List<DeliveryStaff> staffs = CustomerStaffService.getAllStaffs();
        if (staffs.isEmpty()) {
            System.out.println("  (Chưa có nhân viên nào.)\n");
            return;
        }

        System.out.println("+------------+----------------------+-----------------+--------------+----------+");
        System.out.println("| Mã NV      | Họ tên               | Điện thoại      | Phương tiện  | Trạng thái|");
        System.out.println("+------------+----------------------+-----------------+--------------+----------+");
        for (DeliveryStaff s : staffs) {
            System.out.println(s);
        }
        System.out.println("+------------+----------------------+-----------------+--------------+----------+\n");
    }

    // ----------------------------------------------------------
    //  [CP] Cập nhật thông tin shipper
    //  FIX: Dùng getOptionalStringInput — nhấn Enter để giữ giá trị cũ
    // ----------------------------------------------------------
    private static void handleUpdateDeliveryStaff() {
        System.out.println("\n===== CẬP NHẬT NHÂN VIÊN GIAO HÀNG =====");
        String id = InputHelper.getStringInput("Mã shipper cần cập nhật: ");
        DeliveryStaff existing = CustomerStaffService.findStaff(id);
        if (existing == null) {
            System.out.println("❌ Không tìm thấy shipper với mã '" + id + "'.\n");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println(existing);
        System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

        String newName = InputHelper.getOptionalStringInput("Tên mới [" + existing.getName() + "]: ");
        if (newName.isEmpty()) newName = existing.getName();

        String newPhone = InputHelper.getOptionalStringInput("Số điện thoại mới [" + existing.getPhone() + "]: ");
        if (newPhone.isEmpty()) newPhone = existing.getPhone();

        String newVehicle = InputHelper.getOptionalStringInput("Loại phương tiện mới [" + existing.getVehicleType() + "]: ");
        if (newVehicle.isEmpty()) newVehicle = existing.getVehicleType();

        String newStatus = InputHelper.getOptionalStringInput("Trạng thái mới [" + existing.getStatus() + "] (Active/Inactive): ");
        if (newStatus.isEmpty()) newStatus = existing.getStatus();

        DeliveryStaff updated = new DeliveryStaff(id, newName, newPhone, newVehicle, newStatus,
                existing.getDeliveredOrdersCount());
        System.out.println(CustomerStaffService.updateStaff(updated));
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [X] Xóa nhân viên giao hàng
    // ----------------------------------------------------------
    private static void handleDeleteDeliveryStaff() {
        System.out.println("\n===== XÓA NHÂN VIÊN GIAO HÀNG =====");
        String id = InputHelper.getStringInput("Mã shipper cần xóa: ");
        DeliveryStaff existing = CustomerStaffService.findStaff(id);
        if (existing == null) {
            System.out.println("❌ Không tìm thấy shipper với mã '" + id + "'.\n");
            return;
        }

        System.out.println("Thông tin shipper sẽ bị xóa:");
        System.out.println(existing);
        String confirm = InputHelper.getStringInput("\nXác nhận xóa shipper \"" + id + "\"? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println(CustomerStaffService.deleteStaff(id));
        } else {
            System.out.println("  Đã hủy thao tác xóa.");
        }
        System.out.println();
    }
}
