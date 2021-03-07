package com.studentsac.iachat.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.ChatsProvider;
import com.studentsac.iachat.providers.UsersProvider;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String userId;
    UsersProvider usersProvider;
    ChatsProvider chatsProvider;
    FirebaseAuth mAuth;

    ImageView imageViewBack;
    TextView textViewUsername;
    CircleImageView circleImageViewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userId = getIntent().getStringExtra("id");
        usersProvider = new UsersProvider();
        chatsProvider = new ChatsProvider();

        mAuth = FirebaseAuth.getInstance();
        showChatToolbar(R.layout.chat_toolbar);
        getUserInfo();
        createChat();

        checkIfExistChat();

    }

    private void checkIfExistChat() {
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        chatsProvider.getChatByUsers(currentUser.getUid(),userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    if(queryDocumentSnapshots.size()==0){
                        createChat();
                    }
                else{
                        Toast.makeText(ChatActivity.this, "Ya has intercambiado informacion con este amigo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void createChat() {
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        Chat chat = new Chat();
        chat.setId(currentUser.getUid()+userId);
        chat.setTimestamp(new Date().getTime());
        ArrayList<String> ids = new ArrayList<>();
        ids.add(currentUser.getUid());
        ids.add(userId);
        chat.setIds(ids);
        chatsProvider.create(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChatActivity.this, "Chat creado con exito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        usersProvider.getUserInfo(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot !=null){
                    if(documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        textViewUsername.setText(user.getUsername());
                        if(user.getImage()!=null){
                            if(!user.getImage().equals("")){
                                Picasso.with(ChatActivity.this).load(user.getImage()).into(circleImageViewUser);
                            }
                        }
                    }
                }
            }
        });
    }

    private void showChatToolbar(int resource){
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,null);
        actionBar.setCustomView(view);

        imageViewBack =  view.findViewById(R.id.imageViewBack);
        textViewUsername =  view.findViewById(R.id.username);
        circleImageViewUser =  view.findViewById(R.id.circleImageUser);


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}