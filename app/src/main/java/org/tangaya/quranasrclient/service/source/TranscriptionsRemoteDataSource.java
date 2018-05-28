package org.tangaya.quranasrclient.service.source;

import android.os.Handler;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.service.model.Transcription;

import java.util.LinkedHashMap;
import java.util.Map;

public class TranscriptionsRemoteDataSource implements TranscriptionsDataSource {

    private static TranscriptionsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;

    private final static Map<String, Transcription> TRANSCRIPTION_SERVICE_DATA;

    static {
        TRANSCRIPTION_SERVICE_DATA = new LinkedHashMap<>(2);
        addTranscriptin("trans1", "Bismillah", 0);
        addTranscriptin("trans2", "Bismillahirrahman", 0);
        addTranscriptin("trans3", "Bismillahirrahmanirrahim", 1);
    }

    public static TranscriptionsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranscriptionsRemoteDataSource();
        }
        return INSTANCE;
    }

    private TranscriptionsRemoteDataSource(){}


    @Override
    public void getTranscription(@NonNull GetTranscriptionCallback callback) {
        getTranscription("mock getTranscription", callback);
    }

    @Override
    public void getTranscription(@NonNull String transcription_id, @NonNull final GetTranscriptionCallback callback) {
        final Transcription transcription = TRANSCRIPTION_SERVICE_DATA.get(transcription_id);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTranscriptionLoaded(transcription);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    private static void addTranscriptin(String id, String transStr, int isFinal) {
        Transcription newTranscription = new Transcription(id, transStr, isFinal);
        TRANSCRIPTION_SERVICE_DATA.put(newTranscription.getId(), newTranscription);
    }
}
