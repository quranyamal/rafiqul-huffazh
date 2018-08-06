package org.tangaya.quranasrclient.data.model;

import org.tangaya.quranasrclient.util.QuranScriptFactory;
import org.tangaya.quranasrclient.util.MurojaahEvaluator;

public class Evaluation {

    private MurojaahEvaluator murojaahEvaluator;
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

        reference = QuranScriptFactory.getChapter(chapter).getVerseArabicScript(verse);
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
