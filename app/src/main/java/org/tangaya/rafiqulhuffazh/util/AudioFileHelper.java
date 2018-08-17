package org.tangaya.rafiqulhuffazh.util;

import android.os.Environment;

public class AudioFileHelper {

    private static String RECORDING_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/recording/";

    private static String QARI1_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/test/";

    public static String getRecordingPath() {
        return RECORDING_PATH;
    }

    public static String getRecordingFilePath(int surah, int ayah) {
        return RECORDING_PATH + surah + "_" + ayah + ".wav";
    }

    public static String getQari1FilePath(int surah, int ayah) {
        return QARI1_PATH+ surah + "_" + ayah + ".wav";
    }

}
