package org.tangaya.quranasrclient.view.ui;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.model.EvaluationOld;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityMurojaahBinding;
import org.tangaya.quranasrclient.view.navigator.MurojaahNavigator;
import org.tangaya.quranasrclient.viewmodel.MurojaahViewModel;

import java.util.ArrayList;

import timber.log.Timber;

public class MurojaahActivity extends Activity implements LifecycleOwner, MurojaahNavigator {

    private int CHAPTER_NUM;

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mMurojaahDataBinding;
    private LifecycleRegistry mLifecycleRegistry;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        mMurojaahDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mMurojaahDataBinding.setLifecycleOwner(this);


        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();

        CHAPTER_NUM = sharedPref.getInt("CURRENT_CHAPTER_NUM", -1) + 1;
        Log.d("MA", "chapter num:"+CHAPTER_NUM);

        if (mViewModel==null) {
            mViewModel = obtainViewModel(this);
            mViewModel.onActivityCreated(this, CHAPTER_NUM);
        }

        mMurojaahDataBinding.setViewmodel(mViewModel);

        Timber.d("queue size ==> " + mViewModel.getQueueSize());

        final Observer<Integer> numWorkerObserver = new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable Integer numAvailWorkers) {
                Timber.d("num worker has been changed ==> " + numAvailWorkers);
                Timber.d("queue size ==> " + mViewModel.getQueueSize());
                mViewModel.numAvailableWorkers.set(numAvailWorkers);
                if (numAvailWorkers>0) {
                    if (mViewModel.getQueueSize()>0) {
                        mViewModel.dequeueRecognitionTasks();
                    } else {
                        Timber.d("recognition task queue empty. do nothing");
                    }
                } else {
                    Timber.d("no worker available. do nothing");
                }
            }
        };

        mViewModel.getStatusListener().getNumWorkersAvailable().observe(this, numWorkerObserver);

        final Observer<ArrayList<EvaluationOld>> evalsObserver = new Observer<ArrayList<EvaluationOld>>() {
            @Override
            public void onChanged(@Nullable ArrayList<EvaluationOld> evaluations) {
                Timber.d("eval set has changed. num eval = " + evaluations.size());
            }
        };

        mViewModel.getEvalsMutableLiveData().observe(this, evalsObserver);

        //textView = findViewById(R.id.progressbar_info);
        //progressBar = findViewById(R.id.recognizing_progressbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.deleteRecordingFiles();
    }

    public static MurojaahViewModel obtainViewModel(Activity activity) {

        return new MurojaahViewModel(activity.getApplication());
        //return ViewModelProviders.of(activity, factory).get(MurojaahViewModel.class);
    }

    @Override
    public void gotoResult() {
//        showProgressBar();

        Intent intent = new Intent(this, ScoreboardActivity.class);
        finish();

        startActivity(intent);
    }
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

//    private void showProgressBarOld() {
//
//        Timber.d("showing progress bar");
//        progressBar = findViewById(R.id.recognizing_progressbar);
//        // Start long running operation in a background thread
//        new Thread(new Runnable() {
//            public void run() {
//                while (progressStatus < 100) {
//                    progressStatus += 1;
//                    // Update the progress bar and display the
//                    //current value in the text view
//                    handler.post(new Runnable() {
//                        public void run() {
//                            progressBar.setProgress(progressStatus);
//                            textView.setText(progressStatus+"/"+progressBar.getMax());
//                        }
//                    });
//                    try {
//                        // Sleep for 200 milliseconds.
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//        Timber.d("end of progress bar");
//    }
}
