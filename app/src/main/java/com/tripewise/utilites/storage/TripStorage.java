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

import com.tripewise.utilites.storage.dao.TripDao;
import com.tripewise.utilites.storage.data.TripData;

import java.util.concurrent.ExecutionException;

@Database(entities = {TripData.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
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

    public long insertTripData(TripData tripData) throws ExecutionException, InterruptedException {
        long id = new AddTripDataAsync(tripData).execute().get();

        if (id > 0) {
            Log.d("DatabaseConfigAsync : ", "Trip added to databse");
            return id;
        } else {
            Log.d("DatabaseConfigAsync : ", "Adding to database failed");
            return 0;
        }
    }

    private static class AddTripDataAsync extends AsyncTask<Void, Void, Long> {

        private TripData tripData;

        public AddTripDataAsync(TripData tripData) {
            this.tripData = tripData;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return INSTANCE.tripDao().addTrip(tripData);
        }
    }

    public int updateBillCount(int billCount, int tripId) throws ExecutionException, InterruptedException {
        int id = new UpdateTripBillCount(billCount, tripId).execute().get();

        if (id > 0) {
            Log.d("DatabaseConfigAsync : ", "Bill Count updated in database");
            return id;
        } else {
            Log.d("DatabaseConfigAsync : ", "TripId not found");
            return 0;
        }
    }

    private static class UpdateTripBillCount extends AsyncTask<Void, Void, Integer> {
        private int billCount;
        private int id;

        UpdateTripBillCount(int billCount, int id) {
            this.billCount = billCount;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return INSTANCE.tripDao().updateBillCount(billCount, id);
        }
    }

    public int deleteTrip(int tripId) throws ExecutionException, InterruptedException {
        int id = new DeleteTripEntry(tripId).execute().get();

        if (id > 0) {
            Log.d("DatabaseConfigAsync : ", "Trip deleted from database");
            return id;
        } else {
            Log.d("DatabaseConfigAsync : ", "TripId not found");
            return 0;
        }
    }

    private static class DeleteTripEntry extends AsyncTask<Void, Void, Integer> {
        private int id;

        DeleteTripEntry(int id) {
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return INSTANCE.tripDao().deleteTripEntry(id);
        }
    }
}
