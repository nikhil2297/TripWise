package com.tripewise.bills;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.CustomEditText;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.BillAsyncConfig;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddBillFragment extends Fragment implements View.OnClickListener {
    private TripData tripData;

    private CustomEditText etBillName;
    private CustomEditText etBillAmount;

    private MaterialButton btCreate;

    private ChipGroup billPayerChip;
    private ChipGroup billPeopleChip;

    private ImageView ivBack;

    private NavController controller;

    private List<PersonData> personDataList;

    private ArrayList<BillData.BillPeople> billPeopleList;
    private ArrayList<BillData.BillPeople> paidPeopleList;

    private ArrayList<BillData.BillPeople> finalBillPeopleList;
    private ArrayList<BillData.BillPeople> finalPaidPeopleList;

    private PeopleAsyncConfig peopleAsyncConfig;

    private BillAsyncConfig billAsyncConfig;

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

        controller = NavHostFragment.findNavController(this);

        etBillName = view.findViewById(R.id.et_bill_name);
        etBillAmount = view.findViewById(R.id.et_amount);

        btCreate = view.findViewById(R.id.bt_create);

        billPayerChip = view.findViewById(R.id.chip_bill_payer);
        billPeopleChip = view.findViewById(R.id.chip_bill_people);

        ivBack = view.findViewById(R.id.iv_back);

        init();
    }

    private void init() {
        billAsyncConfig = new BillAsyncConfig(getActivity());

        peopleAsyncConfig = new PeopleAsyncConfig(getActivity());

        finalBillPeopleList = new ArrayList<>();
        finalPaidPeopleList = new ArrayList<>();

        billPeopleList = new ArrayList<>();
        paidPeopleList = new ArrayList<>();

        btCreate.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        createPaidPeople();
    }

    private void createPaidPeople() {
        peopleAsyncConfig.getPersonData(tripData.getId()).observe(getViewLifecycleOwner(), new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personData) {
                personDataList = personData;

                for (PersonData data : personData) {
                    billPayerChip.addView(createPaidPeopleView(billPayerChip, data));

                    billPeopleChip.addView(createBillPeopleView(billPeopleChip, data));

                    setBillPeople(false, data);
                }
            }
        });
    }

    private boolean validation() {
        boolean isBillName = etBillName.getText().toString().length() > 1;

        boolean isBillPaidPeople = finalPaidPeopleList.size() > 0;

        boolean isBillAmount = etBillAmount.getText().toString().length() > 1;

        return isBillAmount && isBillName && isBillPaidPeople;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create:
                if (validation()) {
                    BillData billData = createBillData();
                    try {
                        billAsyncConfig.insertBillData(billData);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            peopleAsyncConfig.updatePersonData(billData, personDataList);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.iv_back:
                controller.popBackStack();
                break;
        }
    }

    private View createPaidPeopleView(ChipGroup chipGroup, final PersonData data) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_trip_chip, (ViewGroup) chipGroup.getParent(), false);

        final ConstraintLayout layoutChip = view.findViewById(R.id.layout_chip);

        final MaterialCardView chipCardView = view.findViewById(R.id.materialCardView);

        final TextView tvName = layoutChip.findViewById(R.id.tv_chip_name);

        TextView tvFullName = layoutChip.findViewById(R.id.tv_chip_full_name);

        tvFullName.setText(data.getPersonName());
        tvName.setText(data.getPersonName().substring(0, 2).toUpperCase());
        tvName.setTextColor(Color.BLACK);

        setUnCheckChip(chipCardView, data);

        layoutChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipCardView.getCardBackgroundColor().getDefaultColor() == Color.WHITE) {
                    createAmountPopUp(chipCardView, data, tvName);
                } else {
                    setUnCheckChip(chipCardView, data);

                    setPaidBillPeople(true, 0, data);

                    tvName.setTextColor(Color.BLACK);
                }
            }
        });

        return view;
    }

    private View createBillPeopleView(ChipGroup chipGroup, final PersonData data) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_trip_chip, (ViewGroup) chipGroup.getParent(), false);

        final ConstraintLayout layoutChip = view.findViewById(R.id.layout_chip);

        final MaterialCardView chipCardView = view.findViewById(R.id.materialCardView);

        final TextView tvName = layoutChip.findViewById(R.id.tv_chip_name);

        TextView tvFullName = layoutChip.findViewById(R.id.tv_chip_full_name);

        tvFullName.setText(data.getPersonName());
        tvFullName.setVisibility(View.VISIBLE);
        tvName.setText(data.getPersonName().substring(0, 2).toUpperCase());

        setCheckedChip(chipCardView, data);

        layoutChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipCardView.getCardBackgroundColor().getDefaultColor() == Color.WHITE) {
                    setCheckedChip(chipCardView, data);
                    tvName.setTextColor(Color.WHITE);

                    setBillPeople(false, data);
                } else {
                    setUnCheckChip(chipCardView, data);
                    tvName.setTextColor(Color.BLACK);

                    setBillPeople(true, data);
                }
            }
        });

        return view;
    }

    private void setBillPeople(boolean isRemove, PersonData data) {
        if (!isRemove) {
            BillData.BillPeople people = new BillData.BillPeople();
            people.setPeopleName(data.getPersonName());
            people.setPeopleNumber(data.getMobileNumber());

            billPeopleList.add(people);
        } else {
            Iterator<BillData.BillPeople> iterator = billPeopleList.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().getPeopleNumber().equals(data.getMobileNumber())) {
                    iterator.remove();
                }
            }
        }

        finalBillPeopleList = billPeopleList;

        Log.d("AddBillFragment", "Bill People List size : " + billPeopleList.size());
    }

    private void setPaidBillPeople(boolean isRemove, long amount, PersonData data) {
        if (!isRemove) {
            BillData.BillPeople people = new BillData.BillPeople();
            people.setAmount(amount);
            people.setPeopleNumber(data.getMobileNumber());
            people.setPeopleName(data.getPersonName());

            paidPeopleList.add(people);
        } else {
            Iterator<BillData.BillPeople> iterator = paidPeopleList.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().getPeopleNumber().equals(data.getMobileNumber())) {
                    iterator.remove();
                }
            }
        }

        finalPaidPeopleList = paidPeopleList;
    }

    private void createAmountPopUp(final MaterialCardView cardView, final PersonData personData, final TextView tvName) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_bill_amount_dialog);

        final MaterialCardView chipCardView = dialog.findViewById(R.id.materialCardView);

        TextView tvChipName = dialog.findViewById(R.id.tv_chip_name);

        final CustomEditText etAmount = dialog.findViewById(R.id.tv_amount_paid);
        CustomEditText etName = dialog.findViewById(R.id.et_traveller_name);

        final MaterialButton btSave = dialog.findViewById(R.id.bt_save);

        tvChipName.setText(personData.getPersonName().substring(0, 2).toUpperCase());

        etName.setText(personData.getPersonName());

        chipCardView.setCardBackgroundColor(personData.getPersonColor());

        etAmount.addOnTextChangeListener(new CustomEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                btSave.setEnabled(s.length() > 0);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPaidBillPeople(false, Long.parseLong(etAmount.getText()), personData);

                setCheckedChip(cardView, personData);

                tvName.setTextColor(Color.WHITE);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setCheckedChip(MaterialCardView checkedChip, PersonData data) {
        checkedChip.setCardBackgroundColor(data.getPersonColor());
        checkedChip.setStrokeWidth(0);
        checkedChip.setStrokeColor(Color.TRANSPARENT);
    }

    private void setUnCheckChip(MaterialCardView unChekChip, PersonData data) {
        unChekChip.setCardBackgroundColor(Color.WHITE);
        unChekChip.setStrokeWidth(1);
        unChekChip.setStrokeColor(Color.BLACK);
    }

    private BillData createBillData() {
        BillData data = new BillData();
        data.setBillName(etBillName.getText().toString());
        data.setBillAmount(Long.parseLong(etBillAmount.getText().toString()));
        data.setTripId(tripData.getId());
        data.setBillPaidPeopleList(finalPaidPeopleList);
        data.setBillPeopleList(sortBillPeople(finalBillPeopleList));

        return data;
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
}