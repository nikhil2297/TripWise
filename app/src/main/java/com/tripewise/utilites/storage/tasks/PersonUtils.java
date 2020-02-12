package com.tripewise.utilites.storage.tasks;

import com.google.gson.Gson;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.ArrayList;
import java.util.List;

public class PersonUtils {
    private BillData billData;

    private List<PersonData> personData;

    PersonUtils(BillData billData, List<PersonData> personData) {
        this.billData = billData;
        this.personData = personData;
    }

    List<PersonData> initPersonData() {

        getPaidPeopleData();

        for (PersonData data : personData) {
            sortReceivingDetails(data);
        }

        for (PersonData data : personData) {
            sortSendDetails(data);
        }

        return personData;
    }

    private void getPaidPeopleData() {
        for (int i = 0; i < personData.size(); i++) {
            for (int j = 0; j < billData.getBillPaidPeopleList().size(); j++) {
                if (personData.get(i).getMobileNumber().equals(billData.getBillPaidPeopleList().get(j).getPeopleNumber())) {
                    sortPaidDetails(personData.get(i), billData.getBillPaidPeopleList().get(j));
                    break;
                }
            }
        }
    }

    private void sortPaidDetails(PersonData personData, BillData.BillPeople billPeople) {
        if (personData != null) {
            PaymentDetailsData detailsData = personData.getPaymentData();

            PaymentDetailsData.Details details = new PaymentDetailsData.Details();

            details.setName(billPeople.getPeopleName());
            details.setAmount(billPeople.getAmount());
            details.setBillName(billData.getBillName());
            details.setMobileNumber(personData.getMobileNumber());

            detailsData.setPaidData(details);

            personData.setPaymentDetails(new Gson().toJson(detailsData).toString());
        }
    }

    private void sortReceivingDetails(PersonData data) {
        if (data != null && data.getPaymentData().getPaidDetails() != null) {
            PaymentDetailsData paymentDetailsData = data.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsArrayList = paymentDetailsData.getReceiveDetails();

            int receivingPeopleSize = paymentDetailsData.getReceiveDetails().size();
            int paidPeopleSize = billData.getBillPaidPeopleList().size();

            for (int i = 0; i < paidPeopleSize; i++) {
                if (data.getMobileNumber().equals(billData.getBillPaidPeopleList().get(i).getPeopleNumber())) {
                    for (int j = 0; j < receivingPeopleSize; j++) {
                        PaymentDetailsData.Details details = paymentDetailsData.getReceiveDetails().get(j);

                        details.setAmount(details.getAmount() + (billData.getBillPaidPeopleList().get(i).getAmount() / billData.getBillPeopleList().size()));
                        details.setName(details.getName());

                        detailsArrayList.set(j, details);
                    }
                }
            }
            paymentDetailsData.setReceiveDetails(detailsArrayList);

            data.setPaymentData(paymentDetailsData);
        }
    }

    private void sortSendDetails(PersonData data) {
        if (data != null) {
            PaymentDetailsData paymentDetailsData = data.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsArrayList = data.getPaymentData().getSendDetails();

            int sendingPeopleSize = paymentDetailsData.getSendDetails().size();
            int paidPeopleSize = billData.getBillPaidPeopleList().size();

            for (int i = 0; i < sendingPeopleSize; i++) {
                for (int j = 0; j < paidPeopleSize; j++) {
                    String billPersonNumber = billData.getBillPaidPeopleList().get(j).getPeopleNumber();

                    if (!data.getMobileNumber().equals(billPersonNumber)) {
                        PaymentDetailsData.Details details = paymentDetailsData.getSendDetails().get(i);

                        if (data.getPaymentData().getSendDetails().get(i).getMobileNumber().equals(billPersonNumber)) {
                            details.setAmount(details.getAmount() + (billData.getBillPaidPeopleList().get(j).getAmount() / billData.getBillPeopleList().size()));
                        } else {
                            details.setAmount(details.getAmount());
                        }

                        detailsArrayList.set(i, details);
                    }
                }
            }

            paymentDetailsData.setSendDetails(detailsArrayList);

            data.setPaymentData(paymentDetailsData);
        }
    }

    List<PersonData> finalCalculation() {

        for (PersonData data : personData) {
            calculatePaymentDetails(data);
        }

        return personData;
    }

    private void calculatePaymentDetails(PersonData personData) {
        PaymentDetailsData detailsData = personData.getPaymentData();

        int receivingPeopleSize = detailsData.getReceiveDetails().size();
        int sendingPeopleSize = detailsData.getSendDetails().size();

        for (int i = 0; i < receivingPeopleSize; i++) {
            PaymentDetailsData.Details receivingDetails = detailsData.getReceiveDetails().get(i);

            for (int j = 0; j < sendingPeopleSize; j++) {
                PaymentDetailsData.Details sendingDetails = detailsData.getSendDetails().get(j);

                if (receivingDetails.getMobileNumber().equals(sendingDetails.getMobileNumber())) {
                    long receiveAmount = receivingDetails.getAmount();
                    long sendingAmount = sendingDetails.getAmount();

                    long finalResult = receiveAmount - sendingAmount;

                    if (finalResult > 0) {

                        //Creating object for receiving data
                        PaymentDetailsData.Details details = new PaymentDetailsData.Details();
                        details.setAmount(finalResult);
                        details.setName(receivingDetails.getName());
                        details.setMobileNumber(receivingDetails.getMobileNumber());

                        detailsData.getReceiveDetails().set(i, details);

                        //Create object for sending data
                        PaymentDetailsData.Details details2 = new PaymentDetailsData.Details();
                        details2.setName(sendingDetails.getName());
                        details2.setAmount(0);
                        details2.setMobileNumber(sendingDetails.getMobileNumber());

                        detailsData.getSendDetails().set(j, details2);
                    } else {
                        //Creating object for receiving data
                        PaymentDetailsData.Details details = new PaymentDetailsData.Details();
                        details.setAmount(0);
                        details.setName(receivingDetails.getName());
                        details.setMobileNumber(receivingDetails.getMobileNumber());

                        detailsData.getReceiveDetails().set(i, details);

                        //Create object for sending data
                        PaymentDetailsData.Details details2 = new PaymentDetailsData.Details();
                        details2.setName(sendingDetails.getName());
                        details2.setAmount(Math.abs(finalResult));
                        details2.setMobileNumber(sendingDetails.getMobileNumber());

                        detailsData.getSendDetails().set(j, details2);
                    }
                }
            }
        }

        personData.setPaymentData(detailsData);
    }
}
