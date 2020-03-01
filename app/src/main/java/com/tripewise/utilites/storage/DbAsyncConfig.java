package com.tripewise.utilites.storage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DbAsyncConfig {
    private static final String TAG = DbAsyncConfig.class.getSimpleName();
    private static TripStorage storage;

    public DbAsyncConfig(Context context) {
        if (storage == null) {
            storage = TripStorage.getDataBaseInstance(context);
        }
    }

    /**
     * Used for all the select queries
     *
     * @param objects it is an list of object
     * @return LiveData<Any Object type>
     */

    public LiveData<?> getObservableData(Object... objects) {
        if (objects[0].equals(TripData.class.getSimpleName())) {
            return storage.tripDao().getAllData();
        } else if (objects[0].equals(PersonData.class.getSimpleName())) {
            return storage.personDao().getAllData((Integer) objects[1]);
        } else if (objects[0].equals(BillData.class.getSimpleName())) {
            return (storage.billDao().getAllData((Integer) objects[1]));
        }

        return null;
    }

    public long insertDataToDb(Object... objects) throws ExecutionException, InterruptedException {
        //Default id
        long rowId = -1;

        rowId = new InsertDbOperation().execute(objects).get();

        if (rowId > 0){
            Log.e(TAG, "InsertDbOperation : " + "Inserted new data in table " + objects[0].getClass().getSimpleName());
        }else {
            Log.e(TAG, "InsertDbOperation : " + "Error in inserting new data in table " + objects[0].getClass().getSimpleName());
        }

        return rowId;
    }

    private long insertPersonDataList(List<PersonData> data) throws ExecutionException, InterruptedException {
        long rowId =-1;
        for (PersonData personData : data){
            rowId = new InsertDbOperation().execute(personData).get();
        }

        return rowId;
    }

    private static class InsertDbOperation extends AsyncTask<Object, Void, Long> {
        @Override
        protected Long doInBackground(Object... objects) {
            //Default row id
            long rowId = -1;

            if (objects[0].equals(TripData.class.getSimpleName())) {
                TripData data = (TripData) objects[1];

                rowId = storage.tripDao().addTrip(data);
            } else if (objects[0].equals(BillData.class.getSimpleName())) {
                BillData data = (BillData) objects[1];

                rowId = storage.billDao().insertBilldata(data);
            } else if (objects[0].equals(PersonData.class.getSimpleName())) {
                for (PersonData personData : (List<PersonData>) objects[1]){
                    rowId = storage.personDao().insertPersondata(personData);
                }
            }

            return rowId;
        }
    }

    public long updateDataToDb(Object... objects) throws ExecutionException, InterruptedException {
        //Default id
        long rowId = -1;

        rowId = new UpdateDbOperation().execute(objects).get();

        if (rowId > 0){
            Log.e(TAG, "UpdateDbOperation : " + "Updated with new data in table " + objects[0].getClass().getSimpleName());
        }else {
            Log.e(TAG, "InsertDbOperation : " + "Error in updating new data in table " + objects[0].getClass().getSimpleName());
        }

        return rowId;
    }

    private long updatePersonData(BillData billData, List<PersonData> personData) throws ExecutionException, InterruptedException {
        long rowId = -1;

        PersonHelper helper = new PersonHelper(billData, personData);
        //New PersonData list
        List<PersonData> mainPersonList = helper.initPersonData();

        for (PersonData data : mainPersonList){
            rowId = new UpdateDbOperation().execute(data).get();
        }

        return rowId;
    }

    private static class UpdateDbOperation extends AsyncTask<Object, Void, Long>{

        @Override
        protected Long doInBackground(Object... objects) {
            //Default row id
            long rowId = -1;

            if (objects[0].equals(BillData.class.getSimpleName())) {
                BillData data = (BillData) objects[1];

                rowId = storage.billDao().updateBillDataData(data);
            } else if (objects[0].equals(PersonData.class.getSimpleName())) {
                PersonHelper helper = new PersonHelper((BillData) objects[1], (List<PersonData>) objects[2]);
                //New PersonData list
                List<PersonData> mainPersonList = helper.initPersonData();

                for (PersonData data : mainPersonList){
                    rowId = storage.personDao().updatePersonData(data);
                }
            }

            return rowId;
        }
    }

    public long deleteDataFromDb(Object... objects) throws ExecutionException, InterruptedException {
        //Default id
        long rowId = -1;

        if (objects[0].equals(TripData.class.getSimpleName())) {
            rowId = new DeleteDbOperation().execute(objects).get();
        } else if (objects[0].equals(BillData.class.getSimpleName())) {
            rowId = new DeleteDbOperation().execute(objects).get();
        } else {
            rowId = new DeleteDbOperation().execute(objects).get();
        }

        if (rowId > 0){
            Log.e(TAG, "UpdateDbOperation : " + "Deleted the data in table " + objects[0].getClass().getSimpleName());
        }else {
            Log.e(TAG, "InsertDbOperation : " + "Error in deleting data in table " + objects[0].getClass().getSimpleName());
        }

        return rowId;
    }

    private static class DeleteDbOperation extends AsyncTask<Object, Void, Long>{

        @Override
        protected Long doInBackground(Object... objects) {
            //Default row id
            long rowId = -1;

            if (objects[1].getClass().equals(TripData.class)) {
                TripData data = (TripData) objects[1];

                rowId = storage.tripDao().deleteTripEntry(data.getId());
            } /*else if (objects[0].getClass().equals(BillData.class)) {
                BillData data = (BillData) objects[0];

                rowId = storage.billDao().de(data);
            }*/

            return rowId;
        }
    }
}
