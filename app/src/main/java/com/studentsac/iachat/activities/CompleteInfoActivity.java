package com.studentsac.iachat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.studentsac.iachat.R;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.ImageProvider;
import com.studentsac.iachat.providers.MainActivity;
import com.studentsac.iachat.providers.UsersProvider;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import android.app.Activity;


public class CompleteInfoActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView username;
    Button btnConfirmar;

    UsersProvider mUsersProvider;
    ImageProvider mImageProvider;

    CircleImageView mPhotoProfile;

    Options mOptions;

    ArrayList<String> mReturnValues = new ArrayList<>();
    File mImageFile;

    String mUsername = "";

    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        mAuth = FirebaseAuth.getInstance();
        mUsersProvider = new UsersProvider();
        mImageProvider = new ImageProvider();

        username = findViewById(R.id.username);
        btnConfirmar = findViewById(R.id.btnConfirm);
        mPhotoProfile = findViewById(R.id.photoProfile);

        mDialog = new ProgressDialog(CompleteInfoActivity.this);
        mDialog.setTitle("Espere...");
        mDialog.setMessage("Guardando Informacion");

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(true)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        previewInfo();

        mPhotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPix();
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = username.getText().toString();
                if(!mUsername.equals("") && mImageFile != null){
                    saveImage();
                }
                else{
                    Toast.makeText(CompleteInfoActivity.this, "Debe seleccionar una imagen e ingresar un nombre de usuario", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startPix() {
        Pix.start(CompleteInfoActivity.this,mOptions);
    }

    private void uploadInfoUser(String url) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //mUsername = username.getText().toString();
        User user = new User();
        user.setUsername(mUsername);
        user.setEmail(currentUser.getEmail());
        user.setId(currentUser.getUid());
        user.setImage(url);
        mUsersProvider.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDialog.dismiss();
                Toast.makeText(CompleteInfoActivity.this, "Informacion guardada", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CompleteInfoActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void saveImage() {

        mDialog.show();
        mImageProvider.save(CompleteInfoActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            uploadInfoUser(url);
                        }
                    });
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(CompleteInfoActivity.this, "No se puedo almacenar la informacion", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void previewInfo(){
        FirebaseUser user = mAuth.getCurrentUser();


        mUsersProvider.getUserInfo(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    username.setText(documentSnapshot.getString("username"));
                }
                else{
                    if(user.getDisplayName() != null){
                        username.setText(user.getDisplayName());
                    }else{
                        username.setText("");

                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile = new File(mReturnValues.get(0));
            mPhotoProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(CompleteInfoActivity.this, mOptions);
                } else {
                    Toast.makeText(CompleteInfoActivity.this, "Porfavor permite el acceso a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}