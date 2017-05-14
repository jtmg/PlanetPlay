package com.example.topog.planetplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by topog on 14/05/2017.
 */

public class Adapter extends BaseAdapter {
    private ArrayList<Songs> song;
    private LayoutInflater songInflater;
    public Adapter (Context context, ArrayList<Songs> lista)
    {
        song = lista;
        songInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return song.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLayout = (LinearLayout) songInflater.inflate(R.layout.musica,parent,false);
        TextView songView = (TextView)songLayout.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLayout.findViewById(R.id.song_artist);
        //get song using position
        Songs currSong = song.get(position);
        //get title and artist strings
        songView.setText(currSong.getSongTitle());
        artistView.setText(currSong.getSongArtist());
        //set position as tag
        songLayout.setTag(position);

        return songLayout;
    }
}
