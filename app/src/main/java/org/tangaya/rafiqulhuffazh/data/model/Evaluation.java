package org.tangaya.rafiqulhuffazh.data.model;

import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class Evaluation {

    private Attempt attempt;
    private String transcription, reference;
    private String strResult;

    boolean isCorrect;

    int ayah, surah;

    public Evaluation(Attempt attempt, String transcription_) {

        this.attempt = attempt;
        this.transcription = transcription_;

        surah = attempt.getSurahNum();
        ayah = attempt.getAyahNum();

        reference = QuranUtil.getAyah(surah, ayah);
        isCorrect = transcription.equals(reference);

        if (isCorrect) {
            strResult = "Correct";
        } else {
            strResult = "todo";
        }

    }

    public static void main(String[] args) {

    }

}
