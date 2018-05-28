package org.tangaya.quranasrclient.murojaah;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import org.tangaya.quranasrclient.service.model.Transcription;
import org.tangaya.quranasrclient.service.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.service.repository.TranscriptionsRepository;

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
        transcriptionText.set(transcription.getTranscriptioText());
        isCompleteTranscription.set(transcription.isComplete());
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
}
