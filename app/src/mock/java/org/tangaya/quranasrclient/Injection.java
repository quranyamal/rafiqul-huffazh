package org.tangaya.quranasrclient;

import android.content.Context;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.service.model.TranscriptionsDatabase;
import org.tangaya.quranasrclient.service.repository.TranscriptionsRepository;
import org.tangaya.quranasrclient.service.source.FakeTranscriptionsRemoteDataSource;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Injection {

    public static TranscriptionsRepository provideTranscriptionRepository(@NonNull Context context) {
        checkNotNull(context);
        TranscriptionsDatabase database = TranscriptionsDatabase.getInstance(context);
        return null;
    }

}
