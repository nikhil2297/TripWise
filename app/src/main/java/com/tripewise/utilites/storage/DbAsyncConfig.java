package com.tripewise.utilites.storage;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;

public class DbAsyncConfig {
    private TripStorage storage;

    public DbAsyncConfig(Context context){
        if (storage == null){
            storage = TripStorage.getDataBaseInstance(context);
        }
    }

    public <T> LiveData<T> getObservableData(Object... objects){
        if (objects[0].equals(TripData.class.getSimpleName())){
            return (LiveData<T>) storage.tripDao().getAllData();
        }else if (objects[0].equals(PersonData.class.getSimpleName())){
            return (LiveData<T>) storage.personDao().getAllData((Integer) objects[1]);
        }else if (objects[0].equals(BillData.class.getSimpleName())){
            return (LiveData<T>) storage.billDao().getAllData((Integer) objects[1]);
        }

        return null;
    }
}
