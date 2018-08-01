package org.tangaya.quranasrclient.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class QScriptToArabic {

    private QScriptToArabic() {}

    private static BufferedReader brQScript, brArabic;
    private static List<String> qscrptWordList, arabicWordList;

    private static Context mContext;

    public static int init(Context context) {
        mContext = context;
        AssetManager assetManager = mContext.getAssets();
        try {
            brArabic = new BufferedReader(new InputStreamReader(
                    assetManager.open("quran_arabic_words.txt")));
            brQScript = new BufferedReader(new InputStreamReader(
                    assetManager.open("qscript_words.txt")));

            arabicWordList = Arrays.asList(brArabic.readLine().split(" "));
            qscrptWordList = Arrays.asList(brQScript.readLine().split(" "));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("for debugging");
//        for (String word:qscrptWordList) {
//            System.out.println(word);
//        }

        System.out.println("done init QScriptToArabic");

        return 0;
    }

    public static String getArabicElmt(String qscriptWord, String qscriptWordBefore) {
        System.out.println("running getArabicElmt("+qscriptWord+","+qscriptWordBefore+")");

        ArrayList<Integer> idxList = new ArrayList<>();

        for (int i=0; i<qscrptWordList.size(); i++) {
            if (qscrptWordList.get(i).equals(qscriptWord)) {
                idxList.add(i);
            }
        }

        if (idxList.size()==0) {
            return "--";
        } else if (idxList.size()==1) {
            return arabicWordList.get(idxList.get(0));
        } else {
            if (qscriptWordBefore.equals("")) {
                for (int idx : idxList) {
                    if (arabicWordList.get(idx).substring(0,4).equals("ال"))
                        System.out.println("arabicWordList.get(idx) = " + arabicWordList.get(idx));
                    return arabicWordList.get(idx);
                }
            } else if (qscriptWordBefore.charAt(qscriptWordBefore.length()-1)=='l') { //qamariyah
                for (int idx : idxList)
                    if (arabicWordList.get(idx).substring(0,3).equals("ال")) {
                        return arabicWordList.get(idx);
                    }
            } else {
                if (qscriptWordBefore.substring(0,qscriptWordBefore.length()-2).equals(qscriptWord.charAt(0))) {
                    //syamsiyah
                    for (int idx : idxList) {
                        if (arabicWordList.get(idx).substring(0,4).equals("ال"))
                            Timber.d("8");
                            return arabicWordList.get(idx);
                    }
                } else {
                    for (int idx : idxList) {
                        if (arabicWordList.get(idx).substring(0,4).equals("ال"))
                            return arabicWordList.get(idx);
                    }
                }

            }
        }

        System.out.println("end of getArabicElmt()");

        return "";
    }

    public static String getArabic(String qscript) {
        System.out.println("running getArabic");
        String[] qscripts = qscript.split(" ");

        String arabic = getArabicElmt(qscripts[0], "") + " ";

//        System.out.println(qscripts[0]);
//        System.out.println(getArabicElmt(qscripts[0], ""));

        for (int i=1; i<qscripts.length; i++) {
//            System.out.println(qscripts[i] + "|" + qscripts[i-1]);
//            System.out.println(getArabicElmt(qscripts[i], qscripts[i-1]));

            arabic = arabic + getArabicElmt(qscripts[i], qscripts[i-1]) + " ";
        }

        //System.out.println("final arabic = " + arabic);

        return arabic;
    }

    public static void main(String[] args) {

//        Path currentRelativePath = Paths.get("");
//        String s = currentRelativePath.toAbsolutePath().toString();
//        System.out.println("Current relative path is: " + s);
//
//        init();

        System.out.println(getArabic("bismil lAhir roHmAnir roHIm"));
        System.out.println(getArabic("ealHamdu lillAhi robbil EAlamIn"));
        System.out.println(getArabic("earroHmAnir roHIm"));
        System.out.println(getArabic("mAliki yWmid dIn"));
        System.out.println(getArabic("eiyyAka naEbudu waeiyyAka nastaEIn"));
        System.out.println(getArabic("eihdinAS SirOpol mustaqIm"));
        System.out.println(getArabic("SirOpol lacIna eanEamta EalYhim gVril magMUbi EalYhim walAM MOOOllIn "));

        System.out.println();

        System.out.println(getArabic("qul eaEUcu birobbin nAs"));
        System.out.println(getArabic("malikin nAs"));
        System.out.println(getArabic("eilAhin nAs"));
        System.out.println(getArabic("miN Xarril waswAsil xonnAs"));
        System.out.println(getArabic("eallacI yuwaswisu fI SudUrin nAs"));
        System.out.println(getArabic("minal jinnati wannAs"));
    }

}

