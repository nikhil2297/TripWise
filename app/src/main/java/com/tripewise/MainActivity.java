package com.tripewise;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tripewise.trips.AddTripsDialogFragment;
import com.tripewise.trips.TripsAdapter;
import com.tripewise.utilites.storage.TripStorage;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addTrip;

    private ListView lvTrips;

    private List<TripData> tripDataList;

    private TripsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTrip = findViewById(R.id.fab_add_trips);
        addTrip.setOnClickListener(this);

        TextView tvHelpText = findViewById(R.id.tv_help_message);

        lvTrips = findViewById(R.id.lv_trip);
        lvTrips.setEmptyView(tvHelpText);

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
                }
            }
        });
    }
}
