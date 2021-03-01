package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studentsac.iachat.R;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.UsersProvider;

public class CompleteInfoActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView username;
    Button btnCOnfirmar;

    UsersProvider mUsersProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        mAuth = FirebaseAuth.getInstance();
        mUsersProvider = new UsersProvider();

        username = findViewById(R.id.username);
        btnCOnfirmar = findViewById(R.id.btnConfirm);

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            username.setText(user.getDisplayName());
        }else{
            username.setText("");

        }
        btnCOnfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfoUser();
            }
        });
    }

    private void uploadInfoUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String name = username.getText().toString();
        if(!name.equals("")){
            User user = new User();
            user.setUsername(name);
            user.setEmail(currentUser.getEmail());
            user.setId(currentUser.getUid());
            mUsersProvider.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CompleteInfoActivity.this, "Informacion guardada", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}