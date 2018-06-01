package org.tangaya.quranasrclient.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.tangaya.quranasrclient.data.Transcription;

@Dao
public interface TranscriptionsDao {

    @Query("SELECT * FROM Transcriptions WHERE transcription_id = :transcriptionId")
    Transcription getTranscriptionById(String transcriptionId);

}
