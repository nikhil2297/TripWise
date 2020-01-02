package com.tripewise;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tripewise.trips.AddTripsDialogFragment;
import com.tripewise.trips.TripsAdapter;
import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TripsAdapter.ItemClickListener {

    private FloatingActionButton addTrip;

    private RecyclerView lvTrips;

    private List<TripData> tripDataList;

    private TripsAdapter adapter;

    private TextView tvHelpText;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTrip = findViewById(R.id.fab_add_trips);
        addTrip.setOnClickListener(this);

        tvHelpText = findViewById(R.id.tv_help_message);

        lvTrips = findViewById(R.id.lv_trip);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvTrips.setLayoutManager(manager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(lvTrips.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        lvTrips.addItemDecoration(itemDecoration);

        getTripData();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddTripsDialogFragment tripsDialogFragment = new AddTripsDialogFragment();
        tripsDialogFragment.show(fragmentTransaction, "adding");
    }

    private void getTripData() {
        TripStorage.getDataBaseInstance(this).tripDao().getAllData().observe(this, new Observer<List<TripData>>() {
            @Override
            public void onChanged(List<TripData> tripData) {
                if (adapter != null) {
                    int size = tripDataList.size();

                    for (int i = size; i < tripData.size(); i++) {
                        tripDataList.add(tripData.get(i));
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    tripDataList = tripData;
                    adapter = new TripsAdapter(MainActivity.this, tripDataList);
                    lvTrips.setAdapter(adapter);

                    adapter.setOnItemClickListener(MainActivity.this);
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

    }

    private void showFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.addToBackStack(fragment.getClass().getName());

        currentFragment = fragment;
    }
}
