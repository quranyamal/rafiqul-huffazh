package org.tangaya.quranasrclient.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class AudioPlayer extends MediaPlayer {

    public AudioPlayer() {
        setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void play(Uri fileUri) {
        try {
            setDataSource(fileUri.getPath());
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

}
