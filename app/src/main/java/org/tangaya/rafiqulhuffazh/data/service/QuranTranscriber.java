package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;

import java.util.LinkedList;

import timber.log.Timber;

public class QuranTranscriber {

    private static QuranTranscriber INSTANCE = null;

    private LinkedList<QuranAyahAudio> audioList = new LinkedList<>();
    private MutableLiveData<LinkedList<QuranAyahAudio>> transcriptionQueueLiveData = new MutableLiveData<>();

    private QuranTranscriber() {
        transcriptionQueueLiveData.setValue(audioList);
    }

    public static QuranTranscriber getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuranTranscriber();
        }
        return INSTANCE;
    }

    public void addToQueue(QuranAyahAudio audio) {
        audioList.add(audio);
        transcriptionQueueLiveData.setValue(audioList);
    }

    public void poll() {
        if (audioList.size()!=0) {
            Timber.d("transcribing audio");
            RecognitionTaskNew recognitionTask = new RecognitionTaskNew(audioList.poll());
            recognitionTask.executeTask();
        } else {
            Timber.d("queue empty. do nothing");
        }
    }

}
