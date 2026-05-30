// ============================================================
// File: FormCancelledException.java
// Package: com.cdms.core
// Description: Ngoại lệ Runtime dùng để báo hiệu người dùng chủ động
//              hủy bỏ biểu mẫu nhập liệu nửa chừng bằng cách gõ 'cancel'.
// Phân công: Đỗ Chí Thành (Leader - Thành viên 1)
// ============================================================
package com.cdms.core;

public class FormCancelledException extends RuntimeException {
    public FormCancelledException() {
        super("Người dùng đã hủy thao tác nhập liệu.");
    }
}
