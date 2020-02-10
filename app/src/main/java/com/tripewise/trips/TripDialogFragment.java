package com.tripewise.trips;

import android.graphics.Color;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tripewise.R;
import com.tripewise.utilites.Util;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;
import com.tripewise.utilites.storage.tasks.TripAsyncConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripDialogFragment extends Fragment implements View.OnClickListener {

    private EditText etTripName;
    private EditText etDestination;

    private ChipGroup travellersChip;

    private ArrayList<String> memberName;

    private List<PersonData> personData;

    private Button btSave;
    private Button btCancel;

    private boolean isMember;
    private boolean isTripName;

    private TripAsyncConfig asyncConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        asyncConfig = new TripAsyncConfig(getActivity());

        memberName = new ArrayList<>();

        personData = new ArrayList<>();


        etTripName = view.findViewById(R.id.et_trip_name);
        etDestination = view.findViewById(R.id.et_destination);

        travellersChip = view.findViewById(R.id.chip_travellers);

        btSave = view.findViewById(R.id.bt_save);
        btCancel = view.findViewById(R.id.bt_cancel);

        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        travellersChip.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {
                isMember = travellersChip.getChildCount() >= 2;
                setButtonState();
            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                isMember = travellersChip.getChildCount() >= 2;
                setButtonState();
            }
        });


        etTripName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isTripName = charSequence.length() > 0;
                setButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Chip createChip(String text) {
        Chip chip = new Chip(getActivity());
        chip.setText(text);
        chip.setTextColor(Color.WHITE);
        chip.setChipBackgroundColorResource(R.color.colorBlack);
        chip.setChipIcon(getResources().getDrawable(R.drawable.ic_people));
        chip.setCloseIconVisible(true);
        chip.setCloseIconTintResource(R.color.colorWhite);
        chip.setCloseIcon(getActivity().getResources().getDrawable(R.drawable.ic_close));
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chip tempChip = (Chip) v;
                memberName.remove(tempChip.getText().toString());
                travellersChip.removeView(v);
            }
        });
        return chip;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                if (isMember && isTripName) {
                    TripData data = new TripData();
                    data.setTripName(etTripName.getText().toString());
                    data.setMemberName(memberName);
                    data.setMemberCount(memberName.size());
                    data.setGifPath(Util.createGif());

                    try {
                        asyncConfig.insertTripData(data);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    updatePeopleData();
                }
                break;
            case R.id.bt_cancel:
                break;
        }
    }

    private void setButtonState() {
        if (isMember && isTripName) {
            btSave.setEnabled(true);
        } else {
            btSave.setEnabled(false);
        }
    }

    private void updatePeopleData() {
        ArrayList<PaymentDetailsData.Details> details = new ArrayList<>();

        for (String s : memberName) {
            createPeopleData(s);
        }

        try {
            new PeopleAsyncConfig(getActivity()).insertPersonDetails(personData);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createPeopleData(String name) {
        PaymentDetailsData detailsData = new PaymentDetailsData();

        PersonData data = new PersonData();
        data.setPersonName(name);

        for (String s : memberName) {
            if (!s.equals(name)) {
                PaymentDetailsData.Details details = new PaymentDetailsData.Details();

                details.setName(s);

                detailsData.setReceiveData(details);
                detailsData.setSendData(details);

                data.setPaymentData(detailsData);
            }
        }

        personData.add(data);
    }
}
