package org.tangaya.quranasrclient.util;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuranScriptFactory {

    private QuranScriptFactory() {}

    private static BufferedReader brChapter, brVerse, brQScript;
    private static Chapter[] listOfChapter;

    public static String INVALID_INDEX_MESSAGE = "[not exist. invalid index!]";

    public static void init(AssetManager assetManager) {

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
        loadData();
    }

    private static void loadData() {
        System.out.println("loading data ...");

        String lineChapter, lineVerse, lineQScript;
        String[] lineChapterPart, lineVersePart;

        listOfChapter = new Chapter[115];
        for (int i=1; i<=114; i++) {
            listOfChapter[i] = new Chapter();
        }

        try {
            lineChapter = brChapter.readLine(); // skipping first line

            for (int i=1; i<=114; i++) {
                lineChapter = brChapter.readLine();
                lineChapterPart = lineChapter.split(",");
                listOfChapter[i].mTitle = lineChapterPart[2];
            }

            // parse Al-Fathihah verse 1 (without trimming basmalah
            lineVerse = brVerse.readLine();
            lineVersePart = lineVerse.split("\\|");
            lineQScript = brQScript.readLine();

            int surahNum = Integer.parseInt(lineVersePart[0]);
            String verseStr = lineVersePart[2];

            listOfChapter[surahNum].addVerse(verseStr);
            listOfChapter[surahNum].addQScript(lineQScript);

            for (int i=1; i<6236; i++) {
                lineVerse = brVerse.readLine();
                lineVersePart = lineVerse.split("\\|");

                lineQScript = brQScript.readLine();

                surahNum = Integer.parseInt(lineVersePart[0]);
                verseStr = lineVersePart[2];

                if (listOfChapter[surahNum].listOfVerse.size()==0) {
                    // first verse of every chapter
                    verseStr = verseStr.substring(37, verseStr.length());
                }

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

    public static String getArabicVerseScript(int chapter, int verse) {
        return getChapter(chapter).getVerseArabicScript(verse);
    }

    public static String getVerseQScript(int chapter, int verse) {
        if (chapter<1 || chapter>114) {
            return INVALID_INDEX_MESSAGE;
        }
        return getChapter(chapter).getVerseQScript(verse);
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

        public String getVerseArabicScript(int i) {
            if (!isValidVerseNum(i)) {
                return INVALID_INDEX_MESSAGE;
            }
            return listOfVerse.get(i-1).toString();
        }

        public String getVerseQScript(int i) {
            if (!isValidVerseNum(i)) {
                return INVALID_INDEX_MESSAGE;
            }
            return listOfQScript.get(i-1).toString();
        }

        public boolean isValidVerseNum(int num) {
            return num>0 && num <=listOfVerse.size();
        }

        public int getNumVerse() {
            return listOfVerse.size();
        }
    }

}
