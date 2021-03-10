package com.studentsac.iachat.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.HistoriesConfirmActivity;
import com.studentsac.iachat.activities.ProfileActivity;
import com.studentsac.iachat.adapters.StoriesAdapter;
import com.studentsac.iachat.models.Story;
import com.studentsac.iachat.providers.StoriesProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HistoriesFragment extends Fragment {

    View mView;
    LinearLayout linearLayoutAddStatus;
    Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();
    RecyclerView recyclerViewStories;
    StoriesAdapter storiesAdapter;
    StoriesProvider storiesProvider;

    ArrayList<Story> mStoriesAnRepeat;

    Gson gson = new Gson();

    ListenerRegistration listenerRegistrationStories;

    public HistoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_histories, container, false);
        linearLayoutAddStatus = mView.findViewById(R.id.linearLayoutAddStatus);
        recyclerViewStories = mView.findViewById(R.id.recyclerViewStories);

        storiesProvider = new StoriesProvider();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewStories.setLayoutManager(linearLayoutManager);

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(5)                                                   //Number of images to restict selection count
                .setFrontfacing(true)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        linearLayoutAddStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPix();
            }
        });

        getStories();
        setInterval();
        return  mView;
    }

    private void setInterval() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mStoriesAnRepeat != null){
                    for (int i=0;i<mStoriesAnRepeat.size();i++){
                        if(mStoriesAnRepeat.get(i).getJson() != null){
                            Story[] storiesJson = gson.fromJson(mStoriesAnRepeat.get(i).getJson(),Story[].class);

                            for(int j=0;j<storiesJson.length;j++){
                                long now = new Date().getTime();
                                if(now>storiesJson[j].getTimestampLimit()){
                                    if(listenerRegistrationStories != null){
                                        listenerRegistrationStories.remove();
                                    }
                                    getStories();
                                }
                            }
                        }
                    }
                }
            }
        },0,60000);
    }

    private void getStories() {
        listenerRegistrationStories = storiesProvider.getStoriesByTimestampLimit().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if(querySnapshot != null){
                    ArrayList<Story> storiesList = new ArrayList<>();
                    mStoriesAnRepeat = new ArrayList<>();
                    for(DocumentSnapshot documents : querySnapshot.getDocuments()){
                        Story story = documents.toObject(Story.class);
                        storiesList.add(story);
                    }
                    for(Story s: storiesList){
                        boolean found = false;
                        for(Story st: mStoriesAnRepeat){
                            if(st.getIdUser().equals(s.getIdUser())){
                                found = true;
                                break;
                            }
                        }

                        if(!found){
                            mStoriesAnRepeat.add(s);
                        }
                    }

                    for(Story noRepeat: mStoriesAnRepeat){
                        ArrayList<Story> storiesListNew = new ArrayList<>();
                        for(Story s : storiesList){
                            if(s.getIdUser().equals(noRepeat.getIdUser())){
                                storiesListNew.add(s);
                            }
                        }

                        String storiesJson = gson.toJson(storiesListNew);
                        Log.d("STATUS", "JSON: " + storiesJson);
                        noRepeat.setJson(storiesJson);
                    }

                    storiesAdapter = new StoriesAdapter(getActivity(),mStoriesAnRepeat);
                    recyclerViewStories.setAdapter(storiesAdapter);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listenerRegistrationStories != null){
            listenerRegistrationStories.remove();
        }
    }

    public void startPix() {
        Pix.start(HistoriesFragment.this,mOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Intent intent = new Intent(getContext(), HistoriesConfirmActivity.class);
            intent.putExtra("data",mReturnValues);
            startActivity(intent);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(HistoriesFragment.this, mOptions);
                } else {
                    Toast.makeText(getContext(), "Porfavor permite el acceso a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}