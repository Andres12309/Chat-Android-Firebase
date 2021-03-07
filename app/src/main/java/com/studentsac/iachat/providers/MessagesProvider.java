package com.studentsac.iachat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.studentsac.iachat.models.Message;

public class MessagesProvider {
    CollectionReference mCollectionReference;

    public MessagesProvider(){
        mCollectionReference = FirebaseFirestore.getInstance().collection("Messages");
    }

    public Task<Void> create(Message message){
        DocumentReference document = mCollectionReference.document();
        message.setId(document.getId());
        return  document.set(message);
    }

    public Query getMessageByChat(String idChat){
        return mCollectionReference.whereEqualTo("idChat",idChat).orderBy("timestamp",Query.Direction.ASCENDING);
    }

}
