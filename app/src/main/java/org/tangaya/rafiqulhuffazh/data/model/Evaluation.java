package org.tangaya.rafiqulhuffazh.data.model;

import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class Evaluation {

    private Attempt attempt;
    private String transcription, reference;
    private String evalDescription;

    boolean isCorrect;

    int ayah, surah;

    public Evaluation() {}

    public Evaluation(Attempt attempt, String transcription_) {

        this.attempt = attempt;
        this.transcription = transcription_;

        surah = attempt.getSurahNum();
        ayah = attempt.getAyahNum();

        reference = QuranUtil.getAyah(surah, ayah);
        isCorrect = transcription.equals(reference);

        if (isCorrect) {
            evalDescription = "Correct";
        } else {
            evalDescription = "todo";
        }

    }

    public void setEvalDescription(String desc) {
        evalDescription = desc;
    }

    public static void main(String[] args) {

    }

}
