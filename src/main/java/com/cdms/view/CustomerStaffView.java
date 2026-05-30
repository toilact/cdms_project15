// ============================================================
// File: CustomerStaffView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Khách hàng &
//              Nhân viên giao hàng. (Hoàn thiện 100% đặc tả)
// Phân công: Nguyên Quốc Cường (Developer A - Thành viên 2)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.model.Customer;
import com.cdms.model.Parcel;
import com.cdms.model.DeliveryOrder;
import com.cdms.service.CustomerStaffService;
import com.cdms.service.OrderService;
import com.cdms.service.ParcelService;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.ParcelRepository;
import com.cdms.repository.DeliveryOrderRepository;

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
    // ==========================================================

    /**
     * Menu chính cho vai trò Reception Staff.
     */
    public static void showReceptionMenu() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "RECEPTION STAFF - MENU CHÍNH" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ KHÁCH HÀNG]                                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE + "Thêm khách hàng mới                                " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE + "Cập nhật thông tin KH                              " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE + "Hiển thị danh sách KH                              " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE + "Xóa khách hàng                                     " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE + "Tìm kiếm khách hàng theo Tên/SĐT                   " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ KIỆN HÀNG (PARCEL)]                          " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  6. " + WHITE + "Thêm kiện hàng mới                                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  7. " + WHITE + "Xem danh sách kiện hàng                            " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  8. " + WHITE + "Cập nhật thông tin bưu kiện                        " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  9. " + WHITE + "Xóa bưu kiện                                       " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  10." + WHITE + " Tìm kiếm bưu kiện                                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [QUẢN LÝ ĐƠN GIAO HÀNG]                              " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  11." + WHITE + " Tạo đơn giao hàng mới                             " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  12." + WHITE + " Cập nhật trạng thái đơn                           " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  13." + WHITE + " Xem chi tiết đơn giao hàng                        " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  14." + WHITE + " Tìm kiếm đơn theo KH                              " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  15." + WHITE + " Hủy đơn giao hàng                                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  16." + WHITE + " Xem lịch sử giao hàng của một KH                  " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE + "Quay lại Menu chính                                " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-16): " + RESET, 0, 16);

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
        System.out.println(BOLD_CYAN + "\n===== THÊM KHÁCH HÀNG MỚI =====" + RESET);
        String id      = InputHelper.getStringInput("Mã khách hàng (VD: KH001): ");
        String name    = InputHelper.getValidNameInput("Họ tên: ");
        String phone   = InputHelper.getPhoneInput("Số điện thoại: ");
        String address = InputHelper.getStringInput("Địa chỉ: ");

        Customer customer = new Customer(id, name, phone, address);
        System.out.println(BOLD_GREEN + CustomerStaffService.addCustomer(customer) + RESET);
        System.out.println();
    }

    // ----------------------------------------------------------
    //  [B2] Cập nhật thông tin khách hàng
    // ----------------------------------------------------------
    private static void handleUpdateCustomer() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT KHÁCH HÀNG =====" + RESET);
        String id = InputHelper.getStringInput("Mã khách hàng cần cập nhật: ");
        Customer existing = CustomerStaffService.findCustomer(id);
        if (existing == null) {
            System.out.println(BOLD_RED + "❌ Không tìm thấy khách hàng với mã '" + id + "'.\n" + RESET);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println(existing);
        System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

        String newName;
        while (true) {
            newName = InputHelper.getOptionalStringInput("Tên mới [" + existing.getName() + "]: ");
            if (newName.isEmpty()) {
                newName = existing.getName();
                break;
            }
            if (newName.matches("\\d+")) {
                System.out.println("  ⚠ Lỗi: Tên khách hàng không được phép là số! Vui lòng nhập lại.");
                continue;
            }
            break;
        }

        String newPhone;
        while (true) {
            newPhone = InputHelper.getOptionalStringInput("Số điện thoại mới [" + existing.getPhone() + "]: ");
            if (newPhone.isEmpty()) {
                newPhone = existing.getPhone();
                break;
            }
            if (!newPhone.matches("\\d{9,11}")) {
                System.out.println("  ⚠ Lỗi: Số điện thoại không hợp lệ (phải gồm 9-11 chữ số)! Vui lòng nhập lại.");
                continue;
            }
            break;
        }

        String newAddress = InputHelper.getOptionalStringInput("Địa chỉ mới [" + existing.getAddress() + "]: ");
        if (newAddress.isEmpty()) newAddress = existing.getAddress();

        Customer updated = new Customer(id, newName, newPhone, newAddress);
        System.out.println(BOLD_GREEN + CustomerStaffService.updateCustomer(updated) + RESET);
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
        for (Customer customer : customers) {
            System.out.println(customer);
        }
        System.out.println("+------------+----------------------+-----------------+--------------------------------+\n");
    }

    // ----------------------------------------------------------
    //  Xóa khách hàng (Remove customer)
    // ----------------------------------------------------------
    private static void handleDeleteCustomer() {
        System.out.println(BOLD_CYAN + "\n===== XÓA KHÁCH HÀNG (B3-Remove) =====" + RESET);
        String id = InputHelper.getStringInput("Nhập mã khách hàng cần xóa: ");
        Customer existing = CustomerStaffService.findCustomer(id);
        if (existing == null) {
            System.out.println(BOLD_RED + "❌ Không tìm thấy khách hàng với mã '" + id + "'.\n" + RESET);
            return;
        }

        System.out.println("Thông tin khách hàng:");
        System.out.println(existing);
        String confirm = InputHelper.getStringInput("\nXác nhận xóa khách hàng này? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println(BOLD_GREEN + CustomerStaffService.deleteCustomer(id) + RESET);
        } else {
            System.out.println("  Đã hủy thao tác xóa.");
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Tìm kiếm khách hàng theo tên hoặc SĐT
    // ----------------------------------------------------------
    private static void handleSearchCustomer() {
        System.out.println(BOLD_CYAN + "\n===== TÌM KIẾM KHÁCH HÀNG =====" + RESET);
        System.out.println("1. Tìm theo tên (Khớp một phần)");
        System.out.println("2. Tìm theo số điện thoại (Chính xác)");
        int type = InputHelper.getIntInput("Chọn kiểu tìm kiếm (1-2): ", 1, 2);

        if (type == 1) {
            String name = InputHelper.getStringInput("Nhập tên khách hàng: ");
            List<Customer> result = CustomerRepository.findByName(name);
            if (result.isEmpty()) {
                System.out.println("Không tìm thấy khách hàng nào khớp với tên: " + name);
            } else {
                System.out.println(BOLD_GREEN + "Tìm thấy " + result.size() + " khách hàng:" + RESET);
                for (Customer c : result) {
                    System.out.println(c);
                }
            }
        } else {
            String phone = InputHelper.getStringInput("Nhập số điện thoại: ");
            Customer c = CustomerRepository.findByPhone(phone);
            if (c == null) {
                System.out.println("Không tìm thấy khách hàng có số điện thoại: " + phone);
            } else {
                System.out.println(BOLD_GREEN + "Đã tìm thấy khách hàng:" + RESET);
                System.out.println(c);
            }
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Cập nhật bưu kiện (Update parcel)
    // ----------------------------------------------------------
    private static void handleUpdateParcel() {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT THÔNG TIN BƯU KIỆN =====" + RESET);
        String id = InputHelper.getStringInput("Nhập mã bưu kiện: ");
        Parcel existing = ParcelRepository.findById(id);
        if (existing == null) {
            System.out.println(BOLD_RED + "❌ Không tìm thấy bưu kiện với mã '" + id + "'.\n" + RESET);
            return;
        }

        System.out.println("Thông tin bưu kiện hiện tại:");
        System.out.println(existing);
        System.out.println("\n(Nhấn Enter để giữ nguyên giá trị cũ)\n");

        String receiverName = InputHelper.getOptionalStringInput("Họ tên người nhận [" + existing.getReceiverName() + "]: ");
        if (receiverName.isEmpty()) receiverName = existing.getReceiverName();

        String receiverPhone = InputHelper.getOptionalStringInput("SĐT người nhận [" + existing.getReceiverPhone() + "]: ");
        if (receiverPhone.isEmpty()) receiverPhone = existing.getReceiverPhone();

        String pickupAddress = InputHelper.getOptionalStringInput("Địa chỉ lấy hàng [" + existing.getPickupAddress() + "]: ");
        if (pickupAddress.isEmpty()) pickupAddress = existing.getPickupAddress();

        String deliveryAddress = InputHelper.getOptionalStringInput("Địa chỉ giao hàng [" + existing.getDeliveryAddress() + "]: ");
        if (deliveryAddress.isEmpty()) deliveryAddress = existing.getDeliveryAddress();

        double weight = existing.getWeight();
        String weightInput = InputHelper.getOptionalStringInput("Trọng lượng mới [" + existing.getWeight() + " kg]: ");
        if (!weightInput.isEmpty()) {
            try {
                weight = Double.parseDouble(weightInput);
                if (weight <= 0) {
                    System.out.println("Trọng lượng không hợp lệ! Giữ nguyên trọng lượng cũ.");
                    weight = existing.getWeight();
                }
            } catch (NumberFormatException e) {
                System.out.println("Nhập sai định dạng số! Giữ nguyên trọng lượng cũ.");
            }
        }

        existing.setReceiverName(receiverName);
        existing.setReceiverPhone(receiverPhone);
        existing.setPickupAddress(pickupAddress);
        existing.setDeliveryAddress(deliveryAddress);
        existing.setWeight(weight);

        boolean success = ParcelRepository.update(existing);
        if (success) {
            System.out.println(BOLD_GREEN + "✅ Đã cập nhật thông tin bưu kiện thành công!" + RESET);
            System.out.println(existing);
        } else {
            System.out.println(BOLD_RED + "❌ Cập nhật thất bại." + RESET);
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Xóa bưu kiện (Remove parcel)
    // ----------------------------------------------------------
    private static void handleDeleteParcel() {
        System.out.println(BOLD_CYAN + "\n===== XÓA BƯU KIỆN =====" + RESET);
        String id = InputHelper.getStringInput("Nhập mã bưu kiện: ");
        Parcel existing = ParcelRepository.findById(id);
        if (existing == null) {
            System.out.println(BOLD_RED + "❌ Không tìm thấy bưu kiện với mã '" + id + "'.\n" + RESET);
            return;
        }

        System.out.println("Thông tin bưu kiện:");
        System.out.println(existing);
        String confirm = InputHelper.getStringInput("\nXác nhận xóa bưu kiện này? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = ParcelRepository.delete(id);
            if (success) {
                System.out.println(BOLD_GREEN + "✅ Xóa bưu kiện thành công!" + RESET);
            } else {
                System.out.println(BOLD_RED + "❌ Xóa bưu kiện thất bại." + RESET);
            }
        } else {
            System.out.println("  Đã hủy thao tác.");
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Tìm kiếm bưu kiện
    // ----------------------------------------------------------
    private static void handleSearchParcel() {
        System.out.println(BOLD_CYAN + "\n===== TÌM KIẾM BƯU KIỆN =====" + RESET);
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
                System.out.println(p);
            }
        } else if (type == 2) {
            String name = InputHelper.getStringInput("Nhập tên người nhận: ");
            List<Parcel> result = ParcelRepository.findAll().stream()
                    .filter(p -> p.getReceiverName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            if (result.isEmpty()) {
                System.out.println("Không tìm thấy bưu kiện nào có người nhận là: " + name);
            } else {
                System.out.println(BOLD_GREEN + "Tìm thấy " + result.size() + " bưu kiện:" + RESET);
                for (Parcel p : result) {
                    System.out.println(p);
                }
            }
        } else {
            String status = InputHelper.getStringInput("Nhập trạng thái bưu kiện (Pending/Assigned/In Transit/Delivered/Failed/Cancelled): ");
            List<Parcel> result = ParcelRepository.findAll().stream()
                    .filter(p -> status.equalsIgnoreCase(p.getStatus()))
                    .toList();
            if (result.isEmpty()) {
                System.out.println("Không tìm thấy bưu kiện nào có trạng thái: " + status);
            } else {
                System.out.println(BOLD_GREEN + "Tìm thấy " + result.size() + " bưu kiện có trạng thái '" + status + "':" + RESET);
                for (Parcel p : result) {
                    System.out.println(p);
                }
            }
        }
        System.out.println();
    }

    // ----------------------------------------------------------
    //  Xem lịch sử giao hàng của một khách hàng cụ thể
    // ----------------------------------------------------------
    private static void handleViewCustomerDeliveryHistory() {
        System.out.println(BOLD_CYAN + "\n===== XEM LỊCH SỬ GIAO HÀNG CỦA KHÁCH HÀNG =====" + RESET);
        String customerId = InputHelper.getStringInput("Nhập mã khách hàng (VD: KH001): ");
        Customer c = CustomerStaffService.findCustomer(customerId);
        if (c == null) {
            System.out.println(BOLD_RED + "❌ Không tìm thấy khách hàng với mã '" + customerId + "'.\n" + RESET);
            return;
        }

        System.out.println("Khách hàng: " + c.getName() + " (" + customerId + ")");
        System.out.println("SĐT: " + c.getPhone() + " | Địa chỉ: " + c.getAddress());

        // Lấy tất cả đơn hàng liên quan đến khách hàng gửi
        List<DeliveryOrder> orders = OrderService.searchOrdersByCustomer(customerId);
        if (orders.isEmpty()) {
            System.out.println("Khách hàng này chưa thực hiện gửi đơn hàng nào trong hệ thống.");
            return;
        }

        System.out.println(BOLD_GREEN + "\nLịch sử đơn hàng gửi (" + orders.size() + " đơn):" + RESET);
        System.out.println("─".repeat(85));
        System.out.printf("  %-10s │ %-10s │ %-12s │ %-15s │ %-15s%n",
                "Mã Đơn", "Mã Kiện", "Loại dịch vụ", "Ngày tạo", "Trạng thái");
        System.out.println("─".repeat(85));
        for (DeliveryOrder o : orders) {
            System.out.printf("  %-10s │ %-10s │ %-12s │ %-15s │ %-15s%n",
                    o.getId(), o.getParcelId(), o.getDeliveryType(), o.getOrderDate(), o.getStatus());
        }
        System.out.println("─".repeat(85) + "\n");
    }
}
