package org.tangaya.quranasrclient.data.repository;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.quranasrclient.data.model.EvaluationOld;

import java.util.ArrayList;

import timber.log.Timber;

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
        Timber.d("addToEvalSet 1");
        evals.add(eval);
        Timber.d("addToEvalSet 2");
        evalsLiveData.postValue(evals);
        Timber.d("addToEvalSet 3");
    }

    public static void clearEvalData() {
        evalsLiveData.getValue().clear();
    }

}
