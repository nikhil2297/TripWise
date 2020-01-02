package com.tripewise.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.TripData;

public class BillsFragment extends Fragment {
    private TripData tripData;

    private RecyclerView rvBills;

    public static BillsFragment newInstance(TripData tripData) {
        BillsFragment fragment = new BillsFragment();
        fragment.tripData = tripData;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvBills = view.findViewById(R.id.rv_bills);

        TextView tvTripName = view.findViewById(R.id.tv_trip_name);

        FloatingActionButton btAdd = view.findViewById(R.id.fab_add_bills);

        tvTripName.setText(tripData.getTripName());

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvBills.setLayoutManager(manager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvBills.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        rvBills.addItemDecoration(itemDecoration);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
