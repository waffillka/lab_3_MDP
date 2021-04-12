package com.example.lab_3;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListAdapterCustom extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Uri> images;
    ArrayList<Uri> songs;

    static MediaPlayer mediaPlayer = new MediaPlayer();
    static boolean isPlaying = false;
    static int length = 0;
    static int nowPlaying = -1;

    public ListAdapterCustom(Context context, ArrayList<Uri> images, ArrayList<Uri> songs) {
        this.context = context;
        this.images = images;
        this.songs = songs;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return images.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int pos) {
        return images.get(pos);
    }

    // id по позиции
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public void play(View v, int position) {
        if (nowPlaying == -1)
            nowPlaying = position;
        try {
            if (!isPlaying) {
                mediaPlayer = new MediaPlayer();
                if (position == nowPlaying) {
                    mediaPlayer.setDataSource(context, songs.get(position));
                } else {
                    length = 0;
                    mediaPlayer.setDataSource(context, songs.get(position));
                }
                mediaPlayer.prepare();
                mediaPlayer.seekTo(length);
                mediaPlayer.start();
                isPlaying = true;
            } else {
                mediaPlayer.stop();
                length = mediaPlayer.getCurrentPosition();
                mediaPlayer.release();
                mediaPlayer = null;
                if (position == nowPlaying) {
                    isPlaying = false;
                } else {
                    nowPlaying = position;
                    length = 0;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(context, songs.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(length);
                    mediaPlayer.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changePosition() {
        nowPlaying++;
    }

    public static void stopPlay() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.image = convertView.findViewById(R.id.my_picture);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.image.setImageURI(images.get(position));
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(v, position);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
    }

}
