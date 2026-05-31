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
                    + " Tìm kiếm shipper theo Tên/SĐT                    " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  11." + WHITE
                    + " Xem tất cả đơn hàng                              " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE
                    + "Quay lại Menu chính                               " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-11): " + RESET, 0, 11);

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
                case 11:
                    handleShowAllOrders();
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
        System.out.println(BOLD_CYAN + "\n===== THÊM NHÂN VIÊN GIAO HÀNG MỚI =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã shipper (VD: NV001): ",
                    val -> CustomerStaffService.findStaff(val) == null,
                    "Mã shipper đã tồn tại trong hệ thống!");
            String name = InputHelper.getValidNameInput("Họ tên: ");
            String phone = InputHelper.getPhoneInput("Số điện thoại: ",
                    val -> DeliveryStaffRepository.findByPhone(val) == null,
                    "Số điện thoại đã được sử dụng bởi shipper khác!");
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
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Hiển thị danh sách shipper
    // ----------------------------------------------------------
    private static void handleShowStaffList() {
        System.out.println(BOLD_CYAN + "\n===== DANH SÁCH NHÂN VIÊN GIAO HÀNG =====" + RESET);
        List<DeliveryStaff> staffs = CustomerStaffService.getAllStaffs();
        if (staffs.isEmpty()) {
            System.out.println("  (Chưa có nhân viên nào.)\n");
            InputHelper.pressEnterToContinue();
            return;
        }

        // Căn chỉnh tiêu đề bảng khớp hoàn hảo với toString() của Model (UX-05)
        System.out.println(
                "+------------+----------------------+-----------------+--------------+----------+--------------------+");
        System.out.printf("| %-10s | %-20s | %-15s | %-12s | %-8s | %-18s |%n",
                "Mã NV", "Họ tên", "Điện thoại", "Phương tiện", "T.Thái", "Số đơn đã giao");
        System.out.println(
                "+------------+----------------------+-----------------+--------------+----------+--------------------+");

        // Hỗ trợ phân trang danh sách shipper (UX-13)
        InputHelper.printPaginatedList(staffs, 10,
                "+------------+----------------------+-----------------+--------------+----------+--------------------+");
        System.out.println(BOLD_GREEN + "Tổng số nhân viên: " + staffs.size() + RESET);
        System.out.println();
        InputHelper.pressEnterToContinue(); // UX-04
    }

    // ----------------------------------------------------------
    // Cập nhật thông tin shipper
    // ----------------------------------------------------------
    private static void handleUpdateDeliveryStaff() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT NHÂN VIÊN GIAO HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã shipper cần cập nhật: ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Mã shipper không tồn tại trong hệ thống!");
            DeliveryStaff existing = CustomerStaffService.findStaff(id);

            System.out.println("\nThông tin hiện tại:");
            System.out.println(
                    "+------------+----------------------+-----------------+--------------+----------+--------------------+");
            System.out.println(
                    "| Mã NV      | Họ tên               | Điện thoại      | Phương tiện  | T.Thái   | Số đơn đã giao     |");
            System.out.println(
                    "+------------+----------------------+-----------------+--------------+----------+--------------------+");
            System.out.println(existing);
            System.out.println(
                    "+------------+----------------------+-----------------+--------------+----------+--------------------+");
            System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

            String newName = InputHelper.getOptionalValidatedStringInput("Tên mới [" + existing.getName() + "]: ",
                    val -> !val.matches("\\d+"),
                    "Tên shipper không được phép là số!");
            if (newName.isEmpty()) {
                newName = existing.getName();
            }

            String newPhone = InputHelper.getOptionalPhoneInput("Số điện thoại mới [" + existing.getPhone() + "]: ",
                    val -> val.equalsIgnoreCase(existing.getPhone())
                            || DeliveryStaffRepository.findByPhone(val) == null,
                    "Số điện thoại đã được sử dụng bởi shipper khác!");
            if (newPhone.isEmpty()) {
                newPhone = existing.getPhone();
            }

            String newVehicle = InputHelper
                    .getOptionalStringInput("Loại phương tiện mới [" + existing.getVehicleType() + "]: ");
            if (newVehicle.isEmpty()) {
                newVehicle = existing.getVehicleType();
            }

            String newStatus = InputHelper.getOptionalValidatedStringInput(
                    "Trạng thái mới [" + existing.getStatus() + "] (Active/Inactive/Fired): ",
                    val -> val.equalsIgnoreCase("Active") || val.equalsIgnoreCase("Inactive")
                            || val.equalsIgnoreCase("Fired"),
                    "Trạng thái không hợp lệ (Chỉ nhận Active, Inactive hoặc Fired)!");
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
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Xóa nhân viên giao hàng
    // ----------------------------------------------------------
    private static void handleDeleteDeliveryStaff() {
        System.out.println(BOLD_CYAN + "\n===== XÓA NHÂN VIÊN GIAO HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã shipper cần xóa: ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Không tìm thấy shipper!");
            DeliveryStaff existing = CustomerStaffService.findStaff(id);

            System.out.println("\nThông tin shipper sẽ bị xóa:");
            System.out.println(
                    "+------------+----------------------+-----------------+--------------+----------+--------------------+");
            System.out.println(
                    "| Mã NV      | Họ tên               | Điện thoại      | Phương tiện  | T.Thái   | Số đơn đã giao     |");
            System.out.println(
                    "+------------+----------------------+-----------------+--------------+----------+--------------------+");
            System.out.println(existing);
            System.out.println(
                    "+------------+----------------------+-----------------+--------------+----------+--------------------+");

            // Kiểm tra ràng buộc và cảnh báo ảnh hưởng khi xóa (UX-09)
            long activeOrders = DeliveryOrderRepository.findByStaffId(id).stream()
                    .filter(o -> !"Delivered".equalsIgnoreCase(o.getStatus())
                            && !"Cancelled".equalsIgnoreCase(o.getStatus())
                            && !"Failed".equalsIgnoreCase(o.getStatus()))
                    .count();
            if (activeOrders > 0) {
                System.out.println(BOLD_RED + "⚠️ CẢNH BÁO: Shipper này đang chịu trách nhiệm cho " + activeOrders
                        + " đơn hàng chưa hoàn thành!" + RESET);
                System.out.println(BOLD_RED + "❌ Không thể xóa shipper khi họ đang có đơn hàng cần xử lý!" + RESET);
                return;
            }

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
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // [B10] Phân công đơn cho shipper
    // ----------------------------------------------------------
    private static void handleAssignStaff() {
        System.out.println(BOLD_CYAN + "\n===== PHÂN CÔNG ĐƠN GIAO HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            // Hiển thị danh sách các shipper Active khả dụng (UX-02)
            List<DeliveryStaff> activeStaffs = DeliveryStaffRepository.findAll().stream()
                    .filter(s -> "Active".equalsIgnoreCase(s.getStatus()))
                    .toList();
            System.out.println(BOLD_YELLOW + "\nDanh sách shipper đang hoạt động (Active):" + RESET);
            if (activeStaffs.isEmpty()) {
                System.out.println("  ❌ Không có shipper nào khả dụng!");
                return;
            }
            for (DeliveryStaff s : activeStaffs) {
                System.out.printf("  - %-6s : %-20s (Đã giao: %d đơn)%n", s.getId(), s.getName(),
                        s.getDeliveredOrdersCount());
            }

            // Hiển thị danh sách các đơn hàng chưa gán shipper hoặc đang Pending (UX-03)
            List<DeliveryOrder> pendingOrders = DeliveryOrderRepository.findAll().stream()
                    .filter(o -> "Pending".equalsIgnoreCase(o.getStatus()))
                    .toList();
            System.out.println(BOLD_YELLOW + "\nDanh sách đơn hàng chưa phân công (Pending):" + RESET);
            if (pendingOrders.isEmpty()) {
                System.out.println("  (Không có đơn hàng nào cần phân công.)");
            } else {
                for (DeliveryOrder o : pendingOrders) {
                    System.out.printf("  - Đơn: %-6s | Kiện: %-6s | Ngày: %s%n", o.getId(), o.getParcelId(),
                            o.getOrderDate());
                }
            }
            System.out.println();

            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng cần phân công: ",
                    val -> {
                        DeliveryOrder o = DeliveryOrderRepository.findById(val);
                        return o != null && "Pending".equalsIgnoreCase(o.getStatus());
                    },
                    "Mã đơn hàng không hợp lệ (đã phân công, bị hủy hoặc không tồn tại)!");

            // Hiển thị chi tiết đơn trước khi chọn shipper
            DeliveryOrder order = DeliveryOrderRepository.findById(orderId);
            System.out.println("\nThông tin đơn hàng đã chọn:");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println(order);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");

            String staffId = InputHelper.getStringInput("Nhập mã shipper phân công (Staff ID): ",
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
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // [B11] Xem đơn đã giao của shipper
    // ----------------------------------------------------------
    private static void handleViewDeliveredByStaff() {
        System.out.println(BOLD_CYAN + "\n===== ĐƠN HÀNG ĐÃ GIAO THÀNH CÔNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            // Hiển thị danh sách shipper để chọn (UX-02)
            List<DeliveryStaff> staffs = DeliveryStaffRepository.findAll();
            System.out.println(BOLD_YELLOW + "\nDanh sách shipper:" + RESET);
            for (DeliveryStaff s : staffs) {
                System.out.printf("  - %-6s : %-20s (Trạng thái: %s)%n", s.getId(), s.getName(), s.getStatus());
            }
            System.out.println();

            String staffId = InputHelper.getStringInput("Mã nhân viên giao hàng (Staff ID): ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Mã shipper không tồn tại!");
            try {
                List<DeliveryOrder> orders = com.cdms.service.TrackingService.getDeliveredOrdersByStaff(staffId);
                if (orders.isEmpty()) {
                    System.out.println("Shipper này chưa giao thành công đơn hàng nào.");
                    return;
                }
                System.out
                        .println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng đã giao thành công:" + RESET);
                System.out.println("+------------+------------+------------+------------+------------+--------------+");
                System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-12s |%n",
                        "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Ngày Tạo");
                System.out.println("+------------+------------+------------+------------+------------+--------------+");
                InputHelper.printPaginatedList(orders, 10,
                        "+------------+------------+------------+------------+------------+--------------+");
            } catch (Exception e) {
                System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
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

            // Hiển thị trạng thái hiện tại trước khi cập nhật với đầy đủ cước phí
            DeliveryOrder current = DeliveryOrderRepository.findById(orderId);
            System.out.println(BOLD_YELLOW + "\n[ĐƠN HÀNG HIỆN TẠI]:" + RESET);
            ParcelOrderView.printSingleOrderDetails(current);
            System.out.println("Trạng thái hiện tại: " + BOLD_WHITE + current.getStatus() + RESET);
            System.out.println();

            String status = InputHelper.getStringInput(
                    "Trạng thái mới (Picked Up/In Transit/Delivered/Failed/Cancelled): ",
                    val -> val.equalsIgnoreCase("Picked Up") || val.equalsIgnoreCase("In Transit")
                            || val.equalsIgnoreCase("Delivered") || val.equalsIgnoreCase("Failed")
                            || val.equalsIgnoreCase("Cancelled"),
                    "Trạng thái không hợp lệ!");

            System.out.println("\nXác nhận cập nhật trạng thái đơn hàng?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    DeliveryOrder o = com.cdms.service.TrackingService.updateStatus(orderId, status);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật trạng thái thành công sang: " + o.getStatus() + RESET);
                    ParcelOrderView.printSingleOrderDetails(o);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác cập nhật." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
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
            InputHelper.pressEnterToContinue();
            return;
        }
        System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng đang giao:" + RESET);
        System.out.println("+------------+------------+------------+------------+------------+--------------+");
        System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-12s |%n",
                "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Ngày Tạo");
        System.out.println("+------------+------------+------------+------------+------------+--------------+");
        InputHelper.printPaginatedList(orders, 10,
                "+------------+------------+------------+------------+------------+--------------+");
        InputHelper.pressEnterToContinue(); // UX-04
    }

    // ----------------------------------------------------------
    // [B14] Hiển thị đơn giao thất bại hoặc bị hủy
    // ----------------------------------------------------------
    private static void handleShowFailed() {
        System.out.println(BOLD_CYAN + "\n===== DANH SÁCH ĐƠN GIAO THẤT BẠI HOẶC BỊ HỦY =====" + RESET);
        List<DeliveryOrder> orders = com.cdms.service.TrackingService.getFailedOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào giao thất bại (Failed) hoặc bị hủy (Cancelled).");
            InputHelper.pressEnterToContinue();
            return;
        }

        List<String> formattedFailed = orders.stream()
                .map(o -> {
                    String reason = (o.getNotes() != null && !o.getNotes().isEmpty())
                            ? o.getNotes().get(o.getNotes().size() - 1)
                            : "Không có ghi chú";
                    // Giới hạn độ dài lý do để không bị vỡ bảng
                    if (reason.length() > 30) {
                        reason = reason.substring(0, 27) + "...";
                    }
                    return String.format("| %-10s | %-10s | %-10s | %-10s | %-12s | %-30s |",
                            o.getId(), o.getParcelId(),
                            (o.getStaffId() != null ? o.getStaffId() : "Chưa phân"),
                            o.getDeliveryType(),
                            o.getStatus(),
                            reason);
                })
                .toList();

        System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng giao thất bại hoặc bị hủy:" + RESET);
        System.out.println(
                "+------------+------------+------------+------------+--------------+--------------------------------+");
        System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-12s | %-30s |%n",
                "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Lý do Thất bại/Hủy");
        System.out.println(
                "+------------+------------+------------+------------+--------------+--------------------------------+");
        InputHelper.printPaginatedList(formattedFailed, 10,
                "+------------+------------+------------+------------+--------------+--------------------------------+");
        InputHelper.pressEnterToContinue(); // UX-04
    }

    // ----------------------------------------------------------
    // Tìm kiếm shipper theo tên hoặc SĐT
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
                    System.out.println(
                            "+------------+----------------------+-----------------+--------------+----------+--------------------+");
                    System.out.printf("| %-10s | %-20s | %-15s | %-12s | %-8s | %-18s |%n",
                            "Mã NV", "Họ tên", "Điện thoại", "Phương tiện", "T.Thái", "Số đơn đã giao");
                    System.out.println(
                            "+------------+----------------------+-----------------+--------------+----------+--------------------+");
                    InputHelper.printPaginatedList(result, 10,
                            "+------------+----------------------+-----------------+--------------+----------+--------------------+");
                }
            } else {
                String phone = InputHelper.getPhoneInput("Nhập số điện thoại: ");
                DeliveryStaff s = DeliveryStaffRepository.findByPhone(phone);
                if (s == null) {
                    System.out.println("Không tìm thấy shipper nào có số điện thoại: " + phone);
                } else {
                    System.out.println(BOLD_GREEN + "Đã tìm thấy shipper:" + RESET);
                    System.out.println(
                            "+------------+----------------------+-----------------+--------------+----------+--------------------+");
                    System.out.println(
                            "| Mã NV      | Họ tên               | Điện thoại      | Phương tiện  | T.Thái   | Số đơn đã giao     |");
                    System.out.println(
                            "+------------+----------------------+-----------------+--------------+----------+--------------------+");
                    System.out.println(s);
                    System.out.println(
                            "+------------+----------------------+-----------------+--------------+----------+--------------------+");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Hiển thị tất cả đơn hàng (UX-07)
    // ----------------------------------------------------------
    private static void handleShowAllOrders() {
        System.out.println(BOLD_CYAN + "\n===== XEM TẤT CẢ ĐƠN HÀNG =====" + RESET);
        List<DeliveryOrder> orders = DeliveryOrderRepository.findAll();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào trong hệ thống.");
            InputHelper.pressEnterToContinue();
            return;
        }

        List<String> formattedLines = orders.stream().map(order -> {
            String pt = order.getPaymentTerms() != null ? order.getPaymentTerms() : "Receiver Pay";
            String ptFormatted = "Sender Pay".equalsIgnoreCase(pt) ? "Sender (Prepaid)" : "Receiver (COD)";

            // Tính toán cước phí tương ứng (lấy từ hóa đơn hoặc tính động)
            double totalFee = 0.0;
            com.cdms.model.Invoice invoice = com.cdms.repository.InvoiceRepository.findByOrderId(order.getId());
            if (invoice != null) {
                totalFee = invoice.getTotalAmount();
            } else {
                com.cdms.model.Parcel parcel = com.cdms.repository.ParcelRepository.findById(order.getParcelId());
                if (parcel != null) {
                    double baseFee = parcel.calculateFee();
                    double urgent = "Urgent".equalsIgnoreCase(order.getDeliveryType()) ? 20000.0 : 0.0;
                    totalFee = baseFee + urgent;
                }
            }
            String totalFeeStr = String.format("%,.0f", totalFee);

            String payStatus = "Unpaid";
            if (invoice != null) {
                payStatus = invoice.getPaymentStatus();
                if ("Collected".equalsIgnoreCase(payStatus)) {
                    payStatus = "Shipper đã thu";
                }
            } else {
                if ("Sender Pay".equalsIgnoreCase(pt)) {
                    payStatus = "Paid (Quầy)";
                } else {
                    if ("Delivered".equalsIgnoreCase(order.getStatus())) {
                        payStatus = "Shipper đã thu";
                    } else {
                        payStatus = "COD (Chờ thu)";
                    }
                }
            }

            // Chọn màu cho Trạng thái Thanh toán để UI cực kỳ cao cấp
            String payStatusColored;
            if ("Paid".equalsIgnoreCase(payStatus) || "Paid (Quầy)".equalsIgnoreCase(payStatus)) {
                payStatusColored = BOLD_GREEN + String.format("%-15s", payStatus) + RESET;
            } else if ("Collected".equalsIgnoreCase(payStatus) || "Shipper đã thu".equalsIgnoreCase(payStatus)) {
                payStatusColored = BOLD_CYAN + String.format("%-15s", payStatus) + RESET;
            } else {
                payStatusColored = BOLD_RED + String.format("%-15s", payStatus) + RESET;
            }

            // Chọn màu cho Trạng thái đơn hàng
            String statusColor = BOLD_YELLOW;
            if ("Delivered".equalsIgnoreCase(order.getStatus())) {
                statusColor = BOLD_GREEN;
            } else if ("Failed".equalsIgnoreCase(order.getStatus())
                    || "Cancelled".equalsIgnoreCase(order.getStatus())) {
                statusColor = BOLD_RED;
            }

            return String.format(
                    "| %-10s | %-10s | %-10s | %-10s | " + statusColor + "%-10s" + RESET + " | %-18s | %15s | %s |",
                    order.getId(),
                    order.getParcelId(),
                    (order.getStaffId() != null ? order.getStaffId() : "Chưa phân"),
                    order.getDeliveryType(),
                    order.getStatus(),
                    ptFormatted,
                    totalFeeStr,
                    payStatusColored);
        }).toList();

        String tableHeader = "+------------+------------+------------+------------+------------+--------------------+-----------------+-----------------+";
        System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng trong hệ thống:" + RESET);
        System.out.println(tableHeader);
        System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-18s | %-15s | %-15s |%n",
                "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Thanh Toán", "Cước Phí (VND)",
                "Trạng thái TT");
        System.out.println(tableHeader);

        // Hỗ trợ hiển thị phân trang
        InputHelper.printPaginatedList(formattedLines, 10, tableHeader);
        InputHelper.pressEnterToContinue(); // UX-04
    }
}
