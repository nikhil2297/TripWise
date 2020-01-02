package com.tripewise.utilites.storage.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

@Dao
public interface TripDao {
    @Query("Select * from TripData")
    LiveData<List<TripData>> getAllData();

    @Query("Update TripData Set bill_count = :billCount where id = :id")
    int updateBillCount(int billCount, int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addTrip(TripData tripData);

    @Query("Delete from TripData where id = :id")
    int deleteTripEntry(int id);
}
