package org.tangaya.quranasrclient.domain;

import org.tangaya.quranasrclient.domain.model.Transcription;

/**
 * Created by Rahman Adianto on 22-Apr-17.
 */

public interface DecoderRepository {

    String requestTranscriptionId(Transcription data);
    boolean saveFCMToken(String fcm);
    boolean uploadAudio(Transcription transcription);
}

