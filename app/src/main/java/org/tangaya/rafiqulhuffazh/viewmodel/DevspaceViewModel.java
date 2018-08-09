package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.data.model.Attempt;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.service.RecognitionTask;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.view.navigator.DevspaceNavigator;
import org.tangaya.rafiqulhuffazh.data.service.ASRServerStatusListener;
import org.tangaya.rafiqulhuffazh.data.service.AudioPlayer;
import org.tangaya.rafiqulhuffazh.data.service.WavAudioRecorder;

import java.util.ArrayList;
import java.util.LinkedList;

import timber.log.Timber;

public class DevspaceViewModel extends AndroidViewModel {

    ASRServerStatusListener statusListener;


    public final ObservableInt chapter = new ObservableInt();
    public final ObservableInt verse = new ObservableInt();
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();
    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableInt attemptCount = new ObservableInt();

    public final ObservableInt numAvailableWorkers = new ObservableInt();

    private MutableLiveData<ArrayList<EvaluationOld>> evalsMutableLiveData = EvaluationRepository.getEvalsLiveData();

    public MutableLiveData<ArrayList<EvaluationOld>> getEvalsMutableLiveData() {
        return evalsMutableLiveData;
    }

    private String hostname;
    private String port;

    String endpoint;

    WavAudioRecorder mRecorder;
    AudioPlayer mPlayer;
    DevspaceNavigator mNavigator;

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    private LinkedList<RecognitionTask> recognitionTaskQueue = new LinkedList<>();

    public DevspaceViewModel(@NonNull Application application) {
        super(application);

        mRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC,
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mPlayer = new AudioPlayer();

        chapter.set(1);
        verse.set(1);
        serverStatus.set("disconnected");
        isRecording.set(false);
        attemptCount.set(0);

        hostname = ((MyApplication) getApplication()).getServerHostname();
        port = ((MyApplication) getApplication()).getServerPort();
        endpoint = ((MyApplication) getApplication()).getRecognitionEndpoint();

        statusListener = new ASRServerStatusListener(hostname, port);
        RecognitionTask.ENDPOINT = ((MyApplication) getApplication()).getRecognitionEndpoint();
    }

    public void dequeueRecognitionTasks() {
        if (numAvailableWorkers.get()>0) {
            if (recognitionTaskQueue.size()>0) {
                RecognitionTask recognitionTask = recognitionTaskQueue.poll();
                recognitionTask.execute();
            } else {
                Timber.d("Recognition task queue empty");
            }
        } else {
            Timber.d("no worker available");
        }
    }

    private String getRecordingFilepath(int chapter, int verse) {
        return audioDir + "/recording/"+chapter+"_"+verse+".wav";
    }

    private String getTestFilepath(int chapter, int verse) {
        return audioDir + "/test/"+chapter+"_"+verse+".wav";
    }

    public ASRServerStatusListener getStatusListener() {
        return statusListener;
    }

    public void onActivityCreated(DevspaceNavigator navigator) {
        mNavigator = navigator;
    }

    public void onClickRecord() {

        if (!isRecording.get()) {

            mRecorder.setOutputFile(getRecordingFilepath(chapter.get(), verse.get()));
            mRecorder.prepare();
            mRecorder.start();

            isRecording.set(true);

        } else {

            mRecorder.stop();
            mRecorder.reset();

            isRecording.set(false);
        }
    }

    public void playRecordedAudio() {
        mPlayer.play(Uri.parse(getRecordingFilepath(chapter.get(), verse.get())));
        Log.d("DVM", "playing recording...");
    }

    // add to recognizing queue
    public void recognizeRecording() {
        Timber.d("recognizeRecording()");

        Attempt attempt = new Attempt(chapter.get(), verse.get());
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void recognizeTestFile() {
        Timber.d("recognizeTestFile()");

        Attempt attempt = new Attempt(chapter.get(), verse.get());
        attempt.setMockType(Attempt.MockType.MOCK_RECORDING);
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void fakeRecognition() {
        Attempt attempt = new Attempt(chapter.get(), verse.get());
        attempt.setMockType(Attempt.MockType.MOCK_RESULT);
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void playTestFile() {
        mPlayer.play(Uri.parse(getTestFilepath(chapter.get(), verse.get())));
        Log.d("DVM", "playing test file...");
    }

    public void gotoEvalDetail() {
        mNavigator.gotoEvalDetail();
    }

    public void gotoScoreboard() {
        mNavigator.gotoScoreboard();
    }

    public void incrementVerse() {
        verse.set(verse.get()+1);
    }

    public void decrementVerse() {
        if (verse.get()>1) {
            verse.set(verse.get()-1);
        }
    }

    public void incrementChapter() {
        Log.d("DVM", "increment chapter clicked");
        chapter.set(chapter.get()+1);
    }

    public void decrementChapter() {
        Log.d("DVM", "decrement chapter clicked");
        if (chapter.get()>1) {
            chapter.set(chapter.get()-1);
        }
    }

}

