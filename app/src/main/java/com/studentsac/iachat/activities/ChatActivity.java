package com.studentsac.iachat.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.ChatsAdapter;
import com.studentsac.iachat.adapters.MessagesAdapter;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.ChatsProvider;
import com.studentsac.iachat.providers.MessagesProvider;
import com.studentsac.iachat.providers.UsersProvider;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String userId;
    String idChat;
    UsersProvider usersProvider;
    ChatsProvider chatsProvider;
    MessagesProvider messagesProvider;
    FirebaseAuth mAuth;

    ImageView imageViewBack;
    TextView textViewUsername;
    CircleImageView circleImageViewUser;

    EditText editTextMessage;
    ImageView imageViewSendMessage;

    MessagesAdapter messagesAdapter;
    RecyclerView recyclerViewMessage;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userId = this.getIntent().getStringExtra("idUser");
        idChat = this.getIntent().getStringExtra("idChat");

        usersProvider = new UsersProvider();
        messagesProvider = new MessagesProvider();
        chatsProvider = new ChatsProvider();



        editTextMessage = findViewById(R.id.textViewMessage);
        imageViewSendMessage = findViewById(R.id.sendMessage);
        recyclerViewMessage = findViewById(R.id.recycleViewMessages);
        linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        showChatToolbar(R.layout.chat_toolbar);
        getUserInfo();
        //createChat();
        checkIfExistChat();


        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(messagesAdapter != null){
            messagesAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(messagesAdapter != null){
            messagesAdapter.stopListening();
        }
    }

    private void createMessage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String textMessage = editTextMessage.getText().toString();
        if(!textMessage.equals("")){
            Message message = new Message();
            message.setIdChat(idChat);
            message.setIdUserSend(currentUser.getUid());
            message.setIdUserReceive(userId);
            message.setMessage(textMessage);
            message.setStatus("SENT");
            message.setTimestamp(new Date().getTime());
            messagesProvider.create(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editTextMessage.setText("");
                    if(messagesAdapter != null){
                        messagesAdapter.notifyDataSetChanged();
                    }
                    chatsProvider.updateNumberMessage(idChat);
                    //Toast.makeText(ChatActivity.this, "Mensaje enviado con exito", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            //Toast.makeText(this, "No hay nada para enviar", Toast.LENGTH_LONG).show();
        }
    }

    private void checkIfExistChat() {
        chatsProvider.getChatByUsers(mAuth.getCurrentUser().getUid(),userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    if(queryDocumentSnapshots.size()==0){
                        createChat();
                    }
                else{
                    idChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                    getMessageByChat();
                    updateStatusMessages();
                    //Toast.makeText(ChatActivity.this, "Ya has intercambiado informacion con este amigo", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void updateStatusMessages() {
        messagesProvider.getMessageNotRead(idChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                    Message message = document.toObject(Message.class);
                    if(!message.getIdUserSend().equals(mAuth.getCurrentUser().getUid())){
                        messagesProvider.updateStatusMessage(message.getId(),"READ");
                    }
                }
            }
        });
    }

    private void getMessageByChat() {
        Query query = messagesProvider.getMessageByChat(idChat);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query,Message.class)
                .build();
        messagesAdapter = new MessagesAdapter(options,ChatActivity.this);
        recyclerViewMessage.setAdapter(messagesAdapter);
        messagesAdapter.startListening();

        messagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateStatusMessages();
                int allMessage = messagesAdapter.getItemCount();
                int lastMessagePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(lastMessagePosition == -1 || (positionStart >= (allMessage - 1) &&  lastMessagePosition == (positionStart - 1))){
                    recyclerViewMessage.scrollToPosition(positionStart);
                }
            }
        });
    }

    private void createChat() {
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        Chat chat = new Chat();
        chat.setId(currentUser.getUid()+userId);
        chat.setTimestamp(new Date().getTime());
        chat.setNumberMessages(0);
        ArrayList<String> ids = new ArrayList<>();
        ids.add(currentUser.getUid());
        ids.add(userId);
        chat.setIds(ids);

        idChat = chat.getId();
        chatsProvider.create(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getMessageByChat();
                //Toast.makeText(ChatActivity.this, "Chat creado con exito", Toast.LENGTH_SHORT).show();
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