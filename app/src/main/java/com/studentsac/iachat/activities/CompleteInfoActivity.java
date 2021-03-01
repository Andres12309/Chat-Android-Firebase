package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studentsac.iachat.R;

public class CompleteInfoActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        username = findViewById(R.id.username);
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            username.setText(user.getDisplayName());
        }else{
            username.setText("");

        }
    }
}