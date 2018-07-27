package org.tangaya.quranasrclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;
import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Attempt;
import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.data.RecognitionTask;
import org.tangaya.quranasrclient.data.source.EvaluationRepository;
import org.tangaya.quranasrclient.navigator.DevspaceNavigator;
import org.tangaya.quranasrclient.service.ASRServerStatusListener;
import org.tangaya.quranasrclient.service.AudioPlayer;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import timber.log.Timber;

public class DevspaceViewModel extends AndroidViewModel {

    WebSocket webSocket;

    ASRServerStatusListener statusListener;


    public final ObservableInt chapter = new ObservableInt();
    public final ObservableInt verse = new ObservableInt();
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();
    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableInt attemptCount = new ObservableInt();

    public final ObservableInt numAvailableWorkers = new ObservableInt();

    private MutableLiveData<ArrayList<Evaluation>> evalsMutableLiveData = EvaluationRepository.getEvalsLiveData();

    public MutableLiveData<ArrayList<Evaluation>> getEvalsMutableLiveData() {
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
        endpoint = ((MyApplication) getApplication()).getSpeechEndpoint();

        statusListener = new ASRServerStatusListener(hostname, port);
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

        String recordingFilepath = getRecordingFilepath(chapter.get(), verse.get());

        Attempt attempt = new Attempt(chapter.get(), verse.get(), Attempt.SOURCE_FROM_RECORDING);
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void recognizeTestFile() {
        Timber.d("recognizeTestFile()");

        Attempt attempt = new Attempt(chapter.get(), verse.get(), Attempt.SOURCE_FROM_TEST_FILE);
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

