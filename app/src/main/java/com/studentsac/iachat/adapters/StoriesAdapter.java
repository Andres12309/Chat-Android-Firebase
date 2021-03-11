package com.studentsac.iachat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ChatActivity;
import com.studentsac.iachat.activities.StoriesDetailActivity;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.models.Story;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.MessagesProvider;
import com.studentsac.iachat.providers.UsersProvider;
import com.studentsac.iachat.utils.RelativeTime;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {

    FragmentActivity context;
    FirebaseAuth mAuth;

    UsersProvider usersProvider;
    User mUser;
    MessagesProvider messagesProvider;

    ArrayList<Story> stories;
    Gson gson = new Gson();

    public StoriesAdapter(FragmentActivity context,ArrayList<Story> stories){
        this.context = context;
        this.stories = stories;

        mAuth = FirebaseAuth.getInstance();
        usersProvider = new UsersProvider();
        messagesProvider = new MessagesProvider();
        mUser = new User();

    }

    private void getUserInfo(ViewHolder holder, String idUser) {

        usersProvider.getUserInfo(idUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    if(documentSnapshot.exists()){
                        mUser = documentSnapshot.toObject(User.class);
                        holder.textViewUsername.setText(mUser.getUsername());

                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_stories,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story[] storiesJson = gson.fromJson(stories.get(position).getJson(),Story[].class);

        holder.circularStatusView.setPortionsCount(storiesJson.length);

        setLastImageStory(holder,storiesJson);
        getUserInfo(holder, stories.get(position).getIdUser());

        holder.mViewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoriesDetailActivity.class);
                intent.putExtra("stories",stories.get(position).getJson());
                context.startActivity(intent);
            }
        });
    }

    private void setLastImageStory(ViewHolder holder, Story[] storiesJson) {
        if(storiesJson.length>0){
            Picasso.with(context).load(storiesJson[storiesJson.length-1].getUrl()).into(holder.circleImageViewStory);
            String relativeTime = RelativeTime.timeFormatAMPM(storiesJson[storiesJson.length-1].getTimestamp(),context);
            holder.textViewDate.setText(relativeTime);
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUsername;
        TextView textViewDate;
        CircleImageView circleImageViewStory;
        CircularStatusView circularStatusView;

        FrameLayout frameLayoutMessageNotRead;
        TextView textViewNumberMessageNoRead;

        View mViewAdapter;

        public ViewHolder (View view){
            super(view);

            mViewAdapter = view;
            textViewUsername = view.findViewById(R.id.textUsernameView);
            textViewDate = view.findViewById(R.id.textViewDate);
            circleImageViewStory = view.findViewById(R.id.circleImageStory);
            circularStatusView = view.findViewById(R.id.circularStories);

            frameLayoutMessageNotRead = view.findViewById(R.id.frameLayoutMessageNotRead);
            textViewNumberMessageNoRead = view.findViewById(R.id.textViewMessagePend);
        }
    }

}
