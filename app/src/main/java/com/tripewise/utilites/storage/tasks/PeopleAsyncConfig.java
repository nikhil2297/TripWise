package com.tripewise.utilites.storage.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//TODO : We have to move all the calculation to different class
public class PeopleAsyncConfig {
    private static TripStorage storage;

    private Context context;

    private BillData billData;

    private List<PersonData> personDataList;

    private ArrayList<String> people = new ArrayList<>();

    private boolean isCallInit = false;

    public PeopleAsyncConfig(Context context, BillData billData) {
        this.context = context;
        this.billData = billData;

        if (storage == null) {
            storage = TripStorage.getDataBaseInstance(context);
        }

        getPersonData();
    }

    private void init() {
        if (billData != null) {
            for (BillData.BillPeople s : billData.getBillPeopleList()) {
                people.add(s.getPeopleName());
            }

            for (int i = 0; i < personDataList.size(); i++) {
                for (int j = 0; j < billData.getBillPaidPeopleList().size(); j++) {
                    if (personDataList.get(i).getPersonName().equals(billData.getBillPaidPeopleList().get(j).getPeopleName())) {
                        sortPaidDetails(people, personDataList.get(i), billData.getBillPaidPeopleList().get(j));
                        break;
                    }
                }
            }

            for (PersonData data : personDataList) {
                sortReceivingDetails(data);
            }

            for (PersonData data : personDataList) {
                try {
                    new UpdatePersonDataAsync(data).execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sortPaidDetails(ArrayList<String> peopleList, PersonData data, BillData.BillPeople billPeople) {
        if (data != null) {
            PaymentDetailsData detailsData = data.getPaymentData();

            PaymentDetailsData.Details details = new PaymentDetailsData.Details();
            details.setName(billPeople.getPeopleName());
            details.setAmount(billPeople.getAmount());

            detailsData.setPaidData(details);

            data.setPaymentDetails(new Gson().toJson(detailsData).toString());
        }
    }

    private void sortReceivingDetails(PersonData data) {
        if (data != null && data.getPaymentData().getPaidDetails() != null) {
            PaymentDetailsData paymentDetailsData = data.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsArrayList = new ArrayList<>();

            int receivingPeopleSize = paymentDetailsData.getReceiveDetails().size();

            for (int i = 0; i < billData.getBillPaidPeopleList().size(); i++) {
                if (data.getPersonName().equals(billData.getBillPaidPeopleList().get(i).getPeopleName())) {
                    for (int j = 0; j < receivingPeopleSize; j++) {
                        PaymentDetailsData.Details details = paymentDetailsData.getReceiveDetails().get(j);

                        details.setAmount(details.getAmount() + billData.getBillPeopleList().get(i).getAmount());
                        details.setName(details.getName());

                        detailsArrayList.add(details);
                    }
                }
            }

            paymentDetailsData.setReceiveDetails(detailsArrayList);

            data.setPaymentData(paymentDetailsData);
        } else {
            sortSendDetails(data);
        }
    }

    private void sortSendDetails(PersonData data) {
        if (data != null) {
            PaymentDetailsData paymentDetailsData = data.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsArrayList = new ArrayList<>();

            for (int i = 0; i < paymentDetailsData.getSendDetails().size(); i++) {
                for (int j = 0; j < billData.getBillPaidPeopleList().size(); j++) {
                    String billPersonName = billData.getBillPaidPeopleList().get(j).getPeopleName();

                    if (!data.getPersonName().equals(billPersonName)) {
                        PaymentDetailsData.Details details = paymentDetailsData.getSendDetails().get(i);

                        if (data.getPaymentData().getSendDetails().get(i).getName().equals(billPersonName)) {
                            details.setAmount(details.getAmount() + billData.getBillPeopleList().get(i).getAmount());
                        } else {
                            details.setAmount(details.getAmount());
                        }

                        detailsArrayList.add(details);
                    }
                }
            }

            paymentDetailsData.setSendDetails(detailsArrayList);

            data.setPaymentData(paymentDetailsData);
        }
    }

    private void calculateAllPayment(PaymentDetailsData detailsData) {

    }

    public void updatePersonData() {
        isCallInit = true;
    }

    private void getPersonData() {
        storage.personDao().getAllData().observe((LifecycleOwner) context, new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personData) {
                personDataList = personData;

                if (isCallInit) {
                    init();

                    isCallInit = false;
                }

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

    public void updatePersonData(PersonData data) throws ExecutionException, InterruptedException {
        int id = new UpdatePersonDataAsync(data).execute().get();

        if (id > 0) {
            Log.d("PeopleAsyncConfig : ", "Person data updated");
        } else {
            Log.d("PeopleAsyncConfig : ", "Some error happen while performing update query");
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
