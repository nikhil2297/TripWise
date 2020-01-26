package com.tripewise.bills;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private RecyclerView rvPeople;

    private BillPeopleAdapter adapter;

    public static PeopleListDialogFragment newInstance(ArrayList<BillData.BillPeople> billPeople, boolean isPaid, PeopleListListener listListener) {
        PeopleListDialogFragment dialogFragment = new PeopleListDialogFragment();
        dialogFragment.billPeople = billPeople;
        dialogFragment.tempPeople = billPeople;
        dialogFragment.isPaid = isPaid;
        dialogFragment.peopleListListener = listListener;

        return dialogFragment;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

        rvPeople = view.findViewById(R.id.rv_bill_people);

        checkSelectAll = llSelectAll.findViewById(R.id.is_check);

        llSelectAll.setClickable(false);

        init();
    }

    private void init() {
        if (adapter == null) {
            LinearLayoutManager manager = new LinearLayoutManager(rvPeople.getContext());
            rvPeople.setLayoutManager(manager);

            adapter = new BillPeopleAdapter(getActivity(), billPeople, isPaid, isSelectAll);
            rvPeople.setAdapter(adapter);
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
                if (isPaid) {
                    if (adapter.finalBillPeopleList.size() > 0) {
                        peopleListListener.onBillDataChange(adapter.finalBillPeopleList, createTextFiledName());
                        dismissAllowingStateLoss();
                    } else {
                        Toast.makeText(getActivity(), "Please select atleast on member", Toast.LENGTH_LONG);
                    }
                } else {
                    peopleListListener.onBillDataChange(adapter.finalBillPeopleList, createTextFiledName());
                    dismissAllowingStateLoss();
                }
                break;
            case R.id.bt_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }

    private String createTextFiledName() {
        String name = "";
        for (BillData.BillPeople people : adapter.finalBillPeopleList) {
            if (!name.equals(""))
                name = name + ", " + people.getPeopleName();
            else
                name = people.getPeopleName();
        }

        return name;
    }

    private void selectAll(boolean isCheck) {
        for (int i = 0; i < billPeople.size(); i++) {
            billPeople.get(i).setCheck(isCheck);
        }

        adapter.billPeopleList = billPeople;

        adapter.notifyDataSetChanged();
    }

    public interface PeopleListListener {
        void onBillDataChange(ArrayList<BillData.BillPeople> billPeople, String name);
    }


    public class BillPeopleAdapter extends RecyclerView.Adapter<BillPeopleAdapter.ItemHolder> {
        private Context context;

        private ArrayList<BillData.BillPeople> billPeopleList;
        private ArrayList<BillData.BillPeople> finalBillPeopleList;

        private boolean isPaid;
        private boolean isSelectAll;

        BillPeopleAdapter(Context context, ArrayList<BillData.BillPeople> billPeopleList, boolean isPaid, boolean isSelectAll) {
            this.billPeopleList = billPeopleList;
            this.context = context;
            this.isPaid = isPaid;
            this.isSelectAll = isSelectAll;

            finalBillPeopleList = new ArrayList<>();
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.layout_bill_paid_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            if (isPaid) {
                initPaidPeople(holder, billPeopleList.get(position), position);
            } else {
                initBillPeople(holder, billPeopleList.get(position), position);
            }
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return billPeopleList.size();
        }

        private void initPaidPeople(final ItemHolder holder, final BillData.BillPeople people, final int position) {
            holder.tvBillPeopleName.setText(people.getPeopleName());
            holder.tvBillAmount.setText(context.getResources().getString(R.string.amount, people.getAmount()));

            if (people.isCheck()) {
                holder.checkBox.setChecked(true);
                holder.etAmount.setVisibility(View.VISIBLE);
                holder.etAmount.setText(String.valueOf(people.getAmount()));
            } else {
                holder.checkBox.setChecked(false);
                holder.etAmount.setVisibility(View.GONE);
                holder.etAmount.setText("");
            }

            holder.checkBox.setChecked(people.isCheck());

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        holder.etAmount.setVisibility(View.VISIBLE);
                        holder.etAmount.setText(people.getAmount() > 0 ? String.valueOf(people.getAmount()) : "");
                    } else {
                        holder.etAmount.setVisibility(View.GONE);
                        holder.etAmount.setText("");
                    }

                    people.setCheck(b);
                }
            });

            holder.etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        people.setAmount(Long.parseLong(s.toString()));

                        finalBillPeopleList.remove(people);
                        finalBillPeopleList.add(people);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        private void initBillPeople(ItemHolder holder, final BillData.BillPeople people, final int position) {
            holder.tvBillPeopleName.setText(people.getPeopleName());
            holder.tvBillAmount.setText(context.getResources().getString(R.string.amount, people.getAmount()));

            holder.checkBox.setVisibility(View.VISIBLE);

            if (isSelectAll) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.checkBox.setChecked(people.isCheck());

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        finalBillPeopleList.add(people);
                    } else {
                        finalBillPeopleList.remove(people);
                    }
                }
            });
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            TextView tvBillPeopleName;
            TextView tvBillAmount;

            EditText etAmount;

            CheckBox checkBox;

            ItemHolder(@NonNull View view) {
                super(view);

                tvBillPeopleName = view.findViewById(R.id.tv_person_name);
                tvBillAmount = view.findViewById(R.id.tv_amount_to_paid);

                etAmount = view.findViewById(R.id.et_paid_amount);

                checkBox = view.findViewById(R.id.cb_paid);
            }
        }
    }
}
