package org.tangaya.quranasrclient.murojaah;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tangaya.quranasrclient.service.TranscriberService;
import org.tangaya.quranasrclient.service.model.Transcription;
import org.tangaya.quranasrclient.service.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.service.repository.TranscriptionsRepository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MurojaahViewModel extends AndroidViewModel
        implements TranscriptionsDataSource.GetTranscriptionCallback {

    public final ObservableField<String> transcriptionId = new ObservableField<>();
    public final ObservableField<String> transcriptionText = new ObservableField<>();
    public final ObservableField<Boolean> isCompleteTranscription = new ObservableField<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    Context mContext;
    TranscriptionsRepository mTranscriptionsRepository;

    public MurojaahViewModel(@NonNull Application context,
                             @NonNull TranscriptionsRepository transcriptionsRepository) {
        super(context);

        mContext = context;
        mTranscriptionsRepository = transcriptionsRepository;
    }


    @Override
    public void onTranscriptionLoaded(Transcription transcription) {
        transcriptionId.set(transcription.getId());
        transcriptionText.set(transcription.getText());
    }

    @Override
    public void onTranscriptionNotAvailable() {

    }

    public void start() {
        loadTranscriptions();
    }

    public void loadTranscriptions() {
        dataLoading.set(true);

        mTranscriptionsRepository.getTranscription(new TranscriptionsDataSource.GetTranscriptionCallback() {

            @Override
            public void onTranscriptionLoaded(Transcription transcription) {
                Log.d("Murojaah VM", "onTranscriptionLoaded");
            }

            @Override
            public void onTranscriptionNotAvailable() {
                Log.d("Murojaah VM", "onTranscriptionNA");
            }

        });
    }

    void recognize() {
        Log.d("MurojaahViewModel", "recognizing...");
        //String audioFilePath = "/storage/emulated/0/DCIM/bismillah.wav";
        String audioFilePath = "/storage/emulated/0/DCIM/100-1.wav";

        TranscriberService transcriber = new TranscriberService();
        transcriber.startRecognize(audioFilePath);
    }

}
