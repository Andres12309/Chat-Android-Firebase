package com.studentsac.iachat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.UsersProvider;
import com.studentsac.iachat.utils.RelativeTime;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {

    Context context;
    FirebaseAuth mAuth;

    UsersProvider usersProvider;
    User mUser;

    ListenerRegistration listenerRegistration;

    public MessagesAdapter(FirestoreRecyclerOptions options, Context context){
        super(options);
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        usersProvider = new UsersProvider();
        mUser = new User();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {

        holder.textViewMessage.setText(message.getMessage());
        holder.textViewDate.setText(RelativeTime.timeFormatAMPM(message.getTimestamp(),context));
        if(message.getIdUserSend().equals(mAuth.getCurrentUser().getUid())){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(110,0,0,0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(30,20,30,20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.bubble_corner_right));
            holder.textViewMessage.setTextColor(Color.WHITE);
            holder.textViewDate.setTextColor(Color.GRAY);
            holder.imageViewCheck.setVisibility(View.VISIBLE);

            if(message.getStatus().equals("SENT")){
                holder.imageViewCheck.setImageResource(R.drawable.icon_check_false);
            }
            else if(message.getStatus().equals("READ")){
                holder.imageViewCheck.setImageResource(R.drawable.icon_check_true);
            }
            ViewGroup.MarginLayoutParams margin =(ViewGroup.MarginLayoutParams) holder.textViewDate.getLayoutParams();
            margin.rightMargin = 0;

        }
        else{
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0,0,110,0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(50,20,20,20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.bubble_corner_left));
            holder.textViewMessage.setTextColor(Color.WHITE);
            holder.textViewDate.setTextColor(Color.GRAY);
            holder.imageViewCheck.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams margin =(ViewGroup.MarginLayoutParams) holder.textViewDate.getLayoutParams();
            margin.rightMargin = 20;
        }
        showImages(holder,message);

    }

    private void showImages(ViewHolder holder, Message message) {
        if (message.getType().equals("image")) {
            if (message.getUrlImage() != null) {
                if (!message.getUrlImage().equals("")) {
                    holder.imageViewMessage.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(message.getUrlImage()).into(holder.imageViewMessage);

                    if (message.getMessage().equals("\uD83D\uDCF7image")) {
                        holder.textViewMessage.setVisibility(View.GONE);
                        //holder.textViewDate.setPadding(0,0,10,0);
                        ViewGroup.MarginLayoutParams marginDate = (ViewGroup.MarginLayoutParams) holder.textViewDate.getLayoutParams();
                        ViewGroup.MarginLayoutParams marginCheck = (ViewGroup.MarginLayoutParams) holder.imageViewCheck.getLayoutParams();
                        marginDate.topMargin = 15;
                        marginCheck.topMargin = 15;

                    }
                    else {
                        holder.textViewMessage.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    holder.imageViewMessage.setVisibility(View.GONE);
                    holder.textViewMessage.setVisibility(View.VISIBLE);
                }
            }
            else {
                holder.imageViewMessage.setVisibility(View.GONE);
                holder.textViewMessage.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.imageViewMessage.setVisibility(View.GONE);
            holder.textViewMessage.setVisibility(View.VISIBLE);
        }
    }

    public ListenerRegistration getListener(){
        return listenerRegistration;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView textViewDate;
        ImageView imageViewCheck;
        ImageView imageViewMessage;
        LinearLayout linearLayoutMessage;

        View mViewAdapter;

        public ViewHolder (View view){
            super(view);

            mViewAdapter = view;
            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDate);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
            linearLayoutMessage = view.findViewById(R.id.linearLayoutMessage);
            imageViewMessage = view.findViewById(R.id.imageViewMessage);
        }
    }

}
