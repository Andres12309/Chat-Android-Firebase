package com.studentsac.iachat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentsac.iachat.models.User;

import java.util.HashMap;
import java.util.Map;

public class UsersProvider {

    private CollectionReference mCollection;

    public UsersProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<Void> create(User user){
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        return mCollection.document(user.getId()).update(map);
    }
}
