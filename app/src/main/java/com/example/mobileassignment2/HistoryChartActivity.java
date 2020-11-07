package com.example.mobileassignment2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryChartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    LineChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_chart);

        chart = (LineChart) findViewById(R.id.chart);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        // fetch historical scores of current user from the database
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://" + getString(R.string.host_name) + "get-historical-scores?email=" + email;
        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON Response", String.valueOf(response));
                        processHistoricalScoreArray(response);

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

    private void processHistoricalScoreArray(JSONArray array) {

        List<Entry> entries = new ArrayList<Entry>();
        List<String> datePartArray = new ArrayList<String>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject j = array.getJSONObject(i);
                int points = j.getInt("score");

                String datePart = j.getString("date").split("T")[0];
                datePartArray.add(datePart);

                entries.add(new Entry(i, points));

            } catch (JSONException e) {
                continue;
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");

        //to hide right Y and top X border
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setEnabled(false);
        XAxis topXAxis = chart.getXAxis();
        topXAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // the smallest interval for x axis is 1
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //String setter in x-Axis
        chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(datePartArray));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

    }

}
