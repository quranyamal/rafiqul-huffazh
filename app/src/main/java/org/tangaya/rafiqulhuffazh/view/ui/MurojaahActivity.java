package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioRecorder;
import org.tangaya.rafiqulhuffazh.databinding.ActivityMurojaahBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.MurojaahNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.MurojaahViewModel;

import timber.log.Timber;

public class MurojaahActivity extends Activity implements LifecycleOwner, MurojaahNavigator {

    private int SURAH_NUM;

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mMurojaahDataBinding;
    private LifecycleRegistry mLifecycleRegistry;
    private MyAudioRecorder mRecorder = MyAudioRecorder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        mMurojaahDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mMurojaahDataBinding.setLifecycleOwner(this);


        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();

        SURAH_NUM = sharedPref.getInt("CURRENT_SURAH_NUM", -1) + 1;
        Log.d("MA", "surahId num:"+ SURAH_NUM);

        if (mViewModel==null) {
            mViewModel = obtainViewModel(this);
            mViewModel.onActivityCreated(this, SURAH_NUM);
        }

        mMurojaahDataBinding.setViewmodel(mViewModel);

//        Timber.d("queue size ==> " + mViewModel.getQueueSize());
//
//        final Observer<Integer> numWorkerObserver = new Observer<Integer>() {
//
//            @Override
//            public void onChanged(@Nullable Integer numAvailWorkers) {
//                Timber.d("num worker has been changed ==> " + numAvailWorkers);
//                Timber.d("queue size ==> " + mViewModel.getQueueSize());
//                mViewModel.numAvailableWorkers.set(numAvailWorkers);
//                if (numAvailWorkers>0) {
//                    if (mViewModel.getQueueSize()>0) {
//                        mViewModel.pollRecognitionQueue();
//                    } else {
//                        Timber.d("recognition task queue empty. do nothing");
//                    }
//                } else {
//                    Timber.d("no worker available. do nothing");
//                }
//            }
//        };
//        mViewModel.getStatusListener().getNumWorkersAvailable().observe(this, numWorkerObserver);
//
//        final Observer<ArrayList<EvaluationOld>> evalsObserver = new Observer<ArrayList<EvaluationOld>>() {
//            @Override
//            public void onChanged(@Nullable ArrayList<EvaluationOld> evaluations) {
//                Timber.d("eval set has changed. num eval = " + evaluations.size());
//            }
//        };
//        mViewModel.getEvalsMutableLiveData().observe(this, evalsObserver);

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
    public void onMurojaahFinished() {
        showProgressDialog();
    }

    @Override
    public void gotoResult() {
        Intent intent = new Intent(this, ScoreboardActivity.class);
        finish();
        startActivity(intent);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    private void showProgressDialog() {
        final ProgressDialog progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(99);
        progressDoalog.setMessage("Recognizing your recitation... " + progressDoalog.getProgress());
        progressDoalog.setTitle("Please wait");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gotoResult();
            }
        });

        final Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDoalog.incrementProgressBy(5);
            }
        };

        progressDoalog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDoalog.getProgress() <= progressDoalog.getMax()) {
                        Thread.sleep(300);
                        handle.sendMessage(handle.obtainMessage());
                        if (progressDoalog.getProgress() == progressDoalog
                                .getMax()) {
                            progressDoalog.dismiss();
                        }
                    }
                    gotoResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onStartRecording(Recording recording) {
        mRecorder.setOutput(recording);
        mRecorder.prepare();
        mRecorder.start();
        Timber.d("onStartRecording");
    }

    @Override
    public void onStopRecording() {
        mRecorder.stop();
        mRecorder.reset();
        Timber.d("onStopRecording");
    }

}
