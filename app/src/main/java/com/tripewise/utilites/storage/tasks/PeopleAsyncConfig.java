package com.tripewise.utilites.storage.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PeopleAsyncConfig {
    private static TripStorage storage;

    private Context context;

    private BillData billData;

    private List<PersonData> personDataList;

    public PeopleAsyncConfig(Context context, BillData billData) {
        this.context = context;
        this.billData = billData;

        if (storage == null) {
            storage = TripStorage.getDataBaseInstance(context);
        }

        getPersonData();
    }

    private void getPersonData() {
        storage.personDao().getAllData().observe((LifecycleOwner) context, new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personData) {
                personDataList = personData;

                Log.d("PeopleAsyncConfig : ", "Person Data = " + new Gson().toJson(personData).toString());
            }
        });
    }

    public void insertPersonDetails(List<PersonData> data) throws ExecutionException, InterruptedException {
        for (PersonData personData : data) {
            insertPersonData(personData);
        }
    }

    private void insertPersonData(PersonData data) throws ExecutionException, InterruptedException {
        long id = new InsertPersonDataAsync(data).execute().get();

        if (id > 0) {
            Log.d("PeopleAsyncConfig : ", "Person data inserted to database");
        } else {
            Log.d("PeopleAsyncConfig : ", "Some error happen while performing insert query");
        }
    }

    private static class InsertPersonDataAsync extends AsyncTask<Void, Void, Long> {
        private PersonData data;

        InsertPersonDataAsync(PersonData data) {
            this.data = data;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return storage.personDao().insertPersondata(data);
        }
    }

    private void updatePersonData(PersonData data) throws ExecutionException, InterruptedException {
        int id = new UpdatePersonDataAsync(data).execute().get();

        if (id > 0) {
            Log.d("PeopleAsyncConfig : ", "Person data updated");
        } else {
            Log.d("PeopleAsyncConfig : ", "Some error happen while performing update query");
        }
    }

    public void updatePersonData() throws ExecutionException, InterruptedException {
        PersonUtils personUtils = new PersonUtils(billData, personDataList);

        for (PersonData personData : personUtils.initPersonData()) {
            updatePersonData(personData);
        }
    }

    private static class UpdatePersonDataAsync extends AsyncTask<Void, Void, Integer> {
        PersonData data;

        UpdatePersonDataAsync(PersonData data) {
            this.data = data;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return storage.personDao().updatePersonData(data);
        }
    }
}
