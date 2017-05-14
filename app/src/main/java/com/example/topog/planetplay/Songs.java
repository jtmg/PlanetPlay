package com.example.topog.planetplay;

/**
 * Created by topog on 10/05/2017.
 */

public class Songs {
    private long mSongId;
    private String mSongTitle;
    private String mSongArtist;
    public Songs(long id, String title,String artist)
    {
        mSongId = id;
        mSongTitle = title;
        mSongArtist = artist;
    }
    public long getSongID()
    {
        return mSongId;
    }
    public String getSongTitle()
    {
        return mSongTitle;
    }
    public String getSongArtist(){return mSongArtist; }

}
