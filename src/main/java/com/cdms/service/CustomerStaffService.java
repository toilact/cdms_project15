// ============================================================
// File: CustomerStaffService.java
// Package: com.cdms.service
// Description: Xử lý nghiệp vụ quản lý Khách hàng và Shipper.
//              Chỉ gọi Repository, không in ra màn hình.
// Phân công: Nguyên Quốc Cường (Developer A - Thành viên 2)
// ============================================================
package com.cdms.service;

import java.util.List;

import com.cdms.model.Customer;
import com.cdms.model.DeliveryStaff;
import com.cdms.repository.CustomerRepository;
import com.cdms.repository.DeliveryStaffRepository;

public class CustomerStaffService {

    // Utility class — không cho tạo đối tượng
    private CustomerStaffService() {
    }

    // ----------------------------------------------------------
    // QUẢN LÝ KHÁCH HÀNG
    // ----------------------------------------------------------

    /** Trả về danh sách tất cả khách hàng. */
    public static List<Customer> getAllCustomers() {
        return CustomerRepository.findAll();
    }

    /** Tìm khách hàng theo mã, trả về null nếu không tìm thấy. */
    public static Customer findCustomer(String customerId) {
        return CustomerRepository.findById(customerId);
    }

    /** Thêm khách hàng mới. Kiểm tra trùng mã và trùng SĐT trước khi lưu. */
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

    /** Cập nhật thông tin khách hàng, trả về thông báo kết quả. */
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

    public static String deleteCustomer(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return "❌ Lỗi: Mã khách hàng không được để trống.";
        }
        String finalCustomerId = customerId.trim();

        // Kiểm tra xem khách hàng có bưu kiện nào liên kết không
        int parcelCount = 0;
        for (com.cdms.model.Parcel p : com.cdms.repository.ParcelRepository.findAll()) {
            if (finalCustomerId.equalsIgnoreCase(p.getSenderId())) {
                parcelCount++;
            }
        }
        if (parcelCount > 0) {
            return "❌ Lỗi: Không thể xóa khách hàng vì có " + parcelCount + " bưu kiện liên kết với khách hàng này!";
        }

        boolean success = CustomerRepository.delete(customerId);
        if (success) {
            return "✅ Đã xóa khách hàng: " + customerId;
        }
        return "❌ Lỗi: Không tìm thấy khách hàng với mã '" + customerId + "'!";
    }

    // ----------------------------------------------------------
    // QUẢN LÝ SHIPPER
    // ----------------------------------------------------------

    /** Trả về danh sách tất cả nhân viên giao hàng. */
    public static List<DeliveryStaff> getAllStaffs() {
        return DeliveryStaffRepository.findAll();
    }

    /** Tìm shipper theo mã, trả về null nếu không tìm thấy. */
    public static DeliveryStaff findStaff(String staffId) {
        return DeliveryStaffRepository.findById(staffId);
    }

    /** Thêm shipper mới. Kiểm tra trùng mã và trùng SĐT trước khi lưu. */
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

    /** Cập nhật thông tin shipper, trả về thông báo kết quả. */
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

    /** Xóa shipper — chặn nếu còn đơn hàng chưa hoàn thành hoặc có lịch sử giao. */
    public static String deleteStaff(String staffId) {
        if (staffId == null || staffId.trim().isEmpty()) {
            return "❌ Lỗi: Mã shipper không được để trống.";
        }
        String finalStaffId = staffId.trim();

        // 1. Kiểm tra xem shipper có đơn hàng nào chưa hoàn thành hay không
        int activeOrders = 0;
        for (com.cdms.model.DeliveryOrder o : com.cdms.repository.DeliveryOrderRepository.findAll()) {
            if (finalStaffId.equalsIgnoreCase(o.getStaffId())
                    && !"Delivered".equalsIgnoreCase(o.getStatus())
                    && !"Cancelled".equalsIgnoreCase(o.getStatus())
                    && !"Failed".equalsIgnoreCase(o.getStatus())) {
                activeOrders++;
            }
        }
        if (activeOrders > 0) {
            return "❌ Lỗi: Không thể xóa shipper vì đang chịu trách nhiệm cho " + activeOrders + " đơn hàng chưa hoàn thành!";
        }

        // 2. Kiểm tra xem shipper có bất kỳ đơn hàng lịch sử nào không
        int completedOrders = 0;
        for (com.cdms.model.DeliveryOrder o : com.cdms.repository.DeliveryOrderRepository.findAll()) {
            if (finalStaffId.equalsIgnoreCase(o.getStaffId())) {
                completedOrders++;
            }
        }
        if (completedOrders > 0) {
            return "❌ Lỗi: Không thể xóa shipper này vì đã có dữ liệu giao hàng lịch sử trong hệ thống! Vui lòng chuyển trạng thái shipper sang 'Fired' (Đã sa thải) để ngừng hoạt động.";
        }

        boolean success = DeliveryStaffRepository.delete(staffId);
        if (success) {
            return "✅ Đã xóa shipper: " + staffId;
        }
        return "❌ Lỗi: Không tìm thấy shipper với mã '" + staffId + "'!";
    }
}
