package com.tripewise.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tripewise.R;
import com.tripewise.utilites.Util;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.BillAsyncConfig;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddBillFragment extends BottomSheetDialogFragment {
    private TripData tripData;

    AddBillFragment(TripData tripData){
        this.tripData = tripData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btSave = view.findViewById(R.id.bt_save);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new BillAsyncConfig(getActivity()).insertBillData(createBillData());

                    new PeopleAsyncConfig(getActivity(), createBillData()).updatePersonData();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private BillData createBillData() {
        BillData data = new BillData();
        data.setBillName("Morning Lunch");
        data.setBillAmount(1000);
        data.setBillTime(Util.dateToMilli("15 Jan 2019 00:00"));
        data.setBillTimeString("15 Jan 2019 00:00");
        data.setTripId(1);
        data.setBillPaidPeopleList(createPaidPeopleList());
        data.setBillPeopleList(createBillPeopleList());

        return data;
    }

    private ArrayList<BillData.BillPeople> createPaidPeopleList() {
        ArrayList<BillData.BillPeople> billPeople = new ArrayList<>();

        BillData.BillPeople data1 = new BillData.BillPeople();
        data1.setPeopleName(tripData.getMemberName().get(0));
        data1.setAmount(1000);

        billPeople.add(data1);

        return billPeople;
    }

    private ArrayList<BillData.BillPeople> createBillPeopleList() {
        ArrayList<BillData.BillPeople> billPeople = new ArrayList<>();

        BillData.BillPeople data1 = new BillData.BillPeople();
        data1.setPeopleName(tripData.getMemberName().get(1));
        data1.setAmount(1000 / 4);

        BillData.BillPeople data2 = new BillData.BillPeople();
        data2.setPeopleName(tripData.getMemberName().get(2));
        data2.setAmount(1000 / 4);

        BillData.BillPeople data3 = new BillData.BillPeople();
        data3.setPeopleName(tripData.getMemberName().get(3));
        data3.setAmount(1000 / 4);

        billPeople.add(data1);
        billPeople.add(data2);
        billPeople.add(data3);

        return billPeople;
    }
}
