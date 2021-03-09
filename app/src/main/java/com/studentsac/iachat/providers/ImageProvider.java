package com.studentsac.iachat.providers;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studentsac.iachat.models.Message;
import com.studentsac.iachat.models.Story;
import com.studentsac.iachat.utils.CompressorBitmapImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ImageProvider {
    StorageReference mStorage;
    FirebaseStorage firebaseStorage;
    MessagesProvider messagesProvider;
    StoriesProvider storiesProvider;

    int index;

    public ImageProvider(){
        firebaseStorage = FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();
        messagesProvider = new MessagesProvider();
        index = 0;
    }

    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(),500,500);
        StorageReference storage = mStorage.child(new Date()+".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);

        return task;
    }

    public Task<Uri> getDownloadUri(){
        return mStorage.getDownloadUrl();
    }

    public Task<Void> delete(String url){
        return firebaseStorage.getReferenceFromUrl(url).delete();
    }

    public void uploadImages(Context context, ArrayList<Message> messages){
        Uri[] uri = new Uri[messages.size()];
        for(int i=0;i<messages.size();i++){
            File file = CompressorBitmapImage.reduceImageSize(new File(messages.get(i).getUrlImage()));
            uri[i] = Uri.parse("file://"+ file.getPath());

            StorageReference ref = mStorage.child(uri[i].getLastPathSegment());
            ref.putFile(uri[i]).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                messages.get(index).setUrlImage(url);
                                messagesProvider.create(messages.get(index));
                                index++;
                            }
                        });
                    }
                    else{
                        Toast.makeText(context, "Error al guardar informacion", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void uploadImagesStories(Context context, ArrayList<Story> stories){
        Uri[] uri = new Uri[stories.size()];
        File file = CompressorBitmapImage.reduceImageSize(new File(stories.get(index).getUrl()));
        uri[index] = Uri.parse("file://"+ file.getPath());

        StorageReference ref = mStorage.child(uri[index].getLastPathSegment());
        ref.putFile(uri[index]).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            stories.get(index).setUrl(url);
                            storiesProvider.create(stories.get(index));
                            index++;
                            if(index<stories.size()){
                                uploadImagesStories(context, stories);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(context, "Error al guardar informacion", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
