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
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.data.service.RecognitionTask;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.view.navigator.DevspaceNavigator;
import org.tangaya.rafiqulhuffazh.data.service.ASRServerStatusListener;

import java.util.ArrayList;
import java.util.LinkedList;

import timber.log.Timber;

public class DevspaceViewModel extends AndroidViewModel {

    private static DevspaceViewModel INSTANCE = null;


    public final ObservableInt surah = new ObservableInt(1);
    public final ObservableInt ayah = new ObservableInt(1);
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();
    public final ObservableBoolean isRecording = new ObservableBoolean(false);
    public final ObservableInt attemptCount = new ObservableInt(0);

    public final ObservableInt numAvailableWorkers = new ObservableInt();

    private MutableLiveData<ArrayList<EvaluationOld>> evalsMutableLiveData = EvaluationRepository.getEvalsLiveData();
    public MutableLiveData<ArrayList<EvaluationOld>> getEvalsMutableLiveData() {
        return evalsMutableLiveData;
    }


    private String hostname = ((MyApplication) getApplication()).getServerHostname();
    private String port = ((MyApplication) getApplication()).getServerPort();
    ASRServerStatusListener statusListener = new ASRServerStatusListener(hostname, port);

    String endpoint = ServerSetting.getRecognitionEndpoint();
    DevspaceNavigator mNavigator;

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    private LinkedList<RecognitionTask> recognitionTaskQueue = new LinkedList<>();

    private DevspaceViewModel(@NonNull Application application) {
        super(application);

//        surah.set(1);
//        ayah.set(1);
//        serverStatus.set("disconnected");
//        isRecording.set(false);
//        attemptCount.set(0);
//
//        hostname = ((MyApplication) getApplication()).getServerHostname();
//        port = ((MyApplication) getApplication()).getServerPort();
//        endpoint = ((MyApplication) getApplication()).getRecognitionEndpoint();
//
//        statusListener = new ASRServerStatusListener(hostname, port);
//        RecognitionTask.ENDPOINT = ((MyApplication) getApplication()).getRecognitionEndpoint();
    }

    public static DevspaceViewModel getIntance(Application application) {

        if ( INSTANCE == null) {
            INSTANCE = new DevspaceViewModel(application);
            RecognitionTask.ENDPOINT = ServerSetting.getRecognitionEndpoint();
        }
        return INSTANCE;
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

    private String getRecordingFilepath(int surah, int ayah) {
        return audioDir + "/recording/"+surah+"_"+ayah+".wav";
    }

    private String getTestFilepath(int surah, int ayah) {
        return audioDir + "/test/"+surah+"_"+ayah+".wav";
    }

    public ASRServerStatusListener getStatusListener() {
        return statusListener;
    }

    public void onActivityCreated(DevspaceNavigator navigator) {
        mNavigator = navigator;
    }

    public void onClickRecord() {

        if (!isRecording.get()) {
            mNavigator.onStartRecording(surah.get(), ayah.get());
            isRecording.set(true);
        } else {
            mNavigator.onStopRecording();
            isRecording.set(false);
        }
    }

    // add to recognizing queue
    public void recognizeRecording() {
        Timber.d("recognizeRecording()");

        Attempt attempt = new Attempt(surah.get(), ayah.get());
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void recognizeTestFile() {
        Timber.d("recognizeTestFile()");

        Attempt attempt = new Attempt(surah.get(), ayah.get());
        attempt.setMockType(Attempt.MockType.MOCK_RECORDING);
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void fakeRecognition() {
        Attempt attempt = new Attempt(surah.get(), ayah.get());
        attempt.setMockType(Attempt.MockType.MOCK_RESULT);
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        serverStatus.set("recognizing...");
    }

    public void playTestFile() {
        mNavigator.onPlayTestFile(surah.get(), ayah.get());
    }

    public void playRecording() {
        mNavigator.onPlayRecording(surah.get(), ayah.get());
    }

    public void gotoScoreboard() {
        mNavigator.gotoScoreboard();
    }

    public void incrementAyah() {
        ayah.set(ayah.get()+1);
    }

    public void decrementAyah() {
        if (ayah.get()>1) {
            ayah.set(ayah.get()-1);
        }
    }

    public void incrementSurah() {
        Log.d("DVM", "increment surahId clicked");
        surah.set(surah.get()+1);
    }

    public void decrementSurah() {
        Log.d("DVM", "decrement surahId clicked");
        if (surah.get()>1) {
            surah.set(surah.get()-1);
        }
    }

}

