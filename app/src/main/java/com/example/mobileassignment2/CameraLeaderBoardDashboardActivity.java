package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CameraLeaderBoardDashboardActivity extends AppCompatActivity {

    ImageButton cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_leaderboard);

        cameraButton = findViewById(R.id.cameraButton);

//        Intent cameraIntent = new Intent(this, CameraActivity.class);
//        startActivity(cameraIntent);
    }

    public void startCameraIntent(View view) {
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }

    public void startLeaderboardIntent(View view) {
        Intent leaderboardIntent = new Intent(this, LeaderboardActivity.class);
        startActivity(leaderboardIntent);
    }


}
