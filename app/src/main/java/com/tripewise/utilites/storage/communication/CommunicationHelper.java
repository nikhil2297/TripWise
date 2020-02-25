package com.tripewise.utilites.storage.communication;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.tripewise.utilites.storage.DbAsyncConfig;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class CommunicationHelper {
    private Context context;

    private Object[] objects;

    private String actionType;

    private HelperCallBack callBack;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setObject(Object... objects) {
        this.objects = objects;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setCallBack(HelperCallBack callBack){
        this.callBack = callBack;
    }

    public void sendToDestination() {
        if (actionType.equals(CommunicationConstants.TYPE_GET)){
                callBack.getAsyncResponse(fetchLiveDataFromDb());
        }else if (actionType.equals(CommunicationConstants.TYPE_INSERT)){

        }else if (actionType.equals(CommunicationConstants.TYPE_DELETE)){

        }else if (actionType.equals(CommunicationConstants.TYPE_UPDATE)){

        }
    }

    private <T> LiveData<Object> fetchLiveDataFromDb(){
        DbAsyncConfig dbAsyncConfig = new DbAsyncConfig(context);
        return dbAsyncConfig.getObservableData(objects);
    }

    public interface HelperCallBack {
        void getAsyncResponse(Object o);
    }
}
