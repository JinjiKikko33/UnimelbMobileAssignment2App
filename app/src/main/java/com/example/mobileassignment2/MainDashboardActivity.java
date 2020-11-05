package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;



    public class MainDashboardActivity extends AppCompatActivity {

        ImageButton cameraButton;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_dashboard);

            cameraButton = findViewById(R.id.cameraButton);

//        Intent cameraIntent = new Intent(this, CameraActivity.class);
//        startActivity(cameraIntent);
        }

        public void startCameraIntent(View view){
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        }


    }

