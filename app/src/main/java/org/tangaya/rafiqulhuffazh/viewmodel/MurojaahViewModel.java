package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.repository.AudioRepository;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.data.service.QuranTranscriber;
import org.tangaya.rafiqulhuffazh.data.service.ServerStatusListener;
import org.tangaya.rafiqulhuffazh.util.AudioFileHelper;
import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepositoryOld;
import org.tangaya.rafiqulhuffazh.view.navigator.MurojaahNavigator;
import org.tangaya.rafiqulhuffazh.data.service.MyAudioPlayer;

import java.io.File;
import java.util.ArrayList;

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
    public final ObservableInt numAvailableWorkers = new ObservableInt();

    private QuranAyahAudio audio = null;
    private boolean isMockRecording = false;


    private MutableLiveData<QuranAyahAudio> transcribedAudioHolder = new MutableLiveData<>();

    private MyAudioPlayer myAudioPlayer;
    private Context mContext;
    private MurojaahNavigator mNavigator;

    private QuranTranscriber transcriber;
    private MurojaahEvaluator evaluator;
    private EvaluationRepository evalRepo;
    private ServerStatusListener serverStatusListener;

    public MurojaahViewModel(@NonNull Application context) {
        super(context);

        mContext = context;

        isRecording.set(false);
        isHintRequested.set(false);
        isPlaying.set(false);

        serverStatusListener = ServerStatusListener.getInstance();
        transcriber = QuranTranscriber.getInstance(transcribedAudioHolder);
        evaluator = MurojaahEvaluator.getInstance();
        evalRepo = EvaluationRepository.getInstance();

        serverStatus.set(serverStatusListener.getStatus().getValue());
        Timber.d("MurojaahViewModel constructor");
    }

    public void onActivityCreated(MurojaahNavigator navigator, int surah) {
        mNavigator = navigator;
        surahNum.set(surah);
        surahName.set(QuranUtil.getSurahName(surah));
        EvaluationRepositoryOld.clearEvalData();
        //ayahNum.set(1);

        Timber.d("onActivityCreated");
    }

    public ServerStatusListener getServerStatusListener() {
        return serverStatusListener;
    }

    public MutableLiveData<QuranAyahAudio> getTranscribedAudioHolder() {
        return transcribedAudioHolder;
    }

    public void setServerStatus(String status) {
        serverStatus.set(status);
    }

    public void onClickRecord() {

        if (!isRecording.get()) {
            audio = new Recording(surahNum.get(), ayahNum.get());
            mNavigator.onStartRecording((Recording) audio);
            isRecording.set(true);
        } else {
            mNavigator.onStopRecording();
            isRecording.set(false);

            if (isEndOfSurah()) {
                mNavigator.onMurojaahFinished();
            } else {
                incrementAyah();
            }
        }
    }

    public void pollTranscriptionQueue() {

        if (numAvailableWorkers.get()>0) {
            Timber.d("idle worker:"+numAvailableWorkers.get()+" processing queue");
            transcriber.poll();
        } else {
            Timber.d("waiting for idle worker");
        }
    }

    public void evaluate(QuranAyahAudio audio) {
        Evaluation eval = evaluator.evaluate(audio);
        evalRepo.add(eval);
    }

    public void showHint() {
        surahName.set(QuranUtil.getSurahName(surahNum.get()));
        ayahText.set(QuranUtil.getAyah(surahNum.get(), ayahNum.get()));
        hintVisibility.set(View.VISIBLE);
        isHintRequested.set(true);
    }

//    public void recordAyah() {
//        if (isRecording.get()) {
//            finishRecording();
//        } else {
//            startRecording();
//        }
//    }

//    void startRecording() {
//        Timber.d("startRecording");
//        // todo: fix filename of recording. save file to cache directory
//        if (!isMockRecording) {
//            mNavigator.onStartRecording(new Recording(surahNum.get(), ayahNum.get()));
//        }
//        isRecording.set(true);
//    }

//    void finishRecording() {
//        Timber.d("finishRecording");
//
//        if (!isMockRecording) {
//            mNavigator.onStopRecording();
//        }
//
//        Recording recording = new Recording(surahNum.get(), ayahNum.get());
////        mAudioRepository.addRecording(recording);
//
//        //pollRecognitionQueue();
//        //Timber.d("recognitionTaskQueue size: " + mTranscriber.getQueueSize());
//        isRecording.set(false);
//
//        if (isEndOfSurah()) {
//            mNavigator.on

    public void cancelRecording() {
        if (!isMockRecording) {
            mNavigator.onStopRecording();
        }

        isRecording.set(false);
    }

//    public void playReference() {
//        if (!isPlaying.get()) {
//            myAudioPlayer.play(Uri.parse(getTestFilePath()));
//        } else {
//            myAudioPlayer.stop();
//        }
//        isPlaying.set(!isPlaying.get());
//    }

    private boolean isEndOfSurah() {
        return !QuranUtil.isValidAyahNum(surahNum.get(), ayahNum.get()+1);
    }

    private void incrementAyah() {
        ayahNum.set(ayahNum.get()+1);
        hintVisibility.set(View.INVISIBLE);
        isHintRequested.set(false);
    }

    public void deleteRecordingFiles() {

        File recordingDir = new File(AudioFileHelper.getUserRecordingPath());
        for (File file : recordingDir.listFiles()) {
            file.delete();
        }
    }


}
