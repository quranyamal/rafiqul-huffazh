package org.tangaya.quranasrclient.data;

import android.os.Environment;

public class Attempt {

    public static int STATE_UNPROCESSED = 0;
    public static int STATE_PROCESSING = 1;
    public static int STATE_ABORTED = 2;
    public static int STATE_FINISHED = 3;

    String extStorageDir = Environment.getExternalStorageDirectory()+"";
    String quranVerseAudioDir = extStorageDir + "/quran-verse-audio";

    private int mChapter, mVerse, mSessionId;

    // 0: unprocessed, 1: processing, 2: aborted, 3: finished (recognized)
    private int mStatus;

    public Attempt(int chapter, int verse, int sessionId) {
        mChapter = chapter;
        mVerse = verse;
        mSessionId = sessionId;

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

        //return quranVerseAudioDir + "/100-1.wav";
    }

}
