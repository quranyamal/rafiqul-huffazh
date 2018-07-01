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
    private static BufferedReader br;
    private static Chapter[] listOfChapter;

    public static void init(Context context) {
        mContext = context;
        AssetManager assetManager = mContext.getAssets();

        try {
            br = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran-uthmani.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initData();
    }

    private static void initData() {
        System.out.println("loading data ...");

        String line;
        String[] linePart;

        listOfChapter = new Chapter[145];
        for (int i=1; i<=144; i++) {
            listOfChapter[i] = new Chapter();
        }

        try {
            for (int i=0; i<6236; i++) {
                line=br.readLine();
                linePart = line.split("\\|");

                int surahNum = Integer.parseInt(linePart[0]);
                String verseStr = linePart[2];

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

        public Chapter() {
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
