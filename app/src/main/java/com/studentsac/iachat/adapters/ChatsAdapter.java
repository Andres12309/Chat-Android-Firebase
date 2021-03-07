package com.studentsac.iachat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ChatActivity;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context context;
    FirebaseAuth mAuth;

    public ChatsAdapter(FirestoreRecyclerOptions options, Context context){
        super(options);
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat chat) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void goToChatActivity(String id) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chats,parent);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mUsername;
        TextView textViewLastMessage;
        TextView textViewTimestamp;
        ImageView imageViewCheck;
        CircleImageView circleImageViewUser;

        View mViewAdapter;

        public ViewHolder (View view){
            super(view);

            mViewAdapter = view;
            mUsername = view.findViewById(R.id.textUsernameView);
            circleImageViewUser = view.findViewById(R.id.circleImageUser);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
            textViewLastMessage = view.findViewById(R.id.textLastMesage);
        }
    }
}
