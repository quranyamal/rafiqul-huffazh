package org.tangaya.rafiqulhuffazh.data.repository;

import org.tangaya.rafiqulhuffazh.data.model.Evaluation;

import java.util.ArrayList;

import timber.log.Timber;

public class EvaluationRepository {

    private static EvaluationRepository INSTANCE = null;

    private ArrayList<Evaluation> evaluations = new ArrayList<>();

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

    private EvaluationRepository() {}
}
