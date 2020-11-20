package com.icoding.warranty.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface WarrantyDataDao {

    @Query("SELECT * FROM Warrenties")
    Flowable<List<WarrantyData>> getWarranties();

    @Query("SELECT * FROM Warrenties WHERE warrentyId IN(:warrentyId)")
    Flowable<List<WarrantyData>> getSingleWarrenty(int warrentyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWarranty(ArrayList<WarrantyData> warranty);

    @Delete
    Single<Integer> deleteWarranty(WarrantyData warranty);


}
