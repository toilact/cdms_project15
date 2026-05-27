// ============================================================
// File: JSONDataManager.java
// Package: com.cdms.core
// Description: Quản lý toàn bộ dữ liệu in-memory (RAM) và
//              đồng bộ xuống/lên file JSON trong thư mục data/.
//              Đây là "trái tim" lưu trữ của hệ thống CDMS.
// ============================================================
package com.cdms.core;

import com.cdms.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JSONDataManager {

    // ===== THƯ MỤC LƯU TRỮ DỮ LIỆU =====
    private static final String DATA_DIR = "data";

    // ===== TÊN CÁC FILE JSON =====
    private static final String CUSTOMERS_FILE   = DATA_DIR + "/customers.json";
    private static final String PARCELS_FILE     = DATA_DIR + "/parcels.json";
    private static final String ORDERS_FILE      = DATA_DIR + "/orders.json";
    private static final String STAFFS_FILE      = DATA_DIR + "/staffs.json";
    private static final String INVOICES_FILE    = DATA_DIR + "/invoices.json";

    // ===== DỮ LIỆU IN-MEMORY (RAM) =====
    public static List<Customer>      customers = new ArrayList<>();
    public static List<Parcel>        parcels   = new ArrayList<>();
    public static List<DeliveryOrder> orders    = new ArrayList<>();
    public static List<DeliveryStaff> staffs    = new ArrayList<>();
    public static List<Invoice>       invoices  = new ArrayList<>();

    // ===== GSON INSTANCE =====
    /** 
     * Cấu hình Gson:
     * - PrettyPrinting: File JSON dễ đọc cho dev.
     * - LocalDateAdapter: Xử lý Java 8+ LocalDate.
     * - ParcelDeserializer: Xử lý abstract class Parcel → DocumentParcel / GoodsParcel.
     */
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(Parcel.class, new ParcelDeserializer())
            .create();

    // ===== Ngăn khởi tạo đối tượng (Utility class) =====
    private JSONDataManager() {
    }

    // ==========================================================
    //                   ĐỌC DỮ LIỆU (LOAD)
    // ==========================================================

    /**
     * Nạp toàn bộ dữ liệu từ các file JSON trong thư mục data/ vào RAM.
     * Nếu thư mục data/ chưa tồn tại, tự động tạo mới.
     * Nếu file JSON chưa tồn tại hoặc rỗng, khởi tạo List rỗng.
     */
    public static void loadAllData() {
        ensureDataDirectory();

        System.out.println("📂 Đang nạp dữ liệu từ thư mục '" + DATA_DIR + "/'...");

        customers = loadList(CUSTOMERS_FILE,
                new TypeToken<List<Customer>>() {}.getType());

        parcels = loadList(PARCELS_FILE,
                new TypeToken<List<Parcel>>() {}.getType());

        orders = loadList(ORDERS_FILE,
                new TypeToken<List<DeliveryOrder>>() {}.getType());

        staffs = loadList(STAFFS_FILE,
                new TypeToken<List<DeliveryStaff>>() {}.getType());

        invoices = loadList(INVOICES_FILE,
                new TypeToken<List<Invoice>>() {}.getType());

        System.out.printf("   ✅ Khách hàng : %d bản ghi%n", customers.size());
        System.out.printf("   ✅ Kiện hàng  : %d bản ghi%n", parcels.size());
        System.out.printf("   ✅ Đơn hàng   : %d bản ghi%n", orders.size());
        System.out.printf("   ✅ Shipper    : %d bản ghi%n", staffs.size());
        System.out.printf("   ✅ Hóa đơn    : %d bản ghi%n", invoices.size());
        System.out.println("📂 Nạp dữ liệu hoàn tất!\n");
    }

    /**
     * Đọc một file JSON và chuyển đổi thành List<T>.
     *
     * @param filePath Đường dẫn file JSON
     * @param type     Kiểu TypeToken cho Gson
     * @param <T>      Kiểu phần tử trong List
     * @return List đối tượng đã deserialize, hoặc List rỗng nếu file không tồn tại
     */
    private static <T> List<T> loadList(String filePath, Type type) {
        Path path = Paths.get(filePath);

        // File chưa tồn tại → trả về List rỗng
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8)) {

            List<T> result = gson.fromJson(reader, type);
            return (result != null) ? result : new ArrayList<>();

        } catch (Exception e) {
            System.out.println("  ⚠ Lỗi khi đọc file " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==========================================================
    //                   GHI DỮ LIỆU (SAVE)
    // ==========================================================

    /**
     * Ghi đè toàn bộ dữ liệu in-memory hiện tại xuống các file JSON.
     * Được gọi mỗi khi có thay đổi dữ liệu hoặc khi thoát ứng dụng.
     */
    public static void saveAllData() {
        ensureDataDirectory();

        saveList(CUSTOMERS_FILE, customers);
        saveList(PARCELS_FILE, parcels);
        saveList(ORDERS_FILE, orders);
        saveList(STAFFS_FILE, staffs);
        saveList(INVOICES_FILE, invoices);
    }

    /**
     * Ghi một List<T> xuống file JSON (ghi đè toàn bộ nội dung cũ).
     *
     * @param filePath Đường dẫn file JSON
     * @param list     Danh sách đối tượng cần lưu
     * @param <T>      Kiểu phần tử trong List
     */
    private static <T> void saveList(String filePath, List<T> list) {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8)) {

            gson.toJson(list, writer);

        } catch (IOException e) {
            System.out.println("  ⚠ Lỗi khi ghi file " + filePath + ": " + e.getMessage());
        }
    }

    // ==========================================================
    //                   TIỆN ÍCH NỘI BỘ
    // ==========================================================

    /**
     * Kiểm tra và tạo thư mục data/ nếu chưa tồn tại.
     */
    private static void ensureDataDirectory() {
        Path dirPath = Paths.get(DATA_DIR);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
                System.out.println("📁 Đã tạo thư mục lưu trữ: " + DATA_DIR + "/");
            } catch (IOException e) {
                System.out.println("  ⚠ Không thể tạo thư mục " + DATA_DIR + ": " + e.getMessage());
            }
        }
    }
}
