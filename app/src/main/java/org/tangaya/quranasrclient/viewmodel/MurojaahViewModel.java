package org.tangaya.quranasrclient.viewmodel;

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

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Attempt;
import org.tangaya.quranasrclient.data.EvaluationOld;
import org.tangaya.quranasrclient.data.RecognitionTask;
import org.tangaya.quranasrclient.data.source.EvaluationRepository;
import org.tangaya.quranasrclient.navigator.MurojaahNavigator;
import org.tangaya.quranasrclient.service.ASRServerStatusListener;
import org.tangaya.quranasrclient.service.AudioPlayer;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import timber.log.Timber;

public class MurojaahViewModel extends AndroidViewModel
        implements TranscriptionsDataSource.PerformRecognitionCallback {

    public final ObservableField<String> ayahText = new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    public final ObservableField<Integer> chapterNum = new ObservableField<>();
    public final ObservableField<String> chapterName = new ObservableField<>();
    public final ObservableField<Integer> verseNum = new ObservableField<>();
    public final ObservableField<Integer> attemptState= new ObservableField<>();

    public final ObservableField<String> hintText = new ObservableField<>();
    public final ObservableField<Integer> hintVisibility = new ObservableField<>();
    public final ObservableField<String> instructionText = new ObservableField<>();

    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableBoolean isHintRequested = new ObservableBoolean();

    private boolean isLastVerse;

    AudioPlayer audioPlayer;

    Context mContext;
    TranscriptionsRepository mTranscriptionsRepository;
    RecordingRepository mRecordingRepository;
    MurojaahNavigator mNavigator;

    AudioPlayer mPlayer = new AudioPlayer();
    WavAudioRecorder mRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC,
            16000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    Uri audioFileUri;
    EvaluationOld evaluation;
    String endpoint;
    WebSocket webSocket;

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

    public MurojaahViewModel(@NonNull Application context,
                             @NonNull TranscriptionsRepository transcriptionsRepository,
                             @NonNull RecordingRepository recordingRepository) {
        super(context);

        mContext = context;
        mTranscriptionsRepository = transcriptionsRepository;
        mRecordingRepository = recordingRepository;
        verseNum.set(1);

        audioPlayer = new AudioPlayer();
        audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/001.wav");
        audioFileUri = Uri.parse(audioDir);

        isRecording.set(false);
        isHintRequested.set(false);

        endpoint = ((MyApplication) getApplication()).getRecognitionEndpoint();

        String hostname = ((MyApplication) getApplication()).getServerHostname();
        String port = ((MyApplication) getApplication()).getServerPort();
        statusListener = new ASRServerStatusListener(hostname, port);

        RecognitionTask.ENDPOINT = ((MyApplication) getApplication()).getRecognitionEndpoint();
        EvaluationRepository.clearEvalData();
    }

    public void onActivityCreated(MurojaahNavigator navigator, int chapter) {
        mNavigator = navigator;
        chapterNum.set(chapter);
        chapterName.set(QuranScriptRepository.getChapter(chapter).getTitle());
    }

    public void showHint() {
        chapterName.set(QuranScriptRepository.getChapter(chapterNum.get()).getTitle());
        ayahText.set(QuranScriptRepository.getChapter(chapterNum.get()).getVerseScript(verseNum.get()));
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
        attempt.setMockType(Attempt.MockType.MOCK_RECORDING);

        mRecorder.setOutputFile(recordingFilepath);
        mRecorder.prepare();
        mRecorder.start();

        isRecording.set(true);
    }

    void submitAttempt() {

        mRecorder.stop();
        mRecorder.reset();

        Log.d("MVM", "creating web socket");
        try {
            webSocket = new WebSocketFactory().createSocket(endpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timber.d("submitAttempt() 1");

        Attempt attempt = new Attempt(chapterNum.get(), verseNum.get());
        attempt.setMockType(Attempt.MockType.MOCK_RECORDING);

        Timber.d("submitAttempt() 2. chapterNum.get()="+chapterNum.get()+"verseNum.get()");
        Timber.d("file path:" + attempt.getAudioFilePath());
        RecognitionTask recognitionTask = new RecognitionTask(attempt);
        Timber.d("submitAttempt() 3");
        recognitionTaskQueue.add(recognitionTask);
        dequeueRecognitionTasks();
        Timber.d("submitAttempt() 4");

        if (isEndOfSurah()) {
            mNavigator.gotoResult();
        } else {
            incrementAyah();
            isRecording.set(false);
        }
        Timber.d("submitAttempt() 5");

    }

    public void cancelAttempt() {
        mRecorder.stop();
        mRecorder.reset();

        isRecording.set(false);
    }

    public void dequeueRecognitionTasks() {
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

    private boolean isEndOfSurah() {
        return !QuranScriptRepository.getChapter(chapterNum.get()).isValidVerseNum(verseNum.get()+1);
    }

    private void incrementAyah() {
        verseNum.set(verseNum.get()+1);
        hintVisibility.set(View.INVISIBLE);
        isHintRequested.set(false);
    }

    @Override
    public void onRecognitionCompleted() {

    }

    @Override
    public void onRecognitionError() {

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

}
