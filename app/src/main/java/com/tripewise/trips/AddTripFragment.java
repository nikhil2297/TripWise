package com.tripewise.trips;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.tripewise.R;
import com.tripewise.utilites.CustomEditText;
import com.tripewise.utilites.Util;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;
import com.tripewise.utilites.storage.tasks.TripAsyncConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddTripFragment extends Fragment implements View.OnClickListener, ChipGroup.OnCheckedChangeListener {

    private CustomEditText etTripName;
    private CustomEditText etDestination;

    private ChipGroup travellersChip;

    private ArrayList<String> memberName;

    private List<PersonData> personData;

    private NavController controller;

    private Button btSave;
    private Button btCancel;

    private int latestTripId;

    private boolean isMember;
    private boolean isTripName;

    private ImageView ivBack;

    private TripAsyncConfig asyncConfig;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddTripFragmentArgs args = AddTripFragmentArgs.fromBundle(getArguments());
        latestTripId = args.getLatestId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = NavHostFragment.findNavController(this);

        etTripName = view.findViewById(R.id.et_trip_name);
        etDestination = view.findViewById(R.id.et_destination);

        travellersChip = view.findViewById(R.id.chip_travellers);

        ivBack = view.findViewById(R.id.iv_back);

        btSave = view.findViewById(R.id.bt_create);

        init();
    }

    private void init() {
        asyncConfig = new TripAsyncConfig(getActivity());

        memberName = new ArrayList<>();

        personData = new ArrayList<>();

        travellersChip.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {
                isMember = travellersChip.getChildCount() >= 3;
                setButtonState();

            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                isMember = travellersChip.getChildCount() >= 3;
                setButtonState();
            }
        });

        etTripName.addOnTextChangeListener(new CustomEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    isTripName = true;
                    setButtonState();
                } else {
                    isTripName = false;
                    setButtonState();
                }
            }
        });

        btSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        travellersChip.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_create:
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
            case R.id.iv_back:
                controller.popBackStack(R.id.tripFragment, false);
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
        for (int i = 0; i < personData.size(); i++) {
            createPeopleData(personData.get(i), i);
        }

        try {
            new PeopleAsyncConfig(getActivity(), 0).insertPersonDetails(personData);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            controller.popBackStack();
        }
    }

    private void createPeopleData(PersonData mainData, int position) {
        PaymentDetailsData detailsData = new PaymentDetailsData();

        for (PersonData data1 : personData) {
            if (!data1.getPersonName().equals(mainData.getPersonName())) {
                PaymentDetailsData.Details details = new PaymentDetailsData.Details();

                details.setName(data1.getPersonName());
                details.setMobileNumber(data1.getMobileNumber());

                detailsData.setReceiveData(details);
                detailsData.setSendData(details);

                mainData.setPaymentData(detailsData);
            }
        }

        personData.set(position, mainData);
    }

    @Override
    public void onCheckedChanged(ChipGroup chipGroup, int i) {
        if (i == R.id.chip_add) {
            createTravellerDialog();
            chipGroup.clearCheck();
        }
    }

    private View createView(final PersonData data) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_trip_chip, (ViewGroup) travellersChip.getParent(), false);

        final ConstraintLayout layoutChip = view.findViewById(R.id.layout_chip);

        MaterialCardView chipCardView = view.findViewById(R.id.materialCardView);

        chipCardView.setCardBackgroundColor(Util.createRandomColor());

        TextView tvName = layoutChip.findViewById(R.id.tv_chip_name);

        tvName.setText(data.getPersonName().substring(0, 2).toUpperCase());

        final ImageView ivRemove = view.findViewById(R.id.iv_remove);

        layoutChip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layoutChip.clearFocus();

                ivRemove.setVisibility(View.VISIBLE);

                return true;
            }
        });

        layoutChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivRemove.setVisibility(View.GONE);
            }
        });

        personData.get(personData.size() - 1).setPersonColor(chipCardView.getCardBackgroundColor().getDefaultColor());

        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personData.remove(data);

                memberName.remove(data.getPersonName());

                travellersChip.removeView(view);
            }
        });

        return view;
    }

    private void createTravellerDialog() {
        final Dialog builder = new Dialog(getActivity());

        builder.setContentView(R.layout.layout_add_traveller_dialog);

        final CustomEditText etTravellerName = builder.findViewById(R.id.et_traveller_name);

        final CustomEditText etTravellerNumber = builder.findViewById(R.id.et_traveller_number);

        final MaterialButton btCreate = builder.findViewById(R.id.bt_create);

        etTravellerName.addOnTextChangeListener(new CustomEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                boolean status = buttonCreateEnable(s.length() > 2, etTravellerNumber.getText().length() == 10);

                btCreate.setEnabled(status);
            }
        });

        etTravellerNumber.addOnTextChangeListener(new CustomEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(CharSequence s, int start, int before, int count) {
                boolean status = buttonCreateEnable(etTravellerName.getText().length() > 2, s.length() == 10);

                if (s.length() == 10) {
                    status = isPersonValid(s.toString());

                    if (!status) {
                        etTravellerNumber.setErrorEnabled(true);
                        etTravellerNumber.setError("Same mobile number cannot be used");
                    } else {
                        etTravellerNumber.setErrorEnabled(false);
                        etTravellerNumber.setError("");
                    }
                }

                btCreate.setEnabled(status);
            }
        });

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberName.add(etTravellerName.getText());

                PersonData data = new PersonData();
                data.setPersonName(etTravellerName.getText());
                data.setMobileNumber(etTravellerNumber.getText());
                data.setTripId(latestTripId + 1);

                personData.add(data);

                travellersChip.addView(createView(data));

                builder.dismiss();
            }
        });

        builder.show();
    }

    private boolean buttonCreateEnable(boolean isName, boolean isNumber) {
        return isName && isNumber;
    }

    private boolean isPersonValid(String mobileNumber) {
        for (PersonData data : personData) {
            if (data.getMobileNumber().equals(mobileNumber)) {
                return false;
            }
        }

        return true;
    }
}
