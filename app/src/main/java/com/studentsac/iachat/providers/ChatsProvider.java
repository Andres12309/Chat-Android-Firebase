package com.studentsac.iachat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.studentsac.iachat.models.Chat;

import java.util.ArrayList;

public class ChatsProvider {

    CollectionReference mCollectionReference;

    public ChatsProvider(){
        mCollectionReference = FirebaseFirestore.getInstance().collection("Chats");
    }

    public Task<Void> create(Chat chat){
        return mCollectionReference.document(chat.getId()).set(chat);
    }

    public Query getChatsUsers(String idUser){
        return mCollectionReference.whereArrayContains("ids",idUser);
    }

    public Query getChatByUsers(String idUser1, String idUser2){
        ArrayList<String> ids = new ArrayList<>();
        ids.add(idUser1+idUser2);
        ids.add(idUser2+idUser1);

        return mCollectionReference.whereIn("id",ids);
    }
}
