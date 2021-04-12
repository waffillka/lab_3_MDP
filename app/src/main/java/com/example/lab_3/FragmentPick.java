package com.example.lab_3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class FragmentPick extends Fragment implements View.OnClickListener {

    private final int PICK_FROM_GALLERY = 0;
    private final int PICK_MUSIC = 1;

    private ImageButton buttonBrowse;
    private ImageButton buttonMusic;
    private Button buttonClose;
    private Button buttonConfirm;

    private Uri imageUri;
    private Uri musicUri;

    private boolean checkImage = false;
    private boolean checkMusic = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buttonBrowse = getView().findViewById(R.id.button_browse);
        buttonMusic = getView().findViewById(R.id.button_music);
        buttonClose = getView().findViewById(R.id.button_close);
        buttonConfirm = getView().findViewById(R.id.button_confirm);

        buttonBrowse.setOnClickListener(this);
        buttonMusic.setOnClickListener(this);
        buttonClose.setOnClickListener(this);
        buttonConfirm.setOnClickListener(this);

        buttonConfirm.setVisibility(View.GONE);

        buttonBrowse.setClipToOutline(true);
    }

    @Override
    public void onClick(View view) {

        OnButtonClickListener listener = (OnButtonClickListener) getActivity();
        switch (view.getId()) {
            case R.id.button_browse:
                pickImageFromGallery();
                break;
            case R.id.button_music:
                pickMusic();
                break;
            case R.id.button_confirm:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                listener.onConfirmButtonClicked(imageUri, musicUri);
                break;
            case R.id.button_close:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                listener = (OnButtonClickListener) getActivity();
                listener.onCloseButtonClicked();
                break;
        }

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    private void pickMusic(){
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,PICK_MUSIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FROM_GALLERY) {
                try {
                    imageUri = data.getData();
                    InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);

                    buttonBrowse.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    buttonBrowse.setImageBitmap(selectedImage);
                    checkImage = true;
                    //buttonConfirm.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == PICK_MUSIC){
                buttonMusic.setImageResource(R.drawable.unnamed);
                musicUri = data.getData();
                System.out.println(musicUri);
                checkMusic = true;
            }
        }
        if (checkImage && checkMusic)
            buttonConfirm.setVisibility(View.VISIBLE);
    }

    public interface OnButtonClickListener {
        void onCloseButtonClicked();

        void onConfirmButtonClicked(Uri imageUri, Uri musicUri);
    }
}
