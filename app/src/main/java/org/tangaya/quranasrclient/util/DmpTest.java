package org.tangaya.quranasrclient.util;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;

public class DmpTest {

    // todo: cari tau cara diff by word

    private static LinkedList<diff_match_patch.Diff> diff_lineMode(String text1, String text2) {
        diff_match_patch dmp = new diff_match_patch();
        diff_match_patch.LinesToCharsResult a = dmp.diff_linesToChars(text1, text2);
        String lineText1 = a.chars1;
        String lineText2 = a.chars2;
        List<String> lineArray = a.lineArray;
        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(lineText1, lineText2, false);
        dmp.diff_charsToLines(diffs, lineArray);

        return diffs;
    }

    public static void main(String[] args) {
        diff_match_patch dmp = new diff_match_patch();

        String diff = dmp.diff_main("AABBC", "ABBCDE").toString();
        System.out.println("diff: " + diff);

        System.out.println(diff_lineMode("AABC ABCD DBS", "AABC ABCD DBSCC").toString());
    }

}
