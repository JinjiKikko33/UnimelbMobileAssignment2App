package com.example.mobileassignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ImageView profile_image;
    TextView id_txt, name_txt, email_txt;
    Button signOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        id_txt = findViewById(R.id.id_txt);
        name_txt = findViewById(R.id.name_txt);
        email_txt = findViewById(R.id.email_txt);

        id_txt.setText(currentUser.getUid());
        name_txt.setText(currentUser.getDisplayName());
        email_txt.setText(currentUser.getEmail());

        profile_image = findViewById(R.id.profile_image);
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profile_image);

        signOut = findViewById(R.id.sign_out_btn);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
