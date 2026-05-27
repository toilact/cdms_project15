// ============================================================
// File: LocalDateAdapter.java
// Package: com.cdms.core
// Description: Custom Gson TypeAdapter cho java.time.LocalDate.
//              Gson mặc định không hỗ trợ Java 8+ Date/Time API,
//              nên cần adapter riêng để serialize/deserialize.
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

    /** Định dạng lưu trữ ngày trong file JSON: yyyy-MM-dd (ISO standard) */
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
