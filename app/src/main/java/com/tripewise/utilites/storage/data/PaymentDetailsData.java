package com.tripewise.utilites.storage.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentDetailsData {
    @SerializedName("paid_details")
    private ArrayList<Details> paidDetails;

    @SerializedName("receive_details")
    private ArrayList<Details> receiveDetails;

    @SerializedName("send_details")
    private ArrayList<Details> sendDetails;

    public static class Details {
        @SerializedName("name")
        private String name;

        @SerializedName("amount")
        private long amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }
    }

    public ArrayList<Details> getPaidDetails() {
        return paidDetails;
    }

    public void setPaidDetails(ArrayList<Details> paidDetails) {
        this.paidDetails = paidDetails;
    }

    public ArrayList<Details> getReceiveDetails() {
        return receiveDetails;
    }

    public void setReceiveDetails(ArrayList<Details> receiveDetails) {
        this.receiveDetails = receiveDetails;
    }

    public ArrayList<Details> getSendDetails() {
        return sendDetails;
    }

    public void setSendDetails(ArrayList<Details> sendDetails) {
        this.sendDetails = sendDetails;
    }

    public void setPaidData(Details data) {
        if (getPaidDetails() == null) {
            ArrayList<Details> details = new ArrayList<>();
            details.add(data);

            setPaidDetails(details);
        } else {
            getPaidDetails().add(data);
        }
    }

    public void setReceiveData(Details data) {
        if (getReceiveDetails() == null) {
            ArrayList<Details> details = new ArrayList<>();
            details.add(data);

            setReceiveDetails(details);
        } else {
            getReceiveDetails().add(data);
        }
    }

    public void setSendData(Details data) {
        if (getSendDetails() == null) {
            ArrayList<Details> details = new ArrayList<>();
            details.add(data);

            setSendDetails(details);
        } else {
            getSendDetails().add(data);
        }
    }
}
