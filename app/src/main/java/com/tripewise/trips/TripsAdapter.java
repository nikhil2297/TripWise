package com.tripewise.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.card.MaterialCardView;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class TripsAdapter extends BaseExpandableListAdapter {
    private Context context;

    private List<TripData> tripData;

    private ItemClickListener listener;

    TripsAdapter(Context context, List<TripData> tripData) {
        this.context = context;
        this.tripData = tripData;
    }

    void setOnItemClickListener(ItemClickListener l) {
        this.listener = l;
    }

    @Override
    public int getGroupCount() {
        return tripData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return tripData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return tripData.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        final ParentItemHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_trip_card, viewGroup, false);

            holder = new ParentItemHolder(view);

            view.setTag(holder);
        } else {
            holder = (ParentItemHolder) view.getTag();
        }

        String id;

        holder.tvTripData.setText(context.getResources().getString(R.string.trip_people, tripData.get(i).getMemberCount(), tripData.get(i).getBillCount()));
        holder.tvTripName.setText(tripData.get(i).getTripName());

        /*
        Load gif in webview slow down app
        holder.webGif.loadUrl(tripData.get(i).getGifPath());
        holder.webGif.getSettings().setLoadWithOverviewMode(true);
        holder.webGif.getSettings().setUseWideViewPort(true);
        */

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final ChildItemHolder holder;

        final TripData data = (TripData) getChild(i, i1);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_child_trip_card, viewGroup, false);

            holder = new ChildItemHolder(view);

            view.setTag(holder);
        } else {
            holder = (ChildItemHolder) view.getTag();
        }

        holder.tvPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPeopleClick(data);
            }
        });

        holder.tvBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBillClick(data);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public interface ItemClickListener {
        void onBillClick(TripData tripData);

        void onPeopleClick(TripData tripData);
    }

    class ParentItemHolder {
        TextView tvTripName;
        TextView tvTripData;

        MaterialCardView mainLayout;

        ConstraintLayout tripDetailsLayout;

        // WebView webGif;

        ParentItemHolder(View view) {
            tvTripName = view.findViewById(R.id.tv_trip_name);
            tvTripData = view.findViewById(R.id.tv_trip_data);

            mainLayout = view.findViewById(R.id.trip_card);

            tripDetailsLayout = view.findViewById(R.id.parent);

            //      webGif = view.findViewById(R.id.gif_web);

            //     webGif.setClickable(true);

        }
    }

    class ChildItemHolder {
        TextView tvBill;
        TextView tvPeople;

        ChildItemHolder(View view) {
            tvBill = view.findViewById(R.id.tv_bill);
            tvPeople = view.findViewById(R.id.tv_people);
        }
    }
}
