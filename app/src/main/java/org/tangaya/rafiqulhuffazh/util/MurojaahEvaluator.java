package org.tangaya.rafiqulhuffazh.util;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import timber.log.Timber;

public class MurojaahEvaluator {

    private static MurojaahEvaluator INSTANCE = null;

    MutableLiveData<Evaluation> evalResult = new MutableLiveData<>();

    public MutableLiveData<Evaluation> getEvalResult() {
        return evalResult;
    }

    public static String CORRECT_MESSAGE = "benar";
    public static String INCORRECT_MESSAGE_INSERTION_PART = "penambahan elemen";
    public static String INCORRECT_MESSAGE_MISSING_PART = "elemen hilang";
    public static String INCORRECT_MESSAGE_INSERTION_AND_MISSING_PART = "penambahan dan elemen hilang";
    public static String INCORRECT_MESSAGE_SKIPPING_ONE_AYAH = "melewatkan satu ayat";
    public static String INCORRECT_MESSAGE_SKIPPING_SOME_AYAHS = "melewatkan beberapa ayat ";
    public static String INCORRECT_MESSAGE_RETURNING_TO_PREV_AYAH = "mundur ke ayat sebelumnya";

    private static diff_match_patch dmp = new diff_match_patch();

    private MurojaahEvaluator() {}

    public static MurojaahEvaluator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MurojaahEvaluator();
        }
        return INSTANCE;
    }

    public Evaluation evaluate(QuranAyahAudio audio) {
        Evaluation eval = new Evaluation(audio);

        String evalDesc = getEvalDescription(audio.getSurah(), audio.getAyah(), audio.getTranscription());
        eval.setEvalDescription(evalDesc);

        int levenshteinValue = getLevenshteinDistance(audio.getQScript(), audio.getTranscription());
        int maxPts = audio.getQScript().length();

        eval.setMaxPoints(maxPts);
        eval.setEarnedPoints(maxPts - levenshteinValue);
        evalResult.setValue(eval);

        // debugging stuff
        eval.diffs.set(dmp.diff_main(audio.getQScript(), audio.getTranscription()));

        return eval;
    }

    public String getEvalDescription(int surah, int ayah, String recognized) {

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

    public int getLevenshteinDistance(String reference, String recognized) {
        LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(reference, recognized);
        return dmp.diff_levenshtein(diff);
    }

    public float getScore(String reference, String recognized){
        LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(reference, recognized);
        float score = 1- ((float) dmp.diff_levenshtein(diff)/reference.length());
        Timber.d("score:" + score);
        return score;
    }
}
