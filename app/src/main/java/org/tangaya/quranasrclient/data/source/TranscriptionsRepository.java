package org.tangaya.quranasrclient.data.source;

import android.util.Log;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.service.Transcriber;
import org.tangaya.quranasrclient.service.TranscriberOld;

public class TranscriptionsRepository {

    TranscriberOld transcriberOld = new TranscriberOld();
    private TranscriptionsDataSource mTranscriptionsDataSource;

    public TranscriptionsRepository() {
        mTranscriptionsDataSource = TranscriptionsDataSource.getInstance();
    }

    public void requestTranscription(Recording recording) {

            Log.d("MurojaahViewModel", "recognizing...");
            String audioFilePath = "/storage/emulated/0/DCIM/100-1.wav";
            transcriberOld.startRecognize(audioFilePath);
    }

    public String getTranscription(int chapter, int verse) {

        return "transkripsi";
    }

}
