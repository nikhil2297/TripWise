package com.tripewise.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.PersonData;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ItemHolder> {
    private Context context;

    private List<PersonData> personDataList;

    private PeopleAdapterListener adapterListener;

    public static PeopleAdapter newInstance(Context context, List<PersonData> data, PeopleAdapterListener listener) {
        PeopleAdapter adapter = new PeopleAdapter();
        adapter.context = context;
        adapter.personDataList = data;
        adapter.adapterListener = listener;

        return adapter;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_people_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final PersonData data = personDataList.get(position);

        holder.chipCardView.setCardBackgroundColor(data.getPersonColor());

        holder.tvChipName.setText(data.getPersonName().substring(0, 2).toUpperCase());
        holder.tvTravellerMobileNumber.setText(data.getMobileNumber());
        holder.tvTravellerName.setText(data.getPersonName());
        holder.tvAmountSend.setText(context.getResources().getString(R.string.amount, data.getPayingAmount()));
        holder.tvAmountReceive.setText(context.getResources().getString(R.string.amount, data.getReceivingAmount()));
        holder.tvTotalAmountPaid.setText(context.getResources().getString(R.string.amount, data.getTotalAmountPaid()));

        holder.peopleDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onPersonClick(data);
            }
        });
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
        private TextView tvTotalAmountPaid;

        private MaterialCardView chipCardView;

        private ConstraintLayout peopleDetailsLayout;

        ItemHolder(@NonNull View itemView) {
            super(itemView);

            tvChipName = itemView.findViewById(R.id.tv_chip_name);
            tvTravellerName = itemView.findViewById(R.id.tv_traveller_name);
            tvTravellerMobileNumber = itemView.findViewById(R.id.tv_traveller_number);
            tvAmountReceive = itemView.findViewById(R.id.tv_amount_receive);
            tvAmountSend = itemView.findViewById(R.id.tv_amount_send);
            tvTotalAmountPaid = itemView.findViewById(R.id.tv_total_amount);

            chipCardView = itemView.findViewById(R.id.materialCardView);

            peopleDetailsLayout = itemView.findViewById(R.id.people_detail_layout);
        }
    }

    public interface PeopleAdapterListener {
        void onPersonClick(PersonData personData);
    }
}
