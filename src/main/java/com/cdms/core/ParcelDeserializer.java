// ============================================================
// File: ParcelDeserializer.java
// Package: com.cdms.core
// Description: JsonDeserializer tùy chỉnh cho lớp abstract Parcel.
//              Đọc trường "type" trong JSON để tạo đúng lớp con:
//              "Document" → DocumentParcel, "Goods" → GoodsParcel.
// ============================================================
package com.cdms.core;

import com.cdms.model.DocumentParcel;
import com.cdms.model.GoodsParcel;
import com.cdms.model.Parcel;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ParcelDeserializer implements JsonDeserializer<Parcel> {

    /** Đọc trường "type" rồi tạo DocumentParcel hoặc GoodsParcel tương ứng. */
    @Override
    public Parcel deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        // Đọc trường "type" để xác định lớp con cần tạo
        String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "";

        switch (type) {
            case "Document":
                return context.deserialize(jsonObject, DocumentParcel.class);
            case "Goods":
                return context.deserialize(jsonObject, GoodsParcel.class);
            default:
                throw new JsonParseException(
                        "Không thể nhận diện loại kiện hàng (Parcel type): '" + type + "'. "
                        + "Giá trị hợp lệ: 'Document' hoặc 'Goods'.");
        }
    }
}
