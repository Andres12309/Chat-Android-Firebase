package com.studentsac.iachat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ChatActivity;
import com.studentsac.iachat.models.Chat;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.MessagesProvider;
import com.studentsac.iachat.providers.UsersProvider;
import com.studentsac.iachat.utils.RelativeTime;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context context;
    FirebaseAuth mAuth;

    UsersProvider usersProvider;
    User mUser;
    MessagesProvider messagesProvider;

    ListenerRegistration listenerRegistration;
    ListenerRegistration listenerRegistrationLastMessage;

    public ChatsAdapter(FirestoreRecyclerOptions options, Context context){
        super(options);
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        usersProvider = new UsersProvider();
        messagesProvider = new MessagesProvider();
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

        getLastMessage(holder,chat.getId());

        getUserInfo(holder,idUser);

        clickChatView(holder,idUser,chat.getId());

        getMessageNotRead(holder,chat.getId());
    }

    private void getMessageNotRead(ViewHolder holder, String idChat) {
        messagesProvider.getMessageNotReadReceive(idChat,mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if(querySnapshot != null){
                    int size = querySnapshot.size();
                    if(size>0){
                        holder.frameLayoutMessageNotRead.setVisibility(View.VISIBLE);
                        holder.textViewNumberMessageNoRead.setText(String.valueOf(size));
                        holder.textViewTimestamp.setTextColor(context.getResources().getColor(R.color.colorPrimaryLight));
                    }
                    else{
                        holder.frameLayoutMessageNotRead.setVisibility(View.GONE);
                        holder.textViewTimestamp.setTextColor(context.getResources().getColor(R.color.colorGrayDark));
                    }
                }
            }
        });
    }

    private void getLastMessage(ViewHolder holder, String idChat) {
        listenerRegistrationLastMessage = messagesProvider.getLastMessage(idChat).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if(querySnapshot != null){
                    int sizeDocuments = querySnapshot.size();
                    if(sizeDocuments>0){
                        Message message = querySnapshot.getDocuments().get(0).toObject(Message.class);
                        holder.textViewLastMessage.setText(message.getMessage());
                        holder.textViewTimestamp.setText(RelativeTime.timeFormatAMPM(message.getTimestamp(),context));

                        if(message.getIdUserSend().equals(mAuth.getCurrentUser().getUid())){
                            holder.imageViewCheck.setVisibility(View.VISIBLE);

                            if(message.getStatus().equals("SENT")){
                                holder.imageViewCheck.setImageResource(R.drawable.icon_check_false);
                            }
                            else if(message.getStatus().equals("READ")){
                                holder.imageViewCheck.setImageResource(R.drawable.icon_check_true);
                            }
                        }
                        else{
                            holder.imageViewCheck.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
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

        listenerRegistration = usersProvider.getUserInfo(idUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
    public ListenerRegistration getListenerLastMessage(){
        return listenerRegistrationLastMessage;
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

        FrameLayout frameLayoutMessageNotRead;
        TextView textViewNumberMessageNoRead;

        View mViewAdapter;

        public ViewHolder (View view){
            super(view);

            mViewAdapter = view;
            textViewUsername = view.findViewById(R.id.textUsernameView);
            circleImageViewUser = view.findViewById(R.id.circleImageUser);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
            textViewLastMessage = view.findViewById(R.id.textLastMessage);
            frameLayoutMessageNotRead = view.findViewById(R.id.frameLayoutMessageNotRead);
            textViewNumberMessageNoRead = view.findViewById(R.id.textViewMessagePend);
        }
    }
}
