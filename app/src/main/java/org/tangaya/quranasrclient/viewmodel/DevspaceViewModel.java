package org.tangaya.quranasrclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;
import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.data.RecognitionResponse;
import org.tangaya.quranasrclient.data.VerseRecognitionTask;
import org.tangaya.quranasrclient.navigator.DevspaceNavigator;
import org.tangaya.quranasrclient.service.AudioPlayer;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DevspaceViewModel extends AndroidViewModel {

    WebSocket webSocket;

    public final ObservableInt chapter = new ObservableInt();
    public final ObservableInt verse = new ObservableInt();
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();
    public final ObservableBoolean isRecording = new ObservableBoolean();
    public final ObservableInt attemptCount = new ObservableInt();

    private String hostname;
    private String port;

    String endpoint;

    WavAudioRecorder mRecorder;
    AudioPlayer mPlayer;
    DevspaceNavigator mNavigator;

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String audioDir = extStorageDir + "/rafiqul-huffazh";

    private Queue<VerseRecognitionTask> recognitionTaskQueue = new LinkedList<>();

    private String getTestFilepath(int chapter, int verse) {
        return audioDir + "/test/"+chapter+"_"+verse+".wav";
    }

    private String getRecordingFilepath(int chapter, int verse) {
        return audioDir + "/recording/"+chapter+"_"+verse+".wav";
    }

    public DevspaceViewModel(@NonNull Application application) {
        super(application);

        mRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC,
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mPlayer = new AudioPlayer();

        chapter.set(1);
        verse.set(1);
        serverStatus.set("disconnected");
        isRecording.set(false);
        attemptCount.set(0);

        hostname = ((MyApplication) getApplication()).getServerHostname();
        port = ((MyApplication) getApplication()).getServerPort();
        endpoint = ((MyApplication) getApplication()).getSpeechEndpoint();
    }

    public void onActivityCreated(DevspaceNavigator navigator) {
        mNavigator = navigator;
    }

    public void onClickRecord() {

        if (!isRecording.get()) {

            mRecorder.setOutputFile(getRecordingFilepath(chapter.get(), verse.get()));
            mRecorder.prepare();
            mRecorder.start();

            isRecording.set(true);

        } else {

            mRecorder.stop();
            mRecorder.reset();

            isRecording.set(false);
        }
    }

    public void playRecordedAudio() {
        mPlayer.play(Uri.parse(getRecordingFilepath(chapter.get(), verse.get())));
        Log.d("DVM", "playing recording...");
    }

    public void recognizeRecording() {

        String recordingFilepath = getRecordingFilepath(chapter.get(), verse.get());

        if (new File(recordingFilepath).exists()) {
            Log.d("DVM", "file exists");

            try {
                //webSocket.recreate();
                webSocket = new WebSocketFactory().createSocket(endpoint);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Evaluation evaluation = new Evaluation(chapter.get(), verse.get(), 123);
            evaluation.setFilepath(recordingFilepath);
            final VerseRecognitionTask recognitionTask = new VerseRecognitionTask(webSocket);

            recognitionTaskQueue.add(recognitionTask);
            

            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);

                    recognitionTask.execute(evaluation);
                    Log.d("DVM", "executing asyncTaskRecognizingTest");
                    serverStatus.set("connected");
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
                    result.set("can not connect to ASR server");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Log.d("DVM", "onTextMessage: " + text);
                    RecognitionResponse response = new RecognitionResponse(text);

                    Log.d("DVM", "response status: " + response.getStatus());

                    if (response.isTranscriptionFinal()) {
                        result.set(text);
                        evaluation.setResponse(text);
                        ((MyApplication) getApplication()).getEvaluations().add(evaluation);
                        Log.d("DVM", "response added to evaluation");
                        Log.d("DVM", "evaluation count: " + attemptCount);
                        attemptCount.set(((MyApplication) getApplication()).getEvaluations().size());
                    }
                }
            });

            serverStatus.set("connecting...");
            webSocket.connectAsynchronously();
            result.set("recognizing " + recordingFilepath);

        } else {
            Log.d("DVM", "file does not exist");
            result.set("file does not exist");
        }

        Log.d("DVM", "recognizing file:" + recordingFilepath );
    }

    public void recognizeTestFile() {

        String testFilepath = getTestFilepath(chapter.get(), verse.get());

        try {
            //webSocket.recreate();
            webSocket = new WebSocketFactory().createSocket(endpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Evaluation evaluation = new Evaluation(chapter.get(), verse.get(), 123);
        evaluation.setFilepath(testFilepath);

        final VerseRecognitionTask recognitionTask = new VerseRecognitionTask(webSocket);

        // todo: create listener class
        webSocket.addListener(new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                super.onConnected(websocket, headers);

                recognitionTask.execute(evaluation);
                Log.d("DVM", "executing asyncTaskRecognizingTest");
                serverStatus.set("recognizing...");
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
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                super.onTextMessage(websocket, text);

                Log.d("DVM", "onTextMessage: " + text);
                Log.d("DVM", "response added to evaluation");
                RecognitionResponse response = new RecognitionResponse(text);

                Log.d("DVM", "response status: " + response.getStatus());

                if (response.isTranscriptionFinal()) {
                    evaluation.setResponse(text);
                    ((MyApplication) getApplication()).getEvaluations().add(evaluation);
                    Log.d("DVM", "evaluation count: " + attemptCount);
                    result.set(text);
                    attemptCount.set(((MyApplication) getApplication()).getEvaluations().size());
                }
            }
        });

        serverStatus.set("connecting...");
        result.set("recognizing " + testFilepath);
        webSocket.connectAsynchronously();
    }

    public void playTestFile() {
        mPlayer.play(Uri.parse(getTestFilepath(chapter.get(), verse.get())));
        Log.d("DVM", "playing test file...");
    }


    public void connect() {

        String endpoint = "ws://"+hostname+":"+port+"/client/ws/speech";

        serverStatus.set("connecting ...");
        try {
            webSocket = new WebSocketFactory().createSocket(endpoint);

            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);

                    serverStatus.set("connected");
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                    serverStatus.set("disconnected");
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    super.onConnectError(websocket, exception);

                    serverStatus.set("error");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Log.d("DVM", "onTextMessage. message: " + text);

                    JSONObject json = new JSONObject(text);

                    Log.d("DVM", "json: " + json);

                    Log.d("DVM", "transcript: " + json.getJSONObject("result").getJSONArray("hypotheses")
                                .getJSONObject(0));

                    result.set(text);
                }
            });

            webSocket.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoEvalDetail() {
        mNavigator.gotoEvalDetail();
    }

    public void incrementVerse() {
        verse.set(verse.get()+1);
    }

    public void decrementVerse() {
        if (verse.get()>1) {
            verse.set(verse.get()-1);
        }
    }

    public void incrementChapter() {
        Log.d("DVM", "increment chapter clicked");
        chapter.set(chapter.get()+1);
    }

    public void decrementChapter() {
        Log.d("DVM", "decrement chapter clicked");
        if (chapter.get()>1) {
            chapter.set(chapter.get()-1);
        }
    }

}

