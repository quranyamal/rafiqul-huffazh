package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioRecorder;
import org.tangaya.rafiqulhuffazh.databinding.ActivityMurojaahBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.MurojaahNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.MurojaahViewModel;

import timber.log.Timber;

public class MurojaahActivity extends Activity implements LifecycleOwner, MurojaahNavigator {

    private int SURAH_NUM;

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mBinding;
    private LifecycleRegistry mLifecycleRegistry;
    private MyAudioRecorder mRecorder = MyAudioRecorder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mBinding.setLifecycleOwner(this);

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();

        SURAH_NUM = sharedPref.getInt("CURRENT_SURAH_NUM", -1) + 1;
        Log.d("MA", "surahId num:"+ SURAH_NUM);

        if (mViewModel==null) {
            mViewModel = obtainViewModel(this);
            mViewModel.onActivityCreated(this, SURAH_NUM);
        }

        mBinding.setViewmodel(mViewModel);

        mViewModel.setServerStatus(((MyApplication)getApplication())
                .getServerStatusListener().getStatus().getValue());

        setupObservers();
    }

    private void setupObservers() {
        final Observer<String> serverStatusObserver = new Observer<String>() {

            @Override
            public void onChanged(@Nullable String serverStatus) {
                Timber.d("server status has changed ==> " + serverStatus);
                mViewModel.serverStatus.set(serverStatus);
            }
        };
        ((MyApplication)getApplication()).getServerStatusListener()
                .getStatus().observe(this, serverStatusObserver);

        final Observer<Integer> numWorkerObserver = new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable Integer numAvailWorkers) {
                Timber.d("num worker has changed ==> " + numAvailWorkers);
                mViewModel.numAvailableWorkers.set(numAvailWorkers);
                if (numAvailWorkers > 0) {
                    mViewModel.pollTranscriptionQueue();
                }
            }
        };
        ((MyApplication)getApplication()).getServerStatusListener()
                .getNumWorkersAvailable().observe(this, numWorkerObserver);

        final Observer<QuranAyahAudio> transcribedAudioObserver = new Observer<QuranAyahAudio>() {
            @Override
            public void onChanged(@Nullable QuranAyahAudio audio) {
                Timber.d("new transcribed audio arrived");
                Timber.d(audio.getTranscription());

                mViewModel.evaluate(audio);
            }
        };
        mViewModel.getTranscribedAudioHolder().observe(this, transcribedAudioObserver);
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
        mViewModel.transcribeRecording();
        Timber.d("onStopRecording");
    }

    public static MurojaahViewModel obtainViewModel(Activity activity) {

        return new MurojaahViewModel(activity.getApplication());
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

}
