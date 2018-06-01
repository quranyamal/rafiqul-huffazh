package org.tangaya.quranasrclient.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import org.tangaya.quranasrclient.service.repository.MediaPlayerRepository;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

/**
 * Created by Rahman Adianto on 27-Apr-17.
 */

public class AudioPlayer implements MediaPlayerRepository {

    private MediaPlayer mPlayer;
    private int lastPosition;
    private String mPath;
    private boolean mPathOk;

    public AudioPlayer(Context context, String path) {

        Uri uri = Uri.fromFile(new File(path.replace("\\ ", " ")));
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPath = path;
        try {
            Timber.d(uri.toString());
            mPlayer.setDataSource(context, uri);
            Timber.d("Audio Player Initialized");
            mPathOk = true;
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
            mPathOk = false;
        }
    }

    @Override
    public boolean isPathOk() {
        return mPathOk;
    }

    @Override
    public String getPath() {
        return mPath;
    }

    @Override
    public int prepare() {

        try {
            mPlayer.prepare();
            Timber.d("Audio Player Prepared");
            return mPlayer.getDuration();
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
        }
        return 0;
    }

    @Override
    public void start() {

        mPlayer.start();
        Timber.d("Audio Player Started");
    }

    @Override
    public void start(final Callback callback) {

        mPlayer.start();
        Timber.d("Audio Player Started");
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Timber.d("Audio Player Completed");
                callback.onCompleted();
            }
        });
    }

    @Override
    public void pause() {

        lastPosition = getCurrentPosition();
        pauseWithoutSavePosition();
    }

    @Override
    public void pauseWithoutSavePosition() {

        mPlayer.pause();
        Timber.d("Audio Player Paused");
    }

    @Override
    public void seekTo(int msec) {

        mPlayer.seekTo(msec);
        Timber.d(String.format("Audio Player seekTo %d", msec));
    }

    @Override
    public void stop() {

        if (mPlayer != null) {
            mPlayer.stop();
            Timber.d("Audio Player Stop");
        }
    }

    @Override
    public boolean isPlaying() {

        return mPlayer.isPlaying();
    }

    @Override
    public int getCurrentPosition() {

        if (isPlaying()) {
            return mPlayer.getCurrentPosition();
        }

        return 0;
    }

    @Override
    public int getLastPosition() {

        return lastPosition;
    }

    @Override
    public void release() {

        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
            Timber.d("Audio Player Released");
        }
    }
}