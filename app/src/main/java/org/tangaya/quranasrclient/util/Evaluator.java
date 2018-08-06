package org.tangaya.quranasrclient.util;

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

    public String getDiffType(int chapter, int verse, String recognized) {
//        assert !reference.equals(recognized);   // should never equal

        String reference = QuranScriptFactory.getChapter(chapter).getVerseQScript(verse);

        // todo: case of skipping n verse
//        int numVerse = QuranScriptFactory.getChapter(chapter).getNumVerse();
//        for (int i=0; i<numVerse; i++) {
//            String nextIQScript = QuranScriptFactory.getChapter(chapter).getVerseQScript(verse);
//            if (reference.equals(nextIQScript));
//            return "skipping " + i + "verse" + (i>1? "s":"");
//        }

        System.out.println("diff_main("+reference+","+recognized+")");
        LinkedList<Diff> diffs = diff_main(reference, recognized);

        System.out.println("getDiffType. diffs = " + diffs);

        Set<Operation> oprSet = new HashSet<>();

        for (Diff diff : diffs) {
            oprSet.add(diff.operation);
        }

        oprSet.remove(Operation.EQUAL);

        if (oprSet.size()>1) {
            return "insertion + deletion";
        } else if (oprSet.contains(Operation.INSERT)) {
            return "insertion";
        } else {
            return "deletion";
        }
    }

    public int getLevenshtein(String reference, String recognized) {
        LinkedList<Diff> diff = diff_main(reference, recognized);
        return diff_levenshtein(diff);
    }

    public float getScore(String reference, String recognized){
        LinkedList<Diff> diff = diff_main(reference, recognized);
        float score = 1- ((float) diff_levenshtein(diff)/reference.length());
        Timber.d("score:" + score);
        return score;
    }

    public static void main(String[] args) {
        Evaluator eval = new Evaluator();

        System.out.println(eval.getDiffType(1,1, "mAliki yWmid dIn"));
    }

}
