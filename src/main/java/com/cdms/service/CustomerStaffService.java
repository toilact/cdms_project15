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

import com.cdms.model.Customer;
import com.cdms.model.DeliveryStaff;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.DeliveryStaffRepository;

import java.util.List;

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

    public static List<DeliveryStaff> getAllStaffs() {
        return DeliveryStaffRepository.findAll();
    }

    public static DeliveryStaff findStaff(String staffId) {
        return DeliveryStaffRepository.findById(staffId);
    }

    public static String addStaff(DeliveryStaff staff) {
        if (staff == null || staff.getId() == null || staff.getId().trim().isEmpty()) {
            return "❌ Lỗi: Mã shipper không được để trống.";
        }
        if (DeliveryStaffRepository.findById(staff.getId()) != null) {
            return "❌ Lỗi: Mã shipper '" + staff.getId() + "' đã tồn tại!";
        }
        if (DeliveryStaffRepository.findByPhone(staff.getPhone()) != null) {
            return "❌ Lỗi: Số điện thoại này đã được sử dụng bởi shipper khác.";
        }
        DeliveryStaffRepository.add(staff);
        return "✅ Đã thêm shipper: " + staff.getId() + " - " + staff.getName();
    }

    public static String updateStaff(DeliveryStaff updated) {
        boolean success = DeliveryStaffRepository.update(updated);
        if (success) {
            return "✅ Đã cập nhật shipper: " + updated.getId();
        }
        return "❌ Lỗi: Không tìm thấy shipper với mã '" + updated.getId() + "'!";
    }

    public static String deleteStaff(String staffId) {
        boolean success = DeliveryStaffRepository.delete(staffId);
        if (success) {
            return "✅ Đã xóa shipper: " + staffId;
        }
        return "❌ Lỗi: Không tìm thấy shipper với mã '" + staffId + "'!";
    }
}
