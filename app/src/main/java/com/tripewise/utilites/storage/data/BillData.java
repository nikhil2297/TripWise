package com.tripewise.utilites.storage.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.tripewise.utilites.storage.Converter;

import java.util.ArrayList;

//TODO : Still need to bill db table
@Entity
public class BillData {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "trip_id")
    private int tripId;

    @ColumnInfo(name = "bill_name")
    private String billName;

    @ColumnInfo(name = "bill_amount")
    private long billAmount;

    @ColumnInfo(name = "bill_time")
    private long billTime;

    @ColumnInfo(name = "bill_time_string")
    private String billTimeString;

    @ColumnInfo(name = "bill_people")
    private ArrayList<BillPeople> billPeopleList;

    public class BillPeople{
       @ColumnInfo(name = "bill_people_name")
       private String peopleName;

       @ColumnInfo(name = "bill_people_amount")
       private int amount;

       @ColumnInfo(name = "bill_people_amount_type")
       private int amountType;

        public String getPeopleName() {
            return peopleName;
        }

        public void setPeopleName(String peopleName) {
            this.peopleName = peopleName;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAmountType() {
            return amountType;
        }

        public void setAmountType(int amountType) {
            this.amountType = amountType;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public long getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(long billAmount) {
        this.billAmount = billAmount;
    }

    public long getBillTime() {
        return billTime;
    }

    public void setBillTime(long billTime) {
        this.billTime = billTime;
    }

    public String getBillTimeString() {
        return billTimeString;
    }

    public void setBillTimeString(String billTimeString) {
        this.billTimeString = billTimeString;
    }

    @TypeConverters({Converter.class})
    public ArrayList<BillPeople> getBillPeopleList() {
        return billPeopleList;
    }

    public void setBillPeopleList(ArrayList<BillPeople> billPeopleList) {
        this.billPeopleList = billPeopleList;
    }
}
