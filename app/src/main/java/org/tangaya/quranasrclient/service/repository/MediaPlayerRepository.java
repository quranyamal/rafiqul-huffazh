package org.tangaya.quranasrclient.service.repository;

/**
 * Created by Rahman Adianto on 27-Apr-17.
 */

public interface MediaPlayerRepository {

    interface Callback {
        void onCompleted();
    }

    int prepare();
    void start();
    void start(Callback callback);
    void pause();
    void pauseWithoutSavePosition();
    void seekTo(int msec);
    void stop();
    boolean isPlaying();
    int getCurrentPosition();
    int getLastPosition();
    void release();
    boolean isPathOk();
    String getPath();
}