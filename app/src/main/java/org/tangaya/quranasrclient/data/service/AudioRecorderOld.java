package org.tangaya.quranasrclient.data.service;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.util.Log;

import org.tangaya.quranasrclient.util.AppExecutors;
import org.tangaya.quranasrclient.util.AudioFileHelper;

import java.io.IOException;

public class AudioRecorderOld {

    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int SOURCE = MediaRecorder.AudioSource.MIC;

    private String mFilename = "tmp.wav";
    private boolean isRecording;
    private int bufferSize;

    private AudioRecord mRecorder;
    private AudioFileHelper mOutput;

    AppExecutors mAppExecutors = new AppExecutors();

    public AudioRecorderOld() {
        isRecording = false;
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, ENCODING);
    }

    public void startRecording() {
        isRecording = true;

        mRecorder = new AudioRecord(
                SOURCE,
                SAMPLE_RATE,
                CHANNEL,
                ENCODING,
                bufferSize
        );

        mOutput = new AudioFileHelper(mFilename, SAMPLE_RATE);

        mRecorder.startRecording();

        // disini ada noise suppresor
        if (Build.VERSION.SDK_INT > 15) {
            if (NoiseSuppressor.isAvailable()) {
                NoiseSuppressor noiseSuppressor = NoiseSuppressor.create(mRecorder.getAudioSessionId());
                noiseSuppressor.setEnabled(true);
            }
        }

        Runnable recordingRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("AR", "start of run()");
                while (isRecording) {
                    Log.d("AR","inside run isRecording()");
                    short audioData[] = new short[SAMPLE_RATE/4];
                    mRecorder.read(audioData, 0, SAMPLE_RATE/4);
                    byte[] result = shortToByte(audioData);
                    //callback.onReadResult(result);
                    if (isRecording)
                        mOutput.pushToFile(result);
                }
                Log.d("AR", "end of run()");
            }
        };

        mAppExecutors.diskIO().execute(recordingRunnable);
    }

    public void stopRecording() {
        isRecording = false;
        mOutput.close();
        try {
            mOutput.rawToWave16Mono();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mOutput.clearRaw();
        mOutput = null;

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private byte[] shortToByte(short[] data) {
        int short_index, byte_index;
        int iterations = data.length;

        byte[] buffer = new byte[data.length * 2];

        short_index = byte_index = 0;

        while (short_index!=iterations) {
            buffer[byte_index]      = (byte) (data[short_index] & 0x00FF);
            buffer[byte_index+1]    = (byte) (data[short_index] & 0x00FF >> 8);

            ++short_index;
            byte_index += 2;
        }

        return buffer;
    }

    public String getState() {
        return isRecording ? "RECORDING":"STOPPED";
    }
}
