package com.tripewise.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.TripAsyncConfig;

import java.util.List;

public class TripFragment extends Fragment implements TripsAdapter.ItemClickListener, View.OnClickListener {
    private FloatingActionButton addTrip;

    private RecyclerView lvTrips;

    private List<TripData> tripDataList;

    private TripsAdapter adapter;

    private TextView tvHelpText;

    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = NavHostFragment.findNavController(this);

        addTrip = view.findViewById(R.id.fab_add_trips);
        addTrip.setOnClickListener(this);

        tvHelpText = view.findViewById(R.id.tv_help_message);

        lvTrips = view.findViewById(R.id.lv_trip);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        lvTrips.setLayoutManager(manager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(lvTrips.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        lvTrips.addItemDecoration(itemDecoration);

        getTripData();
    }

    private void getTripData() {
        TripAsyncConfig asyncConfig = new TripAsyncConfig(getActivity());

        asyncConfig.onTripDataChange(new TripAsyncConfig.TripConfigListener() {
            @Override
            public void onDataChange(List<TripData> tripData) {
                if (adapter != null) {
                    int size = tripDataList.size();

                    for (int i = size; i < tripData.size(); i++) {
                        tripDataList.add(tripData.get(i));
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    tripDataList = tripData;
                    adapter = new TripsAdapter(getActivity(), tripDataList);
                    lvTrips.setAdapter(adapter);

                    adapter.setOnItemClickListener(TripFragment.this);
                }

                if (tripData.size() > 0) {
                    tvHelpText.setVisibility(View.GONE);
                } else {
                    tvHelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(TripData tripData) {
        TripFragmentDirections.ActionTripFragmentToBillsFragment direction = TripFragmentDirections.actionTripFragmentToBillsFragment(new Gson().toJson(tripData).toString());
        controller.navigate(direction);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        TripDialogFragment tripDialogFragment = new TripDialogFragment();
        tripDialogFragment.show(fragmentTransaction, "adding");
    }
}
