package com.studentsac.iachat.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.ConfirmImageSendActivity;
import com.studentsac.iachat.activities.HistoriesConfirmActivity;
import com.studentsac.iachat.adapters.CardAdapter;

import java.io.File;

public class StoriesPagerFragment extends Fragment {

    View mView;
    CardView cardViewOptions;
    ImageView imageViewPicture;
    ImageView imageViewBack;
    EditText editTextIdea;

    ImageView imageViewSend;

    public static Fragment newInstance(int position, String pathImage) {
        StoriesPagerFragment fragment = new StoriesPagerFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("image", pathImage);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_stories_pager, container, false);

        cardViewOptions = mView.findViewById(R.id.cardViewOptions);
        imageViewPicture = mView.findViewById(R.id.imageViewPicture);
        imageViewBack = mView.findViewById(R.id.imageViewBack);
        editTextIdea = mView.findViewById(R.id.editTextIdea);
        imageViewSend = mView.findViewById(R.id.imageViewSend);

        cardViewOptions.setMaxCardElevation(cardViewOptions.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        String imagePath = getArguments().getString("image");
        int position = getArguments().getInt("position");

        if(imagePath != null){
            File file = new File(imagePath);
            imageViewPicture.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        editTextIdea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((HistoriesConfirmActivity) getActivity()).setIdea(position,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HistoriesConfirmActivity) getActivity()).sendStory();
            }
        });

        return mView;
    }

    public CardView getCardView(){
        return cardViewOptions;
    }
}