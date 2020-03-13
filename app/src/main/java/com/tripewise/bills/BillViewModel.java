package com.tripewise.bills;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripewise.utilites.storage.communication.CommunicationConstants;
import com.tripewise.utilites.storage.communication.CommunicationHelper;
import com.tripewise.utilites.storage.data.BillData;

import java.util.List;

public class BillViewModel extends ViewModel {
    private LiveData<List<BillData>> billDataList;

    LiveData<List<BillData>> getBillData(Context context, int tripId) {
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_GET);
        helper.setObject(BillData.class.getSimpleName(), tripId);
        helper.setCallBack(new CommunicationHelper.HelperCallBack() {
            @Override
            public void getResponse(Object object) {
                billDataList = (LiveData<List<BillData>>) object;
            }
        });

        helper.sendToDestination();

        return billDataList;
    }

    void insertBillData(Context context, BillData data) {
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_INSERT);
        helper.setObject(BillData.class.getSimpleName(), data);
        helper.sendToDestination();
    }

    void updateBillData(Context context, BillData data){
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_UPDATE);
        helper.setObject(BillData.class.getSimpleName(), data);
        helper.sendToDestination();
    }

    void deleteBillData(Context context, BillData data) {
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_DELETE);
        helper.setObject(BillData.class.getSimpleName(), data);
        helper.sendToDestination();
    }
}
