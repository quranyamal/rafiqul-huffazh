package org.tangaya.quranasrclient.data;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.util.Evaluator;
import org.tangaya.quranasrclient.util.QScriptToArabic;
import org.tangaya.quranasrclient.util.diff_match_patch;

import timber.log.Timber;

public class EvaluationOld extends BaseObservable {

    String extStorageDir = Environment.getExternalStorageDirectory()+"";
    String quranVerseAudioDir = extStorageDir + "/rafiqul-huffazh";

    // only store response with final transcription status
    //JSONObject mResponse;
    private ObservableField<JSONObject> mResponse = new ObservableField<>();

    //String mTranscription;
    private ObservableField<String> mTranscription = new ObservableField<>();

    private ObservableField<String> mArabicTranscription = new ObservableField<>();

    //String mVerseScript;
    private ObservableField<String> mVerseScript = new ObservableField<>();;

    //String mVerseQScript;
    private ObservableField<String> mVerseQScript = new ObservableField<>();

    //String mFilepath;
    private ObservableField<String> mFilepath = new ObservableField<>();

    //String verseNum;
    private ObservableField<String> verseNum = new ObservableField<>();

    //LinkedList<diff_match_patch.Diff> mDiff;
    //private ObservableField<LinkedList<diff_match_patch.Diff>> mDiff = new ObservableField<>();

    private ObservableField<String> mDiff = new ObservableField<>();

    private ObservableField<String> levScore = new ObservableField<>();

    private ObservableBoolean isCorrect = new ObservableBoolean();

    // Correct | Insertion | Deletion | Combination
    private ObservableField<String> evalStr = new ObservableField<>();

    //private int mChapter, mVerse, mSessionId;
    private ObservableInt mChapter = new ObservableInt();
    private ObservableInt mVerse = new ObservableInt();
    private ObservableInt mSessionId = new ObservableInt();


    // 0: unprocessed, 1: processing, 2: aborted, 3: finished (recognized)
    //private int mStatus;
    private ObservableInt mStatus = new ObservableInt();

    // todo: refactor to mvvm
    private diff_match_patch dmp = new diff_match_patch();

    public EvaluationOld(int chapter, int verse, int sessionId) {

    }

    Attempt attempt;
    String transcription, reference, strResult;
    int chapter, verse;

    Evaluator evaluator = new Evaluator();

    public EvaluationOld(Attempt attempt, String transcription) {
        this.attempt = attempt;
        this.transcription = transcription;

        chapter = attempt.getChapterNum();
        verse = attempt.getVerseNum();

        mChapter.set(chapter);
        mVerse.set(verse);

        verseNum.set("Verse " + verse);

        mVerseScript.set(QuranScriptRepository.getChapter(mChapter.get()).getVerseScript(mVerse.get()));
        mVerseQScript.set(QuranScriptRepository.getChapter(mChapter.get()).getVerseQScript(mVerse.get()));

        isCorrect.set(false);

        notifyChange();

        reference = QuranScriptRepository.getChapter(chapter).getVerseScript(verse);
        isCorrect.set(transcription.equals(reference));

        if (isCorrect.get()) {
            strResult = "Correct";
        } else {
            strResult = evaluator.getDiffType(reference, transcription);
        }

        ////
        mTranscription.set(transcription);
        mVerseScript.set(QuranScriptRepository.getChapter(chapter).getVerseScript(verse));

        mVerseQScript.set(QuranScriptRepository.getChapter(chapter).getVerseQScript(verse));

        mArabicTranscription.set(QScriptToArabic.getArabic(transcription));

        mDiff.set(dmp.diff_main(mVerseQScript.get(), mTranscription.get()).toString());

        //levScore.set(dmp.diff_levenshtein(dmp.diff_main(mVerseQScript.get(), mTranscription.get())));

        float score = new Evaluator().getScore(mVerseQScript.get(), mTranscription.get());
        levScore.set(String.format("%.2f", score));

        if (mTranscription.get().equals(mVerseQScript.get())) {
            evalStr.set("Correct");
            levScore.set("1");
            isCorrect.set(true);
        } else {
            //evalStr.set("Wrong");   // todo: improve

            evalStr.set(new Evaluator().getDiffType(mVerseQScript.get(), mTranscription.get()));
            isCorrect.set(false);
        }

        notifyChange();
    }

    public ObservableInt getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus.set(status);
    }

    public void setFilepath(String filepath) {
        mFilepath.set(filepath);
    }

    public ObservableField<String> getTranscription() {
        return mTranscription;
    }

    public ObservableField<String> getArabicTranscription() {
        return mArabicTranscription;
    }

    public ObservableField<String> getVerseScript() {
        return mVerseScript;
    }

    public ObservableField<String> getVerseQScript() {
        return mVerseQScript;
    }

    public ObservableField<String> getDiff() {
        return mDiff;
    }

    public ObservableField<String> getVerseNum() {
        return verseNum;
    }

    public ObservableBoolean isCorrect() {
        return isCorrect;
    }

    public ObservableField<String> getEvalStr() {
        return evalStr;
    }

    public ObservableField<String> getLevScore() {
        return levScore;
    }
}
