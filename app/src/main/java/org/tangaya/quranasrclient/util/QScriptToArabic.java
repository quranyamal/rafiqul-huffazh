package org.tangaya.quranasrclient.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class QScriptToArabic {

    private QScriptToArabic() {}

    private static Context mContext;
    private static BufferedReader brQScript, brArabic;
    private static List<String> qscrptWordList, arabicWordList;

    public static void init(Context context) {
        mContext = context;
        AssetManager assetManager = mContext.getAssets();

        try {
            brQScript = new BufferedReader(new InputStreamReader(
                    assetManager.open("qscript_words.txt")));

            brArabic = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran_arabic_words.txt")));

            qscrptWordList = Arrays.asList(brQScript.readLine().split(" "));
            arabicWordList = Arrays.asList(brArabic.readLine().split(" "));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("for debugging");
        for (String word:qscrptWordList) {
            System.out.println(word);
        }

    }

    public static String getArabic(String qscriptWord, String qscriptWordBefore) {
        if (qscrptWordList.contains(qscriptWord)) {

        }
        return "";
    }

}
