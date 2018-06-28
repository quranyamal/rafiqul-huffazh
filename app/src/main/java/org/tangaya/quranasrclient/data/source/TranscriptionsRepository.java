package org.tangaya.quranasrclient.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.data.Transcription;
import org.tangaya.quranasrclient.service.Transcriber;

public class TranscriptionsRepository implements TranscriptionsDataSource {

    private TranscriptionsDataSource mTranscriptionsDataSource;

    public TranscriptionsRepository() {
        initTranscriptionDataSource();
    }

    void initTranscriptionDataSource() {
            mTranscriptionsDataSource = new TranscriptionsDataSource() {
                        @Override
                        public void getTranscription(@NonNull GetTranscriptionCallback callback) {
                            // todo
                        }

                        @Override
                        public void getTranscription(@NonNull String transcription_id, @NonNull GetTranscriptionCallback callback) {
                            // todo
                        }

                        @Override
                        public void performRecognition(@NonNull Recording recording, @NonNull PerformRecognitionCallback callback) {
                            Transcriber transcriber = new Transcriber();

                        }
                    };
        }

    @Override
    public void getTranscription(@NonNull GetTranscriptionCallback callback) {
        // exec transcription service here
        Transcription mockTranscription = new Transcription("mock", "mock transcription");
        callback.onTranscriptionLoaded(mockTranscription);
    }

    @Override
    public void getTranscription(@NonNull String transcription_id, @NonNull GetTranscriptionCallback callback) {
        Transcription cachedTranscription = getTranscriptionWithId(transcription_id);
    }

    @Override
    public void performRecognition(@NonNull Recording recording, @NonNull PerformRecognitionCallback callback) {
        mTranscriptionsDataSource.performRecognition(recording, callback);

        callback.onRecognitionCompleted();

    }

    @Nullable
    private Transcription getTranscriptionWithId(@NonNull String id) {
        return new Transcription("tr"+id, "from getTranscriptionWithId");
    }
}
