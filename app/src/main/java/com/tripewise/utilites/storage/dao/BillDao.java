package com.tripewise.utilites.storage.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BillDao {
    @Query("Select * from BillData WHERE trip_id = :tripId")
    LiveData<List<BillData>> getAllData(int tripId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBilldata(BillData data);

    @Query("Update BillData Set bill_name = :billName, bill_amount = :billAmount, bill_paid_people = :billPaidPeopleList, bill_people = :billPeopleList where id = :billId")
    int updateBillDataData(int billId, String billName, long billAmount, ArrayList<BillData.BillPeople> billPaidPeopleList, ArrayList<BillData.BillPeople> billPeopleList);

    @Query("Delete from BillData where id = :billId")
    int deleteBillData(int billId);
}
