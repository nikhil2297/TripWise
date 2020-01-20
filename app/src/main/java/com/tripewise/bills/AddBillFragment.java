package com.tripewise.bills;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tripewise.R;
import com.tripewise.utilites.Util;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.BillAsyncConfig;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.ArrayList;

public class AddBillFragment extends Fragment implements View.OnClickListener {
    private TripData tripData;

    private EditText etBillName;
    private EditText etBillDate;
    private EditText etBillTime;
    private EditText etBillPaidPeople;
    private EditText etBillPeople;
    private EditText etBillAmount;

    private Button btSave;
    private Button btCancel;

    private ArrayList<String> paidPeopleList;
    private ArrayList<String> billPeopleList;

    private PeopleAsyncConfig peopleAsyncConfig;

    private BillAsyncConfig billAsyncConfig;

    private boolean isBillName;
    private boolean isBillDate;
    private boolean isBillPaidPeople;
    private boolean isBillAmount;

    AddBillFragment(TripData tripData) {
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

        etBillName = view.findViewById(R.id.et_bill_name);
        etBillAmount = view.findViewById(R.id.et_amount);
        etBillDate = view.findViewById(R.id.et_date);
        etBillTime = view.findViewById(R.id.et_time);
        etBillPaidPeople = view.findViewById(R.id.et_paid_people);
        etBillPeople = view.findViewById(R.id.et_people_list);

        btSave = view.findViewById(R.id.bt_save);
        btCancel = view.findViewById(R.id.bt_cancel);
    }

    private void init() {
        billAsyncConfig = new BillAsyncConfig(getActivity());

        peopleAsyncConfig = new PeopleAsyncConfig(getActivity());

        billPeopleList = new ArrayList<>();
        paidPeopleList = new ArrayList<>();

        attachTextChangeListener(etBillName);
        attachTextChangeListener(etBillDate);
        attachTextChangeListener(etBillAmount);

        etBillDate.setOnClickListener(this);
        etBillTime.setOnClickListener(this);
        etBillPeople.setOnClickListener(this);
        etBillPaidPeople.setOnClickListener(this);
    }

    private void attachTextChangeListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validation()) {
                    btSave.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean validation() {
        boolean isValid = false;
        if (etBillName.getText().toString().length() > 1) {
            isValid = true;
        } else {
            isValid = false;
        }

        if (Util.validateFormat(etBillDate.getText().toString())) {
            isValid = true;
        } else {
            isValid = false;
        }

        if (etBillName.getText().toString().length() > 1) {
            isValid = true;
        } else {
            isValid = false;
        }

        if (etBillName.getText().toString().length() > 1) {
            isValid = true;
        } else {
            isValid = false;
        }

        if (etBillName.getText().toString().length() > 1) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                break;
            case R.id.et_time:
                break;
            case R.id.et_paid_people:
                break;
            case R.id.et_people_list:
                break;
            case R.id.bt_save:
                break;
            case R.id.bt_cancel:
                break;
        }
    }

    //TODO : Remove all the static data
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
