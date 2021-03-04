package com.studentsac.iachat.utils;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.studentsac.iachat.R;

public class MyToolbar {

    public static void show(AppCompatActivity activity, String title, boolean up){
        Toolbar toolbar = activity.findViewById(R.id.toolBar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(up);
    }
}
