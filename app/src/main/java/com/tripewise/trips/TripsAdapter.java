package com.tripewise.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tripewise.R;
import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ItemHolder> {
    private Context context;

    private List<TripData> tripData;

    private ItemClickListener listener;

    public TripsAdapter(Context context, List<TripData> tripData) {
        this.context = context;
        this.tripData = tripData;
    }

    @NonNull
    @Override
    public TripsAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_trip_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TripsAdapter.ItemHolder holder, final int i) {
        holder.tvTripData.setText(context.getResources().getString(R.string.trip_people, tripData.get(i).getMemberCount(), 0));
        holder.tvTripName.setText(tripData.get(i).getTripName());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    TripStorage.getDataBaseInstance(context).deleteTrip(tripData.get(i).getId());
                    tripData.remove(i);
                    notifyItemRemoved(i);
                    holder.itemView.setVisibility(View.GONE);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(tripData.get(i).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripData.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView tvTripName;
        TextView tvTripData;

        ImageView ivDelete;

        ConstraintLayout mainLayout;

        ItemHolder(@NonNull View view) {
            super(view);

            tvTripName = view.findViewById(R.id.tv_trip_name);
            tvTripData = view.findViewById(R.id.tv_trip_data);

            ivDelete = view.findViewById(R.id.iv_delete);

            mainLayout = view.findViewById(R.id.parent);
        }
    }

    public void setOnItemClickListener(ItemClickListener l) {
        this.listener = l;
    }

    public interface ItemClickListener {
        void onClick(int tripId);
    }
}
