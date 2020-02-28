package com.tripewise.bills;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripewise.utilites.storage.communication.CommunicationConstants;
import com.tripewise.utilites.storage.communication.CommunicationHelper;
import com.tripewise.utilites.storage.data.BillData;

import java.util.List;

class BillViewModel extends ViewModel {
    private LiveData<List<BillData>> billDataList;

    LiveData<List<BillData>> getBillData(Context context, Object... objects) {
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_GET);
        helper.setObject(objects);
        helper.setCallBack(new CommunicationHelper.HelperCallBack() {
            @Override
            public void getResponse(Object object) {
                billDataList = (LiveData<List<BillData>>) object;
            }
        });

        helper.sendToDestination();

        return billDataList;
    }

    void insertBillData(Context context, Object... objects) {
        CommunicationHelper helper = new CommunicationHelper(context);
        helper.setActionType(CommunicationConstants.TYPE_INSERT);
        helper.setObject(objects);
        helper.sendToDestination();
    }
}
