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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addTrip(TripData tripData);
}
