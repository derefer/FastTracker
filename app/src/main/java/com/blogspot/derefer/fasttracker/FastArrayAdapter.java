package com.blogspot.derefer.fasttracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FastArrayAdapter extends RecyclerView.Adapter<FastArrayAdapter.ViewHolder> {
    // All methods in this adapter are required for a bare minimum RecyclerView adapter
    private int listItemLayout;
    private ArrayList<Fast> fasts;

    public FastArrayAdapter(int layoutId, ArrayList<Fast> fasts) {
        listItemLayout = layoutId;
        this.fasts = fasts;
    }

    // Specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // Load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(fasts.get(listPosition).returnFastAsString());
    }

    @Override
    public int getItemCount() {
        return fasts.size();
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //item = (TextView) itemView.findViewById(R.id.???);
        }
        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }
    }
}
