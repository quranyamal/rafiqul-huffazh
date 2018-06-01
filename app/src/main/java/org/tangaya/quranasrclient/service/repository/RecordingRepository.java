package org.tangaya.quranasrclient.service.repository;

public interface RecordingRepository {

    interface Callback {
        void onReadResult(byte[] audioByte);
    }

    void startRecording(Callback callback);
    void stopRecording();

}
