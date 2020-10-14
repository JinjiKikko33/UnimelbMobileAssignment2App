package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        // dummy data
        String[] data = {"hello0", "hello1", "hello2", "hello3"};

        mAdapter = new LeaderboardAdapter(data);
        recyclerView.setAdapter(mAdapter);


    }
}

