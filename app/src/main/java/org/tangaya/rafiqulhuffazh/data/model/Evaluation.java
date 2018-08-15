package org.tangaya.rafiqulhuffazh.data.model;

import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class Evaluation {

    private Attempt attempt;
    private String transcription, reference;
    private String strResult;

    boolean isCorrect;

    int verse, chapter;

    public Evaluation(Attempt attempt, String transcription_) {

        this.attempt = attempt;
        this.transcription = transcription_;

        chapter = attempt.getChapterNum();
        verse = attempt.getVerseNum();

        reference = QuranUtil.getAyah(chapter, verse);
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
