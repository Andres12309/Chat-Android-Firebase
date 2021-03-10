package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.OptionsPagerAdapter;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.providers.ImageProvider;
import com.studentsac.iachat.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.Date;

public class ConfirmImageSendActivity extends AppCompatActivity {
    String idChat;
    String idUserReceiver;
    ViewPager viewPager;
    ArrayList<String> data;
    ArrayList<Message> messages = new ArrayList<>();

    ImageProvider imageProvider;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image_send);

        viewPager = findViewById(R.id.viewPager);
        data = getIntent().getStringArrayListExtra("data");
        idChat = getIntent().getStringExtra("idChat");
        idUserReceiver = getIntent().getStringExtra("idUserReceiver");

        mAuth = FirebaseAuth.getInstance();
        imageProvider = new ImageProvider();

        if(data != null){
            for(int i=0;i<data.size();i++){
                Message m = new Message();
                m.setIdChat(idChat);
                m.setIdUserSend(mAuth.getCurrentUser().getUid());
                m.setIdUserReceive(idUserReceiver);
                m.setStatus("SENT");
                m.setTimestamp(new Date().getTime());
                m.setType("image");
                m.setUrlImage(data.get(i));
                m.setMessage("\uD83D\uDCF7image");
                messages.add(m);

            }
        }

        OptionsPagerAdapter pagerAdapter = new OptionsPagerAdapter(
                getApplicationContext(),
                getSupportFragmentManager(),
                dpToPixels(2,this),
                data

        );

        ShadowTransformer transformer = new ShadowTransformer(viewPager,pagerAdapter);
        transformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false,transformer);
    }

    public void sendMessage(){
        imageProvider.uploadImages(ConfirmImageSendActivity.this,messages);
        finish();
    }

    public void setMessage(int position, String message){
        if (message.equals("")) {
            message = "\uD83D\uDCF7image";
        }
        messages.get(position).setMessage(message);
    }

    public static float dpToPixels(int dp, Context context){
        return dp*(context.getResources().getDisplayMetrics().density);
    }
}