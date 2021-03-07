package com.studentsac.iachat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.studentsac.iachat.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends FirestoreRecyclerAdapter<User, FriendsAdapter.ViewHolder> {

    Context context;
    FirebaseAuth mAuth;

    public FriendsAdapter(FirestoreRecyclerOptions options, Context context){
        super(options);
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User user) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(user.getId().equals(currentUser.getUid())){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.height = 0;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.topMargin = 0;
            params.bottomMargin = 0;
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.mUsername.setText(user.getUsername());
        if(user.getImage() != null){
            if(!user.getImage().equals("")){
                Picasso.with(context).load(user.getImage()).into(holder.circleImageViewUser);
            }
            else{
                holder.circleImageViewUser.setImageResource(R.drawable.ic_person);
            }
        }
        else {
            holder.circleImageViewUser.setImageResource(R.drawable.ic_person);
        }

        holder.mViewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity(user.getId());
            }
        });
    }

    private void goToChatActivity(String id) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idUser",id);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_friends,parent);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mUsername;
        CircleImageView circleImageViewUser;
        View mViewAdapter;

        public ViewHolder (View view){
            super(view);

            mViewAdapter = view;
            mUsername = view.findViewById(R.id.textUsernameView);
            circleImageViewUser = view.findViewById(R.id.circleImageUser);

        }
    }
}
