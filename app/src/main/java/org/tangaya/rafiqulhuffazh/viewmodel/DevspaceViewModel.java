package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.util.Log;


import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.data.service.QuranTranscriber;
import org.tangaya.rafiqulhuffazh.data.service.RecognitionTask;
import org.tangaya.rafiqulhuffazh.data.service.ServerStatusListener;
import org.tangaya.rafiqulhuffazh.util.AudioFileHelper;
import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.view.navigator.DevspaceNavigator;

import java.io.File;

import timber.log.Timber;

public class DevspaceViewModel extends AndroidViewModel {

    private static DevspaceViewModel INSTANCE = null;


    public final ObservableInt surah = new ObservableInt(1);
    public final ObservableInt ayah = new ObservableInt(1);
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableBoolean isRecording = new ObservableBoolean(false);
    public final ObservableInt evalCount = new ObservableInt(0);
    public final ObservableInt numAvailableWorkers = new ObservableInt();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    private QuranAyahAudio audio = null;

    private QuranTranscriber transcriber;
    private MurojaahEvaluator evaluator;
    private EvaluationRepository evalRepo;
    private ServerStatusListener serverStatusListener;

    private MutableLiveData<QuranAyahAudio> transcribedAudioHolder = new MutableLiveData<>();

    public MutableLiveData<QuranAyahAudio> getTranscribedAudioHolder() {
        return transcribedAudioHolder;
    }

    public MurojaahEvaluator getEvaluator() {
        return evaluator;
    }

    public void evaluate(QuranAyahAudio audio) {
        Evaluation eval = evaluator.evaluate(audio);
        evalRepo.add(eval);
    }

    public ObservableInt getNumAvailableWorkers() {
        return numAvailableWorkers;
    }

    private DevspaceNavigator mNavigator;

    private DevspaceViewModel(@NonNull Application application) {
        super(application);

        serverStatusListener = ServerStatusListener.getInstance();
        transcriber = QuranTranscriber.getInstance(transcribedAudioHolder);
        evaluator = MurojaahEvaluator.getInstance();
        evalRepo = EvaluationRepository.getInstance();
    }

    public static DevspaceViewModel getInstance(Application application) {

        if ( INSTANCE == null) {
            INSTANCE = new DevspaceViewModel(application);
        }
        RecognitionTask.ENDPOINT = ServerSetting.getRecognitionEndpoint();

        return INSTANCE;
    }

    public ServerStatusListener getServerListener() {
        return serverStatusListener;
    }

    public void onActivityCreated(DevspaceNavigator navigator) {
        mNavigator = navigator;
    }

    public void onClickRecord() {

        if (!isRecording.get()) {
            audio = new Recording(surah.get(), ayah.get());
            mNavigator.onStartRecording((Recording) audio);
            isRecording.set(true);
        } else {
            mNavigator.onStopRecording();
            isRecording.set(false);
        }
    }

    public void transcribeRecording() {
        transcriber.addToQueue(audio);
        Timber.d("added to queue");

        pollTranscriptionQueue();
    }

    public void pollTranscriptionQueue() {

        if (numAvailableWorkers.get()>0) {
            Timber.d("idle worker:"+numAvailableWorkers.get()+" processing queue");
            transcriber.poll();
        } else {
            Timber.d("waiting for idle worker");
        }
    }

    public void transcribeTestFile() {
        transcriber.addToQueue(new QuranAyahAudio(surah.get(), ayah.get()));
        Timber.d("added to queue");

        pollTranscriptionQueue();
    }

//    public void fakeRecognition() {
//        Attempt attempt = new Attempt(surah.get(), ayah.get());
//        attempt.setMockType(Attempt.MockType.MOCK_RESULT);
//        RecognitionTask recognitionTask = new RecognitionTask(attempt);
//        recognitionTaskQueue.add(recognitionTask);
//        dequeueRecognitionTasks();
//        serverStatus.set("recognizing...");
//    }

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

    public void deleteRecordingFiles() {

        File recordingDir = new File(AudioFileHelper.getUserRecordingPath());
        for (File file : recordingDir.listFiles()) {
            file.delete();
        }
    }

}

