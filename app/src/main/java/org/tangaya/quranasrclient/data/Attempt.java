package org.tangaya.quranasrclient.data;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

import java.util.ArrayList;

public class Attempt {

    public static int STATE_UNPROCESSED = 0;
    public static int STATE_PROCESSING = 1;
    public static int STATE_ABORTED = 2;
    public static int STATE_FINISHED = 3;

    String extStorageDir = Environment.getExternalStorageDirectory()+"";
    String quranVerseAudioDir = extStorageDir + "/quran-verse-audio";

    // only store response with final transcription status
    JSONObject mResponse;

    String mTranscription;

    String mVerseScript;

    private int mChapter, mVerse, mSessionId;

    // 0: unprocessed, 1: processing, 2: aborted, 3: finished (recognized)
    private int mStatus;

    public Attempt(int chapter, int verse, int sessionId) {
        mChapter = chapter;
        mVerse = verse;
        mSessionId = sessionId;

        if (chapter==999) {
            mVerseScript = "rekaman";
        } else {
            mVerseScript = QuranScriptRepository.getChapter(mChapter).getVerse(mVerse);
        }

        mStatus = Attempt.STATE_UNPROCESSED;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getAudioFilepath() {
        return quranVerseAudioDir + "/" + mChapter + "-" + mVerse + ".wav";
    }

    public void setResponse(String response) {
        try {
            mResponse = new JSONObject(response);
            mTranscription = mResponse.getJSONObject("result").getJSONArray("hypotheses")
                    .getJSONObject(0).getString("transcript");
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
}
