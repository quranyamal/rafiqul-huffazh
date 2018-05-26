package org.tangaya.quranasrclient.data.source;

import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.data.Transcription;

/**
 * Main entry point for accessing task data
 * Created by Amal Qurany on 5/25/2018
 */
public interface TranscriptionsDataSource {

    interface GetTranscriptionCallback {

        void onTranscriptionLoaded(Transcription transcription);

        void onTranscriptionNotAvailable();
    }

    void getTranscription(@NonNull GetTranscriptionCallback callback);

    void getTranscription(@NonNull String transcription_id,
                         @NonNull GetTranscriptionCallback callback);

}
