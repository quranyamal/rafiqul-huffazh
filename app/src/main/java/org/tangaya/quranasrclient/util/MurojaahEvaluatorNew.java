package org.tangaya.quranasrclient.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class MurojaahEvaluatorNew {

    public static String CORRECT_MESSAGE = "benar";
    public static String INCORRECT_MESSAGE_INSERTION_PART = "penambahan elemen";
    public static String INCORRECT_MESSAGE_MISSING_PART = "elemen hilang";
    public static String INCORRECT_MESSAGE_INSERTION_AND_MISSING_PART = "penambahan dan elemen hilang";
    public static String INCORRECT_MESSAGE_SKIPPING_ONE_VERSE = "melewatkan satu ayat";
    public static String INCORRECT_MESSAGE_SKIPPING_SOME_VERSES = "melewatkan beberapa ayat ";
    public static String INCORRECT_MESSAGE_RETURNING_TO_PREV_VERSE = "mundur ke ayat sebelumnya";

    private static diff_match_patch dmp = new diff_match_patch();

    private MurojaahEvaluatorNew() {}

    public static String evaluate(int chapter, int verse, String recognized) {

        String reference = QuranScriptFactory.getChapter(chapter).getVerseQScript(verse);

        if (recognized.equals(reference)) {
            return CORRECT_MESSAGE;
        } else if (QuranScriptFactory.isValidVerseIndex(chapter, verse+1)) {
            if (recognized.equals(QuranScriptFactory.getVerseQScript(chapter, verse+1))) {
                return INCORRECT_MESSAGE_SKIPPING_ONE_VERSE;
            } else {
                int verseNum = verse+2;
                while (QuranScriptFactory.isValidVerseIndex(chapter, verseNum)) {
                    if (recognized.equals(QuranScriptFactory.getVerseQScript(chapter, verseNum))) {
                        return INCORRECT_MESSAGE_SKIPPING_SOME_VERSES;
                    }
                    verseNum++;
                }
            }
        }

        for (int i=1; i<verse; i++) {
            if (recognized.equals(QuranScriptFactory.getVerseQScript(chapter, i))) {
                return INCORRECT_MESSAGE_RETURNING_TO_PREV_VERSE;
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

}
