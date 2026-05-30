// ============================================================
// File: ParcelOrderView.java
// Package: com.cdms.view
// Description: Giao diện Console cho phân hệ Kiện hàng & Đơn giao hàng
//              (Đã loại bỏ Reflection và showMenu dư thừa)
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

    // Khai báo Service - Không truyền JSONDataManager nữa
    private static final ParcelService parcelService = new ParcelService();
    private static final OrderService orderService = new OrderService();

    // Ngăn khởi tạo đối tượng
    private ParcelOrderView() {
    }

    // ==========================================================
    //  CÁC HÀM THỰC THI (EXECUTE)
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

        try {
            Parcel parcel = parcelService.createParcel(id, senderId, receiverName, 
                    receiverPhone, pickupAddress, deliveryAddress, weight, type);
            
            System.out.println("✅ Tạo kiện hàng thành công: " + id);
            System.out.println(parcel);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
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
        String deliveryType = InputHelper.getStringInput("Loại đơn hàng (Standard/Urgent): ");

        try {
            DeliveryOrder order = orderService.convertParcelToOrder(orderId, parcelId, staffId, deliveryType);
            
            System.out.println("✅ Chuyển đổi kiện hàng thành đơn hàng thành công: " + orderId);
            System.out.println(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

        public static void executeUpdateOrderStatus() {
        System.out.println("\n=== CẬP NHẬT TRẠNG THÁI ĐƠN ===");
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");
        String status = InputHelper.getStringInput("Nhập trạng thái mới (Assigned/In Transit/Delivered/Cancelled/Failed): ");

        try {
            DeliveryOrder order = orderService.updateOrderStatus(orderId, status);
            System.out.println("✅ Cập nhật trạng thái thành công!");
            System.out.println(order);
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    public static void executeViewOrderDetail() {
        System.out.println("\n=== XEM CHI TIẾT ĐƠN HÀNG ===");
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng: ");

        try {
            DeliveryOrder order = orderService.getOrderDetail(orderId);
            System.out.println("✅ Thông tin đơn hàng:");
            System.out.println(order);
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    public static void executeSearchOrdersByCustomer() {
        System.out.println("\n=== TÌM KIẾM ĐƠN THEO KHÁCH HÀNG ===");
        String customerId = InputHelper.getStringInput("Nhập mã khách hàng: ");

        try {
            List<DeliveryOrder> orders = orderService.searchOrdersByCustomer(customerId);

            if (orders.isEmpty()) {
                System.out.println("Không tìm thấy đơn hàng nào của khách hàng " + customerId);
                return;
            }

            System.out.println("✅ Tìm thấy " + orders.size() + " đơn hàng:");
            for (DeliveryOrder o : orders) {
                System.out.println(o);
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    public static void executeCancelOrder() {
        System.out.println("\n=== HỦY ĐƠN GIAO HÀNG ===");
        String orderId = InputHelper.getStringInput("Nhập mã đơn hàng cần hủy: ");

        try {
            DeliveryOrder order = orderService.cancelOrder(orderId);
            System.out.println("✅ Hủy đơn hàng thành công!");
            System.out.println(order);
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }
}