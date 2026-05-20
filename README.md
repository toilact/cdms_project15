# 📦 Courier Delivery Management System (CDMS)

[![Java Version](https://img.shields.io/badge/Java-17%2B-oracle.svg?style=flat-square&logo=openjdk&color=ED8B00)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg?style=flat-square&logo=apache-maven&color=C71A36)](https://maven.apache.org/)
[![Gson](https://img.shields.io/badge/Gson-2.11.0-blue.svg?style=flat-square&logo=google&color=4285F4)](https://github.com/google/gson)
[![Project Status](https://img.shields.io/badge/Status-Skeleton_Ready-brightgreen.svg?style=flat-square)](#)

Hệ thống quản lý giao hàng nhanh (**CDMS - Courier Delivery Management System**) là ứng dụng console được xây dựng trên nền tảng **Java 17 (OOP)** và quản lý bởi **Maven**. Dự án áp dụng chặt chẽ các nguyên lý thiết kế hướng đối tượng (Solid/OOP) và mô hình phân tầng (Layered Architecture) chuẩn mực để làm nền tảng (Skeleton) bàn giao cho đội ngũ 5 thành viên cùng phát triển song song.

---

## 🛠️ Công Nghệ & Thư Viện Sử Dụng

- **Ngôn ngữ:** Java Core (Phiên bản 17 trở lên).
- **Quản lý dự án:** Apache Maven.
- **Lưu trữ dữ liệu:** Đồng bộ In-Memory với File cơ sở dữ liệu JSON thông qua **Google Gson (v2.11.0)**.
- **Đóng gói:** JAR.

---

## 🏗️ Cấu Trúc Thư Mục Đầy Đủ (20 Files Core)

Dưới đây là sơ đồ chi tiết toàn bộ cấu trúc mã nguồn đã được tạo sẵn để tránh xung đột (merge conflicts) khi các thành viên trong nhóm code song song:

```text
cdms_project15/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── cdms/
│                   ├── core/                          # Tiện ích hệ thống, cấu hình Gson & Khởi chạy chính
│                   │   ├── InputHelper.java          # Bộ quét console an toàn chống crash
│                   │   ├── JSONDataManager.java      # Bộ quản lý cơ sở dữ liệu in-memory/JSON
│                   │   ├── LocalDateAdapter.java     # Cấu hình chuyển đổi Gson cho Ngày tháng
│                   │   ├── ParcelDeserializer.java   # Cấu hình Gson đa hình lớp trừu tượng Parcel
│                   │   └── MainApp.java              # Lớp khởi chạy chính & Vòng lặp Menu phân quyền
│                   │
│                   ├── model/                         # Các thực thể nghiệp vụ dữ liệu (OOP Model)
│                   │   ├── Customer.java             # Thực thể khách hàng
│                   │   ├── Parcel.java               # Lớp cha trừu tượng (Abstract) của Kiện hàng
│                   │   ├── DocumentParcel.java       # Kiện hàng tài liệu (Tính phí cố định)
│                   │   ├── GoodsParcel.java          # Kiện hàng hàng hóa (Tính phí theo cân nặng)
│                   │   ├── DeliveryStaff.java        # Thực thể nhân viên giao hàng (Shipper)
│                   │   ├── DeliveryOrder.java        # Thực thể Đơn hàng vận chuyển
│                   │   └── Invoice.java              # Thực thể hóa đơn tính tiền
│                   │
│                   ├── repository/                    # Tầng TRUY XUẤT DỮ LIỆU CRUD (RAM/JSON)
│                   │   ├── package-info.java         # Hướng dẫn quy định & Phân công phát triển Repo
│                   │   └── CustomerRepository.java   # Code mẫu CRUD Khách hàng (Developer A)
│                   │
│                   ├── service/                       # Tầng LOGIC NGHIỆP VỤ (Business Logic)
│                   │   ├── package-info.java         # Hướng dẫn quy định & Phân công phát triển Service
│                   │   └── CustomerStaffService.java # Code mẫu logic kiểm tra & Lưu trữ (Developer A)
│                   │
│                   └── view/                          # Tầng GIAO DIỆN Console phân quyền
│                       ├── CustomerStaffView.java    # Giao diện cho Nhân viên tiếp nhận (Lễ tân)
│                       ├── ParcelOrderView.java      # Giao diện xem/tạo đơn hàng & kiện hàng chi tiết
│                       ├── DispatcherView.java       # Giao diện Điều phối vận chuyển (Dispatcher)
│                       ├── ShipperView.java          # Giao diện Giao nhận hành trình (Shipper)
│                       └── BillingReportView.java    # Giao diện Kế toán & Báo cáo quản lý (Manager)
│
├── data/                                              # Thư mục cơ sở dữ liệu tự sinh chứa các file *.json
├── pom.xml                                            # File cấu hình thư viện Maven
└── README.md                                          # Tài liệu hướng dẫn dự án (File này)
```

---

## 💎 Đặc Điểm Thiết Kế Hướng Đối Tượng (OOP) Nổi Bật

### 1. Đóng gói (Encapsulation) Chặt Chẽ
Mọi Model trong hệ thống đều được thiết kế bảo mật với `private fields`, cung cấp dữ liệu qua `Getters/Setters` an toàn, đầy đủ constructor và phương thức `toString()` chuẩn hóa (không sử dụng Lombok để đảm bảo tương thích 100% trên mọi IDE thuần).

### 2. Kế thừa & Đa hình (Inheritance & Polymorphism)
- Lớp cha trừu tượng **`Parcel.java`** định nghĩa phương thức tính toán phí vận chuyển chung: 
  ```java
  public abstract double calculateFee();
  ```
- Lớp con **`DocumentParcel.java`** override tính phí cố định (**15,000 VND**).
- Lớp con **`GoodsParcel.java`** override tính phí động theo trọng lượng (**Cân nặng × 10,000 VND**).

### 3. Gson Polymorphic Deserialization
Gson thông thường không thể tự động nhận diện và khôi phục (deserialize) các đối tượng từ lớp trừu tượng `Parcel`. Hệ thống đã cài đặt bộ chuyển đổi tùy chỉnh **`ParcelDeserializer`** và **`LocalDateAdapter`** giúp tự động phân tách loại kiện hàng (`DOCUMENT` hoặc `GOODS`) và chuyển đổi kiểu dữ liệu `LocalDate` của Java 8+ một cách mượt mà.

---

## ⚡ Tiện Ích Cốt Lõi (Core Utilities)

- **`InputHelper`**: Bộ quét dữ liệu Console an toàn, bắt lỗi `try-catch` trong vòng lặp chống crash ứng dụng khi người dùng nhập sai kiểu dữ liệu (ngày tháng DD/MM/YYYY, số thực lớn hơn min, số nguyên trong khoảng).
- **`JSONDataManager`**: Bộ lưu trữ/nạp dữ liệu trung tâm. Khi khởi chạy, hệ thống tải toàn bộ dữ liệu vào RAM để ứng dụng truy xuất với tốc độ cực nhanh, khi thoát sẽ tự động lưu lại vào các file JSON tại thư mục `data/`.

---

## 👥 Phân Công Vai Trò & Nhiệm Vụ Trong Team

Dự án được thiết kế dưới dạng khung xương (skeleton) hoàn chỉnh, chia nhiệm vụ cụ thể cho **5 thành viên** thông qua các file Stub View chứa sẵn đánh dấu `// TODO`:

| Thành Viên | Vai Trò Đảm Nhiệm | Module Phụ Trách | File Logic Cần Lập Trình |
| :--- | :--- | :--- | :--- |
| **Thành viên 1**<br>*(Leader)* | **Architect & Core Setup** | Khởi tạo cấu hình, Utilities, Models và luồng Menu điều hướng chính | Đã hoàn thành 100% khung xương |
| **Thành viên 2** | **Developer A** | Quản lý thông tin Khách hàng (`Customer`) & Nhân viên giao hàng (`DeliveryStaff`) | `CustomerStaffView.java` & tạo `CustomerStaffService.java` |
| **Thành viên 3** | **Developer B** | Quản lý Kiện hàng (`Parcel`) & Đơn hàng (`DeliveryOrder`) | `ParcelOrderView.java` & tạo `ParcelOrderService.java` |
| **Thành viên 4** | **Developer C** | Quản lý Điều phối & Theo dõi hành trình (`Tracking`) | `DispatcherView.java`, `ShipperView.java` & tạo `TrackingService.java` |
| **Thành viên 5** | **Developer D** | Quản lý Hóa đơn kế toán (`Invoice`) & Báo cáo thống kê (`Report`) | `BillingReportView.java` & tạo `BillingReportService.java` |

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### 1. Yêu cầu hệ thống
- Máy tính đã cài đặt **JDK 17** hoặc mới hơn.
- Đã cài đặt công cụ quản lý build **Apache Maven 3.x**.

### 2. Cài đặt các thư viện phụ thuộc (Build dự án)
Di chuyển vào thư mục dự án và thực hiện build để Maven tải các dependency (Gson):
```bash
mvn clean install
```

### 3. Khởi chạy ứng dụng trực tiếp từ Terminal
Nhờ cấu hình `exec-maven-plugin` tích hợp sẵn trong `pom.xml`, bạn có thể khởi chạy ứng dụng trực tiếp chỉ bằng một câu lệnh:
```bash
mvn exec:java -Dexec.mainClass="com.cdms.core.MainApp"
```

---

## 🌳 Quy Trình Git & Làm Việc Nhóm (Git Workflow)

1. Dự án hiện đã được đẩy lên nhánh gốc phát triển chung là: `feature/project-skeleton`.
2. Mỗi thành viên khi bắt đầu làm nhiệm vụ cần **checkout một nhánh riêng** từ nhánh gốc này:
   ```bash
   git checkout feature/project-skeleton
   git checkout -b feature/ten-thanh-vien-module-name
   ```
3. Sau khi hoàn thành và kiểm thử logic chạy ổn định, tạo **Pull Request (PR)** gửi cho Leader duyệt trước khi merge vào nhánh chính.