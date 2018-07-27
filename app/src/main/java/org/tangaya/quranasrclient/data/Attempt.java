package org.tangaya.quranasrclient.data;

import android.os.Environment;

public class Attempt {

    public static final int SOURCE_FROM_RECORDING = 0;
    public static final int SOURCE_FROM_TEST_FILE = 1;

    private int mChapter;
    private int mVerse;
    private int mSource;

    private String audioFilePath;

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    public Attempt(int chapter, int verse, int source) {
        mChapter = chapter;
        mVerse = verse;
        mSource = source;

        if(source==Attempt.SOURCE_FROM_RECORDING) {
            audioFilePath = audioDir + "/recording/"+mChapter+"_"+mVerse+".wav";
        } else {
            assert (source==Attempt.SOURCE_FROM_TEST_FILE);
            audioFilePath = audioDir + "/test/"+mChapter+"_"+mVerse+".wav";
        }
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public int getChapterNum() {
        return mChapter;
    }

    public int getVerseNum() {
        return mVerse;
    }

}
