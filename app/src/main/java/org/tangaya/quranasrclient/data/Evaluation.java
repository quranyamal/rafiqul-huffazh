package org.tangaya.quranasrclient.data;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.util.diff_match_patch;

import java.util.LinkedList;

public class Evaluation extends BaseObservable {

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

    //String mVerseScript;
    private ObservableField<String> mVerseScript = new ObservableField<>();;

    //String mVerseQScript;
    private ObservableField<String> mVerseQScript = new ObservableField<>();

    //String mFilepath;
    private ObservableField<String> mFilepath = new ObservableField<>();

    //String verseNum;
    private ObservableField<String> verseNum = new ObservableField<>();

    //LinkedList<diff_match_patch.Diff> mDiff;
    private ObservableField<LinkedList<diff_match_patch.Diff>> mDiff = new ObservableField<>();

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

    public Evaluation(int chapter, int verse, int sessionId) {
        mChapter.set(chapter);
        mVerse.set(verse);
        mSessionId.set(sessionId);

        verseNum.set("Verse " + verse);

        mVerseScript.set(QuranScriptRepository.getChapter(mChapter.get()).getVerseScript(mVerse.get()));
        mVerseQScript.set(QuranScriptRepository.getChapter(mChapter.get()).getVerseQScript(mVerse.get()));

        isCorrect.set(false);

        mStatus.set(Evaluation.STATE_UNPROCESSED);

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
        try {
            mResponse.set(new JSONObject(response));
            mTranscription.set(mResponse.get().getJSONObject("result").getJSONArray("hypotheses")
                    .getJSONObject(0).getString("transcript"));

            // remove last character (.)
            mTranscription.set(mTranscription.get().substring(0, mTranscription.get().length()-1));

            mDiff.set(dmp.diff_main(mVerseQScript.get(), mTranscription.get()));

            if (mTranscription.get().equals(mVerseQScript.get())) {
                evalStr.set("Correct");
                isCorrect.set(true);
            } else {
                evalStr.set("Wrong");   // todo: improve
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

    public ObservableField<String> getVerseScript() {
        return mVerseScript;
    }

    public ObservableField<String> getVerseQScript() {
        return mVerseQScript;
    }

    public ObservableField<LinkedList<diff_match_patch.Diff>> getDiffStr() {
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
}
