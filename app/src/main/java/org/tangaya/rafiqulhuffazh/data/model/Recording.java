package org.tangaya.rafiqulhuffazh.data.model;

public class Recording extends QuranAyahAudio {

    private static int count = 0;
    private int id;
    private String filename;

    public Recording(int surah_, int ayah_) {
        this.surah = surah_;
        this.ayah = ayah_;
        filename = surah + "_" + ayah + ".wav";
        id = Recording.count++;
    }
}
