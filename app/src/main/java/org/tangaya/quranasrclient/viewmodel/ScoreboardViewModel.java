package org.tangaya.quranasrclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.data.EvaluationOld;
import org.tangaya.quranasrclient.data.source.EvaluationRepository;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.navigator.ScoreboardNavigator;
import org.tangaya.quranasrclient.util.Evaluator;

import java.util.ArrayList;

import timber.log.Timber;

public class ScoreboardViewModel extends AndroidViewModel {

    ScoreboardNavigator mNavigator;
    Evaluator evaluator;

    public final ObservableField<String> currentChapter = new ObservableField<>();
    public final ObservableField<String> nextChapter = new ObservableField<>();
    public final ObservableInt score = new ObservableInt();

    private MutableLiveData<ArrayList<EvaluationOld>> evalsMutableLiveData = EvaluationRepository.getEvalsLiveData();

    public ScoreboardViewModel(@NonNull Application application) {
        super(application);
    }


    public void onActivityCreated(ScoreboardNavigator navigator) {
        mNavigator = navigator;

        SharedPreferences preferences = ((MyApplication) getApplication()).getPreferences();
        int chapterNum = preferences.getInt("CURRENT_CHAPTER_NUM", -1)+1;

        currentChapter.set(QuranScriptRepository.getChapter(chapterNum).getTitle());
        nextChapter.set(QuranScriptRepository.getChapter(chapterNum+1).getTitle());

        updateScore();
    }

    public MutableLiveData<ArrayList<EvaluationOld>> getEvalsMutableLiveData() {
        return evalsMutableLiveData;
    }

    public void updateScore() {
        ArrayList<EvaluationOld> evals = EvaluationRepository.getEvalsLiveData().getValue();
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

    public void retryChapter() {
        mNavigator.retryChapter();
    }

    public void nextChapter() {
        mNavigator.nextChapter();
    }

    public void selectAnotherChapter() {
        mNavigator.selectAnotherChapter();
    }

    public void exit() {
        mNavigator.exit();
    }

}
