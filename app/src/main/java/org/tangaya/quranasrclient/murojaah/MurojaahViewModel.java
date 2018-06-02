package org.tangaya.quranasrclient.murojaah;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tangaya.quranasrclient.data.Quran;
import org.tangaya.quranasrclient.service.TranscriberService;
import org.tangaya.quranasrclient.data.Transcription;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;

import java.io.IOException;

public class MurojaahViewModel extends AndroidViewModel
        implements TranscriptionsDataSource.GetTranscriptionCallback {

    public final ObservableField<String> transcriptionId = new ObservableField<>();
    public final ObservableField<String> transcriptionText = new ObservableField<>();
    public final ObservableField<Boolean> isCompleteTranscription = new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    public final ObservableField<Integer> currentSurahNum = new ObservableField<>();
    public final ObservableField<Integer> currentAyahNum = new ObservableField<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    int count_tap = 0;
    TranscriberService transcriber;
    MediaPlayer mediaPlayer;

    Context mContext;
    TranscriptionsRepository mTranscriptionsRepository;
    Uri audioFileUri;

    public MurojaahViewModel(@NonNull Application context,
                             @NonNull TranscriptionsRepository transcriptionsRepository) {
        super(context);

        mContext = context;
        mTranscriptionsRepository = transcriptionsRepository;
        serverStatus.set("tidak diketahui");


        transcriber = new TranscriberService();

        audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/testwaveee.wav");
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
        dataLoading.set(true);

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
        transcriber.startRecognize(audioFilePath);
    }

    public void showHint() {
        Log.d("cek surat", "surat="+currentSurahNum.get());
        Log.d("showHint", Quran.getSurah(currentSurahNum.get()).getAyah(currentAyahNum.get()));
        transcriptionText.set(Quran.getSurah(currentSurahNum.get()).getAyah(currentAyahNum.get()));
        currentAyahNum.set(currentAyahNum.get()+1);
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

}
