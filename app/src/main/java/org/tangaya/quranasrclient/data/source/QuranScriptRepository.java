package org.tangaya.quranasrclient.data.source;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuranScriptRepository {

    private QuranScriptRepository() {}

    private static Context mContext;
    private static BufferedReader brChapter, brVerse;
    private static Chapter[] listOfChapter;

    public static void init(Context context) {
        mContext = context;
        AssetManager assetManager = mContext.getAssets();

        try {
            brChapter = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran_surahs_name.csv")));
            brVerse = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran-uthmani.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initData();
    }

    private static void initData() {
        System.out.println("loading data ...");

        String lineChapter, lineVerse;
        String[] lineChapterPart, lineVersePart;

        listOfChapter = new Chapter[115];
        for (int i=1; i<=114; i++) {
            listOfChapter[i] = new Chapter();
        }

        try {
            lineChapter = brChapter.readLine();

            for (int i=1; i<=114; i++) {
                lineChapter = brChapter.readLine();
                lineChapterPart = lineChapter.split(",");
                listOfChapter[i].mTitle = lineChapterPart[2];
            }

            for (int i=0; i<6236; i++) {
                lineVerse = brVerse.readLine();
                lineVersePart = lineVerse.split("\\|");

                int surahNum = Integer.parseInt(lineVersePart[0]);
                String verseStr = lineVersePart[2];

                listOfChapter[surahNum].addVerse(verseStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("data loaded");
    }

    public static Chapter getChapter(int i) {
        return listOfChapter[i];
    }

    public static class Chapter {
        private int mId;
        private String mTitle;

        private ArrayList<String> listOfVerse;

        private Chapter() {
            listOfVerse = new ArrayList();
        }

        public Chapter(int id, String title) {
            mId = id;
            mTitle = title;
            listOfVerse = new ArrayList<>();
        }

        public int getId() {
            return mId;
        }

        public String getTitle() {
            return mTitle;
        }

        void addVerse(String str) {
            listOfVerse.add(str);
        }

        public String getVerse(int i) {
            return listOfVerse.get(i-1).toString();
        }

        public boolean isValidVerseNum(int num) {
            return num>0 && num <=listOfVerse.size();
        }
    }

}
