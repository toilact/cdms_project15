// ============================================================
// File: InputHelper.java
// Package: com.cdms.core
// Description: Bộ tiện ích nhập liệu từ bàn phím an toàn.
//              Tất cả phương thức đều là static và có cơ chế
//              try-catch chống crash chương trình khi nhập sai.
// ============================================================
package com.cdms.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    /** Scanner dùng chung cho toàn bộ ứng dụng */
    private static final Scanner scanner = new Scanner(System.in);

    /** Định dạng ngày tháng chuẩn: DD/MM/YYYY */
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("  ⚠ Trường này không được để trống. Vui lòng nhập lại.");
        }
    }

    // ---------------------------------------------------------
    // 4. NHẬP NGÀY THÁNG (định dạng DD/MM/YYYY)
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
}
