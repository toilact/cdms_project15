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

    private static int getVisualWidth(String str) {
        if (str == null)
            return 0;
        // Strip ANSI escape sequences
        String stripped = str.replaceAll("\\u001B\\[[;\\d]*[a-zA-Z]", "");
        int width = 0;
        for (int i = 0; i < stripped.length();) {
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

            String group1 = BOLD_CYAN + "  [QUẢN LÝ SHIPPER]";
            String line1 = BOLD_WHITE + "  1. " + WHITE + "Thêm nhân viên giao hàng";
            String line2 = BOLD_WHITE + "  2. " + WHITE + "Phân công đơn cho shipper";
            String line3 = BOLD_WHITE + "  3. " + WHITE + "Xem đơn đã giao của shipper";
            String line4 = BOLD_WHITE + "  4. " + WHITE + "Xem danh sách shipper";
            String line5 = BOLD_WHITE + "  5. " + WHITE + "Cập nhật thông tin shipper";
            String line6 = BOLD_WHITE + "  6. " + WHITE + "Xóa nhân viên giao hàng";
            String lineSep = " ";
            String group2 = BOLD_CYAN + "  [THEO DÕI GIAO HÀNG]";
            String line7 = BOLD_WHITE + "  7. " + WHITE + "Cập nhật trạng thái đơn";
            String line8 = BOLD_WHITE + "  8. " + WHITE + "Hiển thị đơn đang giao";
            String line9 = BOLD_WHITE + "  9. " + WHITE + "Hiển thị đơn giao thất bại";
            String line10 = BOLD_WHITE + "  10." + WHITE + " Tìm kiếm shipper theo Tên/SĐT";
            String line11 = BOLD_WHITE + "  11." + WHITE + " Xem tất cả đơn hàng";
            String line0 = BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính";

            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(group1, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line1, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line2, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line3, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line4, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line5, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line6, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineSep, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(group2, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line7, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line8, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line9, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line10, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line11, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineSep, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line0, 55) + BOLD_YELLOW + "║" + RESET);

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
            String id;
            while (true) {
                id = InputHelper.getStringInput("Mã shipper (VD: NV001): ");
                if (CustomerStaffService.findStaff(id) == null) break;
                System.out.println("  ⚠ Lỗi: Mã shipper đã tồn tại trong hệ thống! Vui lòng nhập lại.");
            }
            String name = InputHelper.getValidNameInput("Họ tên: ");
            String phone;
            while (true) {
                phone = InputHelper.getPhoneInput("Số điện thoại: ");
                if (DeliveryStaffRepository.findByPhone(phone) == null) break;
                System.out.println("  ⚠ Lỗi: Số điện thoại đã được sử dụng bởi shipper khác! Vui lòng nhập lại.");
            }
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
            InputHelper.pressEnterToContinue();
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
            String id;
            while (true) {
                id = InputHelper.getStringInput("Mã shipper cần cập nhật: ");
                if (CustomerStaffService.findStaff(id) != null) break;
                System.out.println("  ⚠ Lỗi: Mã shipper không tồn tại trong hệ thống! Vui lòng nhập lại.");
            }
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

            String newName = InputHelper.getOptionalValidNameInput("Tên mới [" + existing.getName() + "]: ");
            if (newName.isEmpty()) {
                newName = existing.getName();
            }

            String newPhone;
            while (true) {
                newPhone = InputHelper.getOptionalPhoneInput("Số điện thoại mới [" + existing.getPhone() + "]: ");
                if (newPhone.isEmpty()) break;
                if (newPhone.equalsIgnoreCase(existing.getPhone()) || DeliveryStaffRepository.findByPhone(newPhone) == null) break;
                System.out.println("  ⚠ Lỗi: Số điện thoại đã được sử dụng bởi shipper khác! Vui lòng nhập lại.");
            }
            if (newPhone.isEmpty()) {
                newPhone = existing.getPhone();
            }

            String newVehicle = InputHelper
                    .getOptionalStringInput("Loại phương tiện mới [" + existing.getVehicleType() + "]: ");
            if (newVehicle.isEmpty()) {
                newVehicle = existing.getVehicleType();
            }

            String newStatus = InputHelper.getOptionalChoiceInput(
                    "Trạng thái mới [" + existing.getStatus() + "] (Active/Inactive/Fired): ",
                    "Trạng thái không hợp lệ (Chỉ nhận Active, Inactive hoặc Fired)!",
                    "Active", "Inactive", "Fired");
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
            String id;
            while (true) {
                id = InputHelper.getStringInput("Mã shipper cần xóa: ");
                if (CustomerStaffService.findStaff(id) != null) break;
                System.out.println("  ⚠ Lỗi: Không tìm thấy shipper! Vui lòng nhập lại.");
            }
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
            int activeOrders = 0;
            for (DeliveryOrder o : DeliveryOrderRepository.findByStaffId(id)) {
                if (!"Delivered".equalsIgnoreCase(o.getStatus())
                        && !"Cancelled".equalsIgnoreCase(o.getStatus())
                        && !"Failed".equalsIgnoreCase(o.getStatus())) {
                    activeOrders++;
                }
            }
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
            List<DeliveryStaff> activeStaffs = new java.util.ArrayList<>();
            for (DeliveryStaff s : DeliveryStaffRepository.findAll()) {
                if ("Active".equalsIgnoreCase(s.getStatus())) {
                    activeStaffs.add(s);
                }
            }
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
            List<DeliveryOrder> pendingOrders = new java.util.ArrayList<>();
            for (DeliveryOrder o : DeliveryOrderRepository.findAll()) {
                if ("Pending".equalsIgnoreCase(o.getStatus())) {
                    pendingOrders.add(o);
                }
            }
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

            String orderId;
            while (true) {
                orderId = InputHelper.getStringInput("Nhập mã đơn hàng cần phân công: ");
                DeliveryOrder _o = DeliveryOrderRepository.findById(orderId);
                if (_o != null && "Pending".equalsIgnoreCase(_o.getStatus())) break;
                System.out.println("  ⚠ Lỗi: Mã đơn hàng không hợp lệ (đã phân công, bị hủy hoặc không tồn tại)! Vui lòng nhập lại.");
            }

            // Hiển thị chi tiết đơn trước khi chọn shipper
            DeliveryOrder order = DeliveryOrderRepository.findById(orderId);
            System.out.println("\nThông tin đơn hàng đã chọn:");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println(order);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");

            String staffId;
            while (true) {
                staffId = InputHelper.getStringInput("Nhập mã shipper phân công (Staff ID): ");
                var _s = CustomerStaffService.findStaff(staffId);
                if (_s != null && "Active".equalsIgnoreCase(_s.getStatus())) break;
                System.out.println("  ⚠ Lỗi: Mã shipper không tồn tại hoặc không ở trạng thái Active! Vui lòng nhập lại.");
            }

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

            String staffId;
            while (true) {
                staffId = InputHelper.getStringInput("Mã nhân viên giao hàng (Staff ID): ");
                if (CustomerStaffService.findStaff(staffId) != null) break;
                System.out.println("  ⚠ Lỗi: Mã shipper không tồn tại! Vui lòng nhập lại.");
            }
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
            String orderId;
            while (true) {
                orderId = InputHelper.getStringInput("Mã đơn hàng: ");
                if (DeliveryOrderRepository.existsById(orderId)) break;
                System.out.println("  ⚠ Lỗi: Mã đơn hàng không tồn tại! Vui lòng nhập lại.");
            }

            // Hiển thị trạng thái hiện tại trước khi cập nhật với đầy đủ cước phí
            DeliveryOrder current = DeliveryOrderRepository.findById(orderId);
            System.out.println(BOLD_YELLOW + "\n[ĐƠN HÀNG HIỆN TẠI]:" + RESET);
            ParcelOrderView.printSingleOrderDetails(current);
            System.out.println("Trạng thái hiện tại: " + BOLD_WHITE + current.getStatus() + RESET);
            System.out.println();

            String status = InputHelper.getOptionalChoiceInput(
                    "Trạng thái mới (Picked Up/In Transit/Delivered/Failed/Cancelled): ",
                    "Trạng thái không hợp lệ!",
                    "Picked Up", "In Transit", "Delivered", "Failed", "Cancelled");
            while (status.isEmpty()) {
                System.out.println("  ⚠ Lỗi: Trạng thái không được để trống! Vui lòng nhập lại.");
                status = InputHelper.getOptionalChoiceInput(
                        "Trạng thái mới (Picked Up/In Transit/Delivered/Failed/Cancelled): ",
                        "Trạng thái không hợp lệ!",
                        "Picked Up", "In Transit", "Delivered", "Failed", "Cancelled");
            }

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
        List<DeliveryOrder> orders = com.cdms.service.TrackingService.getFailedAndCancelledOrders();
        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào giao thất bại (Failed) hoặc bị hủy (Cancelled).");
            InputHelper.pressEnterToContinue();
            return;
        }

        List<String> formattedFailed = new java.util.ArrayList<>();
        for (DeliveryOrder o : orders) {
            String reason = (o.getNotes() != null && !o.getNotes().isEmpty())
                    ? o.getNotes().get(o.getNotes().size() - 1)
                    : "Không có ghi chú";
            // Giới hạn độ dài lý do để không bị vỡ bảng
            if (reason.length() > 30) {
                reason = reason.substring(0, 27) + "...";
            }
            formattedFailed.add(String.format("| %-10s | %-10s | %-10s | %-10s | %-12s | %-30s |",
                    o.getId(), o.getParcelId(),
                    (o.getStaffId() != null ? o.getStaffId() : "Chưa phân"),
                    o.getDeliveryType(),
                    o.getStatus(),
                    reason));
        }

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

        List<String> formattedLines = new java.util.ArrayList<>();
        for (DeliveryOrder order : orders) {
            String pt = order.getPaymentTerms() != null ? order.getPaymentTerms() : "Receiver Pay";
            String ptFormatted = "Sender Pay".equalsIgnoreCase(pt) ? "Sender (Prepaid)" : "Receiver (COD)";

            // Tính cước phí từ hóa đơn hoặc tính động nếu chưa có hóa đơn
            double totalFee = 0.0;
            com.cdms.model.Invoice invoice = com.cdms.repository.InvoiceRepository.findByOrderId(order.getId());
            if (invoice != null) {
                totalFee = invoice.getTotalAmount();
            } else {
                com.cdms.model.Parcel parcel = com.cdms.repository.ParcelRepository.findById(order.getParcelId());
                if (parcel != null) {
                    double baseFee = parcel.calculateFee();
                    double urgent = "Urgent".equalsIgnoreCase(order.getDeliveryType())
                            ? com.cdms.service.BillingReportService.URGENT_CHARGE
                            : 0.0;
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

            String payStatusColored;
            if ("Paid".equalsIgnoreCase(payStatus) || "Paid (Quầy)".equalsIgnoreCase(payStatus)) {
                payStatusColored = BOLD_GREEN + String.format("%-15s", payStatus) + RESET;
            } else if ("Collected".equalsIgnoreCase(payStatus) || "Shipper đã thu".equalsIgnoreCase(payStatus)) {
                payStatusColored = BOLD_CYAN + String.format("%-15s", payStatus) + RESET;
            } else {
                payStatusColored = BOLD_RED + String.format("%-15s", payStatus) + RESET;
            }

            String statusColor = BOLD_YELLOW;
            if ("Delivered".equalsIgnoreCase(order.getStatus())) {
                statusColor = BOLD_GREEN;
            } else if ("Failed".equalsIgnoreCase(order.getStatus())
                    || "Cancelled".equalsIgnoreCase(order.getStatus())) {
                statusColor = BOLD_RED;
            }

            formattedLines.add(String.format(
                    "| %-10s | %-10s | %-10s | %-10s | " + statusColor + "%-10s" + RESET + " | %-18s | %15s | %s |",
                    order.getId(),
                    order.getParcelId(),
                    (order.getStaffId() != null ? order.getStaffId() : "Chưa phân"),
                    order.getDeliveryType(),
                    order.getStatus(),
                    ptFormatted,
                    totalFeeStr,
                    payStatusColored));
        }

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
