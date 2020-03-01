package com.tripewise.bills;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.tripewise.R;
import com.tripewise.utilites.storage.data.BillData;
import com.tripewise.utilites.storage.data.TripData;

import java.util.List;

//TODO : set adapter fetch data from local db via live data
public class BillsFragment extends Fragment implements View.OnClickListener {
    private TripData tripData;

    private RecyclerView rvBills;

    private FloatingActionButton btAdd;

    private NavController controller;

    private ImageView ivBack;

    private BillViewModel billViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BillsFragmentArgs args = BillsFragmentArgs.fromBundle(getArguments());

        tripData = new Gson().fromJson(args.getTripData(), TripData.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = NavHostFragment.findNavController(this);

        rvBills = view.findViewById(R.id.rv_bills);

        ivBack = view.findViewById(R.id.iv_back);

        TextView tvTripName = view.findViewById(R.id.tv_trip_name);

        btAdd = view.findViewById(R.id.fab_add_bills);

        tvTripName.setText(tripData.getTripName());

        init();
    }

    private void init() {
        billViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(BillViewModel.class);
        /*
         * StaggeredGridLayoutManager is used for Grid like recyclerview with require row count and orientation
         * Mainly its use for show list of uneven view just like Google keep app
         * Just check out that app for better understating
         *
         * */
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvBills.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvBills.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));

        rvBills.addItemDecoration(itemDecoration);

        btAdd.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        getBillData();
    }

    private void getBillData() {
        billViewModel.getBillData(getActivity(), tripData.getId()).observe(getViewLifecycleOwner(), new Observer<List<BillData>>() {
            @Override
            public void onChanged(List<BillData> billData) {
                if (billData != null) {
                    BillsAdapter adapter = new BillsAdapter(getActivity(), billData);

                    rvBills.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_bills:
                BillsFragmentDirections.ActionBillsFragmentToAddBillFragment direction =
                        BillsFragmentDirections.actionBillsFragmentToAddBillFragment(new Gson().toJson(tripData).toString());
                controller.navigate(direction);
                break;
            case R.id.iv_back:
                controller.popBackStack();
                break;
        }
    }
}
