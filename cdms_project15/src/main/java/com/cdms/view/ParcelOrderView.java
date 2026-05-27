// ============================================================
// File: ParcelOrderView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Kiện hàng & Đơn giao hàng
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

    // Khai báo Service
    private static final ParcelService parcelService;
    private static final OrderService orderService;

    // Khởi tạo static services
    static {
        JSONDataManager dataManager;
        try {
            java.lang.reflect.Constructor<JSONDataManager> ctor = JSONDataManager.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            dataManager = ctor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Không thể khởi tạo JSONDataManager", e);
        }

        parcelService = new ParcelService(dataManager);
        orderService = new OrderService(dataManager);
    }

    // Ngăn khởi tạo đối tượng
    private ParcelOrderView() {
    }

    // ==========================================================
    //  SUBMENU: QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG
    // ==========================================================

    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║   QUẢN LÝ KIỆN HÀNG & ĐƠN HÀNG        ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.println("║  1. Thêm kiện hàng mới         (B4)   ║");
            System.out.println("║  2. Xem danh sách kiện hàng    (B5)   ║");
            System.out.println("║  3. Tạo đơn giao hàng mới      (B6)   ║");
            System.out.println("║  4. Cập nhật đơn giao hàng     (B7)   ║");
            System.out.println("║  5. Xem chi tiết đơn giao hàng (B8)   ║");
            System.out.println("║  6. Tìm kiếm đơn theo KH              ║");
            System.out.println("║  7. Hủy đơn giao hàng                 ║");
            System.out.println("║                                       ║");
            System.out.println("║  0. Quay lại Menu chính               ║");
            System.out.println("╚═══════════════════════════════════════╝");

            int choice = InputHelper.getIntInput("Chọn chức năng (0-7): ", 0, 7);

           
            switch (choice) {
                case 1: executeCreateParcel(); break;
                case 2: executeViewParcels(); break;
                case 3: executeCreateOrder(); break;
                case 4: executeUpdateOrderStatus(); break;
                case 5: executeViewOrderDetail(); break;
                case 6: executeSearchOrdersByCustomer(); break;
                case 7: executeCancelOrder(); break;
                case 0:
                    running = false;
                    System.out.println("  ↩ Quay lại Menu chính...\n");
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }

    // ==========================================================
    //  CÁC HÀM THỰC THI - ĐÃ SỬA THÀNH PUBLIC STATIC
    // ==========================================================

    public static void executeCreateParcel() {
        System.out.println("\n=== THÊM KIỆN HÀNG MỚI ===");
        String id = InputHelper.getStringInput("Mã kiện hàng: ");
        String senderId = InputHelper.getStringInput("Mã khách hàng gửi: ");
        String receiverName = InputHelper.getStringInput("Tên người nhận: ");
        String receiverPhone = InputHelper.getStringInput("SĐT người nhận: ");
        String pickupAddress = InputHelper.getStringInput("Địa chỉ lấy hàng: ");
        String deliveryAddress = InputHelper.getStringInput("Địa chỉ giao hàng: ");
        double weight = InputHelper.getDoubleInput("Trọng lượng (kg): ", 0.1);
        String type = InputHelper.getStringInput("Loại kiện (Document/Goods): ");

        Parcel parcel = parcelService.createParcel(id, senderId, receiverName, receiverPhone,
                pickupAddress, deliveryAddress, weight, type);

        if (parcel != null) {
            System.out.println("✅ Tạo kiện hàng thành công!");
            System.out.println(parcel);
        }
    }

    public static void executeViewParcels() {
        System.out.println("\n=== DANH SÁCH KIỆN HÀNG ===");
        List<Parcel> parcels = JSONDataManager.parcels;

        if (parcels.isEmpty()) {
            System.out.println("Chưa có kiện hàng nào.");
            return;
        }

        for (Parcel p : parcels) {
            System.out.println(p);
        }
        System.out.println("Tổng số kiện: " + parcels.size());
    }

    public static void executeCreateOrder() {
        System.out.println("\n=== TẠO ĐƠN GIAO HÀNG ===");
        String orderId = InputHelper.getStringInput("Mã đơn hàng: ");
        String parcelId = InputHelper.getStringInput("Mã kiện hàng cần chuyển: ");
        String staffId = InputHelper.getStringInput("Mã shipper (Staff ID): ");

        DeliveryOrder order = orderService.convertParcelToOrder(orderId, parcelId, staffId);

        if (order != null) {
            System.out.println("✅ Tạo đơn giao hàng thành công!");
            System.out.println(order);
        }
    }

    public static void executeUpdateOrderStatus() {
        System.out.println("\n=== CẬP NHẬT TRẠNG THÁI ĐƠN ===");
        System.out.println("⚠ Chức năng đang được phát triển...");
    }

    public static void executeViewOrderDetail() {
        System.out.println("\n=== XEM CHI TIẾT ĐƠN HÀNG ===");
        System.out.println("⚠ Chức năng đang được phát triển...");
    }

    public static void executeSearchOrdersByCustomer() {
        System.out.println("\n=== TÌM KIẾM ĐƠN THEO KHÁCH HÀNG ===");
        System.out.println("⚠ Chức năng đang được phát triển...");
    }

    public static void executeCancelOrder() {
        System.out.println("\n=== HỦY ĐƠN GIAO HÀNG ===");
        System.out.println("⚠ Chức năng đang được phát triển...");
    }
}