package com.studentsac.iachat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.models.Story;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoriesProvider {
    CollectionReference mCollectionReference;

    public StoriesProvider(){
        mCollectionReference = FirebaseFirestore.getInstance().collection("Stories");
    }

    public Task<Void> create(Story story){
        DocumentReference document = mCollectionReference.document();
        story.setId(document.getId());
        return  document.set(story);
    }

    public Query getStoriesByTimestampLimit(){
        long now = new Date().getTime();
        return mCollectionReference.whereGreaterThan("timestampLimit",now);
    }
}
