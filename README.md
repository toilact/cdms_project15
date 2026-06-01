# 📦 Courier Delivery Management System (CDMS) - Hệ Thống Quản Lý Giao Hàng

[![Java Version](https://img.shields.io/badge/Java-17%2B-oracle.svg?style=flat-square&logo=openjdk&color=ED8B00)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg?style=flat-square&logo=apache-maven&color=C71A36)](https://maven.apache.org/)
[![Gson](https://img.shields.io/badge/Gson-2.11.0-blue.svg?style=flat-square&logo=google&color=4285F4)](https://github.com/google/gson)
[![Project Status](https://img.shields.io/badge/Status-Completed-brightgreen.svg?style=flat-square)](#)

Courier Delivery Management System (**CDMS**) là ứng dụng bảng Console (CLI) chuyên nghiệp quản lý giao hàng nhanh, được lập trình hoàn chỉnh dựa trên nền tảng **Java 17 (OOP)** và quản lý bởi **Maven**. Hệ thống áp dụng thiết kế phân tầng chuẩn mực và các nguyên lý OOP bền vững để xử lý toàn diện quy trình giao nhận.

---

## 🏗️ Kiến Trúc Hệ Thống (Layered Architecture)
Dự án được tổ chức chặt chẽ theo cấu trúc phân tầng giúp cô lập trách nhiệm nghiệp vụ và tăng cường khả năng mở rộng:
* **`core/`**: Khởi chạy ứng dụng (`MainApp`), quét dữ liệu an toàn (`InputHelper`), nạp/ghi cơ sở dữ liệu (`JSONDataManager`).
* **`model/`**: Các thực thể nghiệp vụ hướng đối tượng (`Customer`, `Parcel`, `DeliveryStaff`, `DeliveryOrder`, `Invoice`).
* **`repository/`**: Tầng truy xuất dữ liệu CRUD trực tiếp trên Collections (RAM).
* **`service/`**: Tầng xử lý nghiệp vụ, kiểm tra ràng buộc & quy tắc hệ thống.
* **`view/`**: Giao diện CLI tương tác phân quyền theo vai trò người dùng.

---

## 💎 Đặc Điểm Thiết Kế Hướng Đối Tượng (OOP) Nổi Bật

### 1. Đóng gói (Encapsulation) bảo mật
Mọi thuộc tính của đối tượng được khai báo với phạm vi truy cập `private`, giao tiếp an toàn qua `Getters/Setters` và định dạng chuỗi hiển thị qua phương thức `@Override toString()`.

### 2. Kế thừa & Đa hình (Inheritance & Polymorphism)
* Lớp cha trừu tượng **`Parcel`** định nghĩa phương thức tính phí: `public abstract double calculateFee();`.
* Lớp con **`DocumentParcel`** ghi đè tính phí cố định (**15,000 VND**).
* Lớp con **`GoodsParcel`** ghi đè tính phí động theo trọng lượng (**Khối lượng × 10,000 VND/kg**).

### 3. Giải tuần tự hóa đa hình với Gson
Tích hợp bộ chuyển đổi tùy chỉnh **`ParcelDeserializer`** và **`LocalDateAdapter`** giúp Gson nhận diện chính xác và khôi phục các kiểu dữ liệu kế thừa (`Goods` / `Document`) và định dạng ngày tháng `LocalDate` của Java 8+ từ tệp tin JSON một cách mượt mà.

---

## 👥 Phân Quyền Vai Trò & Các Tính Năng Hoàn Chỉnh

Hệ thống cung cấp giao diện Console phân quyền chuyên nghiệp theo 4 vai trò chính:

### 1. Nhân Viên Tiếp Nhận (Reception Staff)
* Quản lý khách hàng (Thêm, sửa thông tin, hiển thị danh sách phân trang, tìm kiếm theo Tên/SĐT, xóa KH).
* Tiếp nhận bưu kiện (Tạo mới, xem danh sách, cập nhật thông tin, tìm kiếm, xóa bưu kiện).
* Lập đơn giao hàng (Tạo đơn giao, xem lịch sử giao nhận của từng khách hàng).

### 2. Điều Phối Viên (Dispatcher)
* Quản lý Shipper (Thêm mới, xem danh sách phân trang, cập nhật trạng thái hoạt động, tìm kiếm, xóa Shipper).
* Điều phối đơn hàng (Phân công đơn hàng `Pending` cho Shipper khả dụng).
* Giám sát giao nhận (Cập nhật trạng thái, theo dõi đơn đang giao, đơn giao thất bại, xem toàn bộ danh sách đơn hàng).

### 3. Nhân Viên Giao Hàng (Shipper)
* Đăng nhập an toàn qua Staff ID.
* Xem danh sách đơn hàng được phân công giao riêng cho bản thân.
* Cập nhật thời gian nhận hàng/giao hàng thực tế.
* Cập nhật trạng thái đơn hàng (Picked Up, In Transit, Delivered, Failed).
* Ghi nhận ghi chú hành trình (Tracking Notes) trực quan.

### 4. Quản Lý (Manager)
* Tính hóa đơn kế toán và ghi nhận thanh toán tự động cho các đơn giao thành công.
* Đối soát tiền thu hộ (COD) từ Shipper bàn giao về quỹ công ty.
* Báo cáo thống kê thời gian thực (Giám sát live stats dạng dashboard cao cấp).
* Báo cáo doanh thu trực quan theo Ngày, Tháng, Năm.
* Xếp hạng Top 5 Shipper tích cực nhất.
* CRUD hóa đơn thủ công & quản lý hoàn tiền (cho các đơn bị hủy/thất bại).

---

## 💡 Cơ Sở Dữ Liệu JSON (In-Memory Database)

Hệ thống hoạt động theo cơ chế **In-Memory Database** tối ưu:
* Khi khởi động ứng dụng, toàn bộ dữ liệu từ các file JSON sẽ được tải lên RAM thành các Java Collections để truy cập với độ trễ bằng không.
* Khi chọn chức năng thoát (Thoát & Lưu dữ liệu), hệ thống sẽ tự động đồng bộ và ghi đè dữ liệu mới nhất xuống các file JSON tại thư mục `data/`:
  * `customers.json`
  * `parcels.json`
  * `orders.json`
  * `staffs.json`
  * `invoices.json`

---

## 🚀 Hướng Dẫn Khởi Chạy Nhanh (Quick Start)

### Yêu Cầu Hệ Thống:
* **Java Development Kit (JDK):** Phiên bản **17** trở lên (`java -version`).
* **Apache Maven:** Phiên bản **3.x** trở lên (`mvn -version`).

### Các Bước Thực Hiện:
1. Mở Terminal / Command Prompt và di chuyển vào thư mục dự án:
   ```bash
   cd cdms_project15
   ```
2. Cài đặt thư viện phụ thuộc (lần đầu):
   ```bash
   mvn clean install
   ```
3. Biên dịch và khởi chạy ứng dụng trực tiếp:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.cdms.core.MainApp"
   ```

*Hệ thống được thiết kế giao diện Console tối giản, sang trọng tích hợp xử lý đệm tự động giúp hiển thị viền thẳng tắp và đối xứng trên mọi môi trường terminal.*