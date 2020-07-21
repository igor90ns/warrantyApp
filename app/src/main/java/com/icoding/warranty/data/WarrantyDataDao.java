package com.icoding.warranty.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WarrantyDataDao {

    @Query("SELECT * FROM Warrenties")
    Flowable<List<WarrantyData>> getWarranties();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWarranty(ArrayList<WarrantyData> warranty);


}
