package com.tripewise.utilites.storage.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.ArrayList;

@Entity
public class PersonData {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "person_name")
    private String personName;

    @ColumnInfo(name = "total_amount_paid")
    private int totalAmountPaid;

    @ColumnInfo(name = "trip_id")
    private int tripId;

    @ColumnInfo(name = "bill_id")
    private ArrayList<Integer> billIds;

    @ColumnInfo(name = "receiving_amount")
    private int receivingAmount;

    @ColumnInfo(name = "paying_amount")
    private int payingAmount;

    @ColumnInfo(name = "paying_details")
    private String paymentDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public PaymentDetailsData getPaymentData() {
        return new Gson().fromJson(paymentDetails, PaymentDetailsData.class);
    }
}
