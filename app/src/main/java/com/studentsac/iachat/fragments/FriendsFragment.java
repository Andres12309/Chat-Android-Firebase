package com.studentsac.iachat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.FriendsAdapter;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.UsersProvider;

public class FriendsFragment extends Fragment {

    View mvView;
    RecyclerView mRecycleViewFriends;

    FriendsAdapter friendsAdapter;

    UsersProvider usersProvider;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mvView = inflater.inflate(R.layout.fragment_friends, container, false);
        mRecycleViewFriends = mvView.findViewById(R.id.recycleViewFriends);
        usersProvider = new UsersProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleViewFriends.setLayoutManager(linearLayoutManager);

        return mvView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = usersProvider.getAllUserByName();
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                                                .setQuery(query,User.class)
                                                .build();
        friendsAdapter = new FriendsAdapter(options,getContext());
        mRecycleViewFriends.setAdapter(friendsAdapter);
        friendsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        friendsAdapter.stopListening();
    }
}