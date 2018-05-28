package org.tangaya.quranasrclient.service;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.util.Log;

import org.tangaya.quranasrclient.service.repository.RecordingRepository;
import org.tangaya.quranasrclient.util.AudioFileHelper;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by Rahman Adianto on 11-Apr-17.
 */

public class AudioRecorder implements RecordingRepository {

    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    private AudioRecord mRecorder;
    private AudioFileHelper mOutput;
    private boolean isRecording;
    private int bufferSize;
    private String mFilename;

    public AudioRecorder(String filename) {

        isRecording = false;
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNELS, AUDIO_ENCODING);
        mFilename = filename;
    }

    @Override
    public void startRecording(Callback callback) {
        Log.d("ImtihanActivity", "recording...");

        isRecording = true;

        mRecorder = new AudioRecord(
                AUDIO_SOURCE,
                SAMPLE_RATE,
                CHANNELS,
                AUDIO_ENCODING,
                bufferSize
        );

        // Initialized raw buffer file
        mOutput = new AudioFileHelper(mFilename, SAMPLE_RATE);

        mRecorder.startRecording();

        // Add Noise Suppressor
        if (Build.VERSION.SDK_INT > 15) {
            if (NoiseSuppressor.isAvailable()) {
                NoiseSuppressor noiseSuppressor =
                        NoiseSuppressor.create(mRecorder.getAudioSessionId());
                noiseSuppressor.setEnabled(true);
            }
        }

        // Audio will read every bufferSize / 2 bytes
        // Conversion short to byte: 1 short => 2 byte
        short audioData[] = new short[SAMPLE_RATE / 4];

        while (isRecording) {
            mRecorder.read(audioData, 0, SAMPLE_RATE / 4); // Blocking method
            byte[] result = shortToByte(audioData);
            callback.onReadResult(result);
            mOutput.pushToFile(result);
        }
    }

    @Override
    public void stopRecording() {
        Log.d("ImtihanActivity", "stop recording");

        mOutput.close();
        try {
            mOutput.rawToWave16Mono();
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
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

        byte [] buffer = new byte[data.length * 2];

        short_index = byte_index = 0;

        for(/*NOP*/; short_index != iterations; /*NOP*/)
        {
            buffer[byte_index]     = (byte) (data[short_index] & 0x00FF);
            buffer[byte_index + 1] = (byte) ((data[short_index] & 0xFF00) >> 8);

            ++short_index;
            byte_index += 2;
        }

        return buffer;
    }
}
