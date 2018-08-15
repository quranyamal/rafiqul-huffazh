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
import org.tangaya.rafiqulhuffazh.data.service.AudioPlayer;
import org.tangaya.rafiqulhuffazh.data.service.WavAudioRecorder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import timber.log.Timber;

public class MurojaahViewModel extends AndroidViewModel {

    public final ObservableField<String> ayahText = new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    public final ObservableField<Integer> chapterNum = new ObservableField<>();
    public final ObservableField<String> chapterName = new ObservableField<>();
    public final ObservableField<Integer> verseNum = new ObservableField<>(1);
    public final ObservableField<Integer> attemptState= new ObservableField<>();

    public final ObservableField<String> hintText = new ObservableField<>();
    public final ObservableField<Integer> hintVisibility = new ObservableField<>();
    public final ObservableField<String> instructionText = new ObservableField<>();

    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableBoolean isHintRequested = new ObservableBoolean();
    public final ObservableBoolean isPlaying = new ObservableBoolean();

    private boolean isMockRecording = true;

    AudioPlayer audioPlayer;

    Context mContext;
    MurojaahNavigator mNavigator;

    AudioPlayer mPlayer = new AudioPlayer();
    WavAudioRecorder mRecorder;

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

        audioPlayer = new AudioPlayer();
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
            mRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC, 16000,
                                                AudioFormat.CHANNEL_IN_MONO,
                                                AudioFormat.ENCODING_PCM_16BIT);
        }

        Timber.d("MurojaahViewModel constructor");
    }

    public void onActivityCreated(MurojaahNavigator navigator, int chapter) {
        mNavigator = navigator;
        chapterNum.set(chapter);
        chapterName.set(QuranUtil.getSurahName(chapter));
        EvaluationRepository.clearEvalData();
        //verseNum.set(1);

        Timber.d("onActivityCreated");
    }

    public void showHint() {
        chapterName.set(QuranUtil.getSurahName(chapterNum.get()));
        ayahText.set(QuranUtil.getAyah(chapterNum.get(), verseNum.get()));
        hintVisibility.set(View.VISIBLE);
        isHintRequested.set(true);
    }

    public void attemptVerse() {
        if (isRecording.get()) {
            submitAttempt();
        } else {
            createAttempt();
        }
    }

    void createAttempt() {
        // todo: fix filename of recording. save file to cache directory
        recordingFilepath = audioDir + "/recording/"+chapterNum.get()+"_"+verseNum.get()+".wav";
        testFilePath = audioDir + "/test/"+chapterNum.get()+"_"+verseNum.get()+".wav";

        evaluation = new EvaluationOld(chapterNum.get(), verseNum.get(), 123);
        evaluation.setFilepath(recordingFilepath);

        Attempt attempt = new Attempt(chapterNum.get(), verseNum.get());

        if (!isMockRecording) {
            attempt.setMockType(Attempt.MockType.MOCK_RECORDING);
            mRecorder.setOutputFile(recordingFilepath);
            mRecorder.prepare();
            mRecorder.start();
        }

        isRecording.set(true);
    }

    void submitAttempt() {

        if (!isMockRecording) {
            mRecorder.stop();
            mRecorder.reset();
        }

        Timber.d("submitAttempt() 1");

        Attempt attempt = new Attempt(chapterNum.get(), verseNum.get());
        attempt.setMockType(Attempt.MockType.MOCK_RECORDING);

        Timber.d("file path:" + attempt.getAudioFilePath());
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        Timber.d("submitAttempt() 3");
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

        Timber.d("submitAttempt() 5");

    }

    public void cancelAttempt() {
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
        audioPlayer.play(audioFileUri);
    }

    public void playReference() {
        if (!isPlaying.get()) {
            audioPlayer.play(Uri.parse(getTestFilePath()));
        } else {
            audioPlayer.stop();
        }
        isPlaying.set(!isPlaying.get());
    }

    private String getTestFilePath() {
        return audioDir + "/test/"+chapterNum.get()+"_"+verseNum.get()+".wav";
    }

    private boolean isEndOfSurah() {
        return !QuranUtil.isValidAyahNum(chapterNum.get(), verseNum.get()+1);
    }

    private void incrementAyah() {
        verseNum.set(verseNum.get()+1);
        hintVisibility.set(View.INVISIBLE);
        isHintRequested.set(false);
    }

    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d("State", "Yes, it is writable");
            return true;
        } else {
            return false;
        }
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
