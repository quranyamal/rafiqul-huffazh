package org.tangaya.quranasrclient.data.source;

import org.tangaya.quranasrclient.data.Attempt;
import org.tangaya.quranasrclient.util.Evaluator;

public class Evaluation {

    private Evaluator evaluator;
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

        reference = QuranScriptRepository.getChapter(chapter).getVerseScript(verse);
        isCorrect = transcription.equals(reference);

        if (isCorrect) {
            strResult = "Correct";
        } else {
            strResult = evaluator.getDiffType(reference, transcription);
        }
    }
}
