package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioPlayer;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioRecorder;
import org.tangaya.rafiqulhuffazh.databinding.ActivityDevspaceBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.DevspaceNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.DevspaceViewModel;

import timber.log.Timber;

public class DevspaceActivity extends Activity implements LifecycleOwner, DevspaceNavigator {

    public DevspaceViewModel mViewModel;
    private ActivityDevspaceBinding binding;
    private LifecycleRegistry mLifecycleRegistry;

    private MyAudioRecorder mRecorder = MyAudioRecorder.getInstance();
    MyAudioPlayer mPlayer = MyAudioPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Development Space");

        mViewModel = DevspaceViewModel.getInstance(this.getApplication());
        mViewModel.onActivityCreated(this);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        setupObservers();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_devspace);
        binding.setViewmodel(mViewModel);
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
                if (numAvailWorkers>0) {
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

//        final Observer<Evaluation> evalResultObserver = new Observer<Evaluation>() {
//            @Override
//            public void onChanged(@Nullable Evaluation eval) {
//                Timber.d("new eval arrived");
//                mViewModel.addEvalToRepo(eval);
//                mViewModel.evalCount.set(mViewModel.evalCount.get()+1);
//            }
//        };
//        mViewModel.getEvaluator().getEvalResult().observe(this, evalResultObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    public void gotoScoreboard() {
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
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

    @Override
    public void onPlayRecording(int surah, int ayah) {
        mPlayer.play(MyAudioPlayer.Source.RECORDING, surah, ayah);
    }

    @Override
    public void onPlayTestFile(int surah, int ayah) {
        mPlayer.play(MyAudioPlayer.Source.QARI1, surah, ayah);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.deleteRecordingFiles();
    }
}
