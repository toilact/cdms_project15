// ============================================================
// File: CustomerStaffView.java
// Package: com.cdms.view
// Description: Giao diện Console dành cho Nhân viên Lễ tân (Reception Staff)
//              để quản lý khách hàng và tiếp nhận bưu kiện.
// Phân công: Nguyên Quốc Cường (Developer A - Thành viên 2)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.core.FormCancelledException;
import com.cdms.model.Customer;
import com.cdms.model.Parcel;
import com.cdms.model.DeliveryOrder;
import com.cdms.service.CustomerStaffService;
import com.cdms.service.OrderService;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.ParcelRepository;

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

    // Ngăn khởi tạo đối tượng
    private CustomerStaffView() {
    }

    /**
     * Menu chính của Nhân viên Lễ tân (Reception Staff).
     */
    public static void showReceptionMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "RECEPTION STAFF - MENU CHÍNH" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            
            String group1 = BOLD_CYAN + "  [QUẢN LÝ KHÁCH HÀNG]";
            String line1  = BOLD_WHITE + "  1. " + WHITE + "Thêm khách hàng mới";
            String line2  = BOLD_WHITE + "  2. " + WHITE + "Cập nhật thông tin KH";
            String line3  = BOLD_WHITE + "  3. " + WHITE + "Hiển thị danh sách KH";
            String line4  = BOLD_WHITE + "  4. " + WHITE + "Xóa khách hàng";
            String line5  = BOLD_WHITE + "  5. " + WHITE + "Tìm kiếm khách hàng theo Tên/SĐT";
            String lineSep = " ";
            String group2 = BOLD_CYAN + "  [QUẢN LÝ KIỆN HÀNG (PARCEL)]";
            String line6  = BOLD_WHITE + "  6. " + WHITE + "Thêm kiện hàng mới";
            String line7  = BOLD_WHITE + "  7. " + WHITE + "Xem danh sách kiện hàng";
            String line8  = BOLD_WHITE + "  8. " + WHITE + "Cập nhật thông tin bưu kiện";
            String line9  = BOLD_WHITE + "  9. " + WHITE + "Xóa bưu kiện";
            String line10 = BOLD_WHITE + "  10." + WHITE + " Tìm kiếm bưu kiện";
            String group3 = BOLD_CYAN + "  [QUẢN LÝ ĐƠN GIAO HÀNG]";
            String line11 = BOLD_WHITE + "  11." + WHITE + " Tạo đơn giao hàng mới";
            String line12 = BOLD_WHITE + "  12." + WHITE + " Cập nhật trạng thái đơn";
            String line13 = BOLD_WHITE + "  13." + WHITE + " Xem chi tiết đơn giao hàng";
            String line14 = BOLD_WHITE + "  14." + WHITE + " Tìm kiếm đơn theo KH";
            String line15 = BOLD_WHITE + "  15." + WHITE + " Hủy đơn giao hàng";
            String line16 = BOLD_WHITE + "  16." + WHITE + " Xem lịch sử giao hàng của một KH";
            String line17 = BOLD_WHITE + "  17." + WHITE + " Xem danh sách đơn giao hàng";
            String line0  = BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính";

            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(group1, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line1, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line2, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line3, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line4, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line5, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineSep, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(group2, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line6, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line7, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line8, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line9, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line10, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineSep, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(group3, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line11, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line12, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line13, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line14, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line15, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line16, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line17, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(lineSep, 55) + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + RESET + padRight(line0, 55) + BOLD_YELLOW + "║" + RESET);
            
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-17): " + RESET, 0, 17);

            switch (choice) {
                case 1:  handleAddCustomer(); break;
                case 2:  handleUpdateCustomer(); break;
                case 3:  handleShowCustomerList(); break;
                case 4:  handleDeleteCustomer(); break;
                case 5:  handleSearchCustomer(); break;
                case 6:  ParcelOrderView.executeCreateParcel(); break;
                case 7:  ParcelOrderView.executeViewParcels(); break;
                case 8:  handleUpdateParcel(); break;
                case 9:  handleDeleteParcel(); break;
                case 10: handleSearchParcel(); break;
                case 11: ParcelOrderView.executeCreateOrder(); break;
                case 12: ParcelOrderView.executeUpdateOrderStatus(); break;
                case 13: ParcelOrderView.executeViewOrderDetail(); break;
                case 14: ParcelOrderView.executeSearchOrdersByCustomer(); break;
                case 15: ParcelOrderView.executeCancelOrder(); break;
                case 16: handleViewCustomerDeliveryHistory(); break;
                case 17: ParcelOrderView.executeViewOrders(); break;
                case 0:
                    running = false;
                    System.out.println("  ↩ Quay lại Menu chính...\n");
                    break;
                default:
                    break;
            }

            // Nhất quán pressEnterToContinue sau mỗi lựa chọn menu khác 0 (UX-04)
            if (running && choice != 0) {
                InputHelper.pressEnterToContinue();
            }
        }
    }

    // ----------------------------------------------------------
    //  [B1] Thêm khách hàng mới
    // ----------------------------------------------------------
    private static void handleAddCustomer() {
        System.out.println(BOLD_CYAN + "\n===== THÊM KHÁCH HÀNG MỚI =====" + RESET);
        System.out.println("(Nhập 'cancel' tại bất kỳ trường nào để hủy thao tác)");
        try {
            String id = InputHelper.getStringInput("Mã khách hàng (VD: KH001): ",
                    val -> CustomerRepository.findById(val) == null,
                    "Mã khách hàng đã tồn tại trong hệ thống!");
            String name = InputHelper.getValidNameInput("Họ và tên khách hàng: ");
            String phone = InputHelper.getPhoneInput("Số điện thoại: ",
                    val -> CustomerRepository.findByPhone(val) == null,
                    "Số điện thoại này đã được sử dụng bởi khách hàng khác!");
            String address = InputHelper.getStringInput("Địa chỉ khách hàng: ");

            System.out.println("\nXác nhận lưu thông tin khách hàng này?");
            System.out.println("  1. Lưu");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                Customer customer = new Customer(id, name, phone, address);
                System.out.println(BOLD_GREEN + CustomerStaffService.addCustomer(customer) + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác thêm khách hàng mới." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [B2] Cập nhật thông tin khách hàng
    // ----------------------------------------------------------
    private static void handleUpdateCustomer() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT THÔNG TIN KHÁCH HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Mã khách hàng cần cập nhật: ",
                    val -> CustomerRepository.findById(val) != null,
                    "Mã khách hàng không tồn tại trong hệ thống!");
            Customer existing = CustomerStaffService.findCustomer(id);

            System.out.println("Thông tin hiện tại:");
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println(existing);
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

            String newName = InputHelper.getOptionalValidatedStringInput("Họ tên mới [" + existing.getName() + "]: ",
                    val -> !val.matches("\\d+"),
                    "Tên khách hàng không được phép là số!");
            if (newName.isEmpty()) {
                newName = existing.getName();
            }

            String newPhone = InputHelper.getOptionalPhoneInput("Số điện thoại mới [" + existing.getPhone() + "]: ",
                    val -> val.equalsIgnoreCase(existing.getPhone()) || CustomerRepository.findByPhone(val) == null,
                    "Số điện thoại này đã được sử dụng bởi khách hàng khác!");
            if (newPhone.isEmpty()) {
                newPhone = existing.getPhone();
            }

            String newAddress = InputHelper.getOptionalStringInput("Địa chỉ mới [" + existing.getAddress() + "]: ");
            if (newAddress.isEmpty()) {
                newAddress = existing.getAddress();
            }

            System.out.println("\nXác nhận cập nhật thông tin khách hàng?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                Customer updated = new Customer(id, newName, newPhone, newAddress);
                System.out.println(BOLD_GREEN + CustomerStaffService.updateCustomer(updated) + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác cập nhật khách hàng." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [B3] Hiển thị danh sách khách hàng
    // ----------------------------------------------------------
    private static void handleShowCustomerList() {
        System.out.println(BOLD_CYAN + "\n===== DANH SÁCH KHÁCH HÀNG =====" + RESET);
        List<Customer> customers = CustomerStaffService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("  (Chưa có khách hàng nào.)\n");
            return;
        }

        System.out.println("+------------+----------------------+-----------------+--------------------------------+");
        System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
        System.out.println("+------------+----------------------+-----------------+--------------------------------+");
        
        // Sử dụng phân trang tập trung với đường kẻ bảng đồng bộ (UX-13)
        InputHelper.printPaginatedList(customers, 10, "+------------+----------------------+-----------------+--------------------------------+");
        System.out.println(BOLD_GREEN + "Tổng số khách hàng: " + customers.size() + RESET);
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Xóa khách hàng (Remove customer)
    // ----------------------------------------------------------
    private static void handleDeleteCustomer() {
        System.out.println(BOLD_CYAN + "\n===== XÓA KHÁCH HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Nhập mã khách hàng cần xóa: ",
                    val -> CustomerRepository.findById(val) != null,
                    "Không tìm thấy khách hàng với mã này!");
            Customer existing = CustomerStaffService.findCustomer(id);

            System.out.println("Thông tin khách hàng:");
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println(existing);
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");

            // Cảnh báo nếu khách hàng đang có bưu kiện liên kết
            int parcelCount = 0;
            for (Parcel p : ParcelRepository.findAll()) {
                if (id.equalsIgnoreCase(p.getSenderId())) {
                    parcelCount++;
                }
            }
            if (parcelCount > 0) {
                System.out.println(BOLD_RED + "⚠️ CẢNH BÁO: Khách hàng này đang có " + parcelCount + " bưu kiện liên kết trong hệ thống!" + RESET);
            }

            System.out.println("\nXác nhận xóa khách hàng này?");
            System.out.println("  1. Xóa");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                System.out.println(BOLD_GREEN + CustomerStaffService.deleteCustomer(id) + RESET);
            } else {
                System.out.println("  Đã hủy thao tác xóa.");
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Tìm kiếm khách hàng theo tên hoặc SĐT
    // ----------------------------------------------------------
    private static void handleSearchCustomer() {
        System.out.println(BOLD_CYAN + "\n===== TÌM KIẾM KHÁCH HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            System.out.println("1. Tìm theo tên (Khớp một phần)");
            System.out.println("2. Tìm theo số điện thoại (Chính xác)");
            int type = InputHelper.getIntInput("Chọn kiểu tìm kiếm (1-2): ", 1, 2);

            if (type == 1) {
                String name = InputHelper.getStringInput("Nhập tên khách hàng: ");
                List<Customer> result = CustomerRepository.findByName(name);
                if (result.isEmpty()) {
                    System.out.println("Không tìm thấy khách hàng nào khớp với tên: " + name);
                } else {
                    System.out.println("+------------+----------------------+-----------------+--------------------------------+");
                    System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
                    System.out.println("+------------+----------------------+-----------------+--------------------------------+");
                    InputHelper.printPaginatedList(result, 10, "+------------+----------------------+-----------------+--------------------------------+");
                }
            } else {
                String phone = InputHelper.getPhoneInput("Nhập số điện thoại: ");
                Customer c = CustomerRepository.findByPhone(phone);
                if (c == null) {
                    System.out.println("Không tìm thấy khách hàng có số điện thoại: " + phone);
                } else {
                    System.out.println(BOLD_GREEN + "Đã tìm thấy khách hàng:" + RESET);
                    System.out.println("+------------+----------------------+-----------------+--------------------------------+");
                    System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
                    System.out.println("+------------+----------------------+-----------------+--------------------------------+");
                    System.out.println(c);
                    System.out.println("+------------+----------------------+-----------------+--------------------------------+");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Cập nhật bưu kiện (Update parcel)
    // ----------------------------------------------------------
    private static void handleUpdateParcel() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT THÔNG TIN BƯU KIỆN =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Nhập mã bưu kiện: ",
                    val -> ParcelRepository.findById(val) != null,
                    "Không tìm thấy bưu kiện này!");
            Parcel existing = ParcelRepository.findById(id);

            System.out.println("Thông tin bưu kiện hiện tại:");
            System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
            System.out.println("| Mã Kiện    | Mã Khách   | Tên Người Nhận  | Loại Kiện  | Trọng Lượng | Trạng Thái | Phí Vận Chuyển       |");
            System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
            System.out.println(existing);
            System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
            System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

            String receiverName = InputHelper.getOptionalStringInput("Họ tên người nhận [" + existing.getReceiverName() + "]: ");
            if (receiverName.isEmpty()) receiverName = existing.getReceiverName();

            String receiverPhone = InputHelper.getOptionalValidatedStringInput("SĐT người nhận [" + existing.getReceiverPhone() + "]: ",
                    val -> val.matches("\\d{9,11}"),
                    "Số điện thoại người nhận phải có từ 9 đến 11 số!");
            if (receiverPhone.isEmpty()) receiverPhone = existing.getReceiverPhone();

            String pickupAddress = InputHelper.getOptionalStringInput("Địa chỉ lấy hàng [" + existing.getPickupAddress() + "]: ");
            if (pickupAddress.isEmpty()) pickupAddress = existing.getPickupAddress();

            String deliveryAddress = InputHelper.getOptionalStringInput("Địa chỉ giao hàng [" + existing.getDeliveryAddress() + "]: ");
            if (deliveryAddress.isEmpty()) deliveryAddress = existing.getDeliveryAddress();

            double weight = existing.getWeight();
            String weightStr = InputHelper.getOptionalStringInput("Trọng lượng mới [" + existing.getWeight() + " kg]: ");
            if (!weightStr.isEmpty()) {
                try {
                    weight = Double.parseDouble(weightStr);
                    if (weight <= 0) throw new IllegalArgumentException();
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "⚠️ Trọng lượng không hợp lệ! Giữ nguyên giá trị cũ." + RESET);
                    weight = existing.getWeight();
                }
            }

            System.out.println("\nXác nhận cập nhật thông tin bưu kiện?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                existing.setReceiverName(receiverName);
                existing.setReceiverPhone(receiverPhone);
                existing.setPickupAddress(pickupAddress);
                existing.setDeliveryAddress(deliveryAddress);
                existing.setWeight(weight);
                
                boolean success = ParcelRepository.update(existing);
                if (success) {
                    System.out.println(BOLD_GREEN + "✅ Đã cập nhật bưu kiện thành công!" + RESET);
                } else {
                    System.out.println(BOLD_RED + "❌ Cập nhật bưu kiện thất bại." + RESET);
                }
            } else {
                System.out.println("  Đã hủy thao tác.");
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Xóa bưu kiện (Remove parcel)
    // ----------------------------------------------------------
    private static void handleDeleteParcel() {
        System.out.println(BOLD_CYAN + "\n===== XÓA BƯU KIỆN =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String id = InputHelper.getStringInput("Nhập mã bưu kiện: ",
                    val -> ParcelRepository.findById(val) != null,
                    "Không tìm thấy bưu kiện này!");
            Parcel existing = ParcelRepository.findById(id);

            System.out.println("Thông tin bưu kiện:");
            System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
            System.out.println("| Mã Kiện    | Mã Khách   | Tên Người Nhận  | Loại Kiện  | Trọng Lượng | Trạng Thái | Phí Vận Chuyển       |");
            System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
            System.out.println(existing);
            System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");

            // Cảnh báo nếu bưu kiện đang liên kết với một đơn giao hàng
            boolean hasOrder = false;
            for (com.cdms.model.DeliveryOrder o : com.cdms.repository.DeliveryOrderRepository.findAll()) {
                if (id.equalsIgnoreCase(o.getParcelId())) {
                    hasOrder = true;
                    break;
                }
            }
            if (hasOrder) {
                System.out.println(BOLD_RED + "⚠️ CẢNH BÁO: Bưu kiện này đang được liên kết với một đơn giao hàng trong hệ thống!" + RESET);
            }

            System.out.println("\nXác nhận xóa bưu kiện này?");
            System.out.println("  1. Xóa");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                String result = com.cdms.service.ParcelService.deleteParcel(id);
                if (result.startsWith("✅")) {
                    System.out.println(BOLD_GREEN + result + RESET);
                } else {
                    System.out.println(BOLD_RED + result + RESET);
                }
            } else {
                System.out.println("  Đã hủy thao tác.");
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Tìm kiếm bưu kiện
    // ----------------------------------------------------------
    private static void handleSearchParcel() {
        System.out.println(BOLD_CYAN + "\n===== TÌM KIẾM BƯU KIỆN =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            System.out.println("1. Tìm kiếm theo ID bưu kiện");
            System.out.println("2. Tìm kiếm theo tên người nhận");
            System.out.println("3. Tìm kiếm theo trạng thái");
            int type = InputHelper.getIntInput("Chọn kiểu tìm kiếm (1-3): ", 1, 3);

            if (type == 1) {
                String id = InputHelper.getStringInput("Nhập mã bưu kiện: ");
                Parcel p = ParcelRepository.findById(id);
                if (p == null) {
                    System.out.println("Không tìm thấy bưu kiện có mã: " + id);
                } else {
                    System.out.println(BOLD_GREEN + "Đã tìm thấy bưu kiện:" + RESET);
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    System.out.println("| Mã Kiện    | Mã Khách   | Tên Người Nhận  | Loại Kiện  | Trọng Lượng | Trạng Thái | Phí Vận Chuyển       |");
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    System.out.println(p);
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                }
            } else if (type == 2) {
                String name = InputHelper.getStringInput("Nhập tên người nhận: ");
                List<Parcel> result = new java.util.ArrayList<>();
                for (Parcel p : ParcelRepository.findAll()) {
                    if (p.getReceiverName().toLowerCase().contains(name.toLowerCase())) {
                        result.add(p);
                    }
                }
                if (result.isEmpty()) {
                    System.out.println("Không tìm thấy bưu kiện nào có người nhận là: " + name);
                } else {
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    System.out.printf("| %-10s | %-10s | %-15s | %-10s | %-11s | %-10s | %-20s |%n",
                            "Mã Kiện", "Mã Khách", "Tên Người Nhận", "Loại Kiện", "Trọng Lượng", "Trạng Thái", "Phí Vận Chuyển");
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    InputHelper.printPaginatedList(result, 10, "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                }
            } else {
                String status = InputHelper.getStringInput("Nhập trạng thái bưu kiện (Pending/Assigned/In Transit/Delivered/Failed/Cancelled): ");
                List<Parcel> result = new java.util.ArrayList<>();
                for (Parcel p : ParcelRepository.findAll()) {
                    if (status.equalsIgnoreCase(p.getStatus())) {
                        result.add(p);
                    }
                }
                if (result.isEmpty()) {
                    System.out.println("Không tìm thấy bưu kiện nào có trạng thái: " + status);
                } else {
                    System.out.println(BOLD_GREEN + "Tìm thấy " + result.size() + " bưu kiện có trạng thái '" + status + "':" + RESET);
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    System.out.printf("| %-10s | %-10s | %-15s | %-10s | %-11s | %-10s | %-20s |%n",
                            "Mã Kiện", "Mã Khách", "Tên Người Nhận", "Loại Kiện", "Trọng Lượng", "Trạng Thái", "Phí Vận Chuyển");
                    System.out.println("+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    InputHelper.printPaginatedList(result, 10, "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                }
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Xem lịch sử giao hàng của một khách hàng cụ thể
    // ----------------------------------------------------------
    private static void handleViewCustomerDeliveryHistory() {
        System.out.println(BOLD_CYAN + "\n===== XEM LỊCH SỬ GIAO HÀNG CỦA KHÁCH HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String customerId = InputHelper.getStringInput("Nhập mã khách hàng (VD: KH001): ",
                    val -> CustomerRepository.findById(val) != null,
                    "Không tìm thấy khách hàng với mã này!");
            Customer c = CustomerStaffService.findCustomer(customerId);

            System.out.println(BOLD_GREEN + "\nThông tin khách hàng:" + RESET);
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println("| Mã KH      | Tên khách hàng       | Điện thoại      | Địa chỉ                        |");
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");
            System.out.println(c);
            System.out.println("+------------+----------------------+-----------------+--------------------------------+");

            // Lấy tất cả đơn hàng liên quan đến khách hàng gửi
            List<DeliveryOrder> orders = OrderService.searchOrdersByCustomer(customerId);
            if (orders.isEmpty()) {
                System.out.println("Khách hàng này chưa thực hiện gửi đơn hàng nào trong hệ thống.");
                return;
            }

            System.out.println(BOLD_GREEN + "\nLịch sử đơn hàng gửi (" + orders.size() + " đơn):" + RESET);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-12s |%n",
                    "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Ngày Tạo");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            
            List<String> formattedHistory = new java.util.ArrayList<>();
            for (DeliveryOrder o : orders) {
                formattedHistory.add(String.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %-12s |",
                        o.getId(), o.getParcelId(),
                        (o.getStaffId() != null ? o.getStaffId() : "Chưa phân"),
                        o.getDeliveryType(), o.getStatus(),
                        (o.getOrderDate() != null ? o.getOrderDate().toString() : "N/A")));
            }
            
            // Sử dụng phân trang tập trung
            InputHelper.printPaginatedList(formattedHistory, 10, "+------------+------------+------------+------------+------------+--------------+");
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }
}
