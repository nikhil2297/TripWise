package com.tripewise.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeopleDetailsAdapter extends RecyclerView.Adapter<PeopleDetailsAdapter.ItemHolder> {
    private Context context;

    private List<PeopleDetailFragment.TravellerItemObject> itemObjectList;

    public PeopleDetailsAdapter(Context context, List<PeopleDetailFragment.TravellerItemObject> itemObjectList){
        this.context = context;
        this.itemObjectList = itemObjectList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.la));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemObjectList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
