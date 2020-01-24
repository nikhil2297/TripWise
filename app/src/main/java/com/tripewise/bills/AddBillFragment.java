package com.tripewise.bills;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.Util;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.BillAsyncConfig;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.ArrayList;
import java.util.Calendar;

public class AddBillFragment extends Fragment implements View.OnClickListener {
    private TripData tripData;

    private EditText etBillName;
    private TextView etBillDate;
    private TextView etBillTime;
    private TextView etBillPaidPeople;
    private TextView etBillPeople;
    private EditText etBillAmount;

    private Button btSave;
    private Button btCancel;

    private int[] date = new int[3];
    private int[] time = new int[2];

    private ArrayList<BillData.BillPeople> billPeopleList;
    private ArrayList<BillData.BillPeople> paidPeopleList;

    private ArrayList<BillData.BillPeople> finalBillPeopleList;
    private ArrayList<BillData.BillPeople> finalPaidPeopleList;

    private PeopleAsyncConfig peopleAsyncConfig;

    private BillAsyncConfig billAsyncConfig;

    private boolean isBillName;
    private boolean isBillDate;
    private boolean isBillPaidPeople;
    private boolean isBillAmount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddBillFragmentArgs args = AddBillFragmentArgs.fromBundle(getArguments());
        tripData = new Gson().fromJson(args.getTripData(), TripData.class);
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

        init();
    }

    private void init() {
        billAsyncConfig = new BillAsyncConfig(getActivity());

        peopleAsyncConfig = new PeopleAsyncConfig(getActivity());

        finalBillPeopleList = new ArrayList<>();
        finalPaidPeopleList = new ArrayList<>();

        attachTextChangeListener(etBillName);
        attachTextChangeListener(etBillAmount);

        etBillDate.setOnClickListener(this);
        etBillTime.setOnClickListener(this);
        etBillPeople.setOnClickListener(this);
        etBillPaidPeople.setOnClickListener(this);

        setupDate();
        setUpTime();
    }

    private void setupDate() {
        Calendar calendar = Calendar.getInstance();

        date[0] = calendar.get(Calendar.DAY_OF_MONTH);
        date[1] = calendar.get(Calendar.MONTH);
        date[2] = calendar.get(Calendar.YEAR);
    }

    private void setUpTime() {
        Calendar calendar = Calendar.getInstance();

        time[0] = calendar.get(Calendar.HOUR_OF_DAY);
        time[1] = calendar.get(Calendar.MINUTE);
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
                createDatePicker();
                break;
            case R.id.et_time:
                createTimePicker();
                break;
            case R.id.et_paid_people:
                showPaidPeopleList();
                break;
            case R.id.et_people_list:
                showBillPeopleList();
                break;
            case R.id.bt_save:
                break;
            case R.id.bt_cancel:
                break;
        }
    }

    private void showPaidPeopleList() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        PeopleListDialogFragment dialogFragment = PeopleListDialogFragment.newInstance(createPaidPeopleList(), true, new PeopleListDialogFragment.PeopleListListener() {
            @Override
            public void memberAdded(BillData.BillPeople people, int position) {
                finalPaidPeopleList.add(people);

                paidPeopleList.get(position).setCheck(true);
            }

            @Override
            public void memberRemoved(BillData.BillPeople people, int position) {
                finalPaidPeopleList.remove(people);

                paidPeopleList.get(position).setCheck(false);
            }
        });

        dialogFragment.show(fragmentTransaction, "paid-people-list");
    }

    private void showBillPeopleList() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        PeopleListDialogFragment dialogFragment = PeopleListDialogFragment.newInstance(createBillPeopleList(), false, new PeopleListDialogFragment.PeopleListListener() {
            @Override
            public void memberAdded(BillData.BillPeople people, int position) {
                finalBillPeopleList.add(people);

                billPeopleList.get(position).setCheck(true);

                Log.d("AddBillFragment : ", String.valueOf(finalBillPeopleList.size()));
            }

            @Override
            public void memberRemoved(BillData.BillPeople people, int position) {
                finalBillPeopleList.remove(people);

                billPeopleList.get(position).setCheck(false);

                Log.d("AddBillFragment : ", String.valueOf(finalBillPeopleList.size()));
            }
        });

        dialogFragment.show(fragmentTransaction, "bill-people-list");
    }

    private void createTimePicker() {
        TimePickerDialog pickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time[0] = i;
                time[1] = i1;

                etBillTime.setText(i + ":" + i1);
            }
        }, time[0], time[1], true);

        pickerDialog.show();
    }

    private void createDatePicker() {
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.d("AddBillFragment", "Date picker : " + i + " " + i1 + 1 + " " + i2);

                date[0] = i2;
                date[1] = i1;
                date[2] = i;

                etBillDate.setText(i2 + " " + i1 + 1 + " " + i);
            }
        }, date[2], date[1], date[0]);

        pickerDialog.show();
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
        if (paidPeopleList == null) {
            paidPeopleList = new ArrayList<>();

            for (int i = 0; i < tripData.getMemberName().size(); i++) {
                BillData.BillPeople people = new BillData.BillPeople();
                people.setPeopleName(tripData.getMemberName().get(i));

                paidPeopleList.add(people);
            }
        }

        return paidPeopleList;
    }

    private ArrayList<BillData.BillPeople> createBillPeopleList() {
        if (billPeopleList == null) {
            billPeopleList = new ArrayList<>();

            for (int i = 0; i < tripData.getMemberName().size(); i++) {
                BillData.BillPeople people = new BillData.BillPeople();
                people.setPeopleName(tripData.getMemberName().get(i));

                billPeopleList.add(people);
            }
        }

        return billPeopleList;
    }
}
