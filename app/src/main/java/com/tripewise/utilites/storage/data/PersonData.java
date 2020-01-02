package com.tripewise.utilites.storage.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.tripewise.utilites.storage.Converter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PersonData {
    @ColumnInfo(name = "person_name")
    private String personName;

    @ColumnInfo(name = "total_amount_paid")
    private int totalAmountPaid;

    @ColumnInfo(name = "bill_id")
    private ArrayList<Integer> billIds;

    @ColumnInfo(name = "receiving_amount")
    private int receivingAmount;

    @ColumnInfo(name = "paying_amount")
    private int payingAmount;

    @ColumnInfo(name = "paying_people")
    private List<PeopleData> payingPeople;

    @ColumnInfo(name = "receiving_people")
    private List<PeopleData> receivingPeople;

    public class PeopleData{
        @ColumnInfo(name = "people_data_name")
        private String peopleName;

        @ColumnInfo(name = "people_data_amount")
        private int amount;

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
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(int totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public ArrayList<Integer> getBillIds() {
        return billIds;
    }

    public void setBillIds(ArrayList<Integer> billIds) {
        this.billIds = billIds;
    }

    public int getReceivingAmount() {
        return receivingAmount;
    }

    public void setReceivingAmount(int receivingAmount) {
        this.receivingAmount = receivingAmount;
    }

    public int getPayingAmount() {
        return payingAmount;
    }

    public void setPayingAmount(int payingAmount) {
        this.payingAmount = payingAmount;
    }

    @TypeConverters({Converter.class})
    public List<PeopleData> getPayingPeople() {
        return payingPeople;
    }

    public void setPayingPeople(List<PeopleData> payingPeople) {
        this.payingPeople = payingPeople;
    }

    @TypeConverters({Converter.class})
    public List<PeopleData> getReceivingPeople() {
        return receivingPeople;
    }

    public void setReceivingPeople(List<PeopleData> receivingPeople) {
        this.receivingPeople = receivingPeople;
    }
}
