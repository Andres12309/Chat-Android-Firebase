package com.studentsac.iachat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ProfileActivity;
import com.studentsac.iachat.providers.ImageProvider;
import com.studentsac.iachat.providers.UsersProvider;

public class BottomSheetUsername extends BottomSheetDialogFragment {

    UsersProvider usersProvider;
    FirebaseAuth mAuth;

    Button mBtnSave;
    Button mBtnCancel;
    EditText mEditTextUsername;

    String username;

    public static BottomSheetUsername newInstance(String username){
        BottomSheetUsername bottomSheetSelectImage = new BottomSheetUsername();
        Bundle arguments = new Bundle();
        arguments.putString("username",username);
        bottomSheetSelectImage.setArguments(arguments);
        return  bottomSheetSelectImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getArguments().getString("username");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_username,container,false);

        usersProvider = new UsersProvider();
        mAuth = FirebaseAuth.getInstance();

        mBtnSave = view.findViewById(R.id.btnSave);
        mBtnCancel = view.findViewById(R.id.btnCncel);
        mEditTextUsername = view.findViewById(R.id.editTextUsername);
        mEditTextUsername.setText(username);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsername();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void updateUsername() {
        FirebaseUser user = mAuth.getCurrentUser();
        String username = mEditTextUsername.getText().toString();
        if(!username.equals("")){
            usersProvider.updateUsername(user.getUid(),username).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dismiss();
                    Toast.makeText(getContext(), "Nombre de usuario actualizado con exito", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
