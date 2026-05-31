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
import com.cdms.core.FormCancelledException;
import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.Parcel;
import com.cdms.service.OrderService;
import com.cdms.service.ParcelService;
import com.cdms.service.CustomerStaffService;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.ParcelRepository;
import com.cdms.repository.DeliveryOrderRepository;

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
    // CÁC HÀM THỰC THI (EXECUTE)
    // ==========================================================

    public static void executeCreateParcel() {
        System.out.println(BOLD_CYAN + "\n=== THÊM KIỆN HÀNG MỚI ===" + RESET);
        System.out.println("(Nhập 'cancel' tại bất kỳ trường nào để hủy thao tác)");
        try {
            String id = InputHelper.getStringInput("Mã kiện hàng: ",
                    val -> !ParcelRepository.existsById(val),
                    "Mã kiện hàng đã tồn tại trong hệ thống!");
            String senderId = InputHelper.getStringInput("Mã khách hàng gửi: ",
                    val -> CustomerRepository.findById(val) != null,
                    "Mã khách hàng gửi không tồn tại trong hệ thống!");
            String receiverName = InputHelper.getValidNameInput("Tên người nhận: ");
            String receiverPhone = InputHelper.getPhoneInput("SĐT người nhận: ");
            String pickupAddress = InputHelper.getStringInput("Địa chỉ lấy hàng: ");
            String deliveryAddress = InputHelper.getStringInput("Địa chỉ giao hàng: ");
            double weight = InputHelper.getDoubleInput("Trọng lượng (kg): ", 0.1);
            String type = InputHelper.getStringInput("Loại kiện (Document/Goods): ",
                    val -> val.equalsIgnoreCase("Document") || val.equalsIgnoreCase("Goods"),
                    "Loại kiện hàng không hợp lệ (Chỉ nhận 'Document' hoặc 'Goods')!");

            System.out.println("\nXác nhận lưu thông tin kiện hàng này?");
            System.out.println("  1. Lưu");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    Parcel parcel = ParcelService.createParcel(id, senderId, receiverName,
                            receiverPhone, pickupAddress, deliveryAddress, weight, type);

                    System.out.println(BOLD_GREEN + "✅ Tạo kiện hàng thành công: " + id + RESET);
                    System.out.println(
                            "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    System.out.println(
                            "| Mã Kiện    | Mã Khách   | Tên Người Nhận  | Loại Kiện  | Trọng Lượng | Trạng Thái | Phí Vận Chuyển       |");
                    System.out.println(
                            "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                    System.out.println(parcel);
                    System.out.println(
                            "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác thêm kiện hàng mới." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
    }

    public static void executeViewParcels() {
        System.out.println(BOLD_CYAN + "\n=== DANH SÁCH KIỆN HÀNG ===" + RESET);
        List<Parcel> parcels = ParcelRepository.findAll();

        if (parcels.isEmpty()) {
            System.out.println("Chưa có kiện hàng nào.");
            return;
        }

        System.out.println(
                "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
        System.out.printf("| %-10s | %-10s | %-15s | %-10s | %-11s | %-10s | %-20s |%n",
                "Mã Kiện", "Mã Khách", "Tên Người Nhận", "Loại Kiện", "Trọng Lượng", "Trạng Thái", "Phí Vận Chuyển");
        System.out.println(
                "+------------+------------+-----------------+------------+-------------+------------+----------------------+");

        // Hỗ trợ hiển thị phân trang (UX-13)
        InputHelper.printPaginatedList(parcels, 10,
                "+------------+------------+-----------------+------------+-------------+------------+----------------------+");
        System.out.println(BOLD_GREEN + "Tổng số kiện: " + parcels.size() + RESET);
    }

    public static void executeCreateOrder() {
        System.out.println(BOLD_CYAN + "\n=== TẠO ĐƠN GIAO HÀNG ===" + RESET);
        System.out.println("(Nhập 'cancel' tại bất kỳ trường nào để hủy thao tác)");
        try {
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> !DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng đã tồn tại trong hệ thống!");
            String parcelId = InputHelper.getStringInput("Mã kiện hàng cần chuyển: ",
                    val -> {
                        Parcel p = ParcelRepository.findById(val);
                        return p != null && "Pending".equalsIgnoreCase(p.getStatus());
                    },
                    "Kiện hàng không tồn tại hoặc không ở trạng thái Pending!");
            String deliveryType = InputHelper.getStringInput("Loại đơn hàng (Standard/Urgent): ",
                    val -> val.equalsIgnoreCase("Standard") || val.equalsIgnoreCase("Urgent"),
                    "Loại đơn hàng không hợp lệ (Chỉ nhận Standard hoặc Urgent)!");

            System.out.println("\nChọn hình thức thanh toán:");
            System.out.println("  1. Sender Pay (Trả trước - Người gửi trả tại quầy)");
            System.out.println("  2. Receiver Pay (Trả sau/COD - Người nhận trả khi nhận hàng)");
            int paymentChoice = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);
            String paymentTerms = (paymentChoice == 1) ? "Sender Pay" : "Receiver Pay";

            System.out.println("\nXác nhận tạo đơn giao hàng này?");
            System.out.println("  1. Tạo đơn");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    DeliveryOrder order = OrderService.convertParcelToOrder(orderId, parcelId, deliveryType,
                            paymentTerms);

                    System.out.println(
                            BOLD_GREEN + "✅ Chuyển đổi kiện hàng thành đơn hàng thành công: " + orderId + RESET);
                    System.out.println(
                            "+------------+------------+------------+------------+------------+--------------+");
                    System.out.println(
                            "| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
                    System.out.println(
                            "+------------+------------+------------+------------+------------+--------------+");
                    System.out.println(order);
                    System.out.println(
                            "+------------+------------+------------+------------+------------+--------------+");

                    System.out.println("\n💡 HÌNH THỨC THANH TOÁN ĐÃ THIẾT LẬP:");
                    if ("Sender Pay".equals(paymentTerms)) {
                        System.out.println(
                                "  - Loại thanh toán   : " + BOLD_GREEN + "Sender Pay (Trả trước tại quầy)" + RESET);
                        System.out.println("  - Vận hành          : Tiền mặt thu trực tiếp tại quầy lễ tân.");
                    } else {
                        System.out.println(
                                "  - Loại thanh toán   : " + BOLD_YELLOW + "Receiver Pay (Thu hộ COD)" + RESET);
                        System.out.println(
                                "  - Vận hành          : Shipper thu hộ tiền mặt từ người nhận khi giao hàng thành công.");
                    }
                    System.out.println("  - Lưu ý             : " + BOLD_CYAN
                            + "Hóa đơn sẽ được Quản lý (Manager) tính toán & khởi tạo sau khi đơn giao thành công."
                            + RESET);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác tạo đơn giao hàng." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
    }

    public static void executeUpdateOrderStatus() {
        System.out.println(BOLD_CYAN + "\n=== CẬP NHẬT TRẠNG THÁI ĐƠN ===" + RESET);
        System.out.println("(Nhập 'cancel' tại bất kỳ trường nào để hủy thao tác)");
        try {
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại trong hệ thống!");
            String status = InputHelper.getStringInput(
                    "Nhập trạng thái mới (Assigned/Picked Up/In Transit/Delivered/Cancelled/Failed): ",
                    val -> val.equalsIgnoreCase("Assigned") || val.equalsIgnoreCase("Picked Up")
                            || val.equalsIgnoreCase("In Transit") || val.equalsIgnoreCase("Delivered")
                            || val.equalsIgnoreCase("Cancelled") || val.equalsIgnoreCase("Failed"),
                    "Trạng thái không hợp lệ!");

            System.out.println("\nXác nhận cập nhật trạng thái đơn hàng này?");
            System.out.println("  1. Đồng ý");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    DeliveryOrder order = OrderService.updateOrderStatus(orderId, status);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật trạng thái thành công!" + RESET);
                    ParcelOrderView.printSingleOrderDetails(order);
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy thao tác cập nhật trạng thái." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác (Người dùng chủ động hủy bỏ).\n" + RESET);
        }
    }

    public static void executeViewOrderDetail() {
        System.out.println(BOLD_CYAN + "\n=== XEM CHI TIẾT ĐƠN HÀNG ===" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại trong hệ thống!");
            try {
                DeliveryOrder order = OrderService.getOrderDetail(orderId);
                com.cdms.model.Parcel parcel = ParcelRepository.findById(order.getParcelId());
                com.cdms.model.DeliveryStaff staff = order.getStaffId() != null
                        ? CustomerStaffService.findStaff(order.getStaffId())
                        : null;

                System.out
                        .println(BOLD_GREEN + "\n===========================================================" + RESET);
                System.out.println(BOLD_YELLOW + "📋 CHI TIẾT ĐƠN HÀNG: " + order.getId() + " (Dịch vụ: "
                        + order.getDeliveryType() + ")" + RESET);
                System.out.println(BOLD_GREEN + "===========================================================" + RESET);
                if (parcel != null) {
                    System.out.printf("- Kiện hàng liên kết : %s (Loại: %s | Nặng: %.1f kg)%n",
                            parcel.getId(), parcel.getType(), parcel.getWeight());
                    System.out.printf("- Người nhận         : %s (SĐT: %s)%n",
                            parcel.getReceiverName(), parcel.getReceiverPhone());
                    System.out.printf("- Địa chỉ lấy hàng   : %s%n", parcel.getPickupAddress());
                    System.out.printf("- Địa chỉ giao hàng  : %s%n", parcel.getDeliveryAddress());
                    System.out.printf("- Phí vận chuyển     : %,.0f VND%n", parcel.calculateFee());
                }
                System.out.printf("- Trạng thái đơn     : %s%n", order.getStatus());

                // Lấy thông tin Hóa đơn & Thanh toán
                com.cdms.model.Invoice invoice = com.cdms.service.BillingReportService
                        .findInvoiceByOrderId(order.getId());
                if (order.getPaymentTerms() != null) {
                    System.out.printf("- Hình thức thanh toán: %s%n",
                            "Sender Pay".equals(order.getPaymentTerms()) ? "Sender Pay (Trả trước tại quầy)"
                                    : "Receiver Pay (Thu hộ COD)");
                } else {
                    System.out.println("- Hình thức thanh toán: Receiver Pay (COD - Mặc định)");
                }
                if (invoice != null) {
                    System.out.printf("- Mã hóa đơn liên kết : %s%n", invoice.getId());
                    System.out.printf("- Tổng tiền thanh toán: %,.0f VND%n", invoice.getTotalAmount());
                    String statusColor = BOLD_RED;
                    if ("Paid".equalsIgnoreCase(invoice.getPaymentStatus()))
                        statusColor = BOLD_GREEN;
                    else if ("Collected".equalsIgnoreCase(invoice.getPaymentStatus()))
                        statusColor = BOLD_YELLOW;
                    System.out.printf("- Trạng thái thanh toán: %s%s%s%n",
                            statusColor, invoice.getPaymentStatus(), RESET);
                    if (invoice.getPaymentMethod() != null) {
                        System.out.printf("- Phương thức thanh toán: %s%n", invoice.getPaymentMethod());
                    }
                    if (invoice.getPaymentDate() != null) {
                        System.out.printf("- Ngày thanh toán     : %s%n", invoice.getPaymentDate());
                    }
                }
                if (staff != null) {
                    System.out.printf("- Shipper phụ trách  : %s (%s - SĐT: %s)%n",
                            staff.getId(), staff.getName(), staff.getPhone());
                } else {
                    System.out.println("- Shipper phụ trách  : Chưa phân công");
                }
                System.out.printf("- Ngày tạo đơn       : %s%n",
                        order.getOrderDate() != null ? order.getOrderDate() : "N/A");
                System.out.printf("- Ngày giao dự kiến  : %s%n",
                        order.getExpectedDeliveryDate() != null ? order.getExpectedDeliveryDate() : "N/A");
                if (order.getPickupDate() != null) {
                    System.out.printf("- Ngày lấy thực tế   : %s%n", order.getPickupDate());
                }
                if (order.getDeliveryDate() != null) {
                    System.out.printf("- Ngày giao thực tế  : %s%n", order.getDeliveryDate());
                }
                System.out.println(BOLD_GREEN + "-----------------------------------------------------------" + RESET);
                System.out.println(BOLD_YELLOW + "📜 LỊCH SỬ HÀNH TRÌNH / GHI CHÚ TỪ SHIPPER:" + RESET);
                if (order.getNotes() == null || order.getNotes().isEmpty()) {
                    System.out.println("  (Chưa có ghi chú hành trình nào)");
                } else {
                    for (int i = 0; i < order.getNotes().size(); i++) {
                        System.out.printf("  [%d] %s%n", i + 1, order.getNotes().get(i));
                    }
                }
                System.out.println(BOLD_GREEN + "===========================================================" + RESET);
            } catch (Exception e) {
                System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    public static void executeSearchOrdersByCustomer() {
        System.out.println(BOLD_CYAN + "\n=== TÌM KIẾM ĐƠN THEO KHÁCH HÀNG ===" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String customerId = InputHelper.getStringInput("Nhập mã khách hàng: ",
                    val -> CustomerRepository.findById(val) != null,
                    "Mã khách hàng không tồn tại trong hệ thống!");

            try {
                List<DeliveryOrder> orders = OrderService.searchOrdersByCustomer(customerId);

                if (orders.isEmpty()) {
                    System.out.println("Không tìm thấy đơn hàng nào của khách hàng " + customerId);
                    return;
                }

                System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng:" + RESET);
                System.out.println("+------------+------------+------------+------------+------------+--------------+");
                System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
                System.out.println("+------------+------------+------------+------------+------------+--------------+");
                for (DeliveryOrder o : orders) {
                    System.out.println(o);
                }
                System.out.println("+------------+------------+------------+------------+------------+--------------+");
            } catch (Exception e) {
                System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    public static void executeCancelOrder() {
        System.out.println(BOLD_CYAN + "\n=== HỦY ĐƠN GIAO HÀNG ===" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng cần hủy: ",
                    val -> DeliveryOrderRepository.existsById(val),
                    "Mã đơn hàng không tồn tại trong hệ thống!");

            System.out.println("\nXác nhận hủy đơn hàng này?");
            System.out.println("  1. Đồng ý hủy đơn");
            System.out.println("  2. Quay lại (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    DeliveryOrder order = OrderService.cancelOrder(orderId);
                    System.out.println(BOLD_GREEN + "✅ Hủy đơn hàng thành công!" + RESET);
                    System.out.println(
                            "+------------+------------+------------+------------+------------+--------------+");
                    System.out.println(
                            "| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
                    System.out.println(
                            "+------------+------------+------------+------------+------------+--------------+");
                    System.out.println(order);
                    System.out.println(
                            "+------------+------------+------------+------------+------------+--------------+");
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã giữ lại đơn hàng." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        }
    }

    public static void printSingleOrderDetails(DeliveryOrder order) {
        if (order == null)
            return;
        String pt = order.getPaymentTerms() != null ? order.getPaymentTerms() : "Receiver Pay";
        String ptFormatted = "Sender Pay".equalsIgnoreCase(pt) ? "Sender (Prepaid)" : "Receiver (COD)";

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
        } else if ("Failed".equalsIgnoreCase(order.getStatus()) || "Cancelled".equalsIgnoreCase(order.getStatus())) {
            statusColor = BOLD_RED;
        }

        String tableHeader = "+------------+------------+------------+------------+------------+--------------------+-----------------+-----------------+";
        System.out.println(tableHeader);
        System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-18s | %-15s | %-15s |%n",
                "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Thanh Toán", "Cước Phí (VND)",
                "Trạng thái TT");
        System.out.println(tableHeader);
        System.out.printf(
                "| %-10s | %-10s | %-10s | %-10s | " + statusColor + "%-10s" + RESET + " | %-18s | %15s | %s |%n",
                order.getId(),
                order.getParcelId(),
                (order.getStaffId() != null ? order.getStaffId() : "Chưa phân"),
                order.getDeliveryType(),
                order.getStatus(),
                ptFormatted,
                totalFeeStr,
                payStatusColored);
        System.out.println(tableHeader);
    }

    public static void executeViewOrders() {
        System.out.println(BOLD_CYAN + "\n=== DANH SÁCH ĐƠN GIAO HÀNG ===" + RESET);
        List<DeliveryOrder> orders = DeliveryOrderRepository.findAll();

        if (orders.isEmpty()) {
            System.out.println("Chưa có đơn giao hàng nào.");
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
        System.out.println(tableHeader);
        System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-18s | %-15s | %-15s |%n",
                "Mã Đơn", "Mã Kiện", "Mã Shipper", "Dịch Vụ", "Trạng Thái", "Thanh Toán", "Cước Phí (VND)",
                "Trạng thái TT");
        System.out.println(tableHeader);

        // Hỗ trợ hiển thị phân trang
        InputHelper.printPaginatedList(formattedLines, 10, tableHeader);
        System.out.println(BOLD_GREEN + "Tổng số đơn hàng: " + orders.size() + RESET);
    }
}
