package com.tripewise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tripewise.trips.AddTripsDialogFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addTrip;
    private ListView lvTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTrip = findViewById(R.id.fab_add_trips);
        addTrip.setOnClickListener(this);

        TextView tvHelpText = findViewById(R.id.tv_help_message);

        lvTrips = findViewById(R.id.lv_trip);
        lvTrips.setEmptyView(tvHelpText);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddTripsDialogFragment tripsDialogFragment = new AddTripsDialogFragment();
        tripsDialogFragment.show(fragmentTransaction, "adding");
    }
}
