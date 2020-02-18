package com.tripewise.people;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PeopleDetailFragment extends Fragment {
    private RecyclerView rvPeople;

    private ProgressBar pbMoneyStatus;

    private MaterialCardView chipCardView;

    private TextView tvTravellerName;
    private TextView tvTravellerNumber;
    private TextView tvTotalAmountPaid;
    private TextView tvTotalReceiveAmount;
    private TextView tvTotalSendAmount;
    private TextView tvChipText;

    private String mobileNumber;

    private int tripId;

    private List<TravellerItemObject> finalPeopleList;

    private PeopleAsyncConfig asyncConfig;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeopleDetailFragmentArgs args = PeopleDetailFragmentArgs.fromBundle(getArguments());
        mobileNumber = args.getMobileNumber();
        tripId = args.getTripId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPeople = view.findViewById(R.id.rv_people);

        pbMoneyStatus = view.findViewById(R.id.pb_amount);

        chipCardView = view.findViewById(R.id.materialCardView);

        tvTravellerName = view.findViewById(R.id.tv_traveller_name);
        tvTravellerNumber = view.findViewById(R.id.tv_traveller_number);
        tvTotalAmountPaid = view.findViewById(R.id.tv_total_amount);
        tvTotalReceiveAmount = view.findViewById(R.id.tv_amount_receive);
        tvTotalSendAmount = view.findViewById(R.id.tv_amount_send);
        tvChipText = view.findViewById(R.id.tv_chip_name);

        init();
    }

    private void init() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvPeople.setLayoutManager(manager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvPeople.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        rvPeople.addItemDecoration(itemDecoration);

        asyncConfig = new PeopleAsyncConfig(getActivity());

        finalPeopleList = new ArrayList<>();

        getPersonData();
    }

    private void getPersonData() {
        asyncConfig.getPersonData(tripId).observe(getViewLifecycleOwner(), new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personDataList) {
                PeopleDetailsAdapter adapter = new PeopleDetailsAdapter(getActivity(), sortPersonData(personDataList));

                rvPeople.setAdapter(adapter);

                for (PersonData data : personDataList) {
                    if (data.getMobileNumber().equals(mobileNumber)) {
                        setUpPersonData(data);
                    }
                }
            }
        });
    }

    private void setUpPersonData(PersonData personData) {
        tvTravellerName.setText(personData.getPersonName());
        tvTravellerNumber.setText(personData.getMobileNumber());
        tvTotalReceiveAmount.setText(getActivity().getResources().getString(R.string.amount, personData.getReceivingAmount()));
        tvTotalSendAmount.setText(getActivity().getResources().getString(R.string.amount, personData.getPayingAmount()));
        tvTotalAmountPaid.setText(getActivity().getResources().getString(R.string.amount, personData.getTotalAmountPaid()));

        tvChipText.setText(personData.getPersonName().substring(0, 2).toUpperCase());
        chipCardView.setCardBackgroundColor(personData.getPersonColor());

        pbMoneyStatus.setMax((int) (personData.getReceivingAmount() + personData.getPayingAmount()));
        pbMoneyStatus.setProgress((int) personData.getReceivingAmount());
        pbMoneyStatus.setSecondaryProgress(pbMoneyStatus.getMax());
    }

    private List<TravellerItemObject> sortPersonData(List<PersonData> personDataList) {
        for (PersonData data : personDataList) {
            if (data.getMobileNumber().equals(mobileNumber)) {
                sortTransactionData(data.getPaymentData(), data.getPersonColor());
            }
        }

        updateFinalPersonList(personDataList);

        return finalPeopleList;
    }

    private void sortTransactionData(PaymentDetailsData detailsData, int color) {
        Iterator<PaymentDetailsData.Details> receiveDetailIteration = detailsData.getReceiveDetails().listIterator();

        while (receiveDetailIteration.hasNext()) {
            PaymentDetailsData.Details receiveDetails = receiveDetailIteration.next();
            if (receiveDetails.getAmount() <= 0) {
                receiveDetailIteration.remove();
            } else {
                TravellerItemObject itemObject = new TravellerItemObject();
                itemObject.setDetailsData(receiveDetails);
                itemObject.setReceive(true);

                finalPeopleList.add(itemObject);
            }
        }

        Iterator<PaymentDetailsData.Details> sendDetailIterator = detailsData.getSendDetails().listIterator();

        while (sendDetailIterator.hasNext()) {
            PaymentDetailsData.Details sendDetails = sendDetailIterator.next();
            if (sendDetails.getAmount() <= 0) {
                sendDetailIterator.remove();
            } else {
                TravellerItemObject itemObject = new TravellerItemObject();
                itemObject.setDetailsData(sendDetails);
                itemObject.setReceive(false);

                finalPeopleList.add(itemObject);
            }
        }
    }

    private void updateFinalPersonList(List<PersonData> personDataList) {
        for (int i = 0; i < personDataList.size(); i++) {
            PersonData data = personDataList.get(i);

            for (int j = 0; j < finalPeopleList.size(); j++) {
                TravellerItemObject itemObject = finalPeopleList.get(j);

                if (data.getMobileNumber().equals(itemObject.getDetailsData().getMobileNumber())) {
                    itemObject.setPersonColor(data.getPersonColor());
                }
            }
        }
    }

    class TravellerItemObject {
        private int personColor;

        private PaymentDetailsData.Details detailsData;

        private boolean isReceive;

        int getPersonColor() {
            return personColor;
        }

        void setPersonColor(int personColor) {
            this.personColor = personColor;
        }

        PaymentDetailsData.Details getDetailsData() {
            return detailsData;
        }

        void setDetailsData(PaymentDetailsData.Details detailsData) {
            this.detailsData = detailsData;
        }

        boolean isReceive() {
            return isReceive;
        }

        void setReceive(boolean receive) {
            isReceive = receive;
        }
    }
}
