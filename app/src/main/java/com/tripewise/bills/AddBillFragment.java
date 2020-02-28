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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.people.PeopleViewModel;
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

    private PeopleViewModel peopleViewModel;

    private BillViewModel billViewModel;

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
        billViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(BillViewModel.class);

        peopleViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PeopleViewModel.class);

        finalBillPeopleList = new ArrayList<>();
        finalPaidPeopleList = new ArrayList<>();

        billPeopleList = new ArrayList<>();
        paidPeopleList = new ArrayList<>();

        btCreate.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        //Add all the travellers in both of the chip group
        addPeople();
    }

    private void addPeople() {
        peopleViewModel.fetchPersonDetails(getActivity(), PersonData.class, tripData.getId(), new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                personDataList = (List<PersonData>) o;

                for (PersonData data : personDataList) {
                    billPayerChip.addView(createPaidPeopleView(billPayerChip, data));

                    billPeopleChip.addView(createBillPeopleView(billPeopleChip, data));

                    setBillPeople(false, data);
                }
            }
        });
    }

    /**
     * BillName char length should be grater then 1
     * BillPaidPeople as there should at least on traveller who paid the bill
     * BillAmount should be there
     *
     * @return true if all condition satisfy else false
     */
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

                    //Add Bill data to Bill Table
                    insertBillData(billData);
                    //Sort the receiving data and sending data of Person Table

                }
                break;
            case R.id.iv_back:
                controller.popBackStack();
                break;
        }
    }

    private void insertBillData(BillData billData){
        billViewModel.insertBillData(getActivity(), BillData.class.getSimpleName(), billData);
    }

    private void insertPersonData(){

    }

    /**
     * @param chipGroup = Is only used as parent view
     * @param data      = Traveller detail eg. Name, Chip color, mobile number
     * @return = A view that we want to add in chipgroup
     */
    private View createPaidPeopleView(ChipGroup chipGroup, final PersonData data) {
        final View chipView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_traveller_big_chip, (ViewGroup) chipGroup.getParent(), false);

        final ConstraintLayout layoutChip = chipView.findViewById(R.id.layout_chip);

        final MaterialCardView chipCardView = chipView.findViewById(R.id.materialCardView);

        final TextView tvName = layoutChip.findViewById(R.id.tv_chip_name);

        TextView tvFullName = layoutChip.findViewById(R.id.tv_chip_full_name);

        tvFullName.setText(data.getPersonName());
        tvName.setText(data.getPersonName().substring(0, 2).toUpperCase());
        tvName.setTextColor(Color.BLACK);

        setUnCheckChip(chipCardView, data);

        //Switch type chipView for selected state and unselected state works for the TextView too
        layoutChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipCardView.getCardBackgroundColor().getDefaultColor() == Color.WHITE) {
                     /*
                       1. We select a traveller
                       2. Then show a amount popup
                       3. If the user enter the amount and hit the save button then traveller chip is assumed as selected or else stays in unselected state
                      */
                    createAmountPopUp(chipCardView, data, tvName);
                } else {
                    setUnCheckChip(chipCardView, data);

                    setPaidBillPeople(true, 0, data);

                    tvName.setTextColor(Color.BLACK);
                }
            }
        });

        return chipView;
    }

    /**
     * @param chipGroup = Is only used as parent view
     * @param data      = Traveller detail eg. Name, Chip color, mobile number
     * @return = A view that we want to add in chipgroup
     */
    private View createBillPeopleView(ChipGroup chipGroup, final PersonData data) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_traveller_big_chip, (ViewGroup) chipGroup.getParent(), false);

        final ConstraintLayout layoutChip = view.findViewById(R.id.layout_chip);

        final MaterialCardView chipCardView = view.findViewById(R.id.materialCardView);

        final TextView tvName = layoutChip.findViewById(R.id.tv_chip_name);

        TextView tvFullName = layoutChip.findViewById(R.id.tv_chip_full_name);

        tvFullName.setText(data.getPersonName());
        tvFullName.setVisibility(View.VISIBLE);
        tvName.setText(data.getPersonName().substring(0, 2).toUpperCase());

        setCheckedChip(chipCardView, data);

        //Switch type chipView for selected state and unselected state works for the TextView too
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

    /**
     * @param isRemove = if its true then we remove for the billPeople array list else we add into it.
     * @param data     = Traveller details used for adding billPeople into array list or comparing using mobile number to remove it.
     */
    private void setBillPeople(boolean isRemove, PersonData data) {
        if (!isRemove) {
            BillData.BillPeople people = new BillData.BillPeople();
            people.setPeopleName(data.getPersonName());
            people.setPeopleNumber(data.getMobileNumber());
            people.setPeopleColor(data.getPersonColor());

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

    /**
     * @param isRemove = If its true then we remove entry form the paidBillPeople array list else we add into it.
     * @param amount   = Its the amount paid by the traveller for the current build and also use to add data its data.
     * @param data     = Traveller details used for adding data in paidBillPeople array list or comparing using mobile number to remove it.
     */
    private void setPaidBillPeople(boolean isRemove, long amount, PersonData data) {
        if (!isRemove) {
            BillData.BillPeople people = new BillData.BillPeople();
            people.setAmount(amount);
            people.setPeopleNumber(data.getMobileNumber());
            people.setPeopleName(data.getPersonName());
            people.setPeopleColor(data.getPersonColor());

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

    /**
     * @param cardView   = This view is used to pass as a parameter to change its state into checked state
     * @param personData = Traveller details eg: Name, Person chip color etc. Its also pass as a parameter;
     * @param tvName     = As our chipView is a parent view and its is initialize as a local variable and all its childview are also local variable.
     *                   We also change the state the textView inside ChipView. So in above given condition on line number 198. We require tvName to change its state.
     */
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

    /**
     * @param checkedChip = Change its background color and remove the stroke so the user can see its selected
     * @param data        = Use to get background color
     */
    private void setCheckedChip(MaterialCardView checkedChip, PersonData data) {
        checkedChip.setCardBackgroundColor(data.getPersonColor());
        checkedChip.setStrokeWidth(0);
        checkedChip.setStrokeColor(Color.TRANSPARENT);
    }

    /**
     * @param unCheckChip = Change its background color and add the stroke so the user can see its unslected
     * @param data        = not used for now
     */
    private void setUnCheckChip(MaterialCardView unCheckChip, PersonData data) {
        unCheckChip.setCardBackgroundColor(Color.WHITE);
        unCheckChip.setStrokeWidth(1);
        unCheckChip.setStrokeColor(Color.BLACK);
    }

    //We create a BillData that with bill Name, Amount, Trip id, paidPeople list, bill people list.
    private BillData createBillData() {
        BillData data = new BillData();
        data.setBillName(etBillName.getText().toString());
        data.setBillAmount(Long.parseLong(etBillAmount.getText().toString()));
        data.setTripId(tripData.getId());
        data.setBillPaidPeopleList(finalPaidPeopleList);
        data.setBillPeopleList(sortBillPeople(finalBillPeopleList));

        return data;
    }

    /*
     * Step :
     * 1. Create a PaidPeople data form @finalPaidPeopleList
     * 2. Create a BillPeople data from @newBIllPeopleList
     * 3. Compare PaidPeople traveller number with BillPeople traveller number
     * 4. a) If true then don't set the amount as that will be the traveller who paid for the bill
     *    b) If false then add the result amount which we get from dividing the total amount paid by the current traveller/Total number of traveller in bill people
     * 5. After all step is completed then return the @newBillPeopleList
     * */
    private ArrayList<BillData.BillPeople> sortBillPeople(ArrayList<BillData.BillPeople> newBillPeopleList) {
        for (int i = 0; i < finalPaidPeopleList.size(); i++) {
            BillData.BillPeople paidPeople = finalPaidPeopleList.get(i);

            for (int j = 0; j < newBillPeopleList.size(); j++) {
                BillData.BillPeople billPeople = newBillPeopleList.get(j);
                if (!paidPeople.getPeopleNumber().equals(billPeople.getPeopleNumber())) {
                    billPeople.setAmount(billPeople.getAmount() + (paidPeople.getAmount() / newBillPeopleList.size()));
                }
            }
        }
        return newBillPeopleList;
    }


}