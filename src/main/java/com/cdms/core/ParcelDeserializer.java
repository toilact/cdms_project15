// ============================================================
// File: ParcelDeserializer.java
// Package: com.cdms.core
// Description: Custom JsonDeserializer cho lớp trừu tượng Parcel.
//              Gson không thể tự động deserialize một abstract class.
//              Lớp này đọc thuộc tính "type" trong JSON để quyết định
//              tạo đối tượng DocumentParcel hay GoodsParcel.
// ============================================================
package com.cdms.core;

import com.cdms.model.DocumentParcel;
import com.cdms.model.GoodsParcel;
import com.cdms.model.Parcel;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ParcelDeserializer implements JsonDeserializer<Parcel> {

    /**
     * Deserialize một phần tử JSON thành đối tượng Parcel cụ thể.
     * Dựa trên giá trị trường "type":
     *   - "Document" → DocumentParcel
     *   - "Goods"    → GoodsParcel
     *
     * @param json    Phần tử JSON gốc
     * @param typeOfT Kiểu Java mục tiêu (Parcel)
     * @param context Context Gson để ủy quyền deserialize
     * @return Đối tượng DocumentParcel hoặc GoodsParcel
     * @throws JsonParseException Nếu type không hợp lệ
     */
    @Override
    public Parcel deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        // Đọc trường "type" để xác định lớp con cụ thể
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
