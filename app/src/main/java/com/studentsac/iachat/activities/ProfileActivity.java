package com.studentsac.iachat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.studentsac.iachat.R;
import com.studentsac.iachat.fragments.BottomSheetSelectImage;
import com.studentsac.iachat.fragments.BottomSheetUsername;
import com.studentsac.iachat.models.User;
import com.studentsac.iachat.providers.ImageProvider;
import com.studentsac.iachat.providers.UsersProvider;
import com.studentsac.iachat.utils.MyToolbar;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    
    FloatingActionButton mSelectImage;
    BottomSheetSelectImage mBottomSheetSelectImage;
    BottomSheetUsername bottomSheetUsername;

    UsersProvider mUsersProvider;
    ImageProvider mImageProvider;
    FirebaseAuth mAuth;

    TextView mUsernameView;
    TextView mEstadoView;
    TextView mEmailView;
    CircleImageView mCircleImageView;

    User mUser;

    Options mOptions;

    ArrayList<String> mReturnValues = new ArrayList<>();
    File mImageFile;

    ImageView imageViewEditUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mSelectImage = findViewById(R.id.selectImage);

        mUsernameView = findViewById(R.id.usernameView);
        mEstadoView = findViewById(R.id.estadoView);
        mEmailView = findViewById(R.id.emailView);
        mCircleImageView = findViewById(R.id.circleImageProfile);
        imageViewEditUsername = findViewById(R.id.editUsername);

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

        mUsersProvider = new UsersProvider();
        mAuth = FirebaseAuth.getInstance();



        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheetSelectImage();
            }
        });

        imageViewEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheetUsername();
            }
        });

        getUserInfo();

        MyToolbar.show(this,"Perfil",true);
    }

    private void getUserInfo() {
        mUsersProvider.getUserInfo(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    mUser = value.toObject(User.class);
                    mUsernameView.setText(mUser.getUsername());
                    mEmailView.setText(mUser.getEmail());
                    if(mUser.getImage()!=null){
                        if(!mUser.getImage().equals("")){
                            Picasso.with(ProfileActivity.this).load(mUser.getImage()).into(mCircleImageView);
                        }
                    }
                }
            }
        });
    }

    private void openSheetUsername() {
        if(mUser!=null){
            bottomSheetUsername = BottomSheetUsername.newInstance(mUser.getUsername());
            bottomSheetUsername.show(getSupportFragmentManager(),bottomSheetUsername.getTag());
        }
        else{
            Toast.makeText(this, "Ocurrio un error al cargar la informacion", Toast.LENGTH_LONG).show();
        }
    }

    private void openSheetSelectImage() {
        if(mUser!=null){
            mBottomSheetSelectImage = BottomSheetSelectImage.newInstance(mUser.getImage());
            mBottomSheetSelectImage.show(getSupportFragmentManager(),mBottomSheetSelectImage.getTag());
        }
        else{
            Toast.makeText(this, "Ocurrio un error al cargar la informacion", Toast.LENGTH_LONG).show();
        }
    }

    public void setImageDefault(){
        mCircleImageView.setImageResource(R.drawable.ic_person_profile);
    }

    public void startPix() {
        Pix.start(ProfileActivity.this,mOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile = new File(mReturnValues.get(0));
            mCircleImageView.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ProfileActivity.this, mOptions);
                } else {
                    Toast.makeText(ProfileActivity.this, "Porfavor permite el acceso a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private void saveImage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mImageProvider = new ImageProvider();
        mImageProvider.save(ProfileActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getDownloadUri().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            mUsersProvider.updateImage(currentUser.getUid(),url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, "La imagen se actualizo con exito", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(ProfileActivity.this, "No se puedo almacenar la informacion", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}