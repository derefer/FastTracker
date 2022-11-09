package com.blogspot.derefer.fasttracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FastArrayAdapter extends RecyclerView.Adapter<FastArrayAdapter.ViewHolder> {
    // All methods in this adapter are required for a bare minimum RecyclerView adapter
    private ArrayList<Fast> fasts;
    private LayoutInflater inflater;

    public FastArrayAdapter(Context context, ArrayList<Fast> fasts) {
        this.inflater = LayoutInflater.from(context);
        this.fasts = fasts;
    }

    // Specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
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
            item = (TextView) itemView.findViewById(R.id.fastRowTextView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }
    }
}
