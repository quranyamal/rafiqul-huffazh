package org.tangaya.quranasrclient;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    private final TranscriptionsRepository mTranscriptionRepository;

    public static ViewModelFactory getInstance(Application application) {

        TranscriptionsRepository repo = new TranscriptionsRepository();

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, repo);
                }
            }
        }
        return INSTANCE;
    }

    public static ViewModelFactory getInstance(Application application, TranscriptionsRepository repo) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, repo);
                }
            }
        }
        return INSTANCE;
    }

    private ViewModelFactory(Application application, TranscriptionsRepository repository) {
        mApplication = application;
        mTranscriptionRepository = repository;
    }



}
