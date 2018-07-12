package org.tangaya.quranasrclient.data;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.util.diff_match_patch;

import java.util.LinkedList;

public class Attempt {

    public static int STATE_UNPROCESSED = 0;
    public static int STATE_PROCESSING = 1;
    public static int STATE_ABORTED = 2;
    public static int STATE_FINISHED = 3;

    String extStorageDir = Environment.getExternalStorageDirectory()+"";
    String quranVerseAudioDir = extStorageDir + "/rafiqul-huffazh";

    // only store response with final transcription status
    JSONObject mResponse;

    String mTranscription;

    String mVerseScript;

    String mVerseQScript;

    String mFilepath;

    String verseNum;

    LinkedList<diff_match_patch.Diff> mDiff;

    private int mChapter, mVerse, mSessionId;

    // 0: unprocessed, 1: processing, 2: aborted, 3: finished (recognized)
    private int mStatus;

    // todo: refactor to mvvm
    private diff_match_patch dmp = new diff_match_patch();

    public Attempt(int chapter, int verse, int sessionId) {
        mChapter = chapter;
        mVerse = verse;
        mSessionId = sessionId;

        verseNum = "Verse " + verse;

        if (chapter==999) {
            mVerseScript = "rekaman";
        } else {
            mVerseScript = QuranScriptRepository.getChapter(mChapter).getVerseScript(mVerse);
            mVerseQScript = QuranScriptRepository.getChapter(mChapter).getVerseQScript(mVerse);
        }

        mStatus = Attempt.STATE_UNPROCESSED;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public void setFilepath(String filepath) {
        mFilepath = filepath;
    }

    public String getAudioFilepath() {
        //return quranVerseAudioDir + "/" + mChapter + "-" + mVerse + ".wav";

        return mFilepath;
    }

    public void setResponse(String response) {
        try {
            mResponse = new JSONObject(response);
            mTranscription = mResponse.getJSONObject("result").getJSONArray("hypotheses")
                    .getJSONObject(0).getString("transcript");

            // remove last character (.)
            mTranscription = mTranscription.substring(0, mTranscription.length()-1);

            mDiff = dmp.diff_main(mVerseQScript, mTranscription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTranscription() {
        return mTranscription;
    }

    public String getVerseScript() {
        return mVerseScript;
    }

    public String getVerseQScript() {
        return mVerseQScript;
    }

    public String getDiffStr() {
        return mDiff.toString();
    }

    public String getVerseNum() {
        return verseNum;
    }

    public boolean isEqual() {
        return mTranscription.equals(mVerseQScript);
    }
}
