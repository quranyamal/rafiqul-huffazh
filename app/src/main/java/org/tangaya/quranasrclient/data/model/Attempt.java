package org.tangaya.quranasrclient.data.model;

import android.os.Environment;

public class Attempt {

    public enum MockType {
        NONE, MOCK_RECORDING, MOCK_RESULT
    }

    public static final int SOURCE_FROM_RECORDING = 0;
    public static final int SOURCE_FROM_TEST_FILE = 1;

    private MockType mockType;

    private int mChapter;
    private int mVerse;
    private int mSource;

    private String audioFilePath;

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    public Attempt(int chapter, int verse) {
        mockType = MockType.NONE;

        mChapter = chapter;
        mVerse = verse;

        audioFilePath = audioDir + "/recording/"+mChapter+"_"+mVerse+".wav";
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

    public void setMockType(MockType mockType) {
        this.mockType = mockType;
        if (mockType == MockType.MOCK_RECORDING) {
            audioFilePath = audioDir + "/test/"+mChapter+"_"+mVerse+".wav";
        }
    }

    public MockType getMockType() {
        return mockType;
    }
}
