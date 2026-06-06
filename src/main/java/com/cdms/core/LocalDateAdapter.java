// ============================================================
// File: LocalDateAdapter.java
// Package: com.cdms.core
// Description: TypeAdapter cho Gson để đọc/ghi LocalDate dạng
//              chuỗi "yyyy-MM-dd" (Gson không tự hỗ trợ Java 8+).
// ============================================================
package com.cdms.core;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    // Định dạng ISO: yyyy-MM-dd (ví dụ: 2025-12-31)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(FORMATTER));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String dateStr = in.nextString();
        return LocalDate.parse(dateStr, FORMATTER);
    }
}
