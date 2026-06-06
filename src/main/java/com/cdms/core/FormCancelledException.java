// ============================================================
// File: FormCancelledException.java
// Package: com.cdms.core
// Description: Ngoại lệ ném ra khi người dùng gõ "cancel" để
//              thoát giữa chừng khỏi bất kỳ form nhập liệu nào.
// Phân công: Đỗ Chí Thành (Leader - Thành viên 1)
// ============================================================
package com.cdms.core;

public class FormCancelledException extends RuntimeException {
    public FormCancelledException() {
        super("Người dùng đã hủy thao tác nhập liệu.");
    }
}
