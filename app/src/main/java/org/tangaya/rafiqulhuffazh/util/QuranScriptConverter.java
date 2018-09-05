package org.tangaya.rafiqulhuffazh.util;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class QuranScriptConverter {

    private QuranScriptConverter() {}

    private static BufferedReader brMap;
    private static List<String> qscrptWordList, arabicWordList;

    public static int init(AssetManager assetManager) {

        qscrptWordList = new ArrayList<>();
        arabicWordList = new ArrayList<>();

        try {
            brMap = new BufferedReader(new InputStreamReader(
                    assetManager.open("qscript_to_arabic_map.txt")));

            String line;

            while((line = brMap.readLine()) != null) {
                String str[] = line.split(" ");

                qscrptWordList.add(str[0]);
                arabicWordList.add(str[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("for debugging");
//        for (String word:qscrptWordList) {
//            System.out.println(word);
//        }
//
//        System.out.println("for debugging");
//        for (String word:arabicWordList) {
//            System.out.println(word);
//        }

        System.out.println("words count = " + qscrptWordList.size());

        for (int i=0; i<qscrptWordList.size(); i++) {
            System.out.println(i + ". " + qscrptWordList.get(i) + " =>|" + arabicWordList.get(i) + "|");
        }

        System.out.println("QuranScriptConverter initialized");

        return 0;
    }

    public static String getArabicElmt(String qscriptWord, String qscriptWordBefore) {
        System.out.println("running getArabicElmt("+qscriptWord+","+qscriptWordBefore+")");

        if (qscriptWord.equals("") && qscriptWordBefore.equals("")) {
            return "";
        }

        ArrayList<Integer> idxList = new ArrayList<>();

        for (int i=0; i<qscrptWordList.size(); i++) {
            if (qscrptWordList.get(i).equals(qscriptWord)) {
                idxList.add(i);
            }
        }

        if (idxList.size()==0) {
            Timber.d("getArabicElmt. return 1");
            return "--";
        } else if (idxList.size()==1) {
            Timber.d("getArabicElmt. return 2");
            return arabicWordList.get(idxList.get(0));
        } else {
            if (qscriptWordBefore.equals("")) {
                for (int idx : idxList) {
                    String strToTest = arabicWordList.get(idx).substring(0,2);
                    System.out.println("strToTest 3: " + strToTest);
                    if (strToTest.equals("ال")) {
                        System.out.println("arabicWordList.get(idx) = " + arabicWordList.get(idx));
                    }
                    Timber.d("getArabicElmt. return 3");
                    return arabicWordList.get(idx);
                }
            } else if (qscriptWordBefore.charAt(qscriptWordBefore.length()-1)=='l') { //qamariyah
                for (int idx : idxList) {
                    String strToTest = arabicWordList.get(idx).substring(0,2);
                    System.out.println("strToTest 4: " + strToTest);
                    if (strToTest.equals("ال")) {
                        Timber.d("getArabicElmt. return 4");
                        return arabicWordList.get(idx);
                    }
                }
            } else {
                if (qscriptWordBefore.substring(0,qscriptWordBefore.length()-2).equals(qscriptWord.charAt(0))) {
                    //syamsiyah
                    for (int idx : idxList) {
                        String strToTest = arabicWordList.get(idx).substring(0,2);
                        System.out.println("strToTest 5: " + strToTest);
                        if (strToTest.equals("ال")) {
                        }
                        Timber.d("getArabicElmt. return 5");
                        return arabicWordList.get(idx);
                    }
                } else {
                    for (int idx : idxList) {
                        String strToTest = arabicWordList.get(idx).substring(0,2);
                        System.out.println("strToTest 6: " + strToTest);
                        if (strToTest.equals("ال")) {
                            Timber.d("getArabicElmt. return 6");
                            return arabicWordList.get(idx);
                        }
                    }
                }

            }
        }

        System.out.println("end of getArabicElmt()");
        Timber.d("getArabicElmt. return 7");
        return "";
    }

    public static String toArabic(String qscript) {
        System.out.println("running toArabic");
        String[] qscripts = qscript.split(" ");

        String arabic = getArabicElmt(qscripts[0], "") + " ";
        System.out.println("first getArabicElmt returns: " + arabic);

//        System.out.println(qscripts[0]);
//        System.out.println(getArabicElmt(qscripts[0], ""));

        for (int i=1; i<qscripts.length; i++) {
//            System.out.println(qscripts[i] + "|" + qscripts[i-1]);
//            System.out.println(getArabicElmt(qscripts[i], qscripts[i-1]));

            String ret = getArabicElmt(qscripts[i], qscripts[i-1]);
            System.out.println("ret: " +ret);

            arabic = arabic + ret + " ";
        }

        //System.out.println("final arabic = " + arabic);

        return arabic.trim();
    }
}

