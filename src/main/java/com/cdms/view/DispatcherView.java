// ============================================================
// File: DispatcherView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Điều phối viên (Dispatcher).
//              Quản lý việc gán shipper, cập nhật hành trình,
//              và theo dõi trạng thái giao nhận.
// Phân công: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.core.FormCancelledException;
import com.cdms.model.DeliveryStaff;
import com.cdms.model.DeliveryOrder;
import com.cdms.service.CustomerStaffService;
import com.cdms.repository.DeliveryOrderRepository;
import com.cdms.repository.DeliveryStaffRepository;

import java.util.List;

public class DispatcherView {

    // ANSI Colors for beautiful UI
    private static final String RESET = "\u001B[0m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String BOLD_GREEN = "\u001B[1;32m";
    private static final String BOLD_RED = "\u001B[1;31m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD_WHITE = "\u001B[1;37m";

    // Ngăn khởi tạo đối tượng
    private DispatcherView() {
    }

    // ==========================================================
    // SUBMENU: DISPATCHER (Điều phối viên)
    // Các tính năng: B9, B10, B11, B12, B13, B14
    // ==========================================================

    /**
     * Menu chính cho vai trò Dispatcher.
     * Dispatcher có quyền: thêm/xem/sửa/xóa shipper, phân công đơn,
     * cập nhật trạng thái, xem đơn đang giao/thất bại.
     */
    public static void showDispatcherMenu() {
        boolean running = true;

        while (running) {
            System.out.println(
                    BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "DISPATCHER - MENU CHÍNH     " + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ SHIPPER]                                    "
                    + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE
                    + "Thêm nhân viên giao hàng                          " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE
                    + "Phân công đơn cho shipper                         " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE
                    + "Xem đơn đã giao của shipper                       " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE
                    + "Xem danh sách shipper                             " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE
                    + "Cập nhật thông tin shipper                        " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE
                    + "Xóa nhân viên giao hàng                           " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN
                    + "    [THEO DÕI GIAO HÀNG]                               " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE
                    + "Cập nhật trạng thái đơn                           " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  8. " + WHITE
                    + "Hiển thị đơn đang giao                            " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  9. " + WHITE
                    + "Hiển thị đơn giao thất bại                        " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  10." + WHITE
                    + " Tìm kiếm shipper theo Tên/SĐT                   " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE
                    + "Quay lại Menu chính                               " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-10): " + RESET, 0, 10);

            switch (choice) {
                case 1:
                    handleAddDeliveryStaff();
                    break;
                case 2:
                    handleAssignStaff();
                    break;
                case 3:
                    handleViewDeliveredByStaff();
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
                    handleUpdateOrderStatus();
                    break;
                case 8:
                    handleShowInTransit();
                    break;
                case 9:
                    handleShowFailed();
                    break;
                case 10:
                    handleSearchStaff();
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
    // [B9] Thêm nhân viên giao hàng mới
    // ----------------------------------------------------------
    private static void handleAddDeliveryStaff() {
        System.out.println("\n===== THÊM NHÂN VIÊN GIAO HÀNG MỚI =====");
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã shipper (VD: NV001): ",
                    val -> CustomerStaffService.findStaff(val) == null,
                    "Mã shipper đã tồn tại trong hệ thống!");
            String name = InputHelper.getValidNameInput("Họ tên: ");
            String phone = InputHelper.getPhoneInput("Số điện thoại: ");
            String vehicleType = InputHelper.getStringInput("Loại phương tiện (Motorbike/Truck/...): ");

            System.out.println("\nXác nhận lưu thông tin shipper này?");
            System.out.println("  1. Lưu");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                DeliveryStaff staff = new DeliveryStaff(id, name, phone, vehicleType, "Active", 0);
                System.out.println(BOLD_GREEN + CustomerStaffService.addStaff(staff) + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác thêm nhân viên." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    // [DS] Hiển thị danh sách shipper
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
    // [CP] Cập nhật thông tin shipper
    // ----------------------------------------------------------
    private static void handleUpdateDeliveryStaff() {
        System.out.println("\n===== CẬP NHẬT NHÂN VIÊN GIAO HÀNG =====");
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã shipper cần cập nhật: ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Mã shipper không tồn tại trong hệ thống!");
            DeliveryStaff existing = CustomerStaffService.findStaff(id);

            System.out.println("Thông tin hiện tại:");
            System.out.println(existing);
            System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

            String newName = InputHelper.getOptionalValidatedStringInput("Tên mới [" + existing.getName() + "]: ",
                    val -> !val.matches("\\d+"),
                    "Tên shipper không được phép là số!");
            if (newName.isEmpty()) {
                newName = existing.getName();
            }

            String newPhone = InputHelper.getOptionalValidatedStringInput("Số điện thoại mới [" + existing.getPhone() + "]: ",
                    val -> val.matches("\\d{9,11}"),
                    "Số điện thoại không hợp lệ (phải gồm 9-11 chữ số)!");
            if (newPhone.isEmpty()) {
                newPhone = existing.getPhone();
            }

            String newVehicle = InputHelper.getOptionalStringInput("Loại phương tiện mới [" + existing.getVehicleType() + "]: ");
            if (newVehicle.isEmpty()) {
                newVehicle = existing.getVehicleType();
            }

            String newStatus = InputHelper.getOptionalValidatedStringInput("Trạng thái mới [" + existing.getStatus() + "] (Active/Inactive): ",
                    val -> val.equalsIgnoreCase("Active") || val.equalsIgnoreCase("Inactive"),
                    "Trạng thái không hợp lệ (Chỉ nhận Active hoặc Inactive)!");
            if (newStatus.isEmpty()) {
                newStatus = existing.getStatus();
            }

            System.out.println("\nXác nhận lưu cập nhật shipper này?");
            System.out.println("  1. Đồng ý");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                DeliveryStaff updated = new DeliveryStaff(id, newName, newPhone, newVehicle, newStatus,
                        existing.getDeliveredOrdersCount());
                System.out.println(BOLD_GREEN + CustomerStaffService.updateStaff(updated) + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác cập nhật shipper." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    // [X] Xóa nhân viên giao hàng
    // ----------------------------------------------------------
    private static void handleDeleteDeliveryStaff() {
        System.out.println("\n===== XÓA NHÂN VIÊN GIAO HÀNG =====");
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã shipper cần xóa: ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Không tìm thấy shipper!");
            DeliveryStaff existing = CustomerStaffService.findStaff(id);

            System.out.println("Thông tin shipper sẽ bị xóa:");
            System.out.println(existing);

            System.out.println("\nXác nhận xóa shipper này?");
            System.out.println("  1. Xóa");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                System.out.println(BOLD_GREEN + CustomerStaffService.deleteStaff(id) + RESET);
            } else {
                System.out.println("  Đã hủy thao tác xóa.");
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    // [B10] Phân công đơn cho shipper
    // ----------------------------------------------------------
    private static void handleAssignStaff() {
        System.out.println(BOLD_CYAN + "\n===== PHÂN CÔNG ĐƠN GIAO HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại!");
            String staffId = InputHelper.getStringInput("Mã nhân viên giao hàng (Staff ID): ",
                    val -> {
                        var s = CustomerStaffService.findStaff(val);
                        return s != null && "Active".equalsIgnoreCase(s.getStatus());
                    },
                    "Mã shipper không tồn tại hoặc không ở trạng thái Active!");

            System.out.println("\nXác nhận phân công đơn hàng cho shipper này?");
            System.out.println("  1. Phân công");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    com.cdms.service.TrackingService.assignStaff(orderId, staffId);
                    System.out.println(BOLD_GREEN + "✅ Phân công shipper thành công!" + RESET);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy phân công đơn hàng." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    // ----------------------------------------------------------
    // [B11] Xem đơn đã giao của shipper
    // ----------------------------------------------------------
    private static void handleViewDeliveredByStaff() {
        System.out.println(BOLD_CYAN + "\n===== ĐƠN HÀNG ĐÃ GIAO THÀNH CÔNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String staffId = InputHelper.getStringInput("Mã nhân viên giao hàng (Staff ID): ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Mã shipper không tồn tại!");
            try {
                List<DeliveryOrder> orders = com.cdms.service.TrackingService.getDeliveredOrdersByStaff(staffId);
                if (orders.isEmpty()) {
                    System.out.println("Shipper này chưa giao thành công đơn hàng nào.");
                    return;
                }
                System.out.println(BOLD_GREEN + "✅ Danh sách đơn hàng đã giao thành công (" + orders.size() + "):" + RESET);
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
    // [B12] Cập nhật trạng thái đơn
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
                    DeliveryOrder o = com.cdms.service.TrackingService.updateStatus(orderId, status);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật trạng thái thành công sang: " + o.getStatus() + RESET);
                    System.out.println(o);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác cập nhật." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    // ----------------------------------------------------------
    // [B13] Hiển thị đơn đang giao
    // ----------------------------------------------------------
    private static void handleShowInTransit() {
        System.out.println(BOLD_CYAN + "\n===== CÁC ĐƠN HÀNG ĐANG GIAO =====" + RESET);
        List<DeliveryOrder> orders = com.cdms.service.TrackingService.getInTransitOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào đang trong quá trình vận chuyển (In Transit).");
            return;
        }
        System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng đang vận chuyển:" + RESET);
        for (DeliveryOrder o : orders) {
            System.out.println(o);
        }
    }

    // ----------------------------------------------------------
    // [B14] Hiển thị đơn giao thất bại
    // ----------------------------------------------------------
    private static void handleShowFailed() {
        System.out.println(BOLD_CYAN + "\n===== CÁC ĐƠN HÀNG GIAO THẤT BẠI =====" + RESET);
        List<DeliveryOrder> orders = com.cdms.service.TrackingService.getFailedOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào giao thất bại (Failed).");
            return;
        }
        System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng giao thất bại:" + RESET);
        for (DeliveryOrder o : orders) {
            System.out.println(o);
        }
    }

    // ----------------------------------------------------------
    //  Tìm kiếm shipper theo tên hoặc SĐT
    // ----------------------------------------------------------
    private static void handleSearchStaff() {
        System.out.println(BOLD_CYAN + "\n===== TÌM KIẾM NHÂN VIÊN GIAO HÀNG (SHIPPER) =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            System.out.println("1. Tìm theo tên shipper (Khớp một phần)");
            System.out.println("2. Tìm theo số điện thoại (Chính xác)");
            int type = InputHelper.getIntInput("Chọn kiểu tìm kiếm (1-2): ", 1, 2);

            if (type == 1) {
                String name = InputHelper.getStringInput("Nhập tên shipper: ");
                List<DeliveryStaff> result = DeliveryStaffRepository.findByName(name);
                if (result.isEmpty()) {
                    System.out.println("Không tìm thấy shipper nào khớp với tên: " + name);
                } else {
                    System.out.println(BOLD_GREEN + "Tìm thấy " + result.size() + " shipper:" + RESET);
                    for (DeliveryStaff s : result) {
                        System.out.println(s);
                    }
                }
            } else {
                String phone = InputHelper.getPhoneInput("Nhập số điện thoại: ");
                DeliveryStaff s = DeliveryStaffRepository.findByPhone(phone);
                if (s == null) {
                    System.out.println("Không tìm thấy shipper nào có số điện thoại: " + phone);
                } else {
                    System.out.println(BOLD_GREEN + "Đã tìm thấy shipper:" + RESET);
                    System.out.println(s);
                }
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }
}
