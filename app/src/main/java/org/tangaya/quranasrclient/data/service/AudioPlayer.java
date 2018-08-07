package org.tangaya.quranasrclient.data.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class AudioPlayer extends MediaPlayer {

    public AudioPlayer() {
        setAudioStreamType(AudioManager.STREAM_MUSIC);

        setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                reset();
            }
        });
    }

    public void play(Uri fileUri) {
        try {
            reset();
            setDataSource(fileUri.getPath());
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

}
