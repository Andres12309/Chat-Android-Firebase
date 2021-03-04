package com.studentsac.iachat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studentsac.iachat.R;
import com.studentsac.iachat.fragments.BottomSheetSelectImage;
import com.studentsac.iachat.utils.MyToolbar;

public class ProfileActivity extends AppCompatActivity {
    
    FloatingActionButton mSelectImage;
    BottomSheetSelectImage mBottomSheetSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mSelectImage = findViewById(R.id.selectImage);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheetSelectImage();
            }
        });

        MyToolbar.show(this,"Perfil",true);
    }

    private void openSheetSelectImage() {
        mBottomSheetSelectImage = BottomSheetSelectImage.newInstance();
        mBottomSheetSelectImage.show(getSupportFragmentManager(),mBottomSheetSelectImage.getTag());
    }
}