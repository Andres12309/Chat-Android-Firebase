package com.studentsac.iachat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.studentsac.iachat.models.Message;

import java.util.HashMap;
import java.util.Map;

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

    public Task<Void> updateStatusMessage(String idMessage, String statusMessage){
        Map<String,Object> map = new HashMap<>();
        map.put("status",statusMessage);
        return mCollectionReference.document(idMessage).update(map);
    }

    public Query getMessageNotRead(String idChat){
        return mCollectionReference.whereNotEqualTo("idChat",idChat).whereEqualTo("status","SENT");
    }
    public Query getMessageNotReadReceive(String idChat,String idReceiver){
        return mCollectionReference.whereNotEqualTo("idChat",idChat).whereEqualTo("status","SENT").whereEqualTo("idUserReceive",idReceiver);
    }

    public Query getLastMessage(String idChat){
        return mCollectionReference.whereEqualTo("idChat",idChat).orderBy("timestamp",Query.Direction.DESCENDING).limit((1));
    }

}
