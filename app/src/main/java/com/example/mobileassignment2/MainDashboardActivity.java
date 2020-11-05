package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class MainDashboardActivity extends AppCompatActivity {

        ImageButton cameraButton;
        TextView today;
        TextView name;
        TextView days;
        ImageView imageUserIcon;
        String username;
        private FirebaseAuth mAuth;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_dashboard);

            // get logged in user so we can display their name
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser currentUser = mAuth.getCurrentUser();

            cameraButton = findViewById(R.id.cameraButton);
            today = findViewById(R.id.text_today);
            name = findViewById(R.id.text_name);
            imageUserIcon = findViewById(R.id.image_user_icon);
            days = findViewById(R.id.text_days);

            // get the date and set it on the dashboard

            LocalDate currentDate = LocalDate.now();
            String date = Integer.toString(currentDate.getDayOfMonth());
            String month = Integer.toString(currentDate.getMonthValue());
            String dateStr = date + "/" + month;
            today.setText(dateStr);

        // retrieve username and display it on dashboard
        try {
            username = currentUser.getDisplayName();
        } catch (NullPointerException e){
            username = "";
        }

        String userWelcome;
        if (username.length() > 0){
             userWelcome = "Hi, " + username + "!";
        } else {
             userWelcome = "Hello there!";
        }
        name.setText(userWelcome);

        // set the user's profile picture by downloading it
        String imageUrl = currentUser.getPhotoUrl().toString();
        new DownloadImageTask(imageUserIcon).execute(imageUrl);

        // get the date the user joined us and calculate the number of days since then
        updateTodayDate(days, currentUser.getEmail());



        }

        public void startCameraIntent(View view){
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        }

        public void updateTodayDate(final TextView days, String email) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + getString(R.string.host_name) + "/users/date-joined/?email=" + email;


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        String datePart = response.getString("date_joined").split("T")[0];
                        String[] datePartArray = datePart.split("-");
                        Date d1 = new Date(Integer.parseInt(datePartArray[0]) - 1900, Integer.parseInt(datePartArray[1]) - 1 , Integer.parseInt(datePartArray[2]));
                        Date d2 = new Date();

                        long difference_in_time = d2.getTime() - d1.getTime();
                        long difference_In_Days
                                = (difference_in_time
                                / (1000 * 60 * 60 * 24))
                                % 365;
                        String daysMessage = "Day " + Long.toString(difference_In_Days);

                        days.setText(daysMessage);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(request);

        }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView userImage;

            public DownloadImageTask(ImageView userImage){
                this.userImage = userImage;
            }

            @Override
            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap icon = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    icon = BitmapFactory.decodeStream(in);

                } catch (Exception e){
                    Log.e("Error", e.getMessage());

                }
                return icon;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                userImage.setImageBitmap(result);
            }
        }


    }
