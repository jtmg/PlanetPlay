package com.example.topog.planetplay;

/**
 * Created by topog on 10/05/2017.
 */

public class Songs {
    private long mSongId;
    private String mSongTitle;
    private String mSongArtist;
    private String path;
    public Songs(long id, String title,String artist, String caminho)
    {
        mSongId = id;
        mSongTitle = title;
        mSongArtist = artist;
        path = caminho;
    }
    public long getSongID()
    {
        return mSongId;
    }
    public String getSongTitle()
    {
        return mSongTitle;
    }
    public String getPath()
    {
        return  path;
    }

    public String getSongArtist(){return mSongArtist; }

}
