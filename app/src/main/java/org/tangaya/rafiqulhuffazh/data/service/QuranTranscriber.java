package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.MutableLiveData;

import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;

import java.util.LinkedList;

import timber.log.Timber;

public class QuranTranscriber {

    private static QuranTranscriber INSTANCE = null;

    private LinkedList<QuranAyahAudio> audioList = new LinkedList<>();
    private MutableLiveData<LinkedList<QuranAyahAudio>> transcriptionQueueLiveData = new MutableLiveData<>();

    public MutableLiveData<QuranAyahAudio> transcribedAudioHolder;

    private QuranTranscriber(MutableLiveData<QuranAyahAudio> transcribedAudioHolder) {
        transcriptionQueueLiveData.setValue(audioList);
        this.transcribedAudioHolder = transcribedAudioHolder;
    }

    public static QuranTranscriber getInstance(MutableLiveData<QuranAyahAudio> transcribedAudioHolder) {
        if (INSTANCE == null) {
            INSTANCE = new QuranTranscriber(transcribedAudioHolder);
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
            TranscriptionTask transcriptionTask = new TranscriptionTask(audioList.poll(), transcribedAudioHolder);
            transcriptionTask.transcribe();
        } else {
            Timber.d("queue empty. do nothing");
        }
    }
}
