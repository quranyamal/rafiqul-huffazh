package org.tangaya.rafiqulhuffazh.data.model;

abstract public class QuranAyahAudio {

    public static String UNTRANSCRIBED = "UNTRANSCRIBED";

    int surah;
    int ayah;
    String transcription = UNTRANSCRIBED;

    public int getSurah() {
        return surah;
    }

    public int getAyah() {
        return ayah;
    }

    public String getTranscription() {
        return transcription;
    }
}
