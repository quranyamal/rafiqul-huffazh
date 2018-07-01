package org.tangaya.quranasrclient.data.source;

import android.os.Environment;
import android.util.Log;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

public class RecordingRepository {

    WavAudioRecorder mRecorder;

    String recordingPath = Environment.getExternalStorageDirectory().toString();

    public interface Callback {

        void onSaveRecording();
    }

    public RecordingRepository() {
        mRecorder = WavAudioRecorder.getInstance();

        String mRecordFilePath = Environment.getExternalStorageDirectory() + "/testwaveeeNew.wav";
    }

    public void createRecording(int surahNum, int ayahNum) {
        Recording recording = new Recording(surahNum, ayahNum);
        startRecording(recording);
    }

    public void startRecording(Recording recording) {

        Log.d("RR", "recording is starting");
        String filePath = recordingPath + "/recording"+recording.getChapter()+"-"+recording.getVerse()+".wav";

        mRecorder.setOutputFile(filePath);
        if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
            mRecorder.prepare();
            mRecorder.start();
        } else {
            Log.e("RR", "something wrong in performRecording");
        }
    }

    public void saveRecording(Callback callback) {
        mRecorder.stop();
        mRecorder.reset();

        callback.onSaveRecording();
    }

    public Recording getRecording(int recordingId) {
        return null;
    }

    public void addRecording(Recording recording) {

    }

}
