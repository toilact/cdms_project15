// ============================================================
// File: CustomerRepository.java
// Package: com.cdms.repository
// Phân công: Nguyên Quốc Cường (Developer A - Thành viên 2)
// ============================================================
// MỤC ĐÍCH:
//   Cung cấp các thao tác CRUD thuần túy trên danh sách
//   Customer lưu trong JSONDataManager.customers.
//
// LƯU Ý QUAN TRỌNG:
//   - File này là VÍ DỤ MẪU do Leader viết sẵn.
//   - Nguyên Quốc Cường (Developer A) cần HOÀN THIỆN các phương thức TODO bên dưới
//     và tạo thêm file DeliveryStaffRepository.java tương tự.
// ============================================================
package com.cdms.repository;

import com.cdms.core.JSONDataManager;
import com.cdms.model.Customer;

import java.util.List;

public class CustomerRepository {

    // Ngăn khởi tạo đối tượng (Utility class - chỉ dùng static)
    private CustomerRepository() {
    }

    /**
     * Lấy toàn bộ danh sách khách hàng hiện có trong hệ thống.
     *
     * @return List<Customer> chứa tất cả khách hàng
     */
    public static List<Customer> findAll() {
        return new java.util.ArrayList<>(JSONDataManager.customers);
    }

    /**
     * Tìm một khách hàng theo mã khách hàng (customerId).
     *
     * @param customerId Mã khách hàng cần tìm (ví dụ: "KH001")
     * @return Đối tượng Customer nếu tìm thấy, hoặc null nếu không tồn tại
     */
    public static Customer findById(String customerId) {
        for (Customer c : JSONDataManager.customers) {
            if (c.getId().equalsIgnoreCase(customerId)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Thêm một khách hàng mới vào danh sách.
     * Sau khi thêm, dữ liệu sẽ được đồng bộ xuống file JSON.
     *
     * @param customer Đối tượng Customer cần thêm
     */
    public static void add(Customer customer) {
        JSONDataManager.customers.add(customer);
        JSONDataManager.saveAllData(); // Đồng bộ ngay sau khi thay đổi
    }

    /**
     * Cập nhật thông tin khách hàng đã tồn tại.
     * Tìm theo customerId, nếu tìm thấy thì thay thế bằng đối tượng mới.
     *
     * @param updated Đối tượng Customer chứa thông tin đã cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy
     */
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

    /**
     * Xóa một khách hàng theo mã khách hàng.
     *
     * @param customerId Mã khách hàng cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public static boolean delete(String customerId) {
        boolean removed = JSONDataManager.customers
                .removeIf(c -> c.getId().equalsIgnoreCase(customerId));
        if (removed) {
            JSONDataManager.saveAllData();
        }
        return removed;
    }

    /**
     * Tìm kiếm danh sách khách hàng theo tên (chấp nhận khớp một phần, không phân biệt hoa thường).
     *
     * @param name Tên hoặc một phần tên khách hàng cần tìm
     * @return Danh sách khách hàng thỏa mãn điều kiện lọc
     */
    public static List<Customer> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return JSONDataManager.customers.stream()
                .filter(c -> c.getName().toLowerCase().contains(name.trim().toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Tìm kiếm khách hàng theo số điện thoại (chính xác).
     *
     * @param phone Số điện thoại cần tìm kiếm
     * @return Đối tượng Customer thỏa mãn, hoặc null nếu không tìm thấy
     */
    public static Customer findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        return JSONDataManager.customers.stream()
                .filter(c -> c.getPhone().equals(phone.trim()))
                .findFirst()
                .orElse(null);
    }
}

