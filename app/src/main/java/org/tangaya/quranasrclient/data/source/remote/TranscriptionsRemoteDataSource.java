package org.tangaya.quranasrclient.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.data.Transcription;
import org.tangaya.quranasrclient.data.service.Transcriber;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

public class TranscriptionsRemoteDataSource implements TranscriptionsDataSource {

    private static TranscriptionsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;

    private final static Map<String, Transcription> TRANSCRIPTION_SERVICE_DATA;

    static {
        TRANSCRIPTION_SERVICE_DATA = new LinkedHashMap<>(2);
        addTranscriptin("trans1", "Bismillah");
        addTranscriptin("trans2", "Bismillahirrahman");
        addTranscriptin("trans3", "Bismillahirrahmanirrahim");
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

    @Override
    public void performRecognition(@NonNull Recording recording, @NonNull PerformRecognitionCallback callback) {
        Transcriber transcriber = new Transcriber();

        Handler handler= new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        final Transcription transcription  = transcriber.getTranscription(recording);
        //callback.onTranscriptionLoaded(transcription);
    }

    private static void addTranscriptin(String id, String transStr) {
        Transcription newTranscription = new Transcription(id, transStr);
        TRANSCRIPTION_SERVICE_DATA.put(newTranscription.getId(), newTranscription);
    }
}
