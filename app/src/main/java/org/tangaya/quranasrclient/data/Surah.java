package org.tangaya.quranasrclient.data;

import java.util.ArrayList;

public class Surah {
    private int mId;
    private String mTitle;

    private ArrayList<String> listOfAyah;

    public Surah() {
        listOfAyah = new ArrayList();
    }

    public Surah(int id, String title) {
        mId = id;
        mTitle = title;
        listOfAyah = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    void addAyah(String str) {
        listOfAyah.add(str);
    }

    public String getAyah(int i) {
        return listOfAyah.get(i-1).toString();
    }

    public boolean isValidAyahNum(int num) {
        return num>0 && num <=listOfAyah.size();
    }
}
