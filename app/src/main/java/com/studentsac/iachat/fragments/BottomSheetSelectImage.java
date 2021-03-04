package com.studentsac.iachat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ProfileActivity;
import com.studentsac.iachat.providers.ImageProvider;
import com.studentsac.iachat.providers.UsersProvider;

public class BottomSheetSelectImage extends BottomSheetDialogFragment {

    LinearLayout mDeleteImageProfile;
    LinearLayout mSelectImageProfile;
    ImageProvider imageProvider;
    UsersProvider usersProvider;
    FirebaseAuth mAuth;

    String image;

    public static BottomSheetSelectImage newInstance(String usrImageProfile){
        BottomSheetSelectImage bottomSheetSelectImage = new BottomSheetSelectImage();
        Bundle arguments = new Bundle();
        arguments.putString("image",usrImageProfile);
        bottomSheetSelectImage.setArguments(arguments);
        return  bottomSheetSelectImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        image = getArguments().getString("image");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_select_image,container,false);
        mDeleteImageProfile = view.findViewById(R.id.linearLayoutDeleteImage);
        mSelectImageProfile = view.findViewById(R.id.linearLayoutSelectImage);

        imageProvider = new ImageProvider();
        usersProvider = new UsersProvider();
        mAuth = FirebaseAuth.getInstance();

        mDeleteImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });
        mSelectImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
        return view;
    }

    private void updateImage() {
        ((ProfileActivity)getActivity()).startPix();
    }


    private void deleteImage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        imageProvider.delete(image).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    usersProvider.updateImage(currentUser.getUid(),null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){
                                setImageDefault();
                                Toast.makeText(getContext(), "Imagen eliminada con exito", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "No se puedo elimanar la imagen de la BD", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "No se pudo eliminar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setImageDefault() {
        ((ProfileActivity)getActivity()).setImageDefault();
    }
}
