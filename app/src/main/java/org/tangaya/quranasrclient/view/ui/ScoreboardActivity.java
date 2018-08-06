package org.tangaya.quranasrclient.view.ui;

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

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.model.EvaluationOld;
import org.tangaya.quranasrclient.databinding.ActivityScoreboardBinding;
import org.tangaya.quranasrclient.view.navigator.ScoreboardNavigator;
import org.tangaya.quranasrclient.viewmodel.ScoreboardViewModel;

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

        final Observer<ArrayList<EvaluationOld>> evalsObserver = new Observer<ArrayList<EvaluationOld>>() {
            @Override
            public void onChanged(@Nullable ArrayList<EvaluationOld> evaluations) {
                Timber.d("eval set has changed");
                mViewModel.updateScore();
            }
        };
        mViewModel.getEvalsMutableLiveData().observe(this, evalsObserver);
    }



    @Override
    public void showDetail() {
        Intent intent = new Intent(this, ScoreDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void retryChapter() {
        Intent intent = new Intent(this, MurojaahActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void nextChapter() {

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();
        int chapterNum = sharedPref.getInt("CURRENT_CHAPTER_NUM", -1);

        if (chapterNum==115) {
            exit();
        } else {
            chapterNum++;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("CURRENT_CHAPTER_NUM", chapterNum);
            editor.commit();

            Intent intent = new Intent(this, MurojaahActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void selectAnotherChapter() {
        Intent intent = new Intent(this, ChapterSelectionActivity.class);
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
