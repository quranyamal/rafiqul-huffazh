package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.databinding.ActivityScoreboardBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.ScoreboardNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.ScoreboardViewModel;

import java.util.ArrayList;

import timber.log.Timber;

public class ScoreboardActivity extends Activity implements LifecycleOwner, ScoreboardNavigator {

    private ScoreboardViewModel mViewModel;
    private ActivityScoreboardBinding mBinding;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        mViewModel = new ScoreboardViewModel(getApplication());
        mViewModel.onActivityCreated(this);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scoreboard);
        mBinding.setViewmodel(mViewModel);

        final Observer<ArrayList<Evaluation>> evalsObserver = new Observer<ArrayList<Evaluation>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Evaluation> eval) {
                Timber.d("eval set has changed");
                mViewModel.updateScore();
            }
        };
        mViewModel.getEvaluationsLiveData().observe(this, evalsObserver);

        setTitle("Finish");
    }



    @Override
    public void showDetail() {
        Intent intent = new Intent(this, ScoreDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void retrySurah() {
        Intent intent = new Intent(this, MurojaahActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void nextSurah() {

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();
        int surahNum = sharedPref.getInt("CURRENT_SURAH_NUM", -1);

        if (surahNum==115) {
            exit();
        } else {
            surahNum++;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("CURRENT_SURAH_NUM", surahNum);
            editor.commit();

            Intent intent = new Intent(this, MurojaahActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void selectAnotherSurah() {
        Intent intent = new Intent(this, SurahSelectionActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void exit() {
        finish();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
