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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.PersonData;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

public class PeopleFragment extends Fragment {
    private NavController controller;

    private TripData tripData;

    private RecyclerView rvPeople;

    private TextView tvTripName;

    private ImageView ivBack;

    private PeopleViewModel viewModel;

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

        controller = NavHostFragment.findNavController(this);

        tvTripName = view.findViewById(R.id.tv_trip_name);

        ivBack = view.findViewById(R.id.iv_back);

        rvPeople = view.findViewById(R.id.rv_people);

        init();
    }

    private void init() {
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PeopleViewModel.class);

        tvTripName.setText(tripData.getTripName());

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvPeople.setLayoutManager(manager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvPeople.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        rvPeople.addItemDecoration(itemDecoration);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.popBackStack();
            }
        });

        createPeopleList();
    }

    private void createPeopleList() {
        viewModel.fetchPersonDetails(getActivity(), tripData.getId()).observe(getViewLifecycleOwner(), new Observer<List<PersonData>>() {
            @Override
            public void onChanged(List<PersonData> personData) {
                if (personData != null) {
                    PeopleAdapter adapter = PeopleAdapter.newInstance(getActivity(), personData, new PeopleAdapter.PeopleAdapterListener() {
                        @Override
                        public void onPersonClick(PersonData personData) {
                            PeopleFragmentDirections.ActionPeopleFragmentToPeopleDetailFragment direction = PeopleFragmentDirections.actionPeopleFragmentToPeopleDetailFragment(personData.getMobileNumber(), new Gson().toJson(tripData).toString());
                            controller.navigate(direction);
                        }
                    });

                    rvPeople.setAdapter(adapter);
                }
            }
        });
    }
}
