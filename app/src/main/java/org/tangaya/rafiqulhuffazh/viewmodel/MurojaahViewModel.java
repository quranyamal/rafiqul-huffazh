package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.data.model.Attempt;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.data.service.RecognitionTask;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.view.navigator.MurojaahNavigator;
import org.tangaya.rafiqulhuffazh.data.service.ASRServerStatusListener;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioPlayer;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioRecorder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import timber.log.Timber;

public class MurojaahViewModel extends AndroidViewModel {

    public final ObservableField<String> ayahText = new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    public final ObservableField<Integer> surahNum = new ObservableField<>();
    public final ObservableField<String> surahName = new ObservableField<>();
    public final ObservableField<Integer> ayahNum = new ObservableField<>(1);
    public final ObservableField<Integer> attemptState= new ObservableField<>();

    public final ObservableField<String> hintText = new ObservableField<>();
    public final ObservableField<Integer> hintVisibility = new ObservableField<>();
    public final ObservableField<String> instructionText = new ObservableField<>();

    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableBoolean isHintRequested = new ObservableBoolean();
    public final ObservableBoolean isPlaying = new ObservableBoolean();

    private boolean isMockRecording = true;

    MyAudioPlayer myAudioPlayer;

    Context mContext;
    MurojaahNavigator mNavigator;

    MyAudioPlayer mPlayer = new MyAudioPlayer();
    MyAudioRecorder mRecorder;

    Uri audioFileUri;
    EvaluationOld evaluation;
    String endpoint;

    private String storageDir = Environment.getExternalStorageDirectory()+"";

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    String recordingFilepath, testFilePath;

    private LinkedList<RecognitionTask> recognitionTaskQueue = new LinkedList<>();

    public final ObservableInt numAvailableWorkers = new ObservableInt();

    ASRServerStatusListener statusListener;

    private MutableLiveData<ArrayList<EvaluationOld>> evalsMutableLiveData = EvaluationRepository.getEvalsLiveData();

    public MutableLiveData<ArrayList<EvaluationOld>> getEvalsMutableLiveData() {
        return evalsMutableLiveData;
    }

    public MurojaahViewModel(@NonNull Application context) {
        super(context);

        mContext = context;

        myAudioPlayer = new MyAudioPlayer();
        audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/001.wav");
        audioFileUri = Uri.parse(audioDir);

        isRecording.set(false);
        isHintRequested.set(false);
        isPlaying.set(false);

        endpoint = ((MyApplication) getApplication()).getRecognitionEndpoint();

        String hostname = ((MyApplication) getApplication()).getServerHostname();
        String port = ((MyApplication) getApplication()).getServerPort();
        statusListener = new ASRServerStatusListener(hostname, port);

        RecognitionTask.ENDPOINT = ((MyApplication) getApplication()).getRecognitionEndpoint();

        if (!isMockRecording) {
            mRecorder = new MyAudioRecorder(MediaRecorder.AudioSource.MIC, 16000,
                                                AudioFormat.CHANNEL_IN_MONO,
                                                AudioFormat.ENCODING_PCM_16BIT);
        }

        Timber.d("MurojaahViewModel constructor");
    }

    public void onActivityCreated(MurojaahNavigator navigator, int surah) {
        mNavigator = navigator;
        surahNum.set(surah);
        surahName.set(QuranUtil.getSurahName(surah));
        EvaluationRepository.clearEvalData();
        //ayahNum.set(1);

        Timber.d("onActivityCreated");
    }

    public void showHint() {
        surahName.set(QuranUtil.getSurahName(surahNum.get()));
        ayahText.set(QuranUtil.getAyah(surahNum.get(), ayahNum.get()));
        hintVisibility.set(View.VISIBLE);
        isHintRequested.set(true);
    }

    public void recordAyah() {
        if (isRecording.get()) {
            finishRecording();
        } else {
            startRecording();
        }
    }

    void startRecording() {
        // todo: fix filename of recording. save file to cache directory
        recordingFilepath = audioDir + "/recording/"+ surahNum.get()+"_"+ ayahNum.get()+".wav";
        testFilePath = audioDir + "/test/"+ surahNum.get()+"_"+ ayahNum.get()+".wav";

        evaluation = new EvaluationOld(surahNum.get(), ayahNum.get(), 123);
        evaluation.setFilepath(recordingFilepath);

        Attempt attempt = new Attempt(surahNum.get(), ayahNum.get());

        if (!isMockRecording) {
            attempt.setMockType(Attempt.MockType.MOCK_RECORDING);
            mNavigator.onStartRecording(surahNum.get(), ayahNum.get());
        }

        isRecording.set(true);
    }

    void finishRecording() {

        if (!isMockRecording) {
            mNavigator.onStopRecording();
        }

        Attempt attempt = new Attempt(surahNum.get(), ayahNum.get());
        attempt.setMockType(Attempt.MockType.MOCK_RECORDING);

        Timber.d("file path:" + attempt.getAudioFilePath());
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        recognitionTaskQueue.add(recognitionTask);

        Timber.d("recognitionTaskQueue size: " + recognitionTaskQueue.size());
        dequeueRecognitionTasks();
        Timber.d("recognitionTaskQueue size: " + recognitionTaskQueue.size());

        isRecording.set(false);

        if (isEndOfSurah()) {
            mNavigator.onMurojaahFinished();
        } else {
            incrementAyah();
        }

    }

    public void cancelRecording() {
        if (!isMockRecording) {
            mRecorder.stop();
            mRecorder.reset();
        }

        isRecording.set(false);
    }

    public void dequeueRecognitionTasks() {
        Timber.d("dequeueRecognitionTasks()");
        assert (numAvailableWorkers.get()>0);
        assert (getQueueSize()>0);
        RecognitionTask recognitionTask = recognitionTaskQueue.poll();
        recognitionTask.execute();
    }

    public int getQueueSize() {
        return recognitionTaskQueue.size();
    }

    public void playAttemptRecording() {
        myAudioPlayer.play(audioFileUri);
    }

    public void playReference() {
        if (!isPlaying.get()) {
            myAudioPlayer.play(Uri.parse(getTestFilePath()));
        } else {
            myAudioPlayer.stop();
        }
        isPlaying.set(!isPlaying.get());
    }

    private String getTestFilePath() {
        return audioDir + "/test/"+ surahNum.get()+"_"+ ayahNum.get()+".wav";
    }

    private boolean isEndOfSurah() {
        return !QuranUtil.isValidAyahNum(surahNum.get(), ayahNum.get()+1);
    }

    private void incrementAyah() {
        ayahNum.set(ayahNum.get()+1);
        hintVisibility.set(View.INVISIBLE);
        isHintRequested.set(false);
    }


    public ASRServerStatusListener getStatusListener() {
        return statusListener;
    }

    public void deleteRecordingFiles() {

        File recordingDir = new File(audioDir + "/recording/");
        for (File file : recordingDir.listFiles()) {
            file.delete();
        }
    }


}
