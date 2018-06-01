package org.tangaya.quranasrclient.data;

import android.util.Log;

public class Surah {
    private int mId;
    private String mTitle;

    public Surah(int id, String title) {
        mId = id;
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
