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

public class EvaluationOld extends BaseObservable {

    public static int STATE_UNPROCESSED = 0;
    public static int STATE_PROCESSING = 1;
    public static int STATE_ABORTED = 2;
    public static int STATE_FINISHED = 3;

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
        mChapter.set(chapter);
        mVerse.set(verse);
        mSessionId.set(sessionId);

        verseNum.set("Verse " + verse);

        mVerseScript.set(QuranScriptRepository.getChapter(mChapter.get()).getVerseScript(mVerse.get()));
        mVerseQScript.set(QuranScriptRepository.getChapter(mChapter.get()).getVerseQScript(mVerse.get()));

        isCorrect.set(false);

        mStatus.set(EvaluationOld.STATE_UNPROCESSED);

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

    public ObservableField<String> getAudioFilepath() {
        return mFilepath;
    }

    public void setResponse(String response) {
        Log.d("EvaluationOld", "Setting response");

        try {
            mResponse.set(new JSONObject(response));
            mTranscription.set(mResponse.get().getJSONObject("result").getJSONArray("hypotheses")
                    .getJSONObject(0).getString("transcript"));

            // remove last character (.)
            mTranscription.set(mTranscription.get().substring(0, mTranscription.get().length()-1));

            mArabicTranscription.set(QScriptToArabic.getArabic(mTranscription.get()));

            Log.d("EvaluationOld", "Setting mDiff");
            mDiff.set(dmp.diff_main(mVerseQScript.get(), mTranscription.get()).toString());

            //levScore.set(dmp.diff_levenshtein(dmp.diff_main(mVerseQScript.get(), mTranscription.get())));

            float score = new Evaluator().getScore(mVerseQScript.get(), mTranscription.get());
            levScore.set(String.format("%.2f", score));

            Log.d("EvaluationOld", "mDiff: " + mDiff.get());
            Log.d("EvaluationOld", "lev score: " + levScore.get());

            if (mTranscription.get().equals(mVerseQScript.get())) {
                evalStr.set("Correct");
                levScore.set("1");
                isCorrect.set(true);
            } else {
                //evalStr.set("Wrong");   // todo: improve

                evalStr.set(new Evaluator().getDiffType(mVerseQScript.get(), mTranscription.get()));
                isCorrect.set(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        notifyChange();
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

    public class EvaluationResult {

        int levScore;
        String strResult;

        public EvaluationResult() {}

    }
}
