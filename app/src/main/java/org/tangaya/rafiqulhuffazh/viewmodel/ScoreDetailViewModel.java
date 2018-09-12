package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;

import java.util.ArrayList;

public class ScoreDetailViewModel extends AndroidViewModel {

    private EvaluationRepository evaluationRepository;
    public MutableLiveData<ArrayList<Evaluation>> evaluationsLiveData;

    public ScoreDetailViewModel(@NonNull Application application) {
        super(application);

        evaluationRepository = EvaluationRepository.getInstance();
        evaluationsLiveData = evaluationRepository.getEvaluationsLiveData();
    }

    public MutableLiveData<ArrayList<Evaluation>> getEvaluationsLiveData() {
        return evaluationsLiveData;
    }
}
