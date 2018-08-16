package org.tangaya.rafiqulhuffazh.data.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import org.tangaya.rafiqulhuffazh.util.AudioFileHelper;

import java.io.IOException;

public class MyAudioPlayer extends MediaPlayer {

    static String RECORDING_AUDIO_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/recording/";

    static String QARI_AUDIO_PATH = Environment.getExternalStorageDirectory() +
            "/rafiqul-huffazh/test/";

    public enum Source {
        RECORDING,
        QARI1
    }

    public MyAudioPlayer() {
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

    public void play(Source src, int surah, int ayah) {
        try {
            reset();
            switch (src) {
                case RECORDING:
                    setDataSource(AudioFileHelper.getRecordingFilePath(surah, ayah));
                    break;
                case QARI1:
                    setDataSource(AudioFileHelper.getQari1FilePath(surah, ayah));
            }
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

}
