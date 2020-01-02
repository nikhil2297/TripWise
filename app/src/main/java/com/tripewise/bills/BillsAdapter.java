package com.tripewise.bills;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.ItemHolder> {
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
