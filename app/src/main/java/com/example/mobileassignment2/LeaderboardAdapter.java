package com.example.mobileassignment2;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
 * LayoutManager calls the LeaderboardAdapter's onCreateViewHolder() method. This method constructs a ViewHolder
 * and sets the view to display its contents. It typically does this by inflating an XML file. If the viewholder is not yet assigned
 * to any data, the mthod does not actualyl set the view's contents
 *
 * The layout manager then binds the viewholder to the data, calling onBindViewHolder(). It passes the view holder's position in the RecyclerView.
 * The method fetches the data, and fills the view holder's layout.
 *
 * If the list needs an update, call a notification method on the RecycleView.adapter object, such as notifyItemChanged(). The layout manager
 * then rebinds any affected view holders, allowing data to be changed.
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private String[] mdataset;


    // Provide a reference to the views for each data item
    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public LeaderboardViewHolder(@NonNull TextView v) {
            super(v);
            textView = v;
        }
    }

    // store the data for the adapter, used to inject into a view
    public LeaderboardAdapter(String[] dataset){
        mdataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_view, parent, false);
        LeaderboardViewHolder vh = new LeaderboardViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.LeaderboardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mdataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mdataset.length;
    }


}
