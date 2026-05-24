// ============================================================
// File: CustomerStaffService.java
// Package: com.cdms.service
// Phân công: Nguyên Quốc Cường (Developer A - Thành viên 2)
// ============================================================
// MỤC ĐÍCH:
//   Xử lý toàn bộ logic nghiệp vụ liên quan đến quản lý
//   Khách hàng (Customer) và Nhân viên giao hàng (DeliveryStaff).
//
// NGUYÊN TẮC:
//   - Gọi Repository để lấy/lưu dữ liệu (KHÔNG truy cập thẳng JSONDataManager).
//   - KHÔNG chứa Scanner hay System.out.println().
//   - Trả về kết quả (String, List, boolean...) cho View hiển thị.
//
// LƯU Ý QUAN TRỌNG:
//   - File này là VÍ DỤ MẪU do Leader viết sẵn.
//   - Nguyên Quốc Cường (Developer A) cần HOÀN THIỆN các phương thức TODO bên dưới.
// ============================================================
package com.cdms.service;

import java.util.List;

import com.cdms.model.Customer;
import com.cdms.repository.CustomerRepository;

public class CustomerStaffService {

    // Ngăn khởi tạo đối tượng (Utility class - chỉ dùng static)
    private CustomerStaffService() {
    }

    // ==========================================================
    //         QUẢN LÝ KHÁCH HÀNG (CUSTOMER)
    // ==========================================================

    /**
     * Lấy danh sách tất cả khách hàng.
     *
     * @return List<Customer>
     */
    public static List<Customer> getAllCustomers() {
        return CustomerRepository.findAll();
    }

    /**
     * Tìm khách hàng theo mã.
     *
     * @param customerId Mã khách hàng (ví dụ: "KH001")
     * @return Customer hoặc null
     */
    public static Customer findCustomer(String customerId) {
        return CustomerRepository.findById(customerId);
    }

    /**
     * Thêm một khách hàng mới vào hệ thống.
     * Kiểm tra trùng mã trước khi thêm.
     *
     * @param customer Đối tượng Customer cần thêm
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String addCustomer(Customer customer) {
        // Kiểm tra trùng mã khách hàng
        if (CustomerRepository.findById(customer.getId()) != null) {
            return "❌ Lỗi: Mã khách hàng '" + customer.getId() + "' đã tồn tại!";
        }
        CustomerRepository.add(customer);
        return "✅ Đã thêm khách hàng: " + customer.getId() + " - " + customer.getName();
    }

    /**
     * Cập nhật thông tin khách hàng.
     *
     * @param updated Đối tượng Customer với thông tin mới
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String updateCustomer(Customer updated) {
        boolean success = CustomerRepository.update(updated);
        if (success) {
            return "✅ Đã cập nhật khách hàng: " + updated.getId();
        }
        return "❌ Lỗi: Không tìm thấy khách hàng với mã '" + updated.getId() + "'!";
    }

    /**
     * Xóa một khách hàng theo mã.
     *
     * @param customerId Mã khách hàng cần xóa
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String deleteCustomer(String customerId) {
        boolean success = CustomerRepository.delete(customerId);
        if (success) {
            return "✅ Đã xóa khách hàng: " + customerId;
        }
        return "❌ Lỗi: Không tìm thấy khách hàng với mã '" + customerId + "'!";
    }

    // ==========================================================
    //       QUẢN LÝ NHÂN VIÊN GIAO HÀNG (DELIVERY STAFF)
    // ==========================================================

    // TODO [Nguyên Quốc Cường - Developer A]: Tạo file DeliveryStaffRepository.java
    //   trong package repository trước, sau đó viết các phương thức
    //   quản lý DeliveryStaff tương tự như Customer ở trên:
    //
    //   - getAllStaffs()              → Lấy toàn bộ danh sách shipper
    //   - findStaff(String staffId)  → Tìm shipper theo mã
    //   - addStaff(DeliveryStaff s)  → Thêm shipper mới (kiểm tra trùng mã)
    //   - updateStaff(...)           → Cập nhật thông tin shipper
    //   - deleteStaff(String id)     → Xóa shipper theo mã
    //
    // LƯU Ý: Phương thức ở đây trả về String (thông báo) hoặc
    //         List/Object để View nhận và in ra cho người dùng.
    //         KHÔNG dùng System.out.println() trong Service!
}
