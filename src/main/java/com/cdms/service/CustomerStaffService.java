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
// ============================================================
package com.cdms.service;

import java.util.List;

import com.cdms.model.Customer;
import com.cdms.model.DeliveryStaff;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.DeliveryStaffRepository;

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
     * Kiểm tra dữ liệu đầu vào, trùng mã, và trùng số điện thoại.
     *
     * @param customer Đối tượng Customer cần thêm
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String addCustomer(Customer customer) {
        // Kiểm tra dữ liệu đầu vào hợp lệ
        if (customer == null || customer.getId() == null || customer.getId().trim().isEmpty()) {
            return "❌ Lỗi: Mã khách hàng không được để trống.";
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return "❌ Lỗi: Tên khách hàng không được để trống.";
        }
        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            return "❌ Lỗi: Số điện thoại không được để trống.";
        }
        // Kiểm tra trùng mã khách hàng
        if (CustomerRepository.findById(customer.getId()) != null) {
            return "❌ Lỗi: Mã khách hàng '" + customer.getId() + "' đã tồn tại!";
        }
        // Kiểm tra trùng số điện thoại
        if (CustomerRepository.findByPhone(customer.getPhone()) != null) {
            return "❌ Lỗi: Số điện thoại '" + customer.getPhone() + "' đã được sử dụng bởi khách hàng khác.";
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
        if (updated == null || updated.getId() == null) {
            return "❌ Lỗi: Dữ liệu khách hàng không hợp lệ.";
        }
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

    /**
     * Lấy danh sách tất cả nhân viên giao hàng.
     *
     * @return List<DeliveryStaff>
     */
    public static List<DeliveryStaff> getAllStaffs() {
        return DeliveryStaffRepository.findAll();
    }

    /**
     * Tìm nhân viên giao hàng theo mã.
     *
     * @param staffId Mã nhân viên
     * @return DeliveryStaff hoặc null
     */
    public static DeliveryStaff findStaff(String staffId) {
        return DeliveryStaffRepository.findById(staffId);
    }

    /**
     * Thêm nhân viên giao hàng mới.
     * Kiểm tra dữ liệu đầu vào, trùng mã, và trùng số điện thoại.
     *
     * @param staff Đối tượng DeliveryStaff cần thêm
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String addStaff(DeliveryStaff staff) {
        if (staff == null || staff.getId() == null || staff.getId().trim().isEmpty()) {
            return "❌ Lỗi: Mã shipper không được để trống.";
        }
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            return "❌ Lỗi: Tên shipper không được để trống.";
        }
        if (staff.getPhone() == null || staff.getPhone().trim().isEmpty()) {
            return "❌ Lỗi: Số điện thoại không được để trống.";
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

    /**
     * Cập nhật thông tin nhân viên giao hàng.
     *
     * @param updated Đối tượng DeliveryStaff với thông tin mới
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String updateStaff(DeliveryStaff updated) {
        if (updated == null || updated.getId() == null) {
            return "❌ Lỗi: Dữ liệu shipper không hợp lệ.";
        }
        boolean success = DeliveryStaffRepository.update(updated);
        if (success) {
            return "✅ Đã cập nhật shipper: " + updated.getId();
        }
        return "❌ Lỗi: Không tìm thấy shipper với mã '" + updated.getId() + "'!";
    }

    /**
     * Xóa nhân viên giao hàng theo mã.
     *
     * @param staffId Mã nhân viên cần xóa
     * @return Thông báo kết quả (String) để View hiển thị
     */
    public static String deleteStaff(String staffId) {
        boolean success = DeliveryStaffRepository.delete(staffId);
        if (success) {
            return "✅ Đã xóa shipper: " + staffId;
        }
        return "❌ Lỗi: Không tìm thấy shipper với mã '" + staffId + "'!";
    }
}
