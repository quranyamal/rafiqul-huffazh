package org.tangaya.rafiqulhuffazh.data.model;

import android.os.Environment;

public class Attempt {

    public enum MockType {
        NONE, MOCK_RECORDING, MOCK_RESULT
    }

    public static final int SOURCE_FROM_RECORDING = 0;
    public static final int SOURCE_FROM_TEST_FILE = 1;

    private MockType mockType;

    private int mSurah;
    private int mAyah;
    private int mSource;

    private String audioFilePath;

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    public Attempt(int surah, int ayah) {
        mockType = MockType.NONE;

        mSurah = surah;
        mAyah = ayah;

        audioFilePath = audioDir + "/recording/"+ mSurah +"_"+ mAyah +".wav";
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public int getSurahNum() {
        return mSurah;
    }

    public int getAyahNum() {
        return mAyah;
    }

    public void setMockType(MockType mockType) {
        this.mockType = mockType;
        if (mockType == MockType.MOCK_RECORDING) {
            audioFilePath = audioDir + "/test/"+ mSurah +"_"+ mAyah +".wav";
        }
    }

    public MockType getMockType() {
        return mockType;
    }
}
