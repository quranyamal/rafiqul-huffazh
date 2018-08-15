package org.tangaya.rafiqulhuffazh.data.repository;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EvaluationRepository {

    private static ArrayList<EvaluationOld> evals;

    private static MutableLiveData<ArrayList<EvaluationOld>> evalsLiveData;

    static {
        evalsLiveData = new MutableLiveData<>();

        evals = new ArrayList<>();
        evalsLiveData.setValue(evals);
    }

    public static MutableLiveData<ArrayList<EvaluationOld>> getEvalsLiveData() {
        return evalsLiveData;
    }

    public static ArrayList<EvaluationOld> getEvals() {
        return evals;
    }

    public static void addToEvalSet(EvaluationOld eval) {
        evals.add(eval);
        sortEvals();
        evalsLiveData.postValue(evals);
    }

    public static void clearEvalData() {
        evalsLiveData.getValue().clear();
    }

    public static void sortEvals() {
        Collections.sort(evals, new Comparator<EvaluationOld>() {
            @Override
            public int compare(EvaluationOld eval1, EvaluationOld eval2) {
                return Integer.compare(eval1.ayah, eval2.ayah);
            }
        });
    }
}
