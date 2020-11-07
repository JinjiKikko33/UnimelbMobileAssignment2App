package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://" + getString(R.string.host_name) + "/users/get-all-users-scores";
        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON Response", String.valueOf(response));
                        ArrayList<UserRank> ranks = processScoreArray(response);

                        mAdapter = new LeaderboardAdapter(ranks);
                        recyclerView.setAdapter(mAdapter);

                        // iterate through the score results, and create UserRank objects


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("Backend response error", error.toString());

                    }
                });

        queue.add(jsonRequest);


    }

    private ArrayList<UserRank> processScoreArray(JSONArray array) {
        ArrayList<UserRank> ranks = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject j = array.getJSONObject(i);
                String username = j.getString("name");
                int points = j.getInt("score");
                int rank = i + 1;

                UserRank userRank = new UserRank(username, rank, points);
                ranks.add(userRank);


            } catch (JSONException e) {
                continue;
            }
        }
        return ranks;
    }

}

