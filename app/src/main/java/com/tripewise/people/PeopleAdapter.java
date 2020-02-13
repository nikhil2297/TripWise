package com.tripewise.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ItemHolder> {
    private Context context;

    private List<PersonData> personDataList;

    PeopleAdapter(Context context, List<PersonData> data) {
        this.context = context;
        this.personDataList = data;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_people_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        PersonData data = personDataList.get(position);

        holder.chipCardView.setCardBackgroundColor(data.getPersonColor());

        holder.tvChipName.setText(data.getPersonName().substring(0,2).toUpperCase());
        holder.tvTravellerMobileNumber.setText(data.getMobileNumber());
        holder.tvTravellerName.setText(data.getPersonName());
        holder.tvAmountSend.setText(context.getResources().getString(R.string.amount, data.getPayingAmount()));
        holder.tvAmountReceive.setText(context.getResources().getString(R.string.amount, data.getReceivingAmount()));
    }

    @Override
    public int getItemCount() {
        return personDataList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvChipName;
        private TextView tvTravellerName;
        private TextView tvTravellerMobileNumber;
        private TextView tvAmountReceive;
        private TextView tvAmountSend;

        private MaterialCardView chipCardView;
        ItemHolder(@NonNull View itemView) {
            super(itemView);

            tvChipName = itemView.findViewById(R.id.tv_chip_name);
            tvTravellerName = itemView.findViewById(R.id.tv_traveller_name);
            tvTravellerMobileNumber = itemView.findViewById(R.id.tv_traveller_number);
            tvAmountReceive = itemView.findViewById(R.id.tv_receiving_amount);
            tvAmountSend = itemView.findViewById(R.id.tv_sending_amount);

            chipCardView = itemView.findViewById(R.id.materialCardView);
        }
    }
}
