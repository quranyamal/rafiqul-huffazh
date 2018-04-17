package org.tangaya.quranasrclient.domain;

/**
 * Created by Rahman Adianto on 23-Apr-17.
 */

public interface GetTranscriptionIdInteractor extends Interactor {

    interface Callback {
        void onTranscriptionIdResult(String transcriptionId);
        void onTranscriptionIdError();
    }
}