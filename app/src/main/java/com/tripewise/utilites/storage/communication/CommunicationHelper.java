package com.tripewise.utilites.storage.communication;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tripewise.utilites.storage.DbAsyncConfig;

import java.util.concurrent.ExecutionException;

public class CommunicationHelper {
    private static final String TAG = CommunicationHelper.class.getSimpleName();

    private Object[] objects;

    private String actionType;

    private DbAsyncConfig dbAsyncConfig;

    private HelperCallBack callBack;

    public CommunicationHelper(Context context){
        dbAsyncConfig = new DbAsyncConfig(context);
    }

    public void setObject(Object... objects) {
        this.objects = objects;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setCallBack(HelperCallBack callBack) {
        this.callBack = callBack;
    }

    public void sendToDestination() {
        switch (actionType) {
            case CommunicationConstants.TYPE_GET:
                Log.e(TAG, "SendToDestination : " + "Action type :" + CommunicationConstants.TYPE_GET);
                callBack.getResponse(fetchLiveDataFromDb());
                break;
            case CommunicationConstants.TYPE_INSERT:
                Log.e(TAG, "SendToDestination : " + "Action type :" + CommunicationConstants.TYPE_INSERT);
                insertDataIntoDb();
                break;
            case CommunicationConstants.TYPE_UPDATE:
                Log.e(TAG, "SendToDestination : " + "Action type :" + CommunicationConstants.TYPE_UPDATE);
                updateDataIntoDb();
                break;
            case CommunicationConstants.TYPE_DELETE:
                Log.e(TAG, "SendToDestination : " + "Action type :" + CommunicationConstants.TYPE_DELETE);
                deleteDataFromDb();
                break;
                default:
                    Log.e(TAG, "SendToDestination : " + "Action type :" + "Unknown");
        }
    }

    private <T> LiveData<?> fetchLiveDataFromDb() {
        return dbAsyncConfig.getObservableData(objects);
    }

    private void insertDataIntoDb(){
        try {
            dbAsyncConfig.insertDataToDb(objects);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateDataIntoDb(){
        try {
            dbAsyncConfig.updateDataToDb(objects);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void deleteDataFromDb(){
        try {
            dbAsyncConfig.deleteDataFromDb(objects);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface HelperCallBack {
        void getResponse(Object object);
    }
}