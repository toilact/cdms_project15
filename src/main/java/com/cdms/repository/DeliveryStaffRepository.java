// ============================================================
// File: DeliveryStaffRepository.java
// Package: com.cdms.repository
// Description: Các thao tác CRUD cho danh sách DeliveryStaff
//              trong bộ nhớ (JSONDataManager.staffs).
// ============================================================
package com.cdms.repository;

import java.util.ArrayList;
import java.util.List;

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
            return new ArrayList<>();
        }
        List<DeliveryStaff> result = new ArrayList<>();
        String keyword = name.trim().toLowerCase();
        for (DeliveryStaff staff : JSONDataManager.staffs) {
            if (staff.getName().toLowerCase().contains(keyword)) {
                result.add(staff);
            }
        }
        return result;
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
        DeliveryStaff toRemove = null;
        for (DeliveryStaff staff : JSONDataManager.staffs) {
            if (staffId.equalsIgnoreCase(staff.getId())) {
                toRemove = staff;
                break;
            }
        }
        if (toRemove != null) {
            JSONDataManager.staffs.remove(toRemove);
            JSONDataManager.saveAllData();
            return true;
        }
        return false;
    }

    /**
     * Lấy danh sách Top Shipper có số đơn giao thành công cao nhất.
     * Sắp xếp giảm dần theo deliveredOrdersCount, lấy tối đa [limit] người.
     */
    public static List<DeliveryStaff> findTopShippers(int limit) {
        // Sao chép danh sách để không làm ảnh hưởng dữ liệu gốc
        List<DeliveryStaff> sorted = new ArrayList<>(JSONDataManager.staffs);

        // Sắp xếp bong bóng giảm dần theo số đơn đã giao
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                if (sorted.get(j).getDeliveredOrdersCount() < sorted.get(j + 1).getDeliveredOrdersCount()) {
                    DeliveryStaff temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        // Lấy tối đa [limit] phần tử đầu tiên
        List<DeliveryStaff> result = new ArrayList<>();
        for (int i = 0; i < sorted.size() && i < limit; i++) {
            result.add(sorted.get(i));
        }
        return result;
    }
}
