package org.tangaya.rafiqulhuffazh.util;

import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

public class QuranFactory {

    public static String INVALID_INDEX_MESSAGE = "INVALID!";

    private static JSONObject jsonObject;

    public static void init(AssetManager assetManager){

        DataInputStream textFileStream = null;
        try {
            textFileStream = new DataInputStream(assetManager.open(String.format("MyQuran.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(textFileStream);
        String content = scanner.useDelimiter("\\A").next();
        scanner.close();

        try {
            jsonObject = new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getSurahName(int surahNum) {
        try {
            return jsonObject.getJSONArray("surah").getJSONObject(surahNum-1).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return INVALID_INDEX_MESSAGE;
        }
    }

    public static String getAyah(int surahNum, int ayahNum) {
        try {
            return jsonObject.getJSONArray("surah").getJSONObject(surahNum-1).getJSONArray("ayah").getString(ayahNum-1);
        } catch (JSONException e) {
            e.printStackTrace();
            return INVALID_INDEX_MESSAGE;
        }
    }

    public static String getQScript(int surahNum, int ayahNum) {
        try {
            return jsonObject.getJSONArray("surah").getJSONObject(surahNum-1).getJSONArray("qscript").getString(ayahNum-1);
        } catch (JSONException e) {
            e.printStackTrace();
            return INVALID_INDEX_MESSAGE;
        }
    }

    public static int getNumAyah(int surahNum) {
        try {
            return jsonObject.getJSONArray("surah").getJSONObject(surahNum-1).getInt("num_verse");
        } catch (JSONException e) {
            e.printStackTrace();
            return -999;
        }
    }

    public static boolean isValidAyahNum(int surah, int ayah) {
        if (surah<1 || surah>114) {
            return false;
        } else {
            int numAyah = getNumAyah(surah);
            if (ayah<1 || ayah>numAyah) {
                return false;
            }
        }
        return true;
    }

}
