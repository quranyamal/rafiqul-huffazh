package org.tangaya.quranasrclient.data.source;

import android.os.Environment;
import android.util.Log;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.data.service.WavAudioRecorder;

public class RecordingRepository {

    WavAudioRecorder mRecorder;

    String recordingPath = Environment.getExternalStorageDirectory().toString();

    //public interface Callback {
        //void onReadResult(byte[] audioByte);

        //void onFinishRecording();
    //}

    public RecordingRepository() {
        mRecorder = WavAudioRecorder.getInstance();

        String mRecordFilePath = Environment.getExternalStorageDirectory() + "/testwaveeeNew.wav";
    }

    public void createRecording(int surahNum, int ayahNum) {
        Recording recording = new Recording(surahNum, ayahNum);
    }

    public void performRecording(Recording recording) {
        Log.d("RR", "recording is starting");
        String filePath = recordingPath + "/001.wav";

        mRecorder.setOutputFile(filePath);
        if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
            mRecorder.prepare();
            mRecorder.start();
        } else {
            Log.e("RR", "something wrong in performRecording");
        }
    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.reset();
    }

    public Recording getRecording(int recordingId) {
        return null;
    }

    public void addRecording(Recording recording) {

    }

}
