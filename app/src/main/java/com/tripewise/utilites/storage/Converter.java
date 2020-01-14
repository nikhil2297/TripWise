package com.tripewise.utilites.storage;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PersonData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public String formBillPeopleList(List<BillData.BillPeople> billPeople) {
        if (billPeople == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BillData.BillPeople>>() {
        }.getType();
        String json = gson.toJson(billPeople, type);
        return json;
    }

    @TypeConverter
    public List<BillData.BillPeople> toBillPeopleList(String billPeople) {
        if (billPeople == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BillData.BillPeople>>() {
        }.getType();
        List<BillData.BillPeople> billPeopleList = gson.fromJson(billPeople, type);
        return billPeopleList;
    }
}
