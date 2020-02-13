package com.tripewise.people;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;
import com.tripewise.utilites.storage.tasks.PeopleAsyncConfig;

import java.util.List;

public class PeopleFragment extends Fragment {
    private TripData tripData;

    private PeopleAsyncConfig peopleConfig;

    private RecyclerView rvPeople;

    private TextView tvTripName;

    private ImageView ivBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeopleFragmentArgs args = PeopleFragmentArgs.fromBundle(getArguments());

        tripData = new Gson().fromJson(args.getTripData(), TripData.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTripName = view.findViewById(R.id.tv_trip_name);

        ivBack = view.findViewById(R.id.iv_back);

        rvPeople = view.findViewById(R.id.rv_people);

        init();
    }

    private void init(){
        tvTripName.setText(tripData.getTripName());

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvPeople.setLayoutManager(manager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvPeople.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        peopleConfig = new PeopleAsyncConfig(getActivity());

        createPeopleList();
    }

    private void createPeopleList(){
        peopleConfig.getPersonData(tripData.getId()).observe(getViewLifecycleOwner(), new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personData) {
                PeopleAdapter adapter = new PeopleAdapter(getActivity(), personData);

                rvPeople.setAdapter(adapter);
            }
        });
    }
}
