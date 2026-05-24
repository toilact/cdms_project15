// ============================================================
// File: DeliveryOrderRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý CRUD cho DeliveryOrder.
// ============================================================
package com.cdms.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryOrder;

public class DeliveryOrderRepository {

    private DeliveryOrderRepository() {
    }

    public static List<DeliveryOrder> findAll() {
        return JSONDataManager.orders;
    }

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

    public static List<DeliveryOrder> findByStatus(String status) {
        if (status == null) {
            return List.of();
        }
        return JSONDataManager.orders.stream()
                .filter(order -> status.equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList());
    }

    public static List<DeliveryOrder> findDeliveredOrders() {
        return findByStatus("Delivered");
    }

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

    public static long countByStatus(String status) {
        if (status == null) {
            return 0;
        }
        return JSONDataManager.orders.stream()
                .filter(order -> status.equalsIgnoreCase(order.getStatus()))
                .count();
    }
}
