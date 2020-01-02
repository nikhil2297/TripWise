package com.tripewise.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tripewise.R;
import com.tripewise.utilites.storage.data.TripData;

import java.util.ArrayList;
import java.util.List;

public class TripsAdapter extends BaseAdapter {
    private Context context;

    private List<TripData> tripData;

    public TripsAdapter(Context context, List<TripData> tripData) {
        this.context = context;
        this.tripData = tripData;
    }

    @Override
    public int getCount() {
        return tripData.size();
    }

    @Override
    public Object getItem(int i) {
        return tripData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_trip_card, viewGroup, false);

        TextView tvTripName = view.findViewById(R.id.tv_trip_name);
        TextView tvTripeData = view.findViewById(R.id.tv_trip_data);

        tvTripeData.setText(context.getResources().getString(R.string.trip_people, tripData.get(i).getMemberCount(), 0));
        tvTripName.setText(tripData.get(i).getTripName());

        return view;
    }
}
