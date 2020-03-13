package com.tripewise.people;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripewise.utilites.storage.communication.CommunicationConstants;
import com.tripewise.utilites.storage.communication.CommunicationHelper;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.List;

public class PeopleViewModel extends ViewModel {
    private LiveData<List<PersonData>> personDataList;

    public LiveData<List<PersonData>> fetchPersonDetails(Context context, int tripId){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setObject(PersonData.class.getSimpleName(), tripId);
        helper.setActionType(CommunicationConstants.TYPE_GET);
        helper.setCallBack(new CommunicationHelper.HelperCallBack() {
            @Override
            public void getResponse(Object object) {
                personDataList = (LiveData<List<PersonData>>) object;
            }
        });

        helper.sendToDestination();
        return personDataList;
    }

    public void insertPersonDetails(Context context, List<PersonData> personData){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setObject(PersonData.class.getSimpleName(), personData);
        helper.setActionType(CommunicationConstants.TYPE_INSERT);
        helper.sendToDestination();
    }

    public void updatePersonDetails(Context context, BillData data, List<PersonData> personData, int actionType){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setObject(PersonData.class.getSimpleName(), data, personData, actionType);
        helper.setActionType(CommunicationConstants.TYPE_UPDATE);
        helper.sendToDestination();
    }
}
