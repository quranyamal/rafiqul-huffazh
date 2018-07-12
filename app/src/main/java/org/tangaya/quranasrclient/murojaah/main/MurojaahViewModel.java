package org.tangaya.quranasrclient.murojaah.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Attempt;
import org.tangaya.quranasrclient.data.RecognitionResponse;
import org.tangaya.quranasrclient.data.VerseRecognitionTask;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;
import org.tangaya.quranasrclient.service.AudioPlayer;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsDataSource;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableBoolean isHintRequested = new ObservableBoolean();

    private boolean isLastVerse;

    AudioPlayer audioPlayer;

    Context mContext;
    TranscriptionsRepository mTranscriptionsRepository;
    RecordingRepository mRecordingRepository;
    MurojaahNavigator mNavigator;

    AudioPlayer mPlayer = new AudioPlayer();
    WavAudioRecorder mRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC,
            16000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    Uri audioFileUri;
    Attempt attempt;
    String endpoint;
    WebSocket webSocket;

    private String storageDir = Environment.getExternalStorageDirectory()+"";
    //private String quranVerseAudioDir = storageDir + "/rafiqul-huffazh";
    //String recordingFilepath = quranVerseAudioDir + "/999-999.wav";

    String recordingFilepath;

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

        endpoint = ((MyApplication) getApplication()).getSpeechEndpoint();
    }

    void onActivityCreated(MurojaahNavigator navigator, int chapter) {
        mNavigator = navigator;
        chapterNum.set(chapter);
        chapterName.set(QuranScriptRepository.getChapter(chapter).getTitle());
    }

    public void showHint() {
        chapterName.set(QuranScriptRepository.getChapter(chapterNum.get()).getTitle());
        ayahText.set(QuranScriptRepository.getChapter(chapterNum.get()).getVerseScript(verseNum.get()));
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

    void createAttempt() {
        // todo: fix filename of recording. save file to cache directory

        //recordingFilepath = "/storage/extSdCard/rafiqul-huffazh/recording"+"/rec-"+chapterNum.get()+"-"+verseNum.get()+".wav";
        //recordingFilepath = quranVerseAudioDir+"/rec-"+chapterNum.get()+"-"+verseNum.get()+".wav";

        recordingFilepath = storageDir + "/rec-"+chapterNum.get()+"-"+verseNum.get()+".wav";

        attempt = new Attempt(chapterNum.get(), verseNum.get(), 123);
        attempt.setFilepath(recordingFilepath);

        mRecorder.setOutputFile(recordingFilepath);
        mRecorder.prepare();
        mRecorder.start();

        isRecording.set(true);
    }

    void submitAttempt() {

        mRecorder.stop();
        mRecorder.reset();

        Log.d("MVM", "creating web socket");
        try {
            webSocket = new WebSocketFactory().createSocket(endpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final VerseRecognitionTask recognitionTask = new VerseRecognitionTask(webSocket);

        webSocket.addListener(new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> header) throws Exception {
                super.onConnected(websocket, header);

                serverStatus.set("connected");
                recognitionTask.execute(attempt);
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                serverStatus.set("disconnected");
            }

            @Override
            public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                super.onConnectError(websocket, exception);

                serverStatus.set("connection error");
            }

            @Override
            public void onTextMessage(WebSocket webSocket, String text) {
                RecognitionResponse response = new RecognitionResponse(text);
                if (response.isTranscriptionFinal()) {
                    attempt.setResponse(text);
                    ((MyApplication) getApplication()).getAttempts().add(attempt);
                }
            }
        });

        serverStatus.set("connecting...");
        webSocket.connectAsynchronously();

        if (isEndOfSurah()) {
            mNavigator.gotoResult();
        } else {
            incrementAyah();
            isRecording.set(false);
        }

    }

    public void cancelAttempt() {
        mRecorder.stop();
        mRecorder.reset();

        isRecording.set(false);
    }

    public void playAttemptRecording() {
        audioPlayer.play(audioFileUri);
    }

    private boolean isEndOfSurah() {
        return !QuranScriptRepository.getChapter(chapterNum.get()).isValidVerseNum(verseNum.get()+1);
    }

    private void incrementAyah() {
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

    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d("State", "Yes, it is writable");
            return true;
        } else {
            return false;
        }
    }

}
