// ============================================================
// Package: com.cdms.repository
// Vai trò: TẦNG TRUY XUẤT DỮ LIỆU (Data Access Layer)
// ============================================================
//
// ╔══════════════════════════════════════════════════════════════╗
// ║  HƯỚNG DẪN DÀNH CHO CÁC THÀNH VIÊN TRONG NHÓM            ║
// ╚══════════════════════════════════════════════════════════════╝
//
// Tầng Repository có nhiệm vụ DUY NHẤT là thao tác trực tiếp
// với nguồn dữ liệu (các List static trong JSONDataManager).
//
// ► NGUYÊN TẮC VÀNG:
//   - Repository CHỈ chứa các thao tác CRUD thuần túy:
//     + findAll()    → Lấy toàn bộ danh sách
//     + findById()   → Tìm theo mã (ID/Code)
//     + add()        → Thêm phần tử vào List
//     + update()     → Cập nhật phần tử đã tồn tại
//     + delete()     → Xóa phần tử khỏi List
//
//   - KHÔNG được chứa logic nghiệp vụ (if/else phức tạp,
//     tính toán phí, kiểm tra quy tắc...).
//     → Logic nghiệp vụ thuộc về tầng Service.
//
//   - KHÔNG được gọi InputHelper hay in ra System.out.
//     → Giao diện thuộc về tầng View.
//
// ► DỮ LIỆU TRUNG TÂM:
//   Tất cả dữ liệu được lưu trữ tại: JSONDataManager
//   Truy cập qua:
//     - JSONDataManager.customers  → List<Customer>
//     - JSONDataManager.parcels    → List<Parcel>
//     - JSONDataManager.orders     → List<DeliveryOrder>
//     - JSONDataManager.staffs     → List<DeliveryStaff>
//     - JSONDataManager.invoices   → List<Invoice>
//
// ► CÁC FILE CẦN TẠO TRONG PACKAGE NÀY:
//   1. CustomerRepository.java     → Thành viên 2 (Developer A)
//   2. DeliveryStaffRepository.java → Thành viên 2 (Developer A)
//   3. ParcelRepository.java       → Thành viên 3 (Developer B)
//   4. DeliveryOrderRepository.java → Thành viên 3 (Developer B)
//   5. InvoiceRepository.java      → Thành viên 5 (Developer D)
//
// ► VÍ DỤ MẪU (xem file CustomerRepository.java trong package này)
//
// ============================================================
package com.cdms.repository;
