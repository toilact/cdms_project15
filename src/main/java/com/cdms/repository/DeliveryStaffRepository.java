// ============================================================
// File: DeliveryStaffRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý CRUD cho DeliveryStaff.
// ============================================================
package com.cdms.repository;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryStaff;

import java.util.List;

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

    public static DeliveryStaff findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        for (DeliveryStaff staff : JSONDataManager.staffs) {
            if (phone.trim().equals(staff.getPhone())) {
                return staff;
            }
        }
        return null;
    }

    public static void add(DeliveryStaff staff) {
        JSONDataManager.staffs.add(staff);
        JSONDataManager.saveAllData();
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

    public static boolean delete(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            return false;
        }
        boolean removed = JSONDataManager.staffs.removeIf(s -> s.getId().equalsIgnoreCase(staffId.trim()));
        if (removed) {
            JSONDataManager.saveAllData();
        }
        return removed;
    }
}
