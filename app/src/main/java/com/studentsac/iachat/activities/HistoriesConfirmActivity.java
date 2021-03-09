package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.studentsac.iachat.R;
import com.studentsac.iachat.adapters.StoriesPagerAdapter;
import com.studentsac.iachat.models.Story;
import com.studentsac.iachat.providers.ImageProvider;
import com.studentsac.iachat.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.Date;

public class HistoriesConfirmActivity extends AppCompatActivity {

    String idChat;
    String idUserReceiver;
    ViewPager viewPager;
    ArrayList<String> data;
    ArrayList<Story> stories;

    ImageProvider imageProvider;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histories_confirm);

        viewPager = findViewById(R.id.viewpager);
        data = getIntent().getStringArrayListExtra("data");

        mAuth = FirebaseAuth.getInstance();
        imageProvider = new ImageProvider();

        if(data != null){
            for(int i=0;i<data.size();i++){
                long now = new Date().getTime();
                long limit = now + (60*1000*3);

                Story s = new Story();
                s.setIdea(mAuth.getCurrentUser().getUid());
                s.setIdea("");
                s.setTimestamp(now);
                s.setTimestampLimit(limit);
                s.setUrl(data.get(i));
                stories.add(s);

            }
        }

        StoriesPagerAdapter pagerAdapter = new StoriesPagerAdapter(
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

    public void sendStory(){
        imageProvider.uploadImagesStories(HistoriesConfirmActivity.this,stories);
        finish();
    }

    public void setIdea(int position, String idea){
        stories.get(position).setIdea(idea);
    }

    public static float dpToPixels(int dp, Context context){
        return dp*(context.getResources().getDisplayMetrics().density);
    }
}