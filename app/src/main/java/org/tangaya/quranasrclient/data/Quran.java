package org.tangaya.quranasrclient.data;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Quran {

    private static Context mContext;
    private static BufferedReader br;
    private static Surah[] listOfSurah;

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

        listOfSurah = new Surah[145];
        for (int i=1; i<=144; i++) {
            listOfSurah[i] = new Surah();
        }

        try {
            for (int i=0; i<6236; i++) {
                line=br.readLine();
                linePart = line.split("\\|");

                int surahNum = Integer.parseInt(linePart[0]);
                String ayahText = linePart[2];

                listOfSurah[surahNum].addAyah(ayahText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("data loaded");
    }

    public static Surah getSurah(int i) {
        return listOfSurah[i];
    }
}
