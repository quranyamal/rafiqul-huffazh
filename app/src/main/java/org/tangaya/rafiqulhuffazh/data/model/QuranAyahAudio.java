package org.tangaya.rafiqulhuffazh.data.model;

import android.os.Environment;

import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class QuranAyahAudio {

    private static String TEST_AUDIO_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/test/";

    public static String UNTRANSCRIBED = "UNTRANSCRIBED";

    int surah;
    int ayah;
    String filename;
    String qscript, arabic;
    String transcription = UNTRANSCRIBED;
    String arabicTranscription;


    public QuranAyahAudio(int surah, int ayah) {
        this.surah = surah;
        this.ayah = ayah;

        this.qscript = QuranUtil.getQScript(surah, ayah);
        this.arabic = QuranUtil.getAyah(surah, ayah);

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

    public String getQScript() {
        return qscript;
    }

    public String getArabic() {
        return arabic;
    }

    public String getTranscription() {
        return transcription;
    }

    public String getArabicTranscription() {
        return arabicTranscription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public void setArabicTranscription(String arabicTranscription) {
        this.arabicTranscription = arabicTranscription;
    }
}
