package org.tangaya.quranasrclient.domain;

/**
 * Created by Rahman Adianto on 22-Apr-17.
 */

public interface UploadAudioInteractor extends Interactor {

    interface Callback {
        void onUploadStart();
    }
}
