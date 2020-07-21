package com.icoding.warranty.data;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;

    @Database(entities = WarrantyData.class, version = 1, exportSchema = false)
    public abstract class WarrantyDataDatabase extends RoomDatabase {
        public abstract WarrantyDataDao getWarrantyDataDao();
}
