package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;



public class MainDashboardActivity extends AppCompatActivity {

        ImageButton cameraButton;
        TextView today;
        TextView name;
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


//        Intent cameraIntent = new Intent(this, CameraActivity.class);
//        startActivity(cameraIntent);
        }

        public void startCameraIntent(View view){
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        }




    }

