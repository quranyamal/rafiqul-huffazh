package org.tangaya.quranasrclient.service.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.tangaya.quranasrclient.service.model.Transcription;
import org.tangaya.quranasrclient.service.source.TranscriptionsDataSource;

public class TranscriptionsRepository implements TranscriptionsDataSource {

    private static TranscriptionsRepository INSTANCE = null;

    private final TranscriptionsDataSource mTranscriptionsDataSource;


    private TranscriptionsRepository(@NonNull TranscriptionsDataSource transcriptionsDataSource) {
        mTranscriptionsDataSource = transcriptionsDataSource;
    }

    public static TranscriptionsRepository getInstance(TranscriptionsDataSource transcriptionsDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new TranscriptionsRepository(transcriptionsDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTranscription(@NonNull GetTranscriptionCallback callback) {
        Transcription mockTranscription = new Transcription("mock", "mock transcription", 1);
        callback.onTranscriptionLoaded(mockTranscription);
    }

    @Override
    public void getTranscription(@NonNull String transcription_id, @NonNull GetTranscriptionCallback callback) {
        Transcription cachedTranscription = getTranscriptionWithId(transcription_id);
    }

    @Nullable
    private Transcription getTranscriptionWithId(@NonNull String id) {
        return new Transcription("tr"+id, "from getTranscriptionWithId",  1);
    }
}
