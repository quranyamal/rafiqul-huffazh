package org.tangaya.rafiqulhuffazh.data.model;

import android.os.Environment;

public class Recording extends QuranAyahAudio {

    private static String USER_RECORDING_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/recording/";

    private static int count = 0;
    private int id;

    public Recording(int surah, int ayah) {
        super(surah, ayah);

        id = Recording.count++;
    }

    @Override
    public String getFilePath() {
        return USER_RECORDING_PATH + filename;
    }
}
