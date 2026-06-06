// ============================================================
// File: DeliveryOrderRepository.java
// Package: com.cdms.repository
// Description: Các thao tác CRUD cho danh sách DeliveryOrder
//              trong bộ nhớ (JSONDataManager.orders).
// ============================================================
package com.cdms.repository;

import java.util.ArrayList;
import java.util.List;
import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;

public class DeliveryOrderRepository {

    private DeliveryOrderRepository() {
    }

    /**
     * Lấy toàn bộ danh sách đơn hàng
     */
    public static List<DeliveryOrder> findAll() {
        return new ArrayList<>(JSONDataManager.orders);
    }

    /**
     * Tìm đơn hàng theo ID
     */
    public static DeliveryOrder findById(String orderId) {
        if (orderId == null) {
            return null;
        }
        for (DeliveryOrder order : JSONDataManager.orders) {
            if (orderId.equalsIgnoreCase(order.getId())) {
                return order;
            }
        }
        return null;
    }

    /**
     * Tìm đơn hàng theo trạng thái
     */
    public static List<DeliveryOrder> findByStatus(String status) {
        if (status == null) {
            return new ArrayList<>();
        }
        List<DeliveryOrder> result = new ArrayList<>();
        for (DeliveryOrder order : JSONDataManager.orders) {
            if (status.equalsIgnoreCase(order.getStatus())) {
                result.add(order);
            }
        }
        return result;
    }

    /**
     * Lấy danh sách các đơn hàng đã giao thành công
     */
    public static List<DeliveryOrder> findDeliveredOrders() {
        return findByStatus("Delivered");
    }

    /**
     * Cập nhật đơn hàng
     */
    public static boolean update(DeliveryOrder updated) {
        if (updated == null || updated.getId() == null) {
            return false;
        }
        for (int i = 0; i < JSONDataManager.orders.size(); i++) {
            if (JSONDataManager.orders.get(i).getId().equalsIgnoreCase(updated.getId())) {
                JSONDataManager.orders.set(i, updated);
                JSONDataManager.saveAllData();
                return true;
            }
        }
        return false;
    }

    /**
     * Đếm số đơn hàng theo trạng thái
     */
    public static long countByStatus(String status) {
        if (status == null) {
            return 0;
        }
        int count = 0;
        for (DeliveryOrder order : JSONDataManager.orders) {
            if (status.equalsIgnoreCase(order.getStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Thêm đơn hàng mới
     */
    public static void add(DeliveryOrder order) {
        if (order != null) {
            JSONDataManager.orders.add(order);
            JSONDataManager.saveAllData();
        }
    }

    /**
     * Kiểm tra đơn hàng có tồn tại theo ID hay không
     */
    public static boolean existsById(String id) {
        if (id == null) {
            return false;
        }
        for (DeliveryOrder order : JSONDataManager.orders) {
            if (id.equalsIgnoreCase(order.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tìm các đơn hàng được gán cho một shipper cụ thể
     */
    public static List<DeliveryOrder> findByStaffId(String staffId) {
        if (staffId == null) {
            return new ArrayList<>();
        }
        List<DeliveryOrder> result = new ArrayList<>();
        for (DeliveryOrder order : JSONDataManager.orders) {
            if (staffId.equalsIgnoreCase(order.getStaffId())) {
                result.add(order);
            }
        }
        return result;
    }
}
