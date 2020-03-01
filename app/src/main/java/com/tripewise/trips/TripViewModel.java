package com.tripewise.trips;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripewise.utilites.storage.communication.CommunicationConstants;
import com.tripewise.utilites.storage.communication.CommunicationHelper;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class TripViewModel extends ViewModel {
    private LiveData<List<TripData>> tripDataList;

    public LiveData<List<TripData>> fetchTripData(Context context){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_GET);
        helper.setObject(TripData.class.getSimpleName());
        helper.setCallBack(new CommunicationHelper.HelperCallBack() {
            @Override
            public void getResponse(Object object) {
                tripDataList = (LiveData<List<TripData>>) object;
            }
        });
        helper.sendToDestination();

        return tripDataList;
    }

    public void insertTrip(Context context, TripData data){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_INSERT);
        helper.setObject(TripData.class.getSimpleName(), data);
        helper.sendToDestination();
    }
}
