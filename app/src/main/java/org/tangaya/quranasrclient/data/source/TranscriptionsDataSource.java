package org.tangaya.quranasrclient.data.source;

import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.data.Transcription;

/**
 * Main entry point for accessing task data
 * Created by Amal Qurany on 5/25/2018
 */
public interface TranscriptionsDataSource {

    String TRANSCRIBER_ENDPOINT = "ws://192.168.0.217:8888/client/ws/status";

    interface PerformRecognitionCallback {

        void onRecognitionCompleted();

        void onRecognitionError();
    }

    interface GetTranscriptionCallback {

        void onTranscriptionLoaded(Transcription transcription);

        void onTranscriptionNotAvailable();
    }

    void getTranscription(@NonNull GetTranscriptionCallback callback);

    void getTranscription(@NonNull String transcription_id,
                         @NonNull GetTranscriptionCallback callback);

    void performRecognition(@NonNull Recording recording,
                          @NonNull PerformRecognitionCallback callback);

}
