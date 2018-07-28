package org.tangaya.quranasrclient.view;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.ViewModelFactory;
import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityMurojaahBinding;
import org.tangaya.quranasrclient.navigator.MurojaahNavigator;
import org.tangaya.quranasrclient.viewmodel.MurojaahViewModel;

import java.util.ArrayList;

import timber.log.Timber;

public class MurojaahActivity extends AppCompatActivity implements MurojaahNavigator {

    private int CHAPTER_NUM;

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mMurojaahDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMurojaahDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mMurojaahDataBinding.setLifecycleOwner(this);

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();

        CHAPTER_NUM = sharedPref.getInt("CURRENT_CHAPTER_NUM", -1) + 1;
        Log.d("MA", "chapter num:"+CHAPTER_NUM);

        mViewModel = obtainViewModel(this);
        mMurojaahDataBinding.setViewmodel(mViewModel);
        mViewModel.onActivityCreated(this, CHAPTER_NUM);

        final Observer<Integer> numWorkerObserver = new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable Integer numAvailWorkers) {
                Timber.d("num worker has been changed ==> " + numAvailWorkers);
                mViewModel.numAvailableWorkers.set(numAvailWorkers);
                if (numAvailWorkers>0) {
                    mViewModel.dequeueRecognitionTasks();
                }
            }
        };

        mViewModel.getStatusListener().getNumWorkersAvailable().observe(this, numWorkerObserver);

        final Observer<ArrayList<Evaluation>> evalsObserver = new Observer<ArrayList<Evaluation>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Evaluation> evaluations) {
                Timber.d("eval set has changed");

            }
        };

        mViewModel.getEvalsMutableLiveData().observe(this, evalsObserver);

    }

    public static MurojaahViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TranscriptionsRepository transcriptionRepo = new TranscriptionsRepository();
        RecordingRepository recordingRepo = new RecordingRepository();

        return new MurojaahViewModel(activity.getApplication(), transcriptionRepo, recordingRepo);
        //return ViewModelProviders.of(activity, factory).get(MurojaahViewModel.class);
    }

    @Override
    public void gotoResult() {
        Intent intent = new Intent(this, ScoreboardActivity.class);
        finish();
        startActivity(intent);
    }

}
