package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;

import java.util.LinkedList;

public class QuranTranscriber {

    private static QuranTranscriber INSTANCE = null;

    private LinkedList<RecognitionTask> recognitionTaskQueue = new LinkedList<>();
    private MutableLiveData<LinkedList<RecognitionTask>> recognitionTasksLiveData = new MutableLiveData<>();

    private LinkedList<QuranAyahAudio> audioList = new LinkedList<>();
    private MutableLiveData<LinkedList<QuranAyahAudio>> audioListLiveData = new MutableLiveData<>();

    private ServerStatusListener statusListener = null;

    private QuranTranscriber() {
        audioListLiveData.setValue(audioList);
        statusListener = ServerStatusListener.getInstance();
    }

    public static QuranTranscriber getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuranTranscriber();
        }
        return INSTANCE;
    }

    public void addToQueue(QuranAyahAudio audio) {
        audioListLiveData.getValue().add(audio);
    }

    public void processQueue() {
        RecognitionTaskNew recognitionTask = new RecognitionTaskNew(audioList.poll());
        recognitionTask.executeTask();
    }

    public int getQueueSize() {
        return recognitionTaskQueue.size();
    }

}
