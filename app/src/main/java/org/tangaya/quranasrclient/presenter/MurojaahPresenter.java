package org.tangaya.quranasrclient.presenter;

import android.content.Context;

import org.tangaya.quranasrclient.domain.model.Transcription;
import org.tangaya.quranasrclient.view.BaseView;

public interface  MurojaahPresenter extends BasePresenter {
    /* Implemented by View, needed by Presenter */
    interface View extends BaseView {
        void onWorkerAvailable();
        void onWorkerNotAvailable();
        void onDateSet(String date);
        void startOnlineDecodingActivity(String transcriptionId);
        void notifyUploadStart();
    }

    /* Presenter's methods, Implemented by Presenter, needed by View */
    void showDatePicker(Context ctx);
    void prepareOnlineDecoding(Transcription transcription);
    void startOfflineDecoding(Transcription transcription);
}
