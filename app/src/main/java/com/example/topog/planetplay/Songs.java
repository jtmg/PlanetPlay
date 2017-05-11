package com.example.topog.planetplay;

/**
 * Created by topog on 10/05/2017.
 */

public class Songs {
    private long mSongId;
    private String mSongTitle;
    public Songs(long id, String title)
    {
        mSongId = id;
        mSongTitle = title;
    }
    public long getSongID()
    {
        return mSongId;
    }
    public String getSongTitle()
    {
        return mSongTitle;
    }

}
