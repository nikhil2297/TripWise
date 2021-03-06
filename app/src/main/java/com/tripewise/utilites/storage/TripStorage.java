package com.tripewise.utilites.storage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tripewise.utilites.storage.dao.BillDao;
import com.tripewise.utilites.storage.dao.PersonDao;
import com.tripewise.utilites.storage.dao.TripDao;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;

import java.util.concurrent.ExecutionException;

@Database(entities = {TripData.class, PersonData.class, BillData.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TripStorage extends RoomDatabase {

    private static TripStorage INSTANCE;

    public abstract TripDao tripDao();

    public abstract PersonDao personDao();

    public abstract BillDao billDao();

    public static TripStorage getDataBaseInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TripStorage.class) {
                INSTANCE = Room.databaseBuilder(context, TripStorage.class, "trip_db")
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                            }
                        }).build();
            }
        }

        return INSTANCE;
    }
}
