// ============================================================
// File: ParcelRepository.java
// Package: com.cdms.repository
// Description: Repository quản lý truy vấn Parcel.
// ============================================================
package com.cdms.repository;

import com.cdms.core.JSONDataManager;
import com.cdms.model.Parcel;

public class ParcelRepository {

    private ParcelRepository() {
    }

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
}
