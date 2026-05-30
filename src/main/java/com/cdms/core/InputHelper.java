// ============================================================
// File: InputHelper.java
// Package: com.cdms.core
// Description: Bộ tiện ích nhập liệu từ bàn phím an toàn.
//              Tất cả phương thức đều là static và có cơ chế
//              try-catch chống crash chương trình khi nhập sai.
// Phân công: Đỗ Chí Thành (Leader - Thành viên 1)
// ============================================================
package com.cdms.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    /** Scanner dùng chung cho toàn bộ ứng dụng */
    public static final Scanner scanner = new Scanner(System.in);

    /** Định dạng ngày tháng chuẩn: DD/MM/YYYY */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ===== Ngăn khởi tạo đối tượng (Utility class) =====
    private InputHelper() {
    }

    // ---------------------------------------------------------
    // 1. NHẬP SỐ NGUYÊN (trong khoảng [min, max])
    // ---------------------------------------------------------

    /**
     * Yêu cầu người dùng nhập một số nguyên trong khoảng [min, max].
     * Nếu nhập sai định dạng hoặc ngoài khoảng, bắt nhập lại.
     *
     * @param prompt Thông báo hiển thị
     * @param min    Giá trị nhỏ nhất cho phép
     * @param max    Giá trị lớn nhất cho phép
     * @return Số nguyên hợp lệ
     */
    public static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("cancel")) {
                    throw new FormCancelledException();
                }
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  ⚠ Vui lòng nhập số từ %d đến %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Dữ liệu không hợp lệ. Vui lòng nhập một số nguyên.");
            }
        }
    }

    // ---------------------------------------------------------
    // 2. NHẬP SỐ THỰC (lớn hơn min)
    // ---------------------------------------------------------

    /**
     * Yêu cầu người dùng nhập một số thực lớn hơn giá trị min.
     *
     * @param prompt Thông báo hiển thị
     * @param min    Giá trị tối thiểu (không bao gồm)
     * @return Số thực hợp lệ
     */
    public static double getDoubleInput(String prompt, double min) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("cancel")) {
                    throw new FormCancelledException();
                }
                double value = Double.parseDouble(line);
                if (value > min) {
                    return value;
                }
                System.out.printf("  ⚠ Vui lòng nhập số lớn hơn %.1f.%n", min);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Dữ liệu không hợp lệ. Vui lòng nhập một số.");
            }
        }
    }

    /**
     * Yêu cầu người dùng nhập một số thực trong khoảng [min, max].
     *
     * @param prompt Thông báo hiển thị
     * @param min    Giá trị tối thiểu (bao gồm)
     * @param max    Giá trị tối đa (bao gồm)
     * @return Số thực hợp lệ
     */
    public static double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("cancel")) {
                    throw new FormCancelledException();
                }
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value;
                }
                if (max == Double.MAX_VALUE) {
                    System.out.printf("  ⚠ Vui lòng nhập số >= %.1f.%n", min);
                } else {
                    System.out.printf("  ⚠ Vui lòng nhập số từ %.1f đến %.1f.%n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Dữ liệu không hợp lệ. Vui lòng nhập một số.");
            }
        }
    }

    // ---------------------------------------------------------
    // 3. NHẬP CHUỖI (không cho phép trống)
    // ---------------------------------------------------------

    /**
     * Yêu cầu người dùng nhập một chuỗi không được để trống.
     *
     * @param prompt Thông báo hiển thị
     * @return Chuỗi không rỗng đã được trim
     */
    public static String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("cancel")) {
                throw new FormCancelledException();
            }
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("  ⚠ Trường này không được để trống. Vui lòng nhập lại.");
        }
    }

    /**
     * Yêu cầu nhập họ tên hợp lệ (không được là số thuần túy).
     *
     * @param prompt Thông báo hiển thị
     * @return Họ tên hợp lệ
     */
    public static String getValidNameInput(String prompt) {
        while (true) {
            String name = getStringInput(prompt);
            if (name.matches("\\d+")) {
                System.out.println("  ⚠ Lỗi: Họ tên không được phép là số! Vui lòng nhập lại.");
                continue;
            }
            return name;
        }
    }

    /**
     * Yêu cầu nhập số điện thoại hợp lệ (phải gồm 9-11 chữ số).
     *
     * @param prompt Thông báo hiển thị
     * @return Số điện thoại hợp lệ
     */
    public static String getPhoneInput(String prompt) {
        while (true) {
            String phone = getStringInput(prompt);
            if (!phone.matches("\\d{9,11}")) {
                System.out.println("  ⚠ Lỗi: Số điện thoại không hợp lệ (phải gồm 9-11 chữ số)! Vui lòng nhập lại.");
                continue;
            }
            return phone;
        }
    }

    /**
     * Yêu cầu nhập số điện thoại hợp lệ và duy nhất (kiểm tra qua Predicate).
     *
     * @param prompt              Thông báo hiển thị
     * @param uniquenessValidator Hàm kiểm tra tính duy nhất (trả về true nếu hợp
     *                            lệ/duy nhất, false nếu trùng)
     * @param errorMessage        Thông báo lỗi khi bị trùng
     * @return Số điện thoại hợp lệ và duy nhất
     */
    public static String getPhoneInput(String prompt, java.util.function.Predicate<String> uniquenessValidator,
            String errorMessage) {
        while (true) {
            String phone = getPhoneInput(prompt);
            if (uniquenessValidator.test(phone)) {
                return phone;
            }
            System.out.println("  ⚠ Lỗi: " + errorMessage + " Vui lòng nhập lại.");
        }
    }

    /**
     * Nhập chuỗi và kiểm tra tính hợp lệ qua một Predicate tùy chọn ngay lập tức.
     *
     * @param prompt       Thông báo hiển thị
     * @param validator    Hàm kiểm tra tính hợp lệ
     * @param errorMessage Thông báo lỗi khi không hợp lệ
     * @return Chuỗi hợp lệ
     */
    public static String getStringInput(String prompt, java.util.function.Predicate<String> validator,
            String errorMessage) {
        while (true) {
            String val = getStringInput(prompt);
            if (validator.test(val)) {
                return val;
            }
            System.out.println("  ⚠ Lỗi: " + errorMessage + " Vui lòng nhập lại.");
        }
    }

    /**
     * Nhập số thực và kiểm tra tính hợp lệ qua DoublePredicate tùy chọn ngay lập
     * tức.
     *
     * @param prompt       Thông báo hiển thị
     * @param min          Giá trị tối thiểu
     * @param validator    Hàm kiểm tra tính hợp lệ
     * @param errorMessage Thông báo lỗi khi không hợp lệ
     * @return Số thực hợp lệ
     */
    public static double getDoubleInput(String prompt, double min, java.util.function.DoublePredicate validator,
            String errorMessage) {
        while (true) {
            double val = getDoubleInput(prompt, min);
            if (validator.test(val)) {
                return val;
            }
            System.out.println("  ⚠ Lỗi: " + errorMessage + " Vui lòng nhập lại.");
        }
    }

    // ---------------------------------------------------------
    // 4. NHẬP CHUỖI TÙY CHỌN (cho phép để trống)
    // ---------------------------------------------------------

    /**
     * Nhập chuỗi tùy chọn — cho phép người dùng nhấn Enter để bỏ qua.
     * Trả về chuỗi đã trim, có thể là rỗng "".
     * Dùng trong chức năng cập nhật thông tin để giữ nguyên giá trị cũ nếu không
     * nhập.
     *
     * @param prompt Thông báo hiển thị
     * @return Chuỗi đã trim (có thể rỗng nếu người dùng chỉ nhấn Enter)
     */
    public static String getOptionalStringInput(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        if (line.equalsIgnoreCase("cancel")) {
            throw new FormCancelledException();
        }
        return line;
    }

    /**
     * Nhập chuỗi tùy chọn và kiểm tra tính hợp lệ nếu người dùng không để trống.
     *
     * @param prompt       Thông báo hiển thị
     * @param validator    Hàm kiểm tra tính hợp lệ
     * @param errorMessage Thông báo lỗi khi không hợp lệ
     * @return Chuỗi hợp lệ hoặc rỗng
     */
    public static String getOptionalValidatedStringInput(String prompt, java.util.function.Predicate<String> validator,
            String errorMessage) {
        while (true) {
            String val = getOptionalStringInput(prompt);
            if (val.isEmpty()) {
                return val;
            }
            if (validator.test(val)) {
                return val;
            }
            System.out.println("  ⚠ Lỗi: " + errorMessage + " Vui lòng nhập lại.");
        }
    }

    /**
     * Nhập số điện thoại tùy chọn (cho phép Enter bỏ qua), nếu nhập thì phải hợp lệ
     * định dạng và duy nhất.
     *
     * @param prompt              Thông báo hiển thị
     * @param uniquenessValidator Hàm kiểm tra tính duy nhất (trả về true nếu hợp
     *                            lệ/duy nhất, false nếu trùng)
     * @param errorMessage        Thông báo lỗi khi bị trùng
     * @return Số điện thoại hợp lệ hoặc rỗng
     */
    public static String getOptionalPhoneInput(String prompt, java.util.function.Predicate<String> uniquenessValidator,
            String errorMessage) {
        while (true) {
            String phone = getOptionalStringInput(prompt);
            if (phone.isEmpty()) {
                return phone;
            }
            if (!phone.matches("\\d{9,11}")) {
                System.out.println("  ⚠ Lỗi: Số điện thoại không hợp lệ (phải gồm 9-11 chữ số)! Vui lòng nhập lại.");
                continue;
            }
            if (uniquenessValidator.test(phone)) {
                return phone;
            }
            System.out.println("  ⚠ Lỗi: " + errorMessage + " Vui lòng nhập lại.");
        }
    }

    // ---------------------------------------------------------
    // 5. NHẬP NGÀY THÁNG (định dạng DD/MM/YYYY)
    // ---------------------------------------------------------

    /**
     * Yêu cầu người dùng nhập ngày tháng theo định dạng DD/MM/YYYY.
     * Nếu allowBlank = true, cho phép bấm Enter để bỏ qua (trả về null).
     *
     * @param prompt     Thông báo hiển thị
     * @param allowBlank Cho phép bỏ trống hay không
     * @return LocalDate hợp lệ hoặc null nếu bỏ trống
     */
    public static LocalDate getDateInput(String prompt, boolean allowBlank) {
        while (true) {
            try {
                System.out.print(prompt + (allowBlank ? " (Enter để bỏ qua): " : ": "));
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("cancel")) {
                    throw new FormCancelledException();
                }

                // Cho phép bỏ trống
                if (line.isEmpty() && allowBlank) {
                    return null;
                }

                if (line.isEmpty()) {
                    System.out.println("  ⚠ Trường này không được để trống.");
                    continue;
                }

                return LocalDate.parse(line, DATE_FORMAT);

            } catch (DateTimeParseException e) {
                System.out.println("  ⚠ Sai định dạng ngày. Vui lòng nhập theo dạng DD/MM/YYYY (VD: 25/12/2025).");
            }
        }
    }

    // ---------------------------------------------------------
    // 5. TIỆN ÍCH: Tạm dừng chờ Enter
    // ---------------------------------------------------------

    /**
     * Tạm dừng màn hình và chờ người dùng bấm Enter để tiếp tục.
     */
    public static void pressEnterToContinue() {
        System.out.print("\nBấm Enter để tiếp tục...");
        scanner.nextLine();
    }

    // ---------------------------------------------------------
    // 6. TIỆN ÍCH: Hiển thị phân trang danh sách bất kỳ (UX-13)
    // ---------------------------------------------------------

    /**
     * Hiển thị phân trang cho danh sách bất kỳ kèm đường kẻ phân cách tùy chọn.
     *
     * @param list Danh sách cần hiển thị
     * @param pageSize Số lượng dòng hiển thị trên một trang
     * @param separator Đường kẻ bảng phân cách (ví dụ: "+---+"), có thể null
     */
    public static <T> void printPaginatedList(java.util.List<T> list, int pageSize, String separator) {
        if (list == null || list.isEmpty()) {
            System.out.println("  (Chưa có dữ liệu nào phù hợp.)");
            return;
        }
        int count = 0;
        for (T item : list) {
            System.out.println(item);
            count++;
            if (count % pageSize == 0 && count < list.size()) {
                if (separator != null && !separator.isEmpty()) {
                    System.out.println(separator);
                }
                System.out.print("\u001B[1;33m👉 Nhấn Enter để xem tiếp (" + count + "/" + list.size() + ") hoặc nhập 'q' để dừng: \u001B[0m");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    System.out.println("  🛑 Đã dừng hiển thị.");
                    break;
                }
                if (separator != null && !separator.isEmpty()) {
                    System.out.println(separator);
                }
            }
        }
        if (separator != null && !separator.isEmpty() && (count == list.size() || count % pageSize != 0)) {
            System.out.println(separator);
        }
    }
}
