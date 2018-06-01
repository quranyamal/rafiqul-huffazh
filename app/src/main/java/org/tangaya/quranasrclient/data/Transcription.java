package org.tangaya.quranasrclient.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Immutable model class for transcription
 * Created by Amal Qurany on 5/23/2018
 */


@Entity(tableName = "transcriptions")
public final class Transcription {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "transcription_id")
    public String mId;

    @Nullable
    @ColumnInfo(name = "transcription_text")
    public String mText;

    @Ignore
    public Transcription(String id,
                         @Nullable String transStr) {
        mId = id;
        mText = transStr;
    }

    public Transcription(){
        mId = "0";
        mText = "not-set";
    }

    @Nullable
    public String getId() {
        return mId;
    }

    @Nullable
    public String getText() {
        return mText;
    }

}
