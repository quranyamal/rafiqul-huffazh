package org.tangaya.rafiqulhuffazh.data.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import org.json.JSONObject;
import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;
import org.tangaya.rafiqulhuffazh.util.diff_match_patch;


public class EvaluationOld extends BaseObservable {

    static int count = 0;

    MurojaahEvaluator evaluator = MurojaahEvaluator.getInstance();

    public int id;

    // only store response with final transcription status
    //JSONObject mResponse;
    private ObservableField<JSONObject> mResponse = new ObservableField<>();

    //String mTranscription;
    private ObservableField<String> mTranscription = new ObservableField<>();

    private ObservableField<String> mArabicTranscription = new ObservableField<>();

    //String mAyahArabic;
    private ObservableField<String> mAyahArabic = new ObservableField<>();;

    //String mAyahQScript;
    private ObservableField<String> mAyahQScript = new ObservableField<>();

    //String mFilepath;
    private ObservableField<String> mFilepath = new ObservableField<>();

    //String ayahNum;
    private ObservableField<String> ayahNum = new ObservableField<>();

    //LinkedList<diff_match_patch.Diff> mDiff;
    //private ObservableField<LinkedList<diff_match_patch.Diff>> mDiff = new ObservableField<>();

    private ObservableField<String> mDiff = new ObservableField<>();

    private ObservableField<String> scoreStr = new ObservableField<>();

    private ObservableBoolean isCorrect = new ObservableBoolean();

    // Correct | Insertion | Deletion | Combination
    private ObservableField<String> evalStr = new ObservableField<>();

    //private int mSurah, mAyah, mSessionId;
    private ObservableInt mSurah = new ObservableInt();
    private ObservableInt mAyah = new ObservableInt();
    private ObservableInt mStatus = new ObservableInt();

    private int levenshteinValue;

    private ObservableInt earnedPoints = new ObservableInt();
    private ObservableInt maxPoints = new ObservableInt();

    private float score;

    // 0: unprocessed, 1: processing, 2: aborted, 3: finished (recognized)
    //private int mStatus;

    // todo: refactor to mvvm
    private diff_match_patch dmp = new diff_match_patch();

    public EvaluationOld(int surah, int ayah, int sessionId) {}

    Attempt attempt;
    String transcription, reference, strResult;
    public int surah, ayah;

    public EvaluationOld(Attempt attempt, String transcription) {
        this.attempt = attempt;
        this.transcription = transcription;

        surah = attempt.getSurahNum();
        ayah = attempt.getAyahNum();

        mSurah.set(surah);
        mAyah.set(ayah);

        ayahNum.set("Ayat " + ayah);

        mAyahArabic.set(QuranUtil.getAyah(mSurah.get(), mAyah.get()));
        mAyahQScript.set(QuranUtil.getQScript(mSurah.get(), mAyah.get()));

        isCorrect.set(false);

        notifyChange();

        reference = QuranUtil.getQScript(surah, ayah);
        isCorrect.set(transcription.equals(reference));

        if (isCorrect.get()) {
            strResult = "Correct";
        } else {
            strResult = evaluator.getEvalDescrition(attempt.getSurahNum(), attempt.getAyahNum(), transcription);
        }

        ////
        mTranscription.set(transcription);
        mAyahArabic.set(QuranUtil.getAyah(surah, ayah));

        mAyahQScript.set(QuranUtil.getQScript(surah, ayah));

        mArabicTranscription.set(QuranScriptConverter.toArabic(transcription));

        mDiff.set(dmp.diff_main(mAyahQScript.get(), mTranscription.get()).toString());

        //levScore.set(dmp.diff_levenshtein(dmp.diff_main(mAyahQScript.get(), mTranscription.get())));

        levenshteinValue = evaluator.getLevenshteinDistance(mAyahQScript.get(), mTranscription.get());

        maxPoints.set(mAyahQScript.get().length());
        earnedPoints.set(maxPoints.get() - levenshteinValue);

        score = evaluator.getScore(mAyahQScript.get(), mTranscription.get());
        scoreStr.set(String.format("%.2f", score));

        if (mTranscription.get().equals(mAyahQScript.get())) {
            evalStr.set("Correct");
            scoreStr.set("1");
            isCorrect.set(true);
        } else {
            //evalStr.set("Wrong");   // todo: improve

            evalStr.set(evaluator.getEvalDescrition(mSurah.get(), mAyah.get(), transcription));
            isCorrect.set(false);
        }

        notifyChange();
        id = count++;
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

    public ObservableField<String> getAyahArabicScript() {
        return mAyahArabic;
    }

    public ObservableField<String> getAyahQScript() {
        return mAyahQScript;
    }

    public ObservableField<String> getDiff() {
        return mDiff;
    }

    public ObservableField<String> getAyahNum() {
        return ayahNum;
    }

    public ObservableBoolean isCorrect() {
        return isCorrect;
    }

    public ObservableField<String> getEvalStr() {
        return evalStr;
    }

    public ObservableField<String> getScoreStr() {
        return scoreStr;
    }

    public float getScore() {
        return score;
    }

    public ObservableInt getMaxpoints() {
        return maxPoints;
    }

    public ObservableInt getEarnedPoints() {
        return earnedPoints;
    }
}
