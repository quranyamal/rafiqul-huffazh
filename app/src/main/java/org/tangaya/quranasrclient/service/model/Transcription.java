package org.tangaya.quranasrclient.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Immutable model class for transcription
 * Created by Amal Qurany on 5/23/2018
 */


@Entity(tableName = "transcription")
public final class Transcription {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "transcription_id")
    private final String mId;

    @Nullable
    private final String mTranscriptionText;

    @Nullable
    private final int mComplete;

    public Transcription(String id,
                         @Nullable String transStr,
                         @Nullable int isComplete) {
        mId = id;
        mTranscriptionText = transStr;
        mComplete = isComplete;
    }

    public String getId() {
        return mId;
    }

    public String getTranscriptioText() {
        return mTranscriptionText;
    }

    public boolean isComplete() {
        return mComplete==1;
    }
}
