package com.tripewise.trips;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.storage.communication.CommunicationHelper;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class TripFragment extends Fragment implements TripsAdapter.ItemClickListener, View.OnClickListener {
    private FloatingActionButton addTrip;

    private ExpandableListView exlvTrips;

    private List<TripData> tripDataList;

    private TripsAdapter adapter;

    private int latestTripId;

    private TextView tvHelpText;

    private boolean fromBackPressed;

    private NavController controller;

    private TripViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TripViewModel.class);

        controller = NavHostFragment.findNavController(this);

        addTrip = view.findViewById(R.id.fab_add_trips);
        addTrip.setOnClickListener(this);

        tvHelpText = view.findViewById(R.id.tv_help_message);

        exlvTrips = view.findViewById(R.id.exlv_trip);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTripData();
    }

    /**
     * We get list of trips from this method
     * 1. We check for null condition and list size should be > 0 for tripData
     * 2. We check for null condition for adapter and check if fromBackPressed is false or true
     * 3. If adapter is null then we create a new instance of it
     * 4  If adapter not null then we update the listView with extra data.
     * 5. If fromBackPressed is true then we create a new instance of adapter
     * 6. If fromBackPressed is false we update the listView with extra data.
     * <p>
     * Note : formBackPressed = true when user navigate to different screen
     * fromBackPressed = false when step 5 is run
     */
    private void getTripData() {
        viewModel.fetchTripData(getContext()).observe(getViewLifecycleOwner(), new Observer<List<TripData>>() {
            @Override
            public void onChanged(List<TripData> tripData) {
                if (tripData != null && tripData.size() > 0) {
                    if (adapter != null && !fromBackPressed) {
                        int size = tripDataList.size();

                        for (int i = size; i < tripData.size(); i++) {
                            tripDataList.add(tripData.get(i));
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        fromBackPressed = false;

                        tripDataList = tripData;
                        adapter = new TripsAdapter(getActivity(), tripDataList);
                        exlvTrips.setAdapter(adapter);

                        adapter.setOnItemClickListener(TripFragment.this);

                        adapter.notifyDataSetChanged();
                    }

                    if (tripData.size() > 0) {
                        tvHelpText.setVisibility(View.GONE);
                    } else {
                        tvHelpText.setVisibility(View.VISIBLE);
                    }

                    latestTripId = tripData.get(tripData.size() - 1).getId();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        fromBackPressed = true;

        TripFragmentDirections.ActionTripFragmentToPlaceholder direction = TripFragmentDirections.actionTripFragmentToPlaceholder(latestTripId);
        controller.navigate(direction);
    }

    @Override
    public void onBillClick(TripData tripData) {
        fromBackPressed = true;

        TripFragmentDirections.ActionTripFragmentToBillsFragment direction = TripFragmentDirections.
                actionTripFragmentToBillsFragment(new Gson().toJson(tripData).toString());

        controller.navigate(direction);
    }

    @Override
    public void onPeopleClick(TripData tripData) {
        fromBackPressed = true;

        TripFragmentDirections.ActionTripFragmentToPeopleFragment direction = TripFragmentDirections.
                actionTripFragmentToPeopleFragment(new Gson().toJson(tripData).toString());

        controller.navigate(direction);
    }
}
