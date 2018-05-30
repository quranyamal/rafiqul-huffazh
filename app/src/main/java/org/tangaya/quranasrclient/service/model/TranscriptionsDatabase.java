package org.tangaya.quranasrclient.service.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Transcription.class}, version = 1)
public abstract class TranscriptionsDatabase extends RoomDatabase {

    private static TranscriptionsDatabase INSTANCE;

    public abstract TranscriptionsDao transcriptionsDao();

    private static final Object sLock = new Object();

    public static TranscriptionsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TranscriptionsDatabase.class, "Transcriptions.db").build();
            }
            return INSTANCE;
        }
    }
}
