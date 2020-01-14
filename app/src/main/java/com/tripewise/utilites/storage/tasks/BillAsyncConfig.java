package com.tripewise.utilites.storage.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.BillData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BillAsyncConfig {
    private static TripStorage storage;

    private BillConfigListener listener;

    private Context context;

    public BillAsyncConfig(Context context) {
        this.context = context;

        if (storage == null) {
            storage = TripStorage.getDataBaseInstance(context);
        }
    }

    public interface BillConfigListener {
        void onDataChange(List<BillData> billData);
    }

    private BillData billCalculation(BillData billData) {
        long amount = billData.getBillAmount() / billData.getBillPeopleList().size();

        for (BillData.BillPeople data : billData.getBillPeopleList()) {
            data.setAmount(amount);
        }

        return billData;
    }

    public void onBillDataChange(final BillConfigListener listener) {
        this.listener = listener;

        storage.billDao().getAllData().observe((LifecycleOwner) context, new Observer<List<BillData>>() {
            @Override
            public void onChanged(List<BillData> billData) {
                listener.onDataChange(billData);
            }
        });
    }

    public void updateBillData(BillData billData) throws ExecutionException, InterruptedException {
        int id = new UpdateBillAsync(billData).execute().get();

        if (id > 0) {
            Log.d("BillAsyncConfig : ", "Bill Updated from database");
        } else {
            Log.d("BillAsyncConfig : ", "Some error happen while updating bill");
        }
    }

    private static class UpdateBillAsync extends AsyncTask<Void, Void, Integer> {
        private BillData billData;

        UpdateBillAsync(BillData billData) {
            this.billData = billData;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return storage.billDao().updateBillDataData(billData);
        }
    }

    public void insertBillData(BillData billData) throws ExecutionException, InterruptedException {
        long id = new InsertBillAsync(billData).execute().get();

        if (id > 0) {
            Log.d("BillAsyncConfig : ", "Bill inserted from database");
        } else {
            Log.d("BillAsyncConfig : ", "Some error happen while inserting bill");
        }
    }

    private static class InsertBillAsync extends AsyncTask<Void, Void, Long> {
        private BillData billData;

        InsertBillAsync(BillData billData) {
            this.billData = billData;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return storage.billDao().insertBilldata(billData);
        }
    }
}
