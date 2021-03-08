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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ChatActivity;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.UsersProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context context;
    FirebaseAuth mAuth;

    UsersProvider usersProvider;
    User mUser;

    ListenerRegistration listenerRegistration;

    public ChatsAdapter(FirestoreRecyclerOptions options, Context context){
        super(options);
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        usersProvider = new UsersProvider();
        mUser = new User();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat chat) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String idUser="";

        for(int i=0;i<chat.getIds().size();i++){
            if(!currentUser.getUid().equals(chat.getIds().get(i))){
                idUser = chat.getIds().get(i);
                break;
            }
        }

        getUserInfo(holder,idUser);

        clickChatView(holder,idUser,chat.getId());
    }

    private void clickChatView(ViewHolder holder, final String idUser, final String id) {
        holder.mViewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity(idUser,id);
            }
        });
    }

    private void getUserInfo(ViewHolder holder, String idUser) {

        usersProvider.getUserInfo(idUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    if(documentSnapshot.exists()){
                        mUser = documentSnapshot.toObject(User.class);
                        holder.textViewUsername.setText(mUser.getUsername());
                        if(mUser.getImage()!=null){
                            if(!mUser.getImage().equals("")){
                                Picasso.with(context).load(mUser.getImage()).into(holder.circleImageViewUser);
                            }
                            else{
                                holder.circleImageViewUser.setImageResource(R.drawable.ic_person);
                            }
                        }
                        else{
                            holder.circleImageViewUser.setImageResource(R.drawable.ic_person);
                        }
                    }
                }
            }
        });
    }

    public ListenerRegistration getListener(){
        return listenerRegistration;
    }

    private void goToChatActivity(String idUser, String idChat) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idUser",idUser);
        intent.putExtra("idChat",idChat);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chats,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUsername;
        TextView textViewLastMessage;
        TextView textViewTimestamp;
        ImageView imageViewCheck;
        CircleImageView circleImageViewUser;

        View mViewAdapter;

        public ViewHolder (View view){
            super(view);

            mViewAdapter = view;
            textViewUsername = view.findViewById(R.id.textUsernameView);
            circleImageViewUser = view.findViewById(R.id.circleImageUser);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
            textViewLastMessage = view.findViewById(R.id.textLastMesage);
        }
    }
}
