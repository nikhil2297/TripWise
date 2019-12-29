package com.tripewise.utilites.storage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tripewise.utilites.storage.dao.TripDao;
import com.tripewise.utilites.storage.data.TripData;

@Database(entities = {TripData.class}, version = 1, exportSchema = false)
public abstract class TripStorage extends RoomDatabase {

    private static TripStorage INSTANCE;

    public abstract TripDao tripDao();

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
