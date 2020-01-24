package com.tripewise.bills;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tripewise.R;
import com.tripewise.utilites.storage.data.BillData;

import java.util.ArrayList;

public class PeopleListDialogFragment extends DialogFragment implements View.OnClickListener {
    private PeopleListListener peopleListListener;

    private ArrayList<BillData.BillPeople> billPeople;
    private ArrayList<BillData.BillPeople> tempPeople;

    private boolean isPaid;
    private boolean isSelectAll;

    private LinearLayout llSelectAll;

    private CheckBox checkSelectAll;

    private Button btSave;
    private Button btCancel;

    private ListView lvPeople;

    private BillPeopleAdapter adapter;

    public static PeopleListDialogFragment newInstance(ArrayList<BillData.BillPeople> billPeople, boolean isPaid, PeopleListListener listListener) {
        PeopleListDialogFragment dialogFragment = new PeopleListDialogFragment();
        dialogFragment.billPeople = billPeople;
        dialogFragment.tempPeople = billPeople;
        dialogFragment.isPaid = isPaid;
        dialogFragment.peopleListListener = listListener;

        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.laytout_bill_people_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btSave = view.findViewById(R.id.bt_save);
        btCancel = view.findViewById(R.id.bt_cancel);

        llSelectAll = view.findViewById(R.id.ll_select_all);

        lvPeople = view.findViewById(R.id.lv_bill_people);

        checkSelectAll = llSelectAll.findViewById(R.id.is_check);

        lvPeople.setClickable(false);

        init();
    }

    private void init() {
        if (adapter == null) {
            adapter = new BillPeopleAdapter(getActivity(), billPeople, isPaid, isSelectAll);
            lvPeople.setAdapter(adapter);
        }

        if (isPaid) {
            llSelectAll.setVisibility(View.GONE);
        } else {
            llSelectAll.setVisibility(View.VISIBLE);
        }

        llSelectAll.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_all:
                if (checkSelectAll.isChecked()) {
                    checkSelectAll.setChecked(false);
                } else {
                    checkSelectAll.setChecked(true);
                }
                isSelectAll = checkSelectAll.isChecked();

                selectAll(isSelectAll);
                break;
            case R.id.bt_save:
                dismissAllowingStateLoss();
                break;
            case R.id.bt_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }

    private void selectAll(boolean isCheck) {
        for (int i = 0; i < billPeople.size(); i++) {
            billPeople.get(i).setCheck(isCheck);

            if (isCheck) {
                peopleListListener.memberAdded(billPeople.get(i), i);
            } else {
                peopleListListener.memberRemoved(billPeople.get(i), i);
            }
        }

        adapter.billPeopleList = billPeople;

        adapter.notifyDataSetChanged();
    }

    public interface PeopleListListener {
        void memberAdded(BillData.BillPeople people, int position);

        void memberRemoved(BillData.BillPeople people, int position);
    }


    public class BillPeopleAdapter extends BaseAdapter {
        private Context context;

        private ArrayList<BillData.BillPeople> billPeopleList;

        private boolean isPaid;
        private boolean isSelectAll;

        private TextView tvBillPeopleName;
        private TextView tvBillAmount;

        private EditText etAmount;

        private CheckBox checkBox;

        BillPeopleAdapter(Context context, ArrayList<BillData.BillPeople> billPeopleList, boolean isPaid, boolean isSelectAll) {
            this.billPeopleList = billPeopleList;
            this.context = context;
            this.isPaid = isPaid;
            this.isSelectAll = isSelectAll;
        }

        @Override
        public int getCount() {
            return billPeopleList.size();
        }

        @Override
        public Object getItem(int i) {
            return billPeopleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_bill_paid_item, viewGroup, false);

            tvBillPeopleName = view.findViewById(R.id.tv_person_name);
            tvBillAmount = view.findViewById(R.id.tv_amount_to_paid);

            etAmount = view.findViewById(R.id.et_paid_amount);

            checkBox = view.findViewById(R.id.cb_paid);

            if (isPaid) {
                initPaidPeople(billPeopleList.get(i), i);
            } else {
                initBillPeople(billPeopleList.get(i), i);
            }

            return view;
        }

        private void initPaidPeople(final BillData.BillPeople people, final int position) {
            tvBillPeopleName.setText(people.getPeopleName());
            tvBillAmount.setText(context.getResources().getString(R.string.amount, people.getAmount()));

            checkBox.setChecked(people.isCheck());

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        etAmount.setVisibility(View.VISIBLE);

                        peopleListListener.memberAdded(people, position);
                    } else {
                        etAmount.setVisibility(View.GONE);

                        peopleListListener.memberRemoved(people, position);
                    }
                }
            });
        }

        private void initBillPeople(final BillData.BillPeople people, final int position) {
            tvBillPeopleName.setText(people.getPeopleName());
            tvBillAmount.setText(context.getResources().getString(R.string.amount, people.getAmount()));

            if (isSelectAll) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            checkBox.setChecked(people.isCheck());

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        peopleListListener.memberAdded(people, position);
                    } else {
                        peopleListListener.memberRemoved(people, position);
                    }
                }
            });
        }
    }
}
