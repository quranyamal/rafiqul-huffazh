package org.tangaya.quranasrclient.murojaah;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tangaya.quranasrclient.data.Quran;
import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.data.service.TranscriberOld;
import org.tangaya.quranasrclient.data.Transcription;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;

import java.io.IOException;

public class MurojaahViewModel extends AndroidViewModel
        implements TranscriptionsDataSource.GetTranscriptionCallback,
        TranscriptionsDataSource.PerformRecognitionCallback {

    public final ObservableField<String> transcriptionId = new ObservableField<>();
    public final ObservableField<String> transcriptionText = new ObservableField<>();
    public final ObservableField<Boolean> isCompleteTranscription = new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    public final ObservableField<Integer> currentSurahNum = new ObservableField<>();
    public final ObservableField<Integer> currentAyahNum = new ObservableField<>();

    public static final int STATE_IDLE = 0;
    public static final int STATE_RECORDING = 1;
    public static final int STATE_RECOGNIZING = 2;

    public final ObservableField<Integer> attemptState= new ObservableField<>();

    TranscriberOld transcriberOld;
    MediaPlayer mediaPlayer;

    Context mContext;
    TranscriptionsRepository mTranscriptionsRepository;
    RecordingRepository mRecordingRepository;
    MurojaahNavigator mNavigator;

    Uri audioFileUri;

    public MurojaahViewModel(@NonNull Application context,
                             @NonNull TranscriptionsRepository transcriptionsRepository,
                             @NonNull RecordingRepository recordingRepository) {
        super(context);

        mContext = context;
        mTranscriptionsRepository = transcriptionsRepository;
        mRecordingRepository = recordingRepository;
        serverStatus.set("tidak diketahui");
        currentAyahNum.set(0);
        attemptState.set(STATE_IDLE);

        transcriberOld = new TranscriberOld();
        //audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/testwaveee.wav");
        audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/001.wav");
    }

    void onActivityCreated(MurojaahNavigator navigator) {
        mNavigator = navigator;
    }

    @Override
    public void onTranscriptionLoaded(Transcription transcription) {
        Log.d("VM", "onTranscriptionLoaded invoked");
        transcriptionId.set(transcription.getId());
        transcriptionText.set(transcription.getText());
    }

    @Override
    public void onTranscriptionNotAvailable() {
        Log.d("VM", "onTranscriptionNotAvailable invoked");
    }

    public void start() {
        loadTranscriptions();
    }

    public void loadTranscriptions() {

        mTranscriptionsRepository.getTranscription(new TranscriptionsDataSource.GetTranscriptionCallback() {

            @Override
            public void onTranscriptionLoaded(Transcription transcription) {
                Log.d("Murojaah VM", "loadTranscriptions onTranscriptionLoaded");
            }

            @Override
            public void onTranscriptionNotAvailable() {
                Log.d("Murojaah VM", "loadTranscriptions onTranscriptionNA");
            }
        });
    }

    void recognize() {
        Log.d("MurojaahViewModel", "recognizing...");
        //String audioFilePath = "/storage/emulated/0/DCIM/bismillah.wav";
        String audioFilePath = "/storage/emulated/0/DCIM/100-1.wav";
        transcriberOld.startRecognize(audioFilePath);
    }

    public void showHint() {
        Log.d("cek surat", "surat="+currentSurahNum.get());
        Log.d("showHint", Quran.getSurah(currentSurahNum.get()).getAyah(currentAyahNum.get()));
        transcriptionText.set(Quran.getSurah(currentSurahNum.get()).getAyah(currentAyahNum.get()));
    }

    public void createAttempt() {
        attemptState.set(STATE_RECORDING);
        Recording newRecording = new Recording(currentSurahNum.get(),currentAyahNum.get());
        mRecordingRepository.performRecording(newRecording);
        mRecordingRepository.addRecording(newRecording);
    }

    public void submitAttempt() {
        mRecordingRepository.stopRecording();
        attemptState.set(STATE_RECOGNIZING);

        if (isEndOfSurah()) {
            mNavigator.gotoEvaluation();
        } else {
            incrementAyah();
        }
    }

    public void cancelAttempt() {
        attemptState.set(STATE_IDLE);
    }

    public void playRecordedAudio() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplication(), audioFileUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void testDecode() {
        recognize();
    }

    public int getAttemptState() {
        return attemptState.get();
    }

    public boolean isEndOfSurah() {
        return !Quran.getSurah(currentSurahNum.get()).isValidAyahNum(currentAyahNum.get()+1);
    }

    public void incrementAyah() {
        currentAyahNum.set(currentAyahNum.get()+1);
    }

    @Override
    public void onRecognitionCompleted() {

    }

    @Override
    public void onRecognitionError() {

    }
}
