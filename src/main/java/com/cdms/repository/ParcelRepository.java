// ============================================================
// File: ParcelRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý truy vấn Parcel.
// ============================================================
package com.cdms.repository;

import com.cdms.core.JSONDataManager;
import com.cdms.model.Parcel;

import java.util.ArrayList;
import java.util.List;

public class ParcelRepository {

    private ParcelRepository() {
    }

    /**
     * Tìm kiện hàng theo ID
     */
    public static Parcel findById(String parcelId) {
        if (parcelId == null) {
            return null;
        }
        for (Parcel parcel : JSONDataManager.parcels) {
            if (parcelId.equalsIgnoreCase(parcel.getId())) {
                return parcel;
            }
        }
        return null;
    }

    /**
     * Lấy toàn bộ kiện hàng
     */
    public static List<Parcel> findAll() {
        return new ArrayList<>(JSONDataManager.parcels);
    }

    /**
     * Thêm kiện hàng mới
     */
    public static void add(Parcel parcel) {
        if (parcel != null) {
            JSONDataManager.parcels.add(parcel);
            JSONDataManager.saveAllData();
        }
    }

    /**
     * Kiểm tra kiện hàng có tồn tại theo ID hay không
     */
    public static boolean existsById(String id) {
        if (id == null) {
            return false;
        }
        return JSONDataManager.parcels.stream()
                .anyMatch(p -> id.equalsIgnoreCase(p.getId()));
    }

    /**
     * Cập nhật thông tin kiện hàng
     */
    public static boolean update(Parcel updated) {
        if (updated == null || updated.getId() == null) {
            return false;
        }
        for (int i = 0; i < JSONDataManager.parcels.size(); i++) {
            if (JSONDataManager.parcels.get(i).getId().equalsIgnoreCase(updated.getId())) {
                JSONDataManager.parcels.set(i, updated);
                JSONDataManager.saveAllData();
                return true;
            }
        }
        return false;
    }

    /**
     * Xóa bưu kiện theo ID
     */
    public static boolean delete(String parcelId) {
        if (parcelId == null) {
            return false;
        }
        boolean removed = JSONDataManager.parcels.removeIf(p -> parcelId.equalsIgnoreCase(p.getId()));
        if (removed) {
            JSONDataManager.saveAllData();
        }
        return removed;
    }
}
