package org.tangaya.quranasrclient.util;

import java.util.ArrayList;
import java.util.LinkedList;

public class Evaluator {

    diff_match_patch dmp = new diff_match_patch();
    LinkedList<diff_match_patch.Diff> diff;

    int levDif;

    public Evaluator(String reference, String recognized) {
        diff = dmp.diff_main(reference, recognized);
        levDif = dmp.diff_levenshtein(diff);
    }

    public void evaluate() {

    }
    public int getScore() {
        return 0;
    }

    public void printDiff() {
        System.out.println(diff.toString());
        System.out.println("levDiff = " + levDif);
    }

    public static void main(String[] args) {
        Evaluator eval = new Evaluator("ABC", "CBA");
        eval.printDiff();

        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main("ABCED", "ABCDE");
        System.out.println(diffs);
        System.out.println(dmp.diff_levenshtein(diffs));
    }

}
