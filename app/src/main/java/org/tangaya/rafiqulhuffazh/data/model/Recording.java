package org.tangaya.rafiqulhuffazh.data.model;

public class Recording {

    private static int count = 0;

    private int mId;

    private int mSurah;

    private int mAyah;

    private String mFilename;

    public Recording(int surah, int ayah) {
        mId = Recording.count++;
        mSurah = surah;
        mAyah = ayah;
    }

    public Recording(int surah, int ayah, String filename) {
        mId = Recording.count++;
        mSurah = surah;
        mAyah = ayah;
        mFilename = filename;
    }

    public int getSurah() {
        return mSurah;
    }

    public int getAyah() {
        return mAyah;
    }

}
