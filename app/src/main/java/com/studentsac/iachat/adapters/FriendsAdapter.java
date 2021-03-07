package com.studentsac.iachat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends FirestoreRecyclerAdapter<User, FriendsAdapter.ViewHolder> {

    Context context;

    public FriendsAdapter(FirestoreRecyclerOptions options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User user) {
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

        public ViewHolder (View view){
            super(view);

            mUsername = view.findViewById(R.id.textUsernameView);
            circleImageViewUser = view.findViewById(R.id.circleImageUser);

        }
    }
}
