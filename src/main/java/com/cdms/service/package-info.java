// ============================================================
// Package: com.cdms.service
// Vai trò: TẦNG XỬ LÝ NGHIỆP VỤ (Business Logic Layer)
// ============================================================
//
// ╔══════════════════════════════════════════════════════════════╗
// ║  HƯỚNG DẪN DÀNH CHO CÁC THÀNH VIÊN TRONG NHÓM            ║
// ╚══════════════════════════════════════════════════════════════╝
//
// Tầng Service chứa toàn bộ LOGIC NGHIỆP VỤ của ứng dụng.
// Đây là nơi "bộ não" của hệ thống hoạt động.
//
// ► NGUYÊN TẮC VÀNG:
//   - Service GỌI Repository để truy xuất/lưu dữ liệu.
//     Ví dụ: CustomerService gọi CustomerRepository.findById()
//
//   - Service KHÔNG BAO GIỜ truy cập thẳng JSONDataManager.
//     → Luôn đi qua Repository làm trung gian.
//
//   - Service KHÔNG được chứa Scanner hay System.out.println().
//     → Giao diện thuộc về tầng View.
//     → Service CHỈ return kết quả (String, List, boolean...)
//       để View nhận lại và hiển thị cho người dùng.
//
// ► LUỒNG GỌI CHUẨN (3 TẦNG):
//
//   View (Console Menu)
//     │
//     ▼
//   Service (Logic nghiệp vụ)   ← BẠN VIẾT CODE Ở ĐÂY
//     │
//     ▼
//   Repository (CRUD dữ liệu)
//     │
//     ▼
//   JSONDataManager (File JSON)
//
// ► CÁC FILE CẦN TẠO TRONG PACKAGE NÀY:
//   1. CustomerStaffService.java    → Thành viên 2 (Developer A)
//      Nghiệp vụ: Thêm/sửa/xóa/tìm kiếm khách hàng và shipper.
//      Gọi: CustomerRepository, DeliveryStaffRepository
//
//   2. ParcelOrderService.java      → Thành viên 3 (Developer B)
//      Nghiệp vụ: Tạo kiện hàng (Document/Goods), tạo đơn hàng,
//                  gán kiện vào đơn, tính phí calculateFee().
//      Gọi: ParcelRepository, DeliveryOrderRepository
//
//   3. TrackingService.java         → Thành viên 4 (Developer C)
//      Nghiệp vụ: Phân công shipper cho đơn, cập nhật trạng thái
//                  vận chuyển (PENDING → IN_TRANSIT → DELIVERED),
//                  ghi nhận thời gian giao, thêm ghi chú.
//      Gọi: DeliveryOrderRepository, DeliveryStaffRepository
//      Phục vụ cho 2 giao diện: ShipperView (cho Shipper) và
//                              DispatcherView (cho Điều phối viên).
//
//   4. BillingReportService.java    → Thành viên 5 (Developer D)
//      Nghiệp vụ: Tạo hóa đơn từ đơn hàng, thống kê doanh thu
//                  theo ngày/tháng, báo cáo hiệu suất shipper.
//      Gọi: InvoiceRepository, DeliveryOrderRepository,
//           DeliveryStaffRepository
//
// ► VÍ DỤ MẪU (xem file CustomerStaffService.java trong package này)
//
// ============================================================
package com.cdms.service;
