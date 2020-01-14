package com.tripewise.utilites.storage.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;
import java.util.concurrent.ExecutionException;

/*
 * Will not be using this class for a while
 * */

public class TripAsyncConfig {
    private static TripStorage tripStorage;

    private TripConfigListener listener;

    private Context context;

    public TripAsyncConfig(Context context) {
        this.context = context;

        if (tripStorage == null) {
            tripStorage = TripStorage.getDataBaseInstance(context);
        }
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
            return tripStorage.tripDao().addTrip(tripData);
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
            return tripStorage.tripDao().updateBillCount(billCount, id);
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
            return tripStorage.tripDao().deleteTripEntry(id);
        }
    }

    public void onTripDataChange(final TripConfigListener listener) {
        this.listener = listener;

        tripStorage.tripDao().getAllData().observe((LifecycleOwner) context, new Observer<List<TripData>>() {
            @Override
            public void onChanged(List<TripData> tripData) {
                listener.onDataChange(tripData);
            }
        });
    }

    public interface TripConfigListener {

        void onDataChange(List<TripData> tripData);
    }
}
