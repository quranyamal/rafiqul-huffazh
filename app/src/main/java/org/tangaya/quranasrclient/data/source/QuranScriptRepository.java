package org.tangaya.quranasrclient.data.source;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuranScriptRepository {

    private QuranScriptRepository() {}

    private static Context mContext;
    private static BufferedReader brChapter, brVerse, brQScript;
    private static Chapter[] listOfChapter;

    public static void init(Context context) {
        mContext = context;
        AssetManager assetManager = mContext.getAssets();

        try {
            brChapter = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran_surahs_name.csv")));
            brVerse = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran-uthmani.txt")));
            brQScript = new BufferedReader(new InputStreamReader(
                    assetManager.open("qscript.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initData();
    }

    private static void initData() {
        System.out.println("loading data ...");

        String lineChapter, lineVerse, lineQScript;
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

                lineQScript = brQScript.readLine();

                int surahNum = Integer.parseInt(lineVersePart[0]);
                String verseStr = lineVersePart[2];

                listOfChapter[surahNum].addVerse(verseStr);
                listOfChapter[surahNum].addQScript(lineQScript);
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
        private ArrayList<String> listOfQScript;

        private Chapter() {
            listOfVerse = new ArrayList();
            listOfQScript = new ArrayList<>();
        }

        public Chapter(int id, String title) {
            mId = id;
            mTitle = title;
            listOfVerse = new ArrayList<>();
            listOfQScript = new ArrayList<>();
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

        void addQScript(String str) {
            listOfQScript.add(str);
        }

        public String getVerseScript(int i) {
            return listOfVerse.get(i-1).toString();
        }

        public String getVerseQScript(int i) {
            return listOfQScript.get(i-1).toString();
        }

        public boolean isValidVerseNum(int num) {
            return num>0 && num <=listOfVerse.size();
        }
    }

}
