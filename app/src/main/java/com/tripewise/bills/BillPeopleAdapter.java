package com.tripewise.bills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.tripewise.R;
import com.tripewise.utilites.storage.data.BillData;

import java.util.ArrayList;

public class BillPeopleAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<BillData.BillPeople> billPeople;

    private boolean isPaid;
    private boolean isSelectAll;

    BillPeopleAdapter(Context context, ArrayList<BillData.BillPeople> billPeople, boolean isPaid, boolean isSelectAll) {
        this.billPeople = billPeople;
        this.context = context;
        this.isPaid = isPaid;
        this.isSelectAll = isSelectAll;
    }

    @Override
    public int getCount() {
        return billPeople.size();
    }

    @Override
    public Object getItem(int i) {
        return billPeople.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_bill_paid_item, viewGroup, false);

        TextView tvBillPeopleName = view.findViewById(R.id.tv_person_name);
        TextView tvBillAmount = view.findViewById(R.id.tv_amount_to_paid);

        final EditText etAmount = view.findViewById(R.id.et_paid_amount);

        CheckBox checkBox = view.findViewById(R.id.cb_paid);

        if (isPaid) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        etAmount.setVisibility(View.VISIBLE);
                    } else {
                        etAmount.setVisibility(View.GONE);
                    }
                }
            });
        }

        if (isSelectAll) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        tvBillPeopleName.setText(billPeople.get(i).getPeopleName());
        tvBillAmount.setText(context.getResources().getString(R.string.amount, billPeople.get(i).getAmount()));

        return view;
    }
}
