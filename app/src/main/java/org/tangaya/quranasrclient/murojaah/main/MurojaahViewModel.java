package org.tangaya.quranasrclient.murojaah.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
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
    public final ObservableField<String> chapterName = new ObservableField<>();
    public final ObservableField<Integer> verseNum = new ObservableField<>();
    public final ObservableField<Integer> attemptState= new ObservableField<>();

    public final ObservableField<String> hintText = new ObservableField<>();
    public final ObservableField<Integer> hintVisibility = new ObservableField<>();
    public final ObservableField<String> instructionText = new ObservableField<>();

    // todo: change to observable boolean
    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableBoolean isHintRequested = new ObservableBoolean();

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

        //audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/testwaveee.wav");

        audioPlayer = new AudioPlayer();
        audioFileUri = Uri.parse(Environment.getExternalStorageDirectory() + "/001.wav");

        isRecording.set(false);
        isHintRequested.set(false);
    }

    void onActivityCreated(MurojaahNavigator navigator, int chapter) {
        mNavigator = navigator;
        chapterNum.set(chapter);
//        QuranScriptRepository.init(getApplication());
        chapterName.set(QuranScriptRepository.getChapter(chapter).getTitle());
        Log.d("MVM", "chapterNum = " + chapterNum.get());
        Log.d("MVM", "chapterName = " + chapterName.get());
        Log.d("MVM", "chaper = " + QuranScriptRepository.getChapter(chapter).getTitle());

    }

    public void showHint() {
        Log.d("cek surat", "surat="+ chapterNum.get());
        Log.d("cek ayat", "ayat="+ verseNum.get());
        Log.d("showHint", QuranScriptRepository.getChapter(chapterNum.get()).getVerse(verseNum.get()));

        chapterName.set(QuranScriptRepository.getChapter(chapterNum.get()).getTitle());
        Log.d("MVM", "chapterName = " + chapterName.get());

        ayahText.set(QuranScriptRepository.getChapter(chapterNum.get()).getVerse(verseNum.get()));
        hintVisibility.set(View.VISIBLE);
        isHintRequested.set(true);
    }

    public void attemptVerse() {
        if (isRecording.get()) {
            submitAttempt();
        } else {
            createAttempt();
        }
    }

    public void createAttempt() {
        mRecordingRepository.createRecording(chapterNum.get(), verseNum.get());
        isRecording.set(true);
    }

    public void submitAttempt() {
        mRecordingRepository.saveRecording(new RecordingRepository.Callback() {
            @Override
            public void onSaveRecording() {
                // todo
            }
        });

        if (isEndOfSurah()) {
            mNavigator.gotoResult();
        } else {
            incrementAyah();
            isRecording.set(false);
        }

    }

    public void cancelAttempt() {
        // reset recorder todo
        isRecording.set(false);
    }

    public void playAttemptRecording() {
        audioPlayer.play(audioFileUri);
    }

    public boolean isEndOfSurah() {
        return !QuranScriptRepository.getChapter(chapterNum.get()).isValidVerseNum(verseNum.get()+1);
    }

    public void incrementAyah() {
        verseNum.set(verseNum.get()+1);
        hintVisibility.set(View.INVISIBLE);
        isHintRequested.set(false);
    }

    @Override
    public void onRecognitionCompleted() {

    }

    @Override
    public void onRecognitionError() {

    }

}
