// ============================================================
// File: DeliveryStaffRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý CRUD cho DeliveryStaff.
// ============================================================
package com.cdms.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cdms.core.JSONDataManager;
import com.cdms.model.DeliveryStaff;

public class DeliveryStaffRepository {

    private DeliveryStaffRepository() {
    }

    /**
     * Lấy danh sách toàn bộ nhân viên giao hàng
     */
    public static List<DeliveryStaff> findAll() {
        return new ArrayList<>(JSONDataManager.staffs);
    }

    /**
     * Tìm kiếm shipper theo ID
     */
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

    /**
     * Tìm kiếm shipper theo số điện thoại
     */
    public static DeliveryStaff findByPhone(String phone) {
        if (phone == null) {
            return null;
        }
        for (DeliveryStaff staff : JSONDataManager.staffs) {
            if (phone.equalsIgnoreCase(staff.getPhone())) {
                return staff;
            }
        }
        return null;
    }

    /**
     * Tìm kiếm shipper theo tên (Khớp một phần, không phân biệt hoa thường)
     */
    public static List<DeliveryStaff> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return JSONDataManager.staffs.stream()
                .filter(staff -> staff.getName().toLowerCase().contains(name.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Thêm shipper mới
     */
    public static boolean add(DeliveryStaff staff) {
        if (staff == null || findById(staff.getId()) != null) {
            return false;
        }
        JSONDataManager.staffs.add(staff);
        JSONDataManager.saveAllData();
        return true;
    }

    /**
     * Cập nhật thông tin shipper
     */
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

    /**
     * Xóa shipper
     */
    public static boolean delete(String staffId) {
        if (staffId == null) {
            return false;
        }
        boolean removed = JSONDataManager.staffs.removeIf(staff -> staffId.equalsIgnoreCase(staff.getId()));
        if (removed) {
            JSONDataManager.saveAllData();
        }
        return removed;
    }

    /**
     * Lấy danh sách Top Shipper có số đơn giao thành công cao nhất
     */
    public static List<DeliveryStaff> findTopShippers(int limit) {
        return JSONDataManager.staffs.stream()
                .sorted(Comparator.comparingInt(DeliveryStaff::getDeliveredOrdersCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
