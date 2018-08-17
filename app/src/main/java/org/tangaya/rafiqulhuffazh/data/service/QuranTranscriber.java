package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.MyApplication;

import java.util.LinkedList;

public class QuranTranscriber {

    private static QuranTranscriber INSTANCE = null;

    private LinkedList<RecognitionTask> recognitionTaskQueue = new LinkedList<>();

    private MutableLiveData<LinkedList<RecognitionTask>> recognitionTasksLiveData = new MutableLiveData<>();

//    String hostname = ((MyApplication) getApplication()).getServerHostname();
//    String port = ((MyApplication) getApplication()).getServerPort();
//    String endpoint = ((MyApplication) getApplication()).getRecognitionEndpoint();

//    ASRServerStatusListener statusListener = new ASRServerStatusListener(hostname, port);


    private QuranTranscriber() {}

    public static QuranTranscriber getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuranTranscriber();
        }
        return INSTANCE;
    }

    public void addToQueue(RecognitionTask recognitionTask) {
        recognitionTaskQueue.add(recognitionTask);
    }

    public void processQueue() {
        RecognitionTask recognitionTask = recognitionTaskQueue.poll();
        recognitionTask.execute();
    }

    public int getQueueSize() {
        return recognitionTaskQueue.size();
    }

}
