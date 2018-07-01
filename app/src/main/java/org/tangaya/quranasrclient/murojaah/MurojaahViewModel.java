package org.tangaya.quranasrclient.murojaah;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.service.AudioPlayer;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;

public class MurojaahViewModel extends AndroidViewModel
        implements TranscriptionsDataSource.PerformRecognitionCallback {

    public final ObservableField<String> ayahText = new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();

    public final ObservableField<Integer> chapterNum = new ObservableField<>();
    public final ObservableField<Integer> verseNum = new ObservableField<>();
    public final ObservableField<Integer> attemptState= new ObservableField<>();

    public final ObservableField<String> surahName = new ObservableField<>();
    public final ObservableField<String> verseIndicator = new ObservableField<>();
    public final ObservableField<String> hintText = new ObservableField<>();
    public final ObservableField<Integer> hintVisibility = new ObservableField<>();
    public final ObservableField<String> instructionText = new ObservableField<>();

    public static final int STATE_IDLE = 0;
    public static final int STATE_RECORDING = 1;
    public static final int STATE_RECOGNIZING = 2;

    private boolean isRecording;
    private boolean isHintRequested;
    private boolean isLastVerse;

    AudioPlayer audioPlayer;

    Context mContext;
    TranscriptionsRepository mTranscriptionsRepository;
    RecordingRepository mRecordingRepository;
    MurojaahNavigator mNavigator;

    Uri audioFileUri;


    public MurojaahViewModel(@NonNull Application context,
                             @NonNull TranscriptionsRepository transcriptionsRepository,
                             @NonNull RecordingRepository recordingRepository) {
        super(context);

        mContext = context;
        mTranscriptionsRepository = transcriptionsRepository;
        mRecordingRepository = recordingRepository;
        serverStatus.set("tidak diketahui");
        verseNum.set(1);
        attemptState.set(STATE_IDLE);

        //audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/testwaveee.wav");

        audioPlayer = new AudioPlayer();
        audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/001.wav");
    }

    void onActivityCreated(MurojaahNavigator navigator, int chapter) {
        mNavigator = navigator;
        chapterNum.set(chapter);
        //Quran.init(getApplication());
        QuranScriptRepository.init(getApplication());
        Log.d("MVM", "onActivityCreated. chapter="+chapter);
    }

    public void showHint() {
        Log.d("cek surat", "surat="+ chapterNum.get());
        Log.d("cek ayat", "ayat="+ verseNum.get());
        Log.d("showHint", QuranScriptRepository.getChapter(chapterNum.get()).getVerse(verseNum.get()));
        ayahText.set(QuranScriptRepository.getChapter(chapterNum.get()).getVerse(verseNum.get()));
        hintVisibility.set(View.VISIBLE);
    }

    public void createAttempt() {
        attemptState.set(STATE_RECORDING);
        mRecordingRepository.createRecording(chapterNum.get(), verseNum.get());
    }

    public void submitAttempt() {
        mRecordingRepository.saveRecording(new RecordingRepository.Callback() {
            @Override
            public void onSaveRecording() {
                // todo
            }
        });
        attemptState.set(STATE_RECOGNIZING);

        if (isEndOfSurah()) {
            mNavigator.gotoEvaluation();
        } else {
            incrementAyah();
        }
    }

    public void cancelAttempt() {
        attemptState.set(STATE_IDLE);
    }

    public void playAttemptRecording() {
        audioPlayer.play(audioFileUri);
    }

    public int getAttemptState() {
        return attemptState.get();
    }

    public boolean isEndOfSurah() {
        return !QuranScriptRepository.getChapter(chapterNum.get()).isValidVerseNum(verseNum.get()+1);
    }

    public void incrementAyah() {
        verseNum.set(verseNum.get()+1);
        hintVisibility.set(View.INVISIBLE);
    }

    @Override
    public void onRecognitionCompleted() {

    }

    @Override
    public void onRecognitionError() {

    }

}
