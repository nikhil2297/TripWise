package com.tripewise.utilites.storage.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.List;

@Dao
public interface BillDao {
    @Query("Select * from BillData WHERE trip_id = :tripId")
    LiveData<List<BillData>> getAllData(int tripId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBilldata(BillData data);

    @Update
    long updateBillDataData(BillData data);
}
