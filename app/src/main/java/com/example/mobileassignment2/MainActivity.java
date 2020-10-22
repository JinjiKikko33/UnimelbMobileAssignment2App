package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        /**If user is not authenticated, send him to SignInActivity to authenticate first.
         * Else send him to DashboardActivity*/
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (currentUser != null) {

                    //TODO: change to Lishu's activity
                    //Intent dashboardIntent = new Intent(MainActivity.this, DashboardActivity.class);

                    // get the user's email and display name
                    //String email = currentUser.getEmail();
                    //String name = currentUser.getDisplayName();
                    Intent cameraDashBoardIntent = new Intent(MainActivity.this, CameraLeaderBoardDashboardActivity.class);


                    // TODO: start Lishu's homepage activity, and add the user's name and email as Intent extras
                    startActivity(cameraDashBoardIntent);
                    finish();
                } else {
                    Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(signInIntent);
                    finish();
                }
            }
        };

        handler.postDelayed(runnable, 2000);

    }
}
