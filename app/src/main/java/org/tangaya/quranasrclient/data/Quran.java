package org.tangaya.quranasrclient.data;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Quran {

    private Surah[] listOfSurah;
    private Context mContext;
    String file_path;
    BufferedReader br;

    public Quran(Context context) {
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

    /* for local testing
     */
    public Quran() {
        String root_path = new File("").getAbsolutePath();
        file_path = root_path + "/app/src/main/assets/";

        FileReader f = null;
        try {
            f = new FileReader(file_path+"quran-uthmani.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(f);

        initData();
    }

    public Surah getSurah(int i) {
        return listOfSurah[i];
    }

    private void initData() {
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

    public static void main(String[] args) {
        Quran myQuran = new Quran();

        String ayat = myQuran.getSurah(1).getAyah(1);
        System.out.println(ayat);
    }
}
