package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.studentsac.iachat.R;
import com.studentsac.iachat.models.Story;

import java.net.URL;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoriesDetailActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    View mainView;
    ImageView imageViewStory;
    TextView textViewIdea;
    StoriesProgressView storiesProgressView;

    Story[] stories;
    Gson gson = new Gson();

    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_detail);
        setStatusBarColor();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mainView = findViewById(R.id.mainView);
        imageViewStory = findViewById(R.id.imageViewStory);
        textViewIdea = findViewById(R.id.textViewIdea);
        storiesProgressView = findViewById(R.id.storiesProgress);
        storiesProgressView.setStoriesListener(this);

        String storiesJSON = getIntent().getStringExtra("stories");
        stories = gson.fromJson(storiesJSON,Story[].class);

        storiesProgressView.setStoriesCount(stories.length);
        storiesProgressView.setStoryDuration(4000);
        storiesProgressView.startStories(counter);

        setStoriesInfo();

    }

    private void setStoriesInfo(){
        try {
            URL url = new URL(stories[counter].getUrl());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageViewStory.setImageBitmap(image);
            textViewIdea.setText(stories[counter].getIdea());


        }catch (Exception e){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, this.getTheme()));
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onNext() {
        counter = counter + 1;
        setStoriesInfo();
    }

    @Override
    public void onPrev() {
        if((counter-1)<0)
            return;
        counter = counter - 1;
    }

    @Override
    public void onComplete() {
        finish();
    }
}