package org.tangaya.rafiqulhuffazh.data.repository;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.data.model.Evaluation;

import java.util.ArrayList;

import timber.log.Timber;

public class EvaluationRepository {

    private static EvaluationRepository INSTANCE = null;

    private ArrayList<Evaluation> evaluations = new ArrayList<>();
    private MutableLiveData<ArrayList<Evaluation>> evaluationsLiveData = new MutableLiveData<>();

    private EvaluationRepository() {
        evaluationsLiveData.setValue(evaluations);
    }

    public static EvaluationRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EvaluationRepository();
        }
        return INSTANCE;
    }

    public void add(Evaluation eval) {
        evaluations.add(eval);
        Timber.d("eval added to repo");
    }

    public ArrayList<Evaluation> getEvaluations() {
        return evaluations;
    }

    public MutableLiveData<ArrayList<Evaluation>> getEvaluationsLiveData() {
        return evaluationsLiveData;
    }
}
