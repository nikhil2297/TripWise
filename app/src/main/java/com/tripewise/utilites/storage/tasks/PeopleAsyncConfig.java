package com.tripewise.utilites.storage.tasks;

import android.content.Context;

import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.ArrayList;

public class PeopleAsyncConfig {
    private static TripStorage storage;

    private Context context;

    private BillData billData;

    private ArrayList<String> people;

    public PeopleAsyncConfig(Context context, BillData billData) {
        this.context = context;
        this.billData = billData;

        if (storage == null) {
            storage = TripStorage.getDataBaseInstance(context);
        }
    }

    private void init() {
        for (BillData.BillPeople s : billData.getBillPeopleList()) {
            people.add(s.getPeopleName());
        }
    }

    private void sortReceivingDetails(ArrayList<String> peopleList, PersonData data) {

    }

    private void sortSendDetails(ArrayList<String> peopleList, PersonData data) {

    }

    private void sortPaymentDetails(ArrayList<String> peopleList, PaymentDetailsData detailsData) {

    }

    private void calculateAllPayment(PaymentDetailsData detailsData) {

    }
}
