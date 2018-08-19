package org.tangaya.rafiqulhuffazh.util;

import android.os.Environment;

public class AudioFileHelper {

    private static String USER_RECORDING_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/recording/";

    private static String QARI1_AUDIO_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/test/";

    public static String getUserRecordingPath() {
        return USER_RECORDING_PATH;
    }

    public static String getQari1AudioPath() {
        return USER_RECORDING_PATH;
    }

    public static String getUserRecordingFilePath(int surah, int ayah) {
        return USER_RECORDING_PATH + surah + "_" + ayah + ".wav";
    }

    public static String getQari1AudioFilePath(int surah, int ayah) {
        return QARI1_AUDIO_PATH + surah + "_" + ayah + ".wav";
    }

}
