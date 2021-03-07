package com.studentsac.iachat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.ChatsAdapter;
import com.studentsac.iachat.adapters.FriendsAdapter;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.ChatsProvider;
import com.studentsac.iachat.providers.UsersProvider;


public class ChatFragment extends Fragment {
    View mvView;
    RecyclerView mRecycleViewChats;

    ChatsAdapter chatsAdapter;

    ChatsProvider chatsProvider;

    FirebaseAuth mAuth;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mvView = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecycleViewChats = mvView.findViewById(R.id.recycleViewChats);
        chatsProvider = new ChatsProvider();
        mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleViewChats.setLayoutManager(linearLayoutManager);

        return mvView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        Query query = chatsProvider.getChatsUsers(user.getUid());
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query,Chat.class)
                .build();
        chatsAdapter = new ChatsAdapter(options,getContext());
        mRecycleViewChats.setAdapter(chatsAdapter);
        chatsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        chatsAdapter.stopListening();
    }
}