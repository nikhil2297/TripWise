package com.tripewise.bills;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.BillData;

import java.util.List;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.ItemHolder> {
    private Context context;

    private List<BillData> billData;

    private IBillAdapter adapterCallback;

    public static BillsAdapter getInstance(Context context, List<BillData> data, IBillAdapter adapterCallback) {
        BillsAdapter adapter = new BillsAdapter();
        adapter.context = context;
        adapter.billData = data;
        adapter.adapterCallback = adapterCallback;

        return adapter;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_bill_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final BillData data = billData.get(position);

        holder.tvAmount.setText(context.getResources().getString(R.string.bill_amount, data.getBillAmount()));
        holder.tvBillName.setText(data.getBillName());

        for (BillData.BillPeople people : data.getBillPaidPeopleList()) {
            holder.chipBillPayer.addView(createPaidPeopleView(holder.chipBillPayer, people));
        }

        holder.billCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.onBillClick(data);
            }
        });
    }

    /**
     * @param chipGroup consist of all the Traveller who paid the bill
     * @param people    Details about the paid bill people like Name, Color
     * @return chipView that we can add in chipGroup
     */
    private View createPaidPeopleView(ChipGroup chipGroup, final BillData.BillPeople people) {
        final View chipView = LayoutInflater.from(context).inflate(R.layout.layout_traveller_small_chip, (ViewGroup) chipGroup.getParent(), false);

        final ConstraintLayout layoutChip = chipView.findViewById(R.id.layout_chip);

        final MaterialCardView chipCardView = chipView.findViewById(R.id.materialCardView);

        final TextView tvName = layoutChip.findViewById(R.id.tv_chip_name);

        TextView tvFullName = layoutChip.findViewById(R.id.tv_chip_full_name);

        chipCardView.setCardBackgroundColor(people.getPeopleColor());

        tvFullName.setText(people.getPeopleName());
        tvName.setText(people.getPeopleName().substring(0, 2).toUpperCase());
        tvName.setTextColor(Color.WHITE);

        return chipView;
    }

    @Override
    public int getItemCount() {
        return billData.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView tvBillName;
        TextView tvAmount;

        MaterialCardView billCardView;

        ChipGroup chipBillPayer;

        ItemHolder(@NonNull View itemView) {
            super(itemView);

            tvBillName = itemView.findViewById(R.id.tv_bill_name);
            tvAmount = itemView.findViewById(R.id.tv_bill_amount);

            billCardView = itemView.findViewById(R.id.bill_card);

            chipBillPayer = itemView.findViewById(R.id.chip_bill_payer);
        }
    }

    public interface IBillAdapter{
        void  onBillClick(BillData billData);
    }
}
