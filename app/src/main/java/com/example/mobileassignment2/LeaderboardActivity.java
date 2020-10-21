package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * obtain a handle to the object, connect it to a layout manager, and attach an adapter for the data to be displayed:
 */
public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        recyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);

        // set date label to today
        String date = java.time.LocalDate.now().toString();
        TextView dateText = (TextView) findViewById(R.id.daily_leaderboard_date);
        dateText.setText(date);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // fetch daily scores from the database

        // dummy data
        String[] data = {"hello0", "hello1", "hello2", "hello3"};
        ArrayList<UserRank> ranks = new ArrayList<>();
        UserRank a = new UserRank("John", 1, 10);
        UserRank b = new UserRank("Bob", 2, 8);
        UserRank c = new UserRank("Jane", 3, 2);


        ranks.add(a);
        ranks.add(b);
        ranks.add(c);



        mAdapter = new LeaderboardAdapter(ranks);
        recyclerView.setAdapter(mAdapter);


    }
}

