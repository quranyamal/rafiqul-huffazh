package org.tangaya.rafiqulhuffazh.data.model;

import android.os.Environment;

public class QuranAyahAudio {

    private static String TEST_AUDIO_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/test/";

    public static String UNTRANSCRIBED = "UNTRANSCRIBED";

    int surah;
    int ayah;
    String filename;
    String transcription = UNTRANSCRIBED;


    public QuranAyahAudio(int surah, int ayah) {
        this.surah = surah;
        this.ayah = ayah;

        filename = surah + "_" + ayah + ".wav";
    }

    public int getSurah() {
        return surah;
    }

    public int getAyah() {
        return ayah;
    }

    public String getFilePath() {
        return TEST_AUDIO_PATH + filename;
    }

    public String getTranscription() {
        return transcription;
    }
}
