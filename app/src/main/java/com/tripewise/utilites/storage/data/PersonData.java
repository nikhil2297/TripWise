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
    private long totalAmountPaid;

    @ColumnInfo(name = "trip_id")
    private int tripId;

    @ColumnInfo(name = "receiving_amount")
    private long receivingAmount;

    @ColumnInfo(name = "paying_amount")
    private long payingAmount;

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

    public long getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(long totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public long getReceivingAmount() {
        return receivingAmount;
    }

    public void setReceivingAmount(long receivingAmount) {
        this.receivingAmount = receivingAmount;
    }

    public long getPayingAmount() {
        return payingAmount;
    }

    public void setPayingAmount(long payingAmount) {
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
        PaymentDetailsData detailsData = new Gson().fromJson(paymentDetails, PaymentDetailsData.class);
        this.paymentDetails = paymentDetails;
    }

    public PaymentDetailsData getPaymentData() {
        return new Gson().fromJson(paymentDetails, PaymentDetailsData.class);
    }

    public void setPaymentData(PaymentDetailsData data){
        setPaymentDetails(new Gson().toJson(data).toString());
    }
}
