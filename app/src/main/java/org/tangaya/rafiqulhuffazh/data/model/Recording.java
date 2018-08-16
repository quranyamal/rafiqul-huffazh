package org.tangaya.rafiqulhuffazh.data.model;

import android.os.Environment;

public class Recording {

    public static String RECORDING_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/recording/";

    private static int count = 0;

    private int mId;

    private int mSurah, mAyah;

    private String mFilename;

    public Recording(int surah, int ayah) {
        mSurah = surah;
        mAyah = ayah;
        mFilename = surah + "_" + ayah + ".wav";
        mId = Recording.count++;
    }

    public String getFilename() {
        return mFilename;
    }


}
