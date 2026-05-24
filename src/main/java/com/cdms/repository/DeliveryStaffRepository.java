// ============================================================
// File: DeliveryStaffRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý CRUD cho DeliveryStaff.
// ============================================================
package com.cdms.repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryStaff;

public class DeliveryStaffRepository {

    private DeliveryStaffRepository() {
    }

    public static List<DeliveryStaff> findAll() {
        return JSONDataManager.staffs;
    }

    public static DeliveryStaff findById(String staffId) {
        if (staffId == null) {
            return null;
        }
        for (DeliveryStaff staff : JSONDataManager.staffs) {
            if (staffId.equalsIgnoreCase(staff.getId())) {
                return staff;
            }
        }
        return null;
    }

    public static boolean update(DeliveryStaff updated) {
        if (updated == null || updated.getId() == null) {
            return false;
        }
        for (int i = 0; i < JSONDataManager.staffs.size(); i++) {
            if (JSONDataManager.staffs.get(i).getId().equalsIgnoreCase(updated.getId())) {
                JSONDataManager.staffs.set(i, updated);
                JSONDataManager.saveAllData();
                return true;
            }
        }
        return false;
    }

    public static List<DeliveryStaff> findTopShippers(int limit) {
        return JSONDataManager.staffs.stream()
                .sorted(Comparator.comparingInt(DeliveryStaff::getDeliveredOrdersCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
