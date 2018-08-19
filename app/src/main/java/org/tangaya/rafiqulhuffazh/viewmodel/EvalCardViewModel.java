package org.tangaya.rafiqulhuffazh.viewmodel;

import android.arch.lifecycle.ViewModel;

import org.tangaya.rafiqulhuffazh.data.model.Evaluation;

public class EvalCardViewModel extends ViewModel {

    Evaluation evaluation;

    public EvalCardViewModel() {
    }

    public void setEvaluation(Evaluation eval) {
        evaluation = eval;
    }

}
