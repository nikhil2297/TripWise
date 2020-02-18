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

import java.util.List;

public class PeopleDetailsAdapter extends RecyclerView.Adapter<PeopleDetailsAdapter.ItemHolder> {
    private Context context;

    private List<PeopleDetailFragment.TravellerItemObject> itemObjectList;

    PeopleDetailsAdapter(Context context, List<PeopleDetailFragment.TravellerItemObject> itemObjectList) {
        this.context = context;
        this.itemObjectList = itemObjectList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_people_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        PeopleDetailFragment.TravellerItemObject itemObject = itemObjectList.get(position);

        holder.tvTravellerName.setText(itemObject.getDetailsData().getName());
        holder.tvTravellerNumber.setText(itemObject.getDetailsData().getMobileNumber());
        holder.tvChipName.setText(itemObject.getDetailsData().getName().substring(0, 2).toUpperCase());

        if (itemObject.isReceive()) {
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.colorAmountGreen));
            holder.tvAmount.setText(context.getResources().getString(R.string.receive_amount, itemObject.getDetailsData().getAmount()));
        } else {
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.colorAmountRed));
            holder.tvAmount.setText(context.getResources().getString(R.string.send_amount, itemObject.getDetailsData().getAmount()));
        }

        holder.chipCardView.setCardBackgroundColor(itemObject.getPersonColor());
    }

    @Override
    public int getItemCount() {
        return itemObjectList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private MaterialCardView chipCardView;

        private TextView tvChipName;
        private TextView tvTravellerName;
        private TextView tvTravellerNumber;
        private TextView tvAmount;

        ItemHolder(@NonNull View itemView) {
            super(itemView);

            chipCardView = itemView.findViewById(R.id.materialCardView);

            tvChipName = itemView.findViewById(R.id.tv_chip_name);
            tvTravellerName = itemView.findViewById(R.id.tv_traveller_name);
            tvTravellerNumber = itemView.findViewById(R.id.tv_traveller_number);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
