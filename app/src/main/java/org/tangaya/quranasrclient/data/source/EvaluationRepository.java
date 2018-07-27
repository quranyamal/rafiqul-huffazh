package org.tangaya.quranasrclient.data.source;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.quranasrclient.data.Evaluation;

import java.util.ArrayList;

import timber.log.Timber;

public class EvaluationRepository {

    private static ArrayList<Evaluation> evals;

    private static MutableLiveData<ArrayList<Evaluation>> evalsLiveData;

    static {
        evalsLiveData = new MutableLiveData<>();

        evals = new ArrayList<>();
        evalsLiveData.setValue(evals);
    }

    public static MutableLiveData<ArrayList<Evaluation>> getEvalsLiveData() {
        return evalsLiveData;
    }

    public static ArrayList<Evaluation> getEvals() {
        return evals;
    }

    public static void addToEvalSet(Evaluation eval) {
        Timber.d("addToEvalSet 1");
        evals.add(eval);
        Timber.d("addToEvalSet 2");
        evalsLiveData.postValue(evals);
        Timber.d("addToEvalSet 3");
    }

}
