package com.example.mobileassignment2;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import org.w3c.dom.Text;

import java.util.List;

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

    List<UserRank> ranks;

    // Provide a reference to the views for each data item
    public static class LeaderboardViewHolder extends ViewHolder {
        public TextView nameTextView;
        public TextView rankTextView;
        public TextView pointsTextView;

        public LeaderboardViewHolder(@NonNull View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.name);
            rankTextView = (TextView) v.findViewById((R.id.rank));
            pointsTextView = (TextView) v.findViewById(R.id.points);
        }
    }

    // store the data for the adapter, used to inject into a view
    public LeaderboardAdapter(List<UserRank> data){
        ranks = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View leaderboardView = inflater.inflate(R.layout.list_text_view, parent, false);
        LeaderboardViewHolder vh = new LeaderboardViewHolder(leaderboardView);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.LeaderboardViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        UserRank rank = ranks.get(position);

        // Set item views based on your views and data model
        TextView nameView = holder.nameTextView;
        TextView rankView = holder.rankTextView;
        TextView pointsView = holder.pointsTextView;

        nameView.setText(rank.getName());
        rankView.setText(Integer.toString(rank.getRank()));
        pointsView.setText(Integer.toString(rank.getPoints()));



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ranks.size();
    }


}
