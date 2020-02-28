package com.tripewise.people;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripewise.utilites.storage.communication.CommunicationConstants;
import com.tripewise.utilites.storage.communication.CommunicationHelper;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.List;

public class PeopleViewModel extends ViewModel {
    private LiveData<List<PersonData>> personDataList;

    public LiveData<List<PersonData>> fetchPersonDetails(Context context, Object... objects){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setObject(objects);
        helper.setActionType(CommunicationConstants.TYPE_GET);
        helper.setCallBack(new CommunicationHelper.HelperCallBack() {
            @Override
            public void getResponse(Object object) {
                personDataList = (LiveData<List<PersonData>>) object;
            }
        });

        return personDataList;
    }

    private void insertPersonDetails(Context context, Object... objects){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setObject(objects);
        helper.setActionType(CommunicationConstants.TYPE_UPDATE);
        helper.sendToDestination();
    }
}
