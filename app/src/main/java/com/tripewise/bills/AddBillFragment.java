package com.tripewise.bills;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.CustomEditText;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.BillAsyncConfig;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddBillFragment extends Fragment implements View.OnClickListener {
    private TripData tripData;

    private CustomEditText etBillName;
    private CustomEditText etBillAmount;

    private Button btSave;
    private Button btCancel;

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
/*        etBillDate = view.findViewById(R.id.et_date);
        etBillTime = view.findViewById(R.id.et_time);
        etBillPaidPeople = view.findViewById(R.id.et_paid_people);
        etBillPeople = view.findViewById(R.id.et_people_list);

        btSave = view.findViewById(R.id.bt_save);
        btCancel = view.findViewById(R.id.bt_cancel);*/

        init();
    }

    private void init() {
        billAsyncConfig = new BillAsyncConfig(getActivity());

        peopleAsyncConfig = new PeopleAsyncConfig(getActivity());

        finalBillPeopleList = new ArrayList<>();
        finalPaidPeopleList = new ArrayList<>();

/*        etBillDate.setOnClickListener(this);
        etBillTime.setOnClickListener(this);
        etBillPeople.setOnClickListener(this);
        etBillPaidPeople.setOnClickListener(this);*/

        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }

    private boolean validation() {
        boolean isValid = false;

        if (etBillName.getText().toString().length() > 1) {
            isValid = true;
        } else {
            isValid = false;
        }

/*        if (Util.validateFormat(etBillDate.getText().toString())) {
            isValid = true;
        } else {
            isValid = false;
        }*/

        if (finalPaidPeopleList.size() > 0) {
            isValid = true;
        } else {
            isValid = false;
        }

        if (etBillAmount.getText().toString().length() > 1) {
            isValid = true;
        } else {
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
/*            case R.id.et_date:
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
                break;*/
            case R.id.bt_save:
                if (validation()) {
                    BillData billData = createBillData();
                    try {
                        billAsyncConfig.insertBillData(billData);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            peopleAsyncConfig.updatePersonData(billData);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.bt_cancel:
                break;
        }
    }

    private void showPaidPeopleList() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        PeopleListDialogFragment dialogFragment = PeopleListDialogFragment.newInstance(createPaidPeopleList(), true, new PeopleListDialogFragment.PeopleListListener() {
            @Override
            public void onBillDataChange(ArrayList<BillData.BillPeople> billPeople, String name) {
                finalPaidPeopleList = billPeople;

                updatePaidPeopleList();

                //     etBillPaidPeople.setText(name);
                Log.d("AddBillFragment : ", String.valueOf(finalPaidPeopleList.size()));
            }
        });

        dialogFragment.show(fragmentTransaction, "paid-people-list");
    }

    private void showBillPeopleList() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        PeopleListDialogFragment dialogFragment = PeopleListDialogFragment.newInstance(createBillPeopleList(), false, new PeopleListDialogFragment.PeopleListListener() {
            @Override
            public void onBillDataChange(ArrayList<BillData.BillPeople> billPeople, String name) {
                finalBillPeopleList = billPeople;

                if (!name.equals(null)) {
                    //  etBillPeople.setText(name);
                }
                Log.d("AddBillFragment : ", String.valueOf(finalBillPeopleList.size()));
            }
        });

        dialogFragment.show(fragmentTransaction, "bill-people-list");
    }

    private void updatePaidPeopleList() {
        for (int i = 0; i < finalPaidPeopleList.size(); i++) {
            BillData.BillPeople billPeople = finalPaidPeopleList.get(i);

            paidPeopleList.get(i).setCheck(billPeople.isCheck());
            paidPeopleList.get(i).setAmount(billPeople.getAmount());
        }
    }

    private BillData createBillData() {
        BillData data = new BillData();
        data.setBillName(etBillName.getText().toString());
        data.setBillAmount(Integer.parseInt(etBillAmount.getText().toString()));
        data.setTripId(tripData.getId());
        data.setBillPaidPeopleList(finalPaidPeopleList);
        data.setBillPeopleList(createFinalBillPeopleList());

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

    private ArrayList<BillData.BillPeople> createFinalBillPeopleList() {
        if (finalBillPeopleList.size() > 0) {
            return sortBillPeople(finalBillPeopleList);
        } else {
            return sortBillPeople(createBillPeopleList());
        }
    }

    private ArrayList<BillData.BillPeople> sortBillPeople(ArrayList<BillData.BillPeople> newBillPeopleList) {
        for (int i = 0; i < finalPaidPeopleList.size(); i++) {
            BillData.BillPeople paidPeople = finalPaidPeopleList.get(i);

            for (int j = 0; j < newBillPeopleList.size(); j++) {
                BillData.BillPeople billPeople = newBillPeopleList.get(j);
                if (!paidPeople.getPeopleName().equals(billPeople.getPeopleName())) {
                    billPeople.setAmount(billPeople.getAmount() + (paidPeople.getAmount() / newBillPeopleList.size()));
                }
            }
        }
        return newBillPeopleList;
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