package org.tangaya.rafiqulhuffazh.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import timber.log.Timber;

public class MurojaahEvaluator {

    public static String CORRECT_MESSAGE = "benar";
    public static String INCORRECT_MESSAGE_INSERTION_PART = "penambahan elemen";
    public static String INCORRECT_MESSAGE_MISSING_PART = "elemen hilang";
    public static String INCORRECT_MESSAGE_INSERTION_AND_MISSING_PART = "penambahan dan elemen hilang";
    public static String INCORRECT_MESSAGE_SKIPPING_ONE_AYAH = "melewatkan satu ayat";
    public static String INCORRECT_MESSAGE_SKIPPING_SOME_AYAHS = "melewatkan beberapa ayat ";
    public static String INCORRECT_MESSAGE_RETURNING_TO_PREV_AYAH = "mundur ke ayat sebelumnya";

    private static diff_match_patch dmp = new diff_match_patch();

    private MurojaahEvaluator() {}

    public static String evaluate(int surah, int ayah, String recognized) {

        String reference = QuranUtil.getQScript(surah, ayah);

        if (recognized.equals(reference)) {
            return CORRECT_MESSAGE;
        } else if (QuranUtil.isValidAyahNum(surah, ayah+1)) {
            if (recognized.equals(QuranUtil.getQScript(surah, ayah+1))) {
                return INCORRECT_MESSAGE_SKIPPING_ONE_AYAH;
            } else {
                int ayahNum = ayah+2;
                while (QuranUtil.isValidAyahNum(surah, ayahNum)) {
                    if (recognized.equals(QuranUtil.getQScript(surah, ayahNum))) {
                        return INCORRECT_MESSAGE_SKIPPING_SOME_AYAHS;
                    }
                    ayahNum++;
                }
            }
        }

        for (int i=1; i<ayah; i++) {
            if (recognized.equals(QuranUtil.getQScript(surah, i))) {
                return INCORRECT_MESSAGE_RETURNING_TO_PREV_AYAH;
            }
        }

        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(reference, recognized);
        Set<diff_match_patch.Operation> oprSet = new HashSet<>();

        for (diff_match_patch.Diff diff : diffs) {
            oprSet.add(diff.operation);
        }

        oprSet.remove(diff_match_patch.Operation.EQUAL);

        if (oprSet.size()>1) {
            return INCORRECT_MESSAGE_INSERTION_AND_MISSING_PART;
        } else {
            if (oprSet.contains(diff_match_patch.Operation.INSERT)) {
                return INCORRECT_MESSAGE_INSERTION_PART;
            } else {
                return INCORRECT_MESSAGE_MISSING_PART;
            }
        }
    }

    public static int getLevenshteinDistance(String reference, String recognized) {
        LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(reference, recognized);
        return dmp.diff_levenshtein(diff);
    }

    public static float getScore(String reference, String recognized){
        LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(reference, recognized);
        float score = 1- ((float) dmp.diff_levenshtein(diff)/reference.length());
        Timber.d("score:" + score);
        return score;
    }
}