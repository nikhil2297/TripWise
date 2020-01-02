package com.tripewise.utilites.storage.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.TripData;

import java.util.concurrent.ExecutionException;

/*
 * Will not be using this class for a while
 * */

public class DatabaseAsyncConfig {
    private static TripStorage tripStorage;

    public DatabaseAsyncConfig(Context context) {
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
}
