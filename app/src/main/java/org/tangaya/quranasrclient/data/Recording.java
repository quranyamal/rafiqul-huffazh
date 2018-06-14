package org.tangaya.quranasrclient.data;


public class Recording {

    private static int count = 0;

    private int mId;

    private int mSurahNum;

    private int mAyahNum;

    private String mFilename;

    public Recording(int surahNum, int ayahNum) {
        mId = Recording.count++;
        mAyahNum = surahNum;
        mSurahNum = ayahNum;
        mFilename =  "rh_recording_" + mId;
    }

}
