package org.tangaya.rafiqulhuffazh.data.model;

public class Recording {

    private static int count = 0;

    private int mId;

    private int mChapter;

    private int mVerse;

    private String mFilename;

    public Recording(int chapter, int verse) {
        mId = Recording.count++;
        mChapter = chapter;
        mVerse = verse;
    }

    public Recording(int chapter, int verse, String filename) {
        mId = Recording.count++;
        mChapter = chapter;
        mVerse = verse;
        mFilename = filename;
    }

    public int getChapter() {
        return mChapter;
    }

    public int getVerse() {
        return mVerse;
    }

}
