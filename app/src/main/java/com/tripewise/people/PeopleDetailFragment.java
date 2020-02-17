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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
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

        finalPeopleList = new ArrayList<>();

        asyncConfig = new PeopleAsyncConfig(getActivity());

        getPersonData();
    }

    private void getPersonData(){
        asyncConfig.getPersonData(tripId).observe(getViewLifecycleOwner(), new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personDataList) {

            }
        });
    }

    private List<TravellerItemObject> sortPersonData(List<PersonData> personDataList){
        for (PersonData data : personDataList){
            if (data.getMobileNumber().equals(mobileNumber)) {
                sortTransactionData(data.getPaymentData(), data.getPersonColor());
            }
        }

        updateFinalPersonList(personDataList);

        return finalPeopleList;
    }

    private void sortTransactionData(PaymentDetailsData detailsData, int color){
        Iterator<PaymentDetailsData.Details> receiveDetailIteration = detailsData.getReceiveDetails().listIterator();

        while (receiveDetailIteration.hasNext()){
            if (receiveDetailIteration.next().getAmount() <= 0){
                receiveDetailIteration.remove();
            }else {
                TravellerItemObject itemObject = new TravellerItemObject();
                itemObject.setDetailsData(receiveDetailIteration.next());
                itemObject.setReceive(true);

                finalPeopleList.add(itemObject);
            }
        }

        Iterator<PaymentDetailsData.Details> sendDetailIterator = detailsData.getSendDetails().listIterator();

        while (sendDetailIterator.hasNext()){
            if (sendDetailIterator.next().getAmount() <= 0){
                sendDetailIterator.remove();
            }else {
                TravellerItemObject itemObject = new TravellerItemObject();
                itemObject.setDetailsData(receiveDetailIteration.next());
                itemObject.setReceive(false);

                finalPeopleList.add(itemObject);
            }
        }
    }

    private void updateFinalPersonList(List<PersonData> personDataList){
        for (int i = 0 ; i < personDataList.size() ; i++){
            PersonData data = personDataList.get(i);

            for (int j = 0 ; j < finalPeopleList.size() ; j++){
                TravellerItemObject itemObject = finalPeopleList.get(j);

                if (data.getMobileNumber().equals(itemObject.getDetailsData().getMobileNumber())){
                    itemObject.setPersonColor(data.getPersonColor());
                }
            }
        }
    }

    class TravellerItemObject {
        private int personColor;

        private PaymentDetailsData.Details detailsData;

        private boolean isReceive;

        public int getPersonColor() {
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

        public boolean isReceive() {
            return isReceive;
        }

        public void setReceive(boolean receive) {
            isReceive = receive;
        }
    }
}
