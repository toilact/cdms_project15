// ============================================================
// File: CustomerRepository.java
// Package: com.cdms.repository
// Description: Các thao tác CRUD cho danh sách Customer
//              trong bộ nhớ (JSONDataManager.customers).
// Phân công: Nguyên Quốc Cường (Developer A - Thành viên 2)
// ============================================================
package com.cdms.repository;

import com.cdms.core.JSONDataManager;
import com.cdms.model.Customer;

import java.util.List;

public class CustomerRepository {

    // Utility class — không cho tạo đối tượng
    private CustomerRepository() {
    }

    /** Trả về bản sao danh sách tất cả khách hàng. */
    public static List<Customer> findAll() {
        return new java.util.ArrayList<>(JSONDataManager.customers);
    }

    /** Tìm khách hàng theo mã, trả về null nếu không tìm thấy. */
    public static Customer findById(String customerId) {
        for (Customer c : JSONDataManager.customers) {
            if (c.getId().equalsIgnoreCase(customerId)) {
                return c;
            }
        }
        return null;
    }

    /** Thêm khách hàng mới và lưu xuống file JSON. */
    public static void add(Customer customer) {
        JSONDataManager.customers.add(customer);
        JSONDataManager.saveAllData(); // Đồng bộ ngay sau khi thay đổi
    }

    /** Cập nhật thông tin khách hàng. Trả về true nếu thành công, false nếu không tìm thấy. */
    public static boolean update(Customer updated) {
        List<Customer> list = JSONDataManager.customers;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(updated.getId())) {
                list.set(i, updated);
                JSONDataManager.saveAllData();
                return true;
            }
        }
        return false;
    }

    /** Xóa khách hàng theo mã. Trả về true nếu thành công, false nếu không tìm thấy. */
    public static boolean delete(String customerId) {
        Customer toRemove = null;
        for (Customer c : JSONDataManager.customers) {
            if (c.getId().equalsIgnoreCase(customerId)) {
                toRemove = c;
                break;
            }
        }
        if (toRemove != null) {
            JSONDataManager.customers.remove(toRemove);
            JSONDataManager.saveAllData();
            return true;
        }
        return false;
    }

    /** Tìm khách hàng theo tên (khớp một phần, không phân biệt hoa/thường). */
    public static List<Customer> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        List<Customer> result = new java.util.ArrayList<>();
        String keyword = name.trim().toLowerCase();
        for (Customer c : JSONDataManager.customers) {
            if (c.getName().toLowerCase().contains(keyword)) {
                result.add(c);
            }
        }
        return result;
    }

    /** Tìm khách hàng theo SĐT (khớp chính xác). Trả về null nếu không tìm thấy. */
    public static Customer findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        String target = phone.trim();
        for (Customer c : JSONDataManager.customers) {
            if (c.getPhone().equals(target)) {
                return c;
            }
        }
        return null;
    }
}

