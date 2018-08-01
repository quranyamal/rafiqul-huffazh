package org.tangaya.quranasrclient.util;

import android.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import timber.log.Timber;

public class Evaluator extends diff_match_patch {

    int levDif;

    public Evaluator() {}

    boolean isExistDiffWithText(LinkedList<Diff> diffs, String text) {
        for (Diff diff : diffs) {
            if (diff.text.equals(text)) {
                return true;
            }
        }

        return false;
    }

    public String getDiffType(String reference, String recognized) {
        assert !reference.equals(recognized);   // should never equal

        LinkedList<Diff> diffs = diff_main(reference, recognized);

        System.out.println("getDiffType. diffs = " + diffs);

        Set<Operation> oprSet = new HashSet<>();

        for (Diff diff : diffs) {
            oprSet.add(diff.operation);
        }

        oprSet.remove(Operation.EQUAL);

        if (oprSet.size()>1) {
            return "missing and addition";
        } else if (oprSet.contains(Operation.INSERT)) {
            return "addition of element";
        } else {
            return "missing element";
        }
    }

    public float getScore(String reference, String recognized){
        LinkedList<Diff> diff = diff_main(reference, recognized);
        float score = 1- ((float) diff_levenshtein(diff)/reference.length());
        Timber.d("score:" + score);
        return score;
    }

    public static void main(String[] args) {
        Evaluator eval = new Evaluator();

        System.out.println(eval.getDiffType("ABCD EFGH IJKL MNO", "IJKL EFGH ABCD"));
    }

}
