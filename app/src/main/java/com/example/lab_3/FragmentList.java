package com.example.lab_3;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab_3.R;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentList extends Fragment implements View.OnClickListener {

    Button buttonAdd;
    private ArrayList<Uri> images;
    private ArrayList<Uri> songs;
    private static ListAdapterCustom adapter;
    private ListView list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        buttonAdd = rootView.findViewById(R.id.buttonAdd);
        list = rootView.findViewById(R.id.my_list);
        buttonAdd.setOnClickListener(this);

        images = new ArrayList<>();
        songs = new ArrayList<>();
        adapter = new ListAdapterCustom(getActivity().getApplicationContext(), images, songs);
        list.setAdapter(adapter);
        list.setScrollingCacheEnabled(false);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        OnAddButtonListener listener = (OnAddButtonListener) getActivity();
        listener.onAddButtonClicked(buttonAdd);
    }

    public void setImageMusic(Uri imageUri, Uri musicUri){
        images.add(0, imageUri);
        songs.add(0, musicUri);
        //Collections.reverse(images);
        adapter.changePosition();
        adapter.notifyDataSetChanged();
        list.smoothScrollToPosition(0);
    }

    public interface OnAddButtonListener {
        void onAddButtonClicked(Button add);
    }

}
