package com.tripewise.utilites.storage.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tripewise.utilites.storage.data.PersonData;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("Select * from PersonData")
    LiveData<List<PersonData>> getAllData();

    @Update
    int updatePersonData(PersonData data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    int insertPersondata(PersonData data);
}
