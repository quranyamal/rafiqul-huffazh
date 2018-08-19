package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepositoryOld;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.view.navigator.ScoreboardNavigator;

import java.util.ArrayList;

import timber.log.Timber;

public class ScoreboardViewModel extends AndroidViewModel {

    ScoreboardNavigator mNavigator;

    public final ObservableField<String> currentSurah = new ObservableField<>();
    public final ObservableField<String> nextSurah = new ObservableField<>();
    public final ObservableInt score = new ObservableInt();

    private MutableLiveData<ArrayList<EvaluationOld>> evalsMutableLiveData = EvaluationRepositoryOld.getEvalsLiveData();

    public ScoreboardViewModel(@NonNull Application application) {
        super(application);
    }


    public void onActivityCreated(ScoreboardNavigator navigator) {
        mNavigator = navigator;

        SharedPreferences preferences = ((MyApplication) getApplication()).getPreferences();
        int surahNum = preferences.getInt("CURRENT_SURAH_NUM", -1)+1;

        currentSurah.set(QuranUtil.getSurahName(surahNum));
        nextSurah.set(QuranUtil.getSurahName(surahNum+1));

        updateScore();
    }

    public MutableLiveData<ArrayList<EvaluationOld>> getEvalsMutableLiveData() {
        return evalsMutableLiveData;
    }

    public void updateScore() {
        ArrayList<EvaluationOld> evals = EvaluationRepositoryOld.getEvalsLiveData().getValue();
        score.set(getScore(evals));
    }

    private int getScore(ArrayList<EvaluationOld> evaluations) {
        int totalPoints=0, maxPoints=0;

        for (EvaluationOld eval : evaluations) {
            totalPoints += eval.getEarnedPoints().get();
            maxPoints += eval.getMaxpoints().get();
            Timber.d("total pts:"+totalPoints+"max pts:"+maxPoints);
        }
        return Math.round(totalPoints / (float) maxPoints * 100);
    }

    public void showDetail() {
        mNavigator.showDetail();
    }

    public void retrySurah() {
        mNavigator.retrySurah();
    }

    public void nextSurah() {
        mNavigator.nextSurah();
    }

    public void selectAnotherSurah() {
        mNavigator.selectAnotherSurah();
    }

    public void exit() {
        mNavigator.exit();
    }

}
