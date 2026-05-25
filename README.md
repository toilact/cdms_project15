# 📦 Courier Delivery Management System (CDMS)

[![Java Version](https://img.shields.io/badge/Java-17%2B-oracle.svg?style=flat-square&logo=openjdk&color=ED8B00)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg?style=flat-square&logo=apache-maven&color=C71A36)](https://maven.apache.org/)
[![Gson](https://img.shields.io/badge/Gson-2.11.0-blue.svg?style=flat-square&logo=google&color=4285F4)](https://github.com/google/gson)
[![Project Status](https://img.shields.io/badge/Status-Skeleton_Ready-brightgreen.svg?style=flat-square)](#)

Hệ thống quản lý giao hàng nhanh (**CDMS - Courier Delivery Management System**) là ứng dụng console được xây dựng trên nền tảng **Java 17 (OOP)** và quản lý bởi **Maven**. Dự án áp dụng chặt chẽ các nguyên lý thiết kế hướng đối tượng (Solid/OOP) và mô hình phân tầng (Layered Architecture) chuẩn mực để làm nền tảng (Skeleton) bàn giao cho đội ngũ 5 thành viên cùng phát triển song song.

> [!IMPORTANT]
> **Trạng thái dự án:** Khung xương (Skeleton) đã được thiết lập hoàn chỉnh, kiểm thử và biên dịch thành công. Dự án đã sẵn sàng để bàn giao cho các thành viên trong nhóm triển khai các module song song.

---

## 🗺️ Mục Lục Điều Hướng Nhanh

* [🛠️ Công Nghệ & Thư Viện Sử Dụng](#cong-nghe)
* [🏗️ Cấu Trúc Thư Mục Đầy Đủ](#cau-truc-thu-muc)
* [💎 Đặc Điểm Thiết Kế Hướng Đối Tượng (OOP) Nổi Bật](#dac-diem-oop)
* [⚡ Tiện Ích Cốt Lõi (Core Utilities)](#tien-ich-core)
* [👥 Phân Công Vai Trò & Nhiệm Vụ Trong Team](#phan-cong-nhiem-vu)
* [🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng](#cai-dat-khoi-chay)
  * [1. Hướng dẫn cài đặt Apache Maven (mvn) chi tiết](#cai-dat-maven)
  * [2. Di chuyển vào thư mục dự án (BẮT BUỘC)](#cd-thu-muc)
  * [3. Cấu hình thư viện ban đầu](#mvn-install)
  * [4. Khởi chạy ứng dụng trực tiếp từ Terminal](#mvn-run)
* [🌳 Quy Trình Git & Làm Việc Nhóm (Git & GitHub Flow)](#git-workflow)
  * [⚙️ Bước chuẩn bị ban đầu](#git-setup)
  * [🚀 Quy trình 3 giai đoạn làm việc hàng ngày](#git-daily)
  * [🛡️ Cách tạo Pull Request (PR) & Gộp code trên GitHub](#git-pr)
  * [📑 Bảng tra cứu nhanh các lệnh Git cơ bản](#git-cheat-sheet)

---

<div id="cong-nghe"></div>

## 🛠️ Công Nghệ & Thư Viện Sử Dụng

- **Ngôn ngữ:** Java Core (Phiên bản 17 trở lên).
- **Quản lý dự án:** Apache Maven.
- **Lưu trữ dữ liệu:** Đồng bộ In-Memory với File cơ sở dữ liệu JSON thông qua **Google Gson (v2.11.0)**.
- **Đóng gói:** JAR.

---

<div id="cau-truc-thu-muc"></div>

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

<div id="dac-diem-oop"></div>

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

<div id="tien-ich-core"></div>

## ⚡ Tiện Ích Cốt Lõi (Core Utilities)

- **`InputHelper`**: Bộ quét dữ liệu Console an toàn, bắt lỗi `try-catch` trong vòng lặp chống crash ứng dụng khi người dùng nhập sai kiểu dữ liệu (ngày tháng DD/MM/YYYY, số thực lớn hơn min, số nguyên trong khoảng).
- **`JSONDataManager`**: Bộ lưu trữ/nạp dữ liệu trung tâm. Khi khởi chạy, hệ thống tải toàn bộ dữ liệu vào RAM để ứng dụng truy xuất với tốc độ cực nhanh, khi thoát sẽ tự động lưu lại vào các file JSON tại thư mục `data/`.

---

<div id="phan-cong-nhiem-vu"></div>

## 👥 Phân Công Vai Trò & Nhiệm Vụ Trong Team

Dự án được thiết kế dưới dạng khung xương (skeleton) hoàn chỉnh, chia nhiệm vụ cụ thể cho **5 thành viên** thông qua các file Stub View chứa sẵn đánh dấu `// TODO`:

| Thành Viên | Vai Trò Đảm Nhiệm | Module Phụ Trách | File Logic Cần Lập Trình |
| :--- | :--- | :--- | :--- |
| **Thành viên 1**<br>*(Leader)*<br>**Đỗ Chí Thành** | **Architect & Core Setup** | Khởi tạo cấu hình, Utilities, Models và luồng Menu điều hướng chính | Đã hoàn thành 100% khung xương |
| **Thành viên 2**<br>*(Developer A)*<br>**Nguyên Quốc Cường** | **Quản lý thông tin Khách hàng (`Customer`) & Nhân viên giao hàng (`DeliveryStaff`)** | `CustomerStaffView.java` & tạo `CustomerStaffService.java` |
| **Thành viên 3**<br>*(Developer B)*<br>**Trương Đan Huy** | **Quản lý Kiện hàng (`Parcel`) & Đơn hàng (`DeliveryOrder`)** | `ParcelOrderView.java` & tạo `ParcelOrderService.java` |
| **Thành viên 4**<br>*(Developer C)*<br>**Nguyễn Thanh Tùng** | **Quản lý Điều phối & Theo dõi hành trình (`Tracking`)** | `DispatcherView.java`, `ShipperView.java` & tạo `TrackingService.java` |
| **Thành viên 5**<br>*(Developer D)*<br>**Huỳnh Lê Quốc Cường** | **Quản lý Hóa đơn kế toán (`Invoice`) & Báo cáo thống kê (`Report`)** | `BillingReportView.java` & tạo `BillingReportService.java` |

---

<div id="cai-dat-khoi-chay"></div>

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

> [!IMPORTANT]
> **BƯỚC ĐẦU TIÊN:** Trước khi bắt đầu bất kỳ thao tác nào dưới đây, hãy đảm bảo rằng bạn đã cài đặt **Java Development Kit (JDK) 17** trở lên trên máy tính của mình. 
> Bạn có thể kiểm tra bằng lệnh: `java -version`.

---

<div id="cai-dat-maven"></div>

### 1. Hướng dẫn cài đặt Apache Maven (mvn) chi tiết

Maven là công cụ bắt buộc để quản lý các thư viện (như Google Gson) và tự động biên dịch dự án CDMS.

#### 🍎 Đối với hệ điều hành macOS:

Bạn có thể lựa chọn một trong hai cách cài đặt sau:

* **Cách 1: Sử dụng Homebrew (Khuyên dùng - Nhanh nhất)**
  1. Mở Terminal lên.
  2. Nếu chưa cài đặt Homebrew, hãy cài đặt bằng cách chạy lệnh từ trang chủ [brew.sh](https://brew.sh).
  3. Cài đặt Maven bằng lệnh đơn giản:
     ```bash
     brew install maven
     ```
  4. Xác minh cài đặt thành công:
     ```bash
     mvn -version
     ```

* **Cách 2: Cài đặt thủ công (Không dùng Homebrew)**
  1. Tải file nén `apache-maven-3.x.x-bin.tar.gz` từ [Trang chủ Maven](https://maven.apache.org/download.cgi).
  2. Giải nén vào một thư mục cố định (Ví dụ: `/Users/username/maven/`).
  3. Mở file cấu hình Shell bằng Terminal:
     ```bash
     nano ~/.zshrc   # Nếu dùng Zsh (mặc định trên macOS mới)
     # Hoặc: nano ~/.bash_profile   # Nếu dùng Bash
     ```
  4. Thêm dòng sau vào cuối file:
     ```bash
     export MAVEN_HOME=/Users/username/maven/apache-maven-3.x.x
     export PATH=$MAVEN_HOME/bin:$PATH
     ```
  5. Nhấn `Ctrl + O` để lưu, `Ctrl + X` để thoát. Sau đó tải lại cấu hình:
     ```bash
     source ~/.zshrc
     ```
  6. Kiểm tra lại: `mvn -version`.

#### 🪟 Đối với hệ điều hành Windows:

Hãy thực hiện chính xác theo 4 bước sau đây:

1. **Tải phần mềm:** Truy cập [Trang chủ Maven](https://maven.apache.org/download.cgi) và tải tệp **`Binary zip archive`** (ví dụ: `apache-maven-3.9.6-bin.zip`).
2. **Giải nén:** Giải nén tệp vừa tải vào thư mục mong muốn, ví dụ: `C:\Program Files\apache-maven-3.9.6` hoặc `D:\maven\apache-maven-3.9.6`.
3. **Cấu hình biến môi trường (Environment Variables):**
   * Bấm phím Windows, gõ `env` và chọn **Edit the system environment variables** (Chỉnh sửa biến môi trường hệ thống).
   * Tại cửa sổ hiện ra, bấm vào nút **Environment Variables...** ở phía dưới.
   * **Tạo biến MAVEN_HOME:** Ở mục *System Variables* (Biến hệ thống), bấm **New...**
     * *Variable name:* `MAVEN_HOME`
     * *Variable value:* Đường dẫn thư mục giải nén ở Bước 2 (Ví dụ: `C:\Program Files\apache-maven-3.9.6`).
     * Nhấn **OK**.
   * **Cập nhật biến Path:** Trong danh sách *System Variables*, tìm và chọn dòng **`Path`**, sau đó bấm **Edit...**
     * Ở cửa sổ mới hiện ra, bấm nút **New** bên phải.
     * Nhập vào: `%MAVEN_HOME%\bin`
     * Bấm **OK** liên tiếp để đóng tất cả các hộp thoại.
4. **Kiểm tra cài đặt:**
   * **Quan trọng:** Bạn **PHẢI đóng toàn bộ** các cửa sổ Command Prompt (cmd) hoặc PowerShell cũ đang mở trước đó.
   * Mở một cửa sổ Command Prompt (cmd) **mới hoàn toàn** và gõ lệnh:
     ```cmd
     mvn -version
     ```
   * Nếu màn hình hiển thị thông tin phiên bản Maven cùng đường dẫn Java là bạn đã cài đặt thành công!

#### 🐧 Đối với hệ điều hành Linux (Ubuntu/Debian):
Mở terminal và chạy lệnh:
```bash
sudo apt update
sudo apt install maven -y
mvn -version
```

> [!WARNING]
> **CÁC LỖI THƯỜNG GẶP KHI CÀI MAVEN (TROUBLESHOOTING):**
> * **Lỗi `'mvn' is not recognized as an internal or external command...`**: Lỗi này xảy ra do bạn chưa cấu hình biến `Path` đúng, hoặc bạn chưa tắt cửa sổ cmd cũ đi để mở lại cửa sổ cmd mới. Hãy kiểm tra lại đường dẫn trong biến `MAVEN_HOME` và mở cmd mới.
> * **Lỗi `JAVA_HOME is not set...`**: Maven yêu cầu Java để chạy. Hãy chắc chắn rằng bạn đã cài đặt JDK 17 và tạo biến môi trường `JAVA_HOME` trỏ tới thư mục cài đặt JDK của bạn (ví dụ: `C:\Program Files\Java\jdk-17`).

---

<div id="cd-thu-muc"></div>

### 2. Di chuyển vào thư mục dự án (BẮT BUỘC)

> [!CAUTION]
> **LƯU Ý CỰC KỲ QUAN TRỌNG:** 
> Khi mở Terminal/Command Prompt mới, thư mục hoạt động mặc định sẽ là thư mục cá nhân của bạn (Ví dụ: `C:\Users\Admin`). Bạn **không thể** chạy các lệnh Maven ở đây vì hệ thống sẽ không tìm thấy file cấu hình `pom.xml`.
> 
> Bạn **BẮT BUỘC** phải di chuyển (`cd`) vào đúng thư mục chứa dự án `cdms_project15` trước khi thực thi bất kỳ lệnh nào!

```bash
# Di chuyển vào thư mục chứa dự án CDMS
cd cdms_project15
```

#### 🔍 Làm thế nào để kiểm tra xem mình đã đứng đúng thư mục dự án chưa?
* **Trên macOS/Linux:** Chạy lệnh `pwd`. Màn hình phải hiển thị đường dẫn kết thúc bằng `/cdms_project15`.
* **Trên Windows (cmd):** Chạy lệnh `cd`. Màn hình sẽ in ra đường dẫn thư mục hiện tại của bạn.
* **Kiểm tra file cấu hình:** Gõ `ls` (macOS/Linux) hoặc `dir` (Windows). Bạn phải nhìn thấy file `pom.xml` và thư mục `src` nằm ngay trong danh sách hiển thị.

---

<div id="mvn-install"></div>

### 3. Cấu hình thư viện ban đầu

Sau khi đã `cd` vào đúng thư mục dự án, chạy lệnh dưới đây để Maven tự động tải thư viện Google Gson từ Internet về máy cục bộ của bạn và chuẩn bị môi trường:
```bash
mvn clean install
```
*Thời gian chạy lần đầu có thể mất từ 1 - 2 phút tùy thuộc vào tốc độ mạng của bạn để tải các file phụ thuộc.*

---

<div id="mvn-run"></div>

### 4. Khởi chạy ứng dụng trực tiếp từ Terminal (Khuyên dùng)

Bạn không cần phải mất thời gian đóng gói dự án thành file JAR rồi mới chạy. Hãy sử dụng câu lệnh dưới đây để dọn dẹp, tự động biên dịch lại toàn bộ các thay đổi mới nhất và khởi chạy ứng dụng trực tiếp chỉ trong một giây:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.cdms.core.MainApp"
```

---

<div id="git-workflow"></div>

## 🌳 Quy Trình Git & Làm Việc Nhóm (Git & GitHub Flow)

Để đảm bảo mã nguồn dự án luôn hoạt động ổn định, tránh tối đa việc đè code của nhau hoặc gây ra xung đột (merge conflicts), toàn bộ thành viên trong nhóm **bắt buộc** phải tuân thủ nghiêm ngặt quy trình làm việc Git dưới đây:

> [!CAUTION]
> **🚫 TUYỆT ĐỐI KHÔNG ĐẨY (PUSH) TRỰC TIẾP CODE LÊN NHÁNH `main`!**
> Nhánh `main` của dự án đã được thiết lập **Luật bảo vệ nhánh nghiêm ngặt (Branch Protection Rules)** trên GitHub. 
> * Mọi lệnh `git push origin main` hoặc cố gắng ghi đè lên nhánh `main` trực tiếp từ Terminal **sẽ bị máy chủ GitHub từ chối và báo lỗi ngay lập tức**.
> * **Quy trình đúng:** Mọi thành viên chỉ làm việc trên nhánh phụ của mình (`feature/...`), đẩy nhánh đó lên GitHub và tạo **Pull Request (PR)** để Leader duyệt trước khi gộp vào `main`.

<div id="git-setup"></div>

### ⚙️ Bước chuẩn bị ban đầu (Chỉ làm 1 lần duy nhất)

Trước khi thực hiện bất kỳ lệnh Git nào, hãy khai báo danh tính của bạn với Git để các commit được ghi nhận đúng tên:
```bash
# Cấu hình họ tên của bạn (viết không dấu hoặc có dấu tùy ý)
git config --global user.name "Nguyen Van A"

# Cấu hình email tài khoản GitHub của bạn
git config --global user.email "your-email@example.com"
```

---

<div id="git-daily"></div>

### 🚀 Quy trình 3 giai đoạn làm việc hàng ngày

#### GIAI ĐOẠN 1: TẠO NHÁNH TÍNH NĂNG MỚI (BRANCHING)
Mỗi khi bắt đầu làm một nhiệm vụ mới (ví dụ: làm giao diện ShipperView), tuyệt đối **không được code trực tiếp trên nhánh `main`**. Hãy tạo một nhánh con riêng biệt:

```bash
# 1. Chuyển về nhánh chính để lấy code chuẩn
git checkout main

# 2. Cập nhật mã nguồn mới nhất từ GitHub về máy
git pull origin main

# 3. Tạo và chuyển sang nhánh tính năng mới
git checkout -b feature/ten-cua-ban-ten-chuc-nang
```

> [!TIP]
> **Quy chuẩn đặt tên nhánh (Branch Naming Convention):**
> * Thêm tính năng mới: `feature/ten-thành-viên-tên-chức-năng` (Ví dụ: `feature/thanh-shipper-view`)
> * Sửa lỗi phần mềm: `bugfix/ten-thành-viên-tên-lỗi` (Ví dụ: `bugfix/an-fix-scanner-crash`)

---

#### GIAI ĐOẠN 2: LẬP TRÌNH & COMMIT NỘI BỘ (LOCAL WORK)
Trong quá trình viết code, bạn nên chia nhỏ các commit ra theo từng phần việc hoàn thành, tránh gom toàn bộ code cả tuần vào 1 commit duy nhất.

```bash
# 1. Kiểm tra trạng thái thay đổi các file
git status

# 2. Đưa các file bạn đã viết xong vào khu vực chờ commit (Staging Area)
git add src/main/java/com/cdms/view/ShipperView.java

# 3. CHẠY THỬ NGHIỆM BIÊN DỊCH TRƯỚC KHI COMMIT (BẮT BUỘC)
# Điều này đảm bảo code của bạn không bị lỗi cú pháp làm crash cả hệ thống của nhóm
mvn clean compile

# 4. Nếu kết quả báo "BUILD SUCCESS", tiến hành tạo Commit cục bộ
git commit -m "feat: implement main menu layout and validation for shipper view"
```

> [!NOTE]
> **Quy chuẩn viết Commit Message (Conventional Commits):**
> * `feat: ...` ➡️ Khi thêm mới tính năng (Ví dụ: `feat: add input validation helper`)
> * `fix: ...` ➡️ Khi sửa lỗi logic hoặc cú pháp (Ví dụ: `fix: resolve null pointer in customer list`)
> * `docs: ...` ➡️ Khi chỉnh sửa tài liệu, README (Ví dụ: `docs: update maven install guide`)

---

#### GIAI ĐOẠN 3: ĐỒNG BỘ & ĐẨY CODE LÊN GITHUB (PULL & PUSH)

Đây là bước quan trọng nhất để đẩy code của bạn lên GitHub và chuẩn bị gộp vào nhánh chính `main`.

1. **Đồng bộ hóa với code mới nhất trên GitHub:**
   Trước khi đẩy code của mình lên, rất có thể các thành viên khác đã đẩy code của họ lên và được gộp vào `main`. Hãy kéo các thay đổi đó về nhánh của bạn trước:
   ```bash
   git pull origin main
   ```
   * **Nếu không có xung đột (Clean Merge):** Git sẽ tự động gộp và bạn có thể đi tiếp sang bước sau.
   * **Nếu bị Xung đột (Merge Conflict):** Git sẽ báo lỗi và đánh dấu các đoạn code trùng lặp trong file.
     * Mở file bị conflict lên (tìm các ký tự `<<<<<<<`, `=======`, `>>>>>>>`).
     * Trao đổi với đồng đội viết đoạn code đó để quyết định giữ lại code của ai hoặc kết hợp cả hai.
     * Xóa các ký tự đánh dấu của Git, lưu file lại và chạy lệnh xác nhận đã giải quyết:
       ```bash
       git add <file-vừa-sửa-xong>
       git commit -m "merge: resolve merge conflicts with main branch"
       ```

2. **Đẩy nhánh của bạn lên GitHub (Lệnh PUSH với cờ `-u`):**
   
   > [!IMPORTANT]
   > **Tầm quan trọng của cờ `-u` (hoặc `--set-upstream`):**
   > * Khi bạn đẩy một nhánh mới tạo ở máy cục bộ lên GitHub lần đầu tiên, nhánh đó chưa tồn tại trên máy chủ GitHub. Lệnh `git push` thông thường sẽ báo lỗi.
   > * Bạn **phải** sử dụng cờ `-u` để liên kết nhánh cục bộ của bạn với một nhánh cùng tên trên remote GitHub.
   > * **Cú pháp:**
   >   ```bash
   >   git push -u origin feature/ten-cua-ban-ten-chuc-nang
   >   ```
   > * **Lợi ích:** Sau khi đã chạy lệnh này một lần, Git đã thiết lập mối liên kết ngược. Kể từ lần đẩy code thứ 2 trở đi trên nhánh này, bạn **chỉ cần gõ duy nhất 2 từ**:
   >   ```bash
   >   git push
   >   ```
   >   Git sẽ tự hiểu cần phải đẩy lên đâu mà không cần gõ lại toàn bộ tên nhánh dài dòng!

---

<div id="git-pr"></div>

### 🛡️ Cách tạo Pull Request (PR) & Gộp code trên GitHub

Sau khi đẩy nhánh của bạn lên GitHub thành công, hãy thực hiện các bước sau để gộp code vào `main`:

1. Truy cập vào đường dẫn kho chứa của nhóm trên GitHub: [https://github.com/toilact/cdms_project15](https://github.com/toilact/cdms_project15).
2. Bạn sẽ thấy một thanh thông báo màu vàng có nút **Compare & pull request**, hãy bấm vào đó.
3. **Thiết lập PR:**
   * Kiểm tra xem nhánh đích có phải là `main` và nhánh nguồn là nhánh `feature/...` của bạn hay không.
   * Viết tiêu đề PR ngắn gọn và mô tả những gì bạn đã làm.
   * Gán (Assign) Leader hoặc các thành viên khác vào mục **Reviewers** để họ kiểm tra.
4. **Hợp nhất (Merge):**
   * Người Reviewer sẽ xem xét code của bạn, nếu không có lỗi và không có xung đột, họ sẽ bấm nút **Approve**.
   * Bấm **Merge pull request** để chính thức đưa code của bạn vào nhánh `main` chung của cả nhóm!

---

<div id="git-cheat-sheet"></div>

### 📑 Bảng tra cứu nhanh các lệnh Git cơ bản cho thành viên

| Lệnh Git | Ý nghĩa / Công dụng | Khi nào cần sử dụng? |
| :--- | :--- | :--- |
| `git clone <url>` | Tải toàn bộ dự án từ GitHub về máy tính lần đầu tiên | Khi mới tham gia dự án |
| `git checkout main` | Chuyển về làm việc trên nhánh chính | Khi cần cập nhật code mới hoặc tạo nhánh mới |
| `git pull origin main` | Kéo toàn bộ code mới nhất trên GitHub về máy | Đầu ngày làm việc hoặc trước khi tạo nhánh |
| `git checkout -b <nhánh>` | Tạo một nhánh mới và chuyển sang nhánh đó ngay | Khi bắt đầu làm một tính năng/sửa lỗi mới |
| `git status` | Xem trạng thái các file thay đổi, file nào chưa lưu | Sử dụng liên tục để kiểm soát những gì mình đang làm |
| `git add <tên_file>` | Đưa file vào khu vực chuẩn bị commit | Khi đã code xong một file và muốn lưu lại lịch sử |
| `git commit -m "<tin_nhắn>"` | Lưu lại trạng thái code cục bộ kèm ghi chú | Khi hoàn thành một phần việc nhỏ |
| `git push -u origin <nhánh>` | Đẩy nhánh mới lên GitHub lần đầu tiên và liên kết | Khi hoàn thành tính năng và muốn gửi lên GitHub |
| `git push` | Đẩy các thay đổi tiếp theo lên nhánh đã liên kết | Các lần đẩy tiếp theo sau khi đã dùng `-u` |
| `git diff` | Xem chi tiết từng dòng code thay đổi so với bản cũ | Khi muốn kiểm tra lại code trước khi add/commit |

---

> [!TIP]
> **Giải quyết lỗi Authentication khi Push (GitHub Token):**
> Hiện nay GitHub đã loại bỏ hình thức xác thực bằng mật khẩu thông thường khi bạn đẩy code qua Terminal. Nếu gặp thông báo yêu cầu mật khẩu và báo lỗi thất bại, hãy làm theo hướng dẫn:
> 1. Truy cập GitHub cá nhân ➡️ **Settings** ➡️ **Developer Settings** ➡️ **Personal Access Tokens (Tokens classic)**.
> 2. Chọn **Generate new token (classic)**, tích chọn quyền `repo` và nhấn Generate.
> 3. Sao chép đoạn mã Token nhận được (dạng `ghp_...`).
> 4. Khi Terminal hỏi Password, hãy **dán đoạn Token này vào** thay cho mật khẩu thông thường của bạn.
DO CHI THANH