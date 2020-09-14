package com.tripewise.utilites.storage;

import android.util.Log;

import com.google.gson.Gson;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.PaymentDetailsData;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.ArrayList;
import java.util.List;

public class PersonHelper {
    private final static int ACTION_ADD = 1;
    private final static int ACTION_DELETE = 2;

    private final String TAG = this.getClass().getSimpleName();
    private BillData billData;

    private int actionType;

    private List<PersonData> personData;

    public PersonHelper(BillData billData, List<PersonData> personData, int actionType) {
        this.billData = billData;
        this.personData = personData;
        this.actionType = actionType;
    }

    /**
     * Step :
     * 1. First it create or update the entry for Paid bill details
     * 2. Sort receiving details for every person
     * 3. Sort sending details for every person
     * 4. Do final calculation and sort out all the sending and receiving amount
     * 5. get the total of paid amount, receiving amount, sending amount
     *
     * @return updated person list
     */
    public List<PersonData> initPersonData() {

        getPaidPeopleData();

        for (PersonData data : personData) {
            sortReceivingDetails(data);
        }

        for (PersonData data : personData) {
            sortSendDetails(data);
        }

        for (PersonData data : personData) {
            calculatePaymentDetails(data);
        }

        for (PersonData data : personData) {
            getTotalPayingAmount(data);

            getTotalReceivingAmount(data);

            getTotalPaidAmount(data);
        }

        return personData;
    }

    /**
     * Create a entry or update the current data
     * Step :
     * 1. Check the person data with bill paid people list
     * 2. if true then it sort the paid data details else do nothing
     * 3. and update the person data
     */
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

    //********************************************************************************************//
    //*************************Sort Bill Paid Details block***************************************//
    //********************************************************************************************//

    /**
     * Step :
     * 1. Check for person data if not null
     * 2. Null check for person paid details
     * 2.1) if null then it initialize new array list so the it can ad new data
     * 2.2) if not null then it initialize to current paid details
     * 3. Create a new Payment details data in which it add Person name, Bill amount paid, bill name
     * 4. Then this details data will be added to paid details
     * 5. Then we update the setPaymentDetails
     *
     * @param personData To get person details and update paid bill data
     * @param billPeople Used to list of people who paid bill
     */
    private void sortPaidDetails(PersonData personData, BillData.BillPeople billPeople) {
        if (personData != null) {
            PaymentDetailsData detailsData = personData.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsDataList;

            if (detailsData.getPaidDetails() != null) {
                detailsDataList = detailsData.getPaidDetails();
            } else {
                detailsDataList = new ArrayList<>();
            }

            if (actionType == ACTION_ADD) {
                PaymentDetailsData.Details details = new PaymentDetailsData.Details();

                details.setName(billPeople.getPeopleName());
                details.setAmount(billPeople.getAmount());
                details.setBillName(billData.getBillName());
                details.setMobileNumber(personData.getMobileNumber());

                detailsDataList.add(details);
            } else if (actionType == ACTION_DELETE && !detailsDataList.isEmpty()) {
                for (PaymentDetailsData.Details details : detailsDataList) {
                    if (details.getBillName().equals(billData.getBillName())) {
                        detailsDataList.remove(details);
                    }
                }
            }

            detailsData.setPaidDetails(detailsDataList);

            personData.setPaymentDetails(new Gson().toJson(detailsData).toString());
        }
    }

    //********************************************************************************************//
    //****************************Sort Amount Receive Details block*******************************//
    //********************************************************************************************//

    /**
     * Step:
     * 1. Null check for person data and Person paid details data
     * 2. Create a array list of person receive details data
     * 3. Then we create a loop for bill paid people list and check that the person data mobile is available in bill paid people.
     * 3.1 If false then we just the set the current array list which created in step 2 as receive details for the specific person
     * 3.2 If true then we create loop of receiving people list size so that we can update all the receiving amount
     * 4. We also create a billPeople list to check the receiving amount we want to update of the people are include in the bill or not
     * 4.1 If false then we just the set the current array list which created in step 2 as receive details for the specific person
     * 4.2 If true then we update the receiving amount and update the receiving details.
     *
     * @param data To fetch the mobile number and receiving details.
     */

    private void sortReceivingDetails(PersonData data) {
        if (data != null && data.getPaymentData().getPaidDetails() != null) {
            PaymentDetailsData paymentDetailsData = data.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsArrayList = paymentDetailsData.getReceiveDetails();

            int receivingPeopleSize = paymentDetailsData.getReceiveDetails().size();
            int paidPeopleSize = billData.getBillPaidPeopleList().size();
            int billPeopleSize = billData.getBillPeopleList().size();

            for (int i = 0; i < paidPeopleSize; i++) {
                if (data.getMobileNumber().equals(billData.getBillPaidPeopleList().get(i).getPeopleNumber())) {
                    for (int j = 0; j < receivingPeopleSize; j++) {
                        PaymentDetailsData.Details details = paymentDetailsData.getReceiveDetails().get(j);

                        for (int k = 0; k < billPeopleSize; k++) {
                            BillData.BillPeople billPeople = billData.getBillPeopleList().get(k);

                            if (details.getMobileNumber().equals(billPeople.getPeopleNumber())) {
                                if (actionType == ACTION_ADD) {
                                    details.setAmount(details.getAmount() + (billData.getBillPaidPeopleList().get(i).getAmount() / billPeopleSize));
                                } else if (actionType == ACTION_DELETE) {
                                    details.setAmount(details.getAmount() - (billData.getBillPaidPeopleList().get(i).getAmount() / billPeopleSize));
                                }
                                details.setName(details.getName());
                            }
                        }

                        detailsArrayList.set(j, details);
                    }
                }
            }
            paymentDetailsData.setReceiveDetails(detailsArrayList);

            data.setPaymentData(paymentDetailsData);
        }
    }

    //********************************************************************************************//
    //***************************Sort Amount Send Details block***********************************//
    //********************************************************************************************//

    /**
     * Step:
     * 1. Null check for person data and Person paid details data
     * 2. Create a array list of person send details data
     * 3. Then we create loop for sendPeople and also another loop for bill paidPeople
     * 4. Then we check person data mobile number is not present in bill paidPeople list
     * 4.1 If false the he/she will be the one will be receiving the amount
     * 4.2 Else we create a Send Payment details object for each person
     * 5. Then we check the mobile number of each person in Send Payment details object we created with the bill paid people list
     * 5.1 If false then we set the previous amount as a sending amount for that person
     * 5.2 Else we create a loop of bill people list
     * 6. Then we create a billPeople object for each person in the list then we check that the person data mobile number is included in bill people list
     * 6.1 If false then we just update the array list created in step 2 with current billPeople object for that person
     * 6.2 Else we update the amount of the billPeople object person and also update the array list created in step 2
     * 7. Last we update the Send details data and also update payment data
     *
     * @param data To fetch the mobile number and sending details.
     */

    private void sortSendDetails(PersonData data) {
        if (data != null) {
            PaymentDetailsData paymentDetailsData = data.getPaymentData();

            ArrayList<PaymentDetailsData.Details> detailsArrayList = data.getPaymentData().getSendDetails();

            int sendingPeopleSize = paymentDetailsData.getSendDetails().size();
            int paidPeopleSize = billData.getBillPaidPeopleList().size();
            int billPeopleSize = billData.getBillPeopleList().size();

            for (int i = 0; i < sendingPeopleSize; i++) {
                for (int j = 0; j < paidPeopleSize; j++) {
                    String billPersonNumber = billData.getBillPaidPeopleList().get(j).getPeopleNumber();

                    if (!data.getMobileNumber().equals(billPersonNumber)) {
                        PaymentDetailsData.Details details = paymentDetailsData.getSendDetails().get(i);

                        if (data.getPaymentData().getSendDetails().get(i).getMobileNumber().equals(billPersonNumber)) {
                            for (int k = 0; k < billPeopleSize; k++) {
                                BillData.BillPeople billPeople = billData.getBillPeopleList().get(k);

                                if (data.getMobileNumber().equals(billPeople.getPeopleNumber())) {
                                    if (actionType == ACTION_ADD) {
                                        details.setAmount(details.getAmount() + (billData.getBillPaidPeopleList().get(j).getAmount() / billData.getBillPeopleList().size()));
                                    } else if (actionType == ACTION_DELETE) {
                                        details.setAmount(details.getAmount() - (billData.getBillPaidPeopleList().get(j).getAmount() / billData.getBillPeopleList().size()));
                                    }
                                }
                            }
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

    //*********************************************************************//
    //***************Sort Total Receiving Amount Block********************//
    //*******************************************************************//

    /**
     * Step
     * 1. Null check for person data and Person paid details data
     * 2. Create a loop for ReceiveDetails and get the total all the receiving amount in it
     * 3. Then we set it to person data total receiving amount
     *
     * @param data Get the receiving details and update the total receiving amount
     */
    private void getTotalReceivingAmount(PersonData data) {
        if (data != null) {
            data.setReceivingAmount(0);

            PaymentDetailsData paymentData = data.getPaymentData();

            for (PaymentDetailsData.Details details : paymentData.getReceiveDetails()) {
                data.setReceivingAmount(data.getReceivingAmount() + details.getAmount());
            }

            Log.d("PersonUitls", "Name : " + data.getPersonName() + " Total Receving amount : " + data.getReceivingAmount());
        }
    }

    //*********************************************************************//
    //******************Sort Total Send Amount Block**********************//
    //*******************************************************************//

    /**
     * Step
     * 1. Null check for person data
     * 2. Create a loop for SendingDetails and get the total all the sending amount in it
     * 3. Then we set it to person data total paying amount
     *
     * @param data Get the sending details and update the total paying amount
     */
    private void getTotalPayingAmount(PersonData data) {
        if (data != null) {
            data.setPayingAmount(0);

            PaymentDetailsData paymentData = data.getPaymentData();

            for (PaymentDetailsData.Details details : paymentData.getSendDetails()) {
                data.setPayingAmount(data.getPayingAmount() + details.getAmount());
            }

            Log.d("PersonUitls", "Name : " + data.getPersonName() + " Total Paying amount : " + data.getPayingAmount());
        }
    }


    //*********************************************************************//
    //********************Sort Total Paid Amount Block********************//
    //*******************************************************************//

    /**
     * Step
     * 1. Null check for person
     * 2. Create a loop for PaidDetails and get the total all the paid amount in it
     * 3. Then we set it to person data total paid amount
     *
     * @param data Get the paid details and update the total paid amount
     */
    private void getTotalPaidAmount(PersonData data) {
        if (data != null) {
            data.setTotalAmountPaid(0);

            PaymentDetailsData paymentData = data.getPaymentData();

            if (paymentData.getPaidDetails() != null) {
                for (PaymentDetailsData.Details details : paymentData.getPaidDetails()) {
                    if (details != null) {
                        data.setTotalAmountPaid(data.getTotalAmountPaid() + details.getAmount());
                    }
                }
            }

            Log.d("PersonUitls", "Name : " + data.getPersonName() + " Total Paid amount : " + data.getTotalAmountPaid());
        }
    }

    //*********************************************************************//
    //********************Sort Final calculation Block********************//
    //*******************************************************************//

    /**
     * Step :
     * 1. Create a PaymentDetailsData object from personData payment data
     * 2. We create a two loop of receivingPeople and sendingPeople and also create object for each person
     * 3. We check if receivingPeople mobile number is equal to sendingPeople number
     * 4. Then we subtract receivingAmount with sendingAmount and check the final result is greater then zero
     * 4.1 If true then final result is set as receiving amount and zero is set as sending amount
     * 4.2 Else vice versa
     * 5. Then we update the payment data in personData.
     *
     * @param personData get ReceivingData and sending data to update payment data.
     */

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

                    Log.e(TAG, "Receiving Person Name = " + receivingDetails.getName() + " Send Person Name = " + sendingDetails.getName() + " Final Result = " + finalResult);

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
