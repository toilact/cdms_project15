// ============================================================
// File: ShipperView.java
// Package: com.cdms.view
// Description: Giao diện Console cho vai trò Shipper (Nhân viên giao hàng).
//              Xem đơn được phân công, cập nhật ngày nhận/giao hàng thực tế,
//              và thêm ghi chú giao hàng.
//              Refactored to ask Staff ID once on entry and enforce security.
// Phân công: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
// ============================================================
package com.cdms.view;

import com.cdms.core.InputHelper;
import com.cdms.core.FormCancelledException;
import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;
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

    /**
     * Menu chính cho vai trò Delivery Staff (Shipper).
     * Shipper có quyền: xem đơn được phân công, cập nhật trạng thái
     * thực tế (pickupDate, deliveryDate), thêm ghi chú.
     * Hỏi mã Staff ID một lần duy nhất khi vào menu (UX-08).
     */
    public static void showShipperMenu() {
        System.out.println(BOLD_CYAN + "\n===== ĐĂNG NHẬP SHIPPER =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy và quay lại menu chính)");
        
        String staffId;
        try {
            staffId = InputHelper.getStringInput("Nhập mã shipper của bạn (Staff ID): ",
                    val -> CustomerStaffService.findStaff(val) != null,
                    "Mã shipper không tồn tại trong hệ thống!");
        } catch (FormCancelledException e) {
            System.out.println("  ↩ Quay lại Menu chính...\n");
            return;
        }

        DeliveryStaff currentStaff = CustomerStaffService.findStaff(staffId);
        System.out.println(BOLD_GREEN + "\n👋 Chào mừng Shipper: " + currentStaff.getName() + " (Mã: " + currentStaff.getId() + ") đăng nhập thành công!" + RESET);
        System.out.println();

        boolean running = true;
        while (running) {
            System.out.println(
                    BOLD_YELLOW + "  ⚡ ϞϞ(๑⚈ ‿ ⚈๑)ϞϞ ⚡   " + BOLD_RED + "DELIVERY STAFF (SHIPPER) - MENU" + RESET);
            System.out.println(BOLD_YELLOW + "╔═══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_CYAN + "  [TÁC VỤ SHIPPER: " + String.format("%-18s", currentStaff.getName()) + "]                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  1. " + WHITE
                    + "Xem danh sách đơn hàng được giao                   " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  2. " + WHITE
                    + "Cập nhật ngày nhận hàng thực tế                     " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  3. " + WHITE
                    + "Cập nhật ngày giao hàng thực tế                     " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  4. " + WHITE
                    + "Thêm ghi chú giao hàng / hành trình                 " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_WHITE + "  5. " + WHITE
                    + "Cập nhật trạng thái đơn giao hàng                   " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "║                                                       ║" + RESET);
            System.out.println(BOLD_YELLOW + "║" + BOLD_RED + "  0. " + BOLD_WHITE
                    + "Đăng xuất & Quay lại Menu chính                    " + BOLD_YELLOW + "║" + RESET);
            System.out.println(BOLD_YELLOW + "╚═══════════════════════════════════════════════════════╝" + RESET);

            int choice = InputHelper.getIntInput(BOLD_YELLOW + "Chọn chức năng (0-5): " + RESET, 0, 5);

            switch (choice) {
                case 1:
                    handleViewAssignedOrders(staffId);
                    break;
                case 2:
                    handleUpdatePickupDate(staffId);
                    break;
                case 3:
                    handleUpdateDeliveryDate(staffId);
                    break;
                case 4:
                    handleRegisterTrackingNote(staffId);
                    break;
                case 5:
                    handleUpdateOrderStatus(staffId);
                    break;
                case 0:
                    running = false;
                    System.out.println("  ↩ Shipper " + currentStaff.getName() + " đã đăng xuất. Quay lại Menu chính...\n");
                    break;
                default:
                    break;
            }
        }
    }

    // ----------------------------------------------------------
    // Xem đơn được giao
    // ----------------------------------------------------------
    private static void handleViewAssignedOrders(String staffId) {
        System.out.println(BOLD_CYAN + "\n===== DANH SÁCH ĐƠN HÀNG ĐƯỢC GIAO =====" + RESET);
        try {
            // Tự động filter theo shipper đang đăng nhập (UX-08)
            List<DeliveryOrder> orders = DeliveryOrderRepository.findByStaffId(staffId);
            if (orders.isEmpty()) {
                System.out.println("Không có đơn hàng nào được phân công cho bạn.");
                return;
            }
            System.out.println(BOLD_GREEN + "✅ Tìm thấy " + orders.size() + " đơn hàng được phân công cho bạn:" + RESET);
            System.out.println("+------------+------------+------------+------------+------------+---------------+-----------------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Hình Thức TT  | Tiền Thu Hộ (COD)     |");
            System.out.println("+------------+------------+------------+------------+------------+---------------+-----------------------+");
            for (DeliveryOrder o : orders) {
                // Lấy thông tin hóa đơn tương ứng
                com.cdms.model.Invoice inv = com.cdms.service.BillingReportService.findInvoiceByOrderId(o.getId());
                String codAmountStr = "0 VND";
                String termsStr = "Chưa rõ";
                if (o.getPaymentTerms() != null) {
                    if ("Sender Pay".equals(o.getPaymentTerms())) {
                        termsStr = "Prepaid (Gửi)";
                        codAmountStr = "Đã thanh toán";
                    } else {
                        termsStr = "COD (Nhận)";
                        if (inv != null) {
                            codAmountStr = String.format("%,.0f VND", inv.getTotalAmount());
                        } else {
                            // Tính toán động từ Parcel vì chưa phát hành hóa đơn
                            com.cdms.model.Parcel parcel = com.cdms.repository.ParcelRepository.findById(o.getParcelId());
                            double parcelFee = (parcel != null) ? parcel.calculateFee() : 0.0;
                            double urgentCharge = "Urgent".equalsIgnoreCase(o.getDeliveryType()) ? 20000.0 : 0.0;
                            codAmountStr = String.format("%,.0f VND", parcelFee + urgentCharge);
                        }
                    }
                } else {
                    // Mặc định cho đơn hàng cũ không có paymentTerms
                    termsStr = "COD (Nhận)";
                    if (inv != null) {
                        codAmountStr = String.format("%,.0f VND", inv.getTotalAmount());
                    } else {
                        // Tính toán động từ Parcel vì chưa phát hành hóa đơn
                        com.cdms.model.Parcel parcel = com.cdms.repository.ParcelRepository.findById(o.getParcelId());
                        double parcelFee = (parcel != null) ? parcel.calculateFee() : 0.0;
                        double urgentCharge = "Urgent".equalsIgnoreCase(o.getDeliveryType()) ? 20000.0 : 0.0;
                        codAmountStr = String.format("%,.0f VND", parcelFee + urgentCharge);
                    }
                }
                
                System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-10s | %-13s | %-21s |%n",
                        o.getId(), o.getParcelId(),
                        (o.getStaffId() != null ? o.getStaffId() : "Chưa phân"),
                        o.getDeliveryType(), o.getStatus(), termsStr, codAmountStr);
            }
            System.out.println("+------------+------------+------------+------------+------------+---------------+-----------------------+");
        } catch (Exception e) {
            System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Cập nhật ngày nhận hàng
    // ----------------------------------------------------------
    private static void handleUpdatePickupDate(String staffId) {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT NGÀY NHẬN HÀNG THỰC TẾ =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            // Bảo mật: Chỉ cho phép shipper nhập mã đơn được gán cho chính mình!
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ",
                    val -> {
                        DeliveryOrder o = DeliveryOrderRepository.findById(val);
                        return o != null && staffId.equalsIgnoreCase(o.getStaffId());
                    },
                    "Mã đơn hàng không tồn tại hoặc đơn hàng này không được phân công cho bạn!");

            DeliveryOrder current = DeliveryOrderRepository.findById(orderId);
            System.out.println(BOLD_YELLOW + "\n[ĐƠN HÀNG HIỆN TẠI]:" + RESET);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println(current);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("Trạng thái hiện tại: " + BOLD_WHITE + current.getStatus() + RESET);
            if (current.getPickupDate() != null) {
                System.out.println("Ngày nhận hàng hiện tại: " + BOLD_WHITE + current.getPickupDate() + RESET);
            }
            System.out.println();

            java.time.LocalDate pickupDate = InputHelper.getDateInput("Ngày nhận hàng thực tế (DD/MM/YYYY): ", false);

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
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Cập nhật ngày giao hàng
    // ----------------------------------------------------------
    private static void handleUpdateDeliveryDate(String staffId) {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT NGÀY GIAO HÀNG THỰC TẾ =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            // Bảo mật: Chỉ cho phép shipper nhập mã đơn được gán cho chính mình!
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ",
                    val -> {
                        DeliveryOrder o = DeliveryOrderRepository.findById(val);
                        return o != null && staffId.equalsIgnoreCase(o.getStaffId());
                    },
                    "Mã đơn hàng không tồn tại hoặc đơn hàng này không được phân công cho bạn!");

            DeliveryOrder current = DeliveryOrderRepository.findById(orderId);
            System.out.println(BOLD_YELLOW + "\n[ĐƠN HÀNG HIỆN TẠI]:" + RESET);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println(current);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("Trạng thái hiện tại: " + BOLD_WHITE + current.getStatus() + RESET);
            if (current.getDeliveryDate() != null) {
                System.out.println("Ngày giao thực tế hiện tại: " + BOLD_WHITE + current.getDeliveryDate() + RESET);
            }
            System.out.println();

            java.time.LocalDate deliveryDate = InputHelper.getDateInput("Ngày giao hàng thực tế (DD/MM/YYYY): ", false);

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
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Thêm ghi chú giao hàng
    // ----------------------------------------------------------
    private static void handleRegisterTrackingNote(String staffId) {
        System.out.println(BOLD_CYAN + "\n===== THÊM GHI CHÚ HÀNH TRÌNH =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            // Bảo mật: Chỉ cho phép shipper nhập mã đơn được gán cho chính mình!
            String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ",
                    val -> {
                        DeliveryOrder o = DeliveryOrderRepository.findById(val);
                        return o != null && staffId.equalsIgnoreCase(o.getStaffId());
                    },
                    "Mã đơn hàng không tồn tại hoặc đơn hàng này không được phân công cho bạn!");

            DeliveryOrder current = DeliveryOrderRepository.findById(orderId);
            System.out.println(BOLD_YELLOW + "\n[ĐƠN HÀNG HIỆN TẠI]:" + RESET);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println(current);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("Các ghi chú hiện tại: " + BOLD_WHITE + current.getNotes() + RESET);
            System.out.println();

            String note = InputHelper.getStringInput("Nội dung ghi chú mới: ");

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
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }

    // ----------------------------------------------------------
    // Cập nhật trạng thái đơn
    // ----------------------------------------------------------
    private static void handleUpdateOrderStatus(String staffId) {
        System.out.println(BOLD_CYAN + "\n===== CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG =====" + RESET);
        System.out.println("(Nhập 'cancel' để hủy)");
        try {
            // Bảo mật: Chỉ cho phép shipper nhập mã đơn được gán cho chính mình!
            String orderId = InputHelper.getStringInput("Mã đơn hàng: ",
                    val -> {
                        DeliveryOrder o = DeliveryOrderRepository.findById(val);
                        return o != null && staffId.equalsIgnoreCase(o.getStaffId());
                    },
                    "Mã đơn hàng không tồn tại hoặc đơn hàng này không được phân công cho bạn!");

            DeliveryOrder current = DeliveryOrderRepository.findById(orderId);
            System.out.println(BOLD_YELLOW + "\n[ĐƠN HÀNG HIỆN TẠI (UX-01)]:" + RESET);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println(current);
            System.out.println("+------------+------------+------------+------------+------------+--------------+");
            System.out.println("Trạng thái hiện tại: " + BOLD_WHITE + current.getStatus() + RESET);
            System.out.println();

            String status = InputHelper.getStringInput("Trạng thái mới (In Transit/Delivered/Failed): ",
                    val -> val.equalsIgnoreCase("In Transit") || val.equalsIgnoreCase("Delivered") || val.equalsIgnoreCase("Failed"),
                    "Trạng thái không hợp lệ! (Chỉ chấp nhận 'In Transit', 'Delivered' hoặc 'Failed')");

            System.out.println("\nXác nhận cập nhật trạng thái đơn hàng?");
            System.out.println("  1. Cập nhật");
            System.out.println("  2. Hủy (Cancel)");
            int confirm = InputHelper.getIntInput("Lựa chọn (1-2): ", 1, 2);

            if (confirm == 1) {
                try {
                    com.cdms.model.DeliveryOrder o = com.cdms.service.TrackingService.updateStatus(orderId, status);
                    System.out.println(BOLD_GREEN + "✅ Cập nhật trạng thái đơn hàng thành công!" + RESET);
                    System.out.println("+------------+------------+------------+------------+------------+--------------+");
                    System.out.println("| Mã Đơn     | Mã Kiện    | Mã Shipper | Dịch Vụ    | Trạng Thái | Ngày Tạo     |");
                    System.out.println("+------------+------------+------------+------------+------------+--------------+");
                    System.out.println(o);
                    System.out.println("+------------+------------+------------+------------+------------+--------------+");
                } catch (Exception e) {
                    System.out.println(BOLD_RED + "❌ Lỗi: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(BOLD_RED + "❌ Đã hủy cập nhật trạng thái." + RESET);
            }
        } catch (FormCancelledException e) {
            System.out.println(BOLD_RED + "\n❌ Đã hủy thao tác.\n" + RESET);
        } finally {
            InputHelper.pressEnterToContinue(); // UX-04
        }
    }
}
