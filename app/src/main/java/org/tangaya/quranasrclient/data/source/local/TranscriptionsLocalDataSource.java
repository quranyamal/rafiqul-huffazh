package org.tangaya.quranasrclient.data.source.local;

import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.data.Transcription;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.util.AppExecutors;

public class TranscriptionsLocalDataSource implements TranscriptionsDataSource {

    private static volatile TranscriptionsLocalDataSource INSTANCE;

    private TranscriptionsDao mTranscriptionsDao;

    private AppExecutors mAppExecutors;

    private TranscriptionsLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull TranscriptionsDao transcriptionsDao) {
        mAppExecutors = appExecutors;
        mTranscriptionsDao = transcriptionsDao;
    }

    public static TranscriptionsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                            @NonNull TranscriptionsDao trasncDao) {
        if (INSTANCE==null) {
            synchronized (TranscriptionsLocalDataSource.class) {
                if (INSTANCE==null) {
                    INSTANCE = new TranscriptionsLocalDataSource(appExecutors, trasncDao);
                }
            }
        }
        return INSTANCE;
    }



    @Override
    public void getTranscription(@NonNull GetTranscriptionCallback callback) {

    }

    @Override
    public void getTranscription(@NonNull final String transc_id, @NonNull final GetTranscriptionCallback callback) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Transcription transcription = mTranscriptionsDao.getTranscriptionById(transc_id);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (transcription != null) {
                            callback.onTranscriptionLoaded(transcription);
                        } else {
                            callback.onTranscriptionNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }
}
