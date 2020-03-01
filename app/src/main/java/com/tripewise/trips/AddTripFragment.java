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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.tripewise.R;
import com.tripewise.people.PeopleViewModel;
import com.tripewise.utilites.CustomEditText;
import com.tripewise.utilites.Util;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;

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

    private TripViewModel tripViewModel;
    private PeopleViewModel peopleViewModel;

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
        tripViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TripViewModel.class);
        peopleViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PeopleViewModel.class);

        memberName = new ArrayList<>();

        personData = new ArrayList<>();

        //The ChipGroup child count should be more then or equal to 3 because the add button is a child element so total count increase by 1
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
                /*
                 *Trip data is created
                 * 1. TripName
                 * 2. Memeber list
                 * 3. Member count
                 */
                if (isMember && isTripName) {
                    TripData data = new TripData();
                    data.setTripName(etTripName.getText().toString());
                    data.setMemberName(memberName);
                    data.setMemberCount(memberName.size());
                    data.setGifPath(Util.createGif());

                    tripViewModel.insertTrip(getActivity(), data);

                    //After inserting trip data in Trip table we also add all the traveller in Person table
                    updatePeopleData();
                }
                break;
            case R.id.iv_back:
                controller.popBackStack(R.id.tripFragment, false);
                break;
        }
    }

    /**
     * This method is used to check for validation of trip creation
     * 1. Trip Name should be there
     * 2. More the 2 member should be there
     * if all true the create button is enable
     */
    private void setButtonState() {
        if (isMember && isTripName) {
            btSave.setEnabled(true);
        } else {
            btSave.setEnabled(false);
        }
    }

    /**
     * We update Person Data with receiving data and sending data
     * After its updated we insert in Person table
     */
    private void updatePeopleData() {
        for (int i = 0; i < personData.size(); i++) {
            createPeopleData(personData.get(i), i);
        }

        try {
            peopleViewModel.insertPersonDetails(getActivity(), personData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            controller.popBackStack();
        }
    }

    /**
     * Step :
     * 1. Create PaymentDetailsData Object
     * 2. Get a Person details from PersonData list
     * 3. Compare Person Mobile number which we get from Person details with @mainData Person Mobile Number
     * 4. If false then we setReceiveData and setSendData and then we setPaymentData
     * 5. After we update in PersonData list
     *
     * @param mainData The current Person Data we are going to update
     * @param position to updated data of that specific positon
     */
    private void createPeopleData(PersonData mainData, int position) {
        PaymentDetailsData detailsData = new PaymentDetailsData();

        for (PersonData data1 : personData) {
            if (!data1.getMobileNumber().equals(mainData.getMobileNumber())) {
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
            //When user click on add chip we show a dialogbox to add traveller details;
            createTravellerDialog();
            chipGroup.clearCheck();
        }
    }

    /**
     * @param data = Get Traveller detail like Name, Chip color and also to remove the person from person data
     * @return the chip view we created
     */
    private View createView(final PersonData data) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_traveller_big_chip, (ViewGroup) travellersChip.getParent(), false);

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

    /**
     * Things required to create a traveller
     * 1. TravellerName
     * 2. Traveller Number which should be 10 digit and should not be same
     * 3. Click on create the taveller is created
     */
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

    /**
     * This method is only called in TravellerDialogPop to check the validation
     *
     * @param isName   Traveller name length should be greater then 2
     * @param isNumber Traveller number should be 10 digit
     * @return true if all condition satisfy
     */
    private boolean buttonCreateEnable(boolean isName, boolean isNumber) {
        return isName && isNumber;
    }

    /**
     * This method is only used in TravellerDialogPop to check the entered traveller number is not same
     *
     * @param mobileNumber should not be same
     * @return true if condition satisfy
     */
    private boolean isPersonValid(String mobileNumber) {
        for (PersonData data : personData) {
            if (data.getMobileNumber().equals(mobileNumber)) {
                return false;
            }
        }

        return true;
    }
}
