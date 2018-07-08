package org.tangaya.quranasrclient.devspace;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.media.AudioFormat;
import android.media.MediaRecorder;
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
import org.tangaya.quranasrclient.data.Attempt;
import org.tangaya.quranasrclient.data.VerseRecognitionTask;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DevspaceViewModel extends AndroidViewModel {

    WebSocket webSocket; // todo: set simeout?

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String quranVerseAudioDir = extStorageDir + "/quran-verse-audio";

    public final ObservableInt chapter = new ObservableInt();
    public final ObservableInt verse = new ObservableInt();
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();
    public final ObservableBoolean isRecording = new ObservableBoolean();

    private String hostname;
    private String port;

    WavAudioRecorder mRecorder;
    String recordingFilepath = quranVerseAudioDir + "/devspace.wav";

    public DevspaceViewModel(@NonNull Application application) {
        super(application);

        mRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC,
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        chapter.set(1);
        verse.set(1);
        serverStatus.set("disconnected");
        isRecording.set(false);

        hostname = ((MyApplication) getApplication()).getServerHostname();
        port = ((MyApplication) getApplication()).getServerPort();
    }

    public void onClickRecord() {

        if (!isRecording.get()) {

            mRecorder.setOutputFile(recordingFilepath);
            mRecorder.prepare();
            mRecorder.start();

            isRecording.set(true);

        } else {

            mRecorder.stop();
            mRecorder.reset();

            isRecording.set(false);
        }
    }

    public void asyncTaskRecognizingTest() {

        String endpoint = ((MyApplication) getApplication()).getSpeechEndpoint();

        try {
            webSocket = new WebSocketFactory().createSocket(endpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Attempt anAttempt = new Attempt(chapter.get(),verse.get(),3);
        final VerseRecognitionTask recTask = new VerseRecognitionTask(webSocket);

        webSocket.addListener(new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                super.onConnected(websocket, headers);

                recTask.execute(anAttempt);
                Log.d("DVM", "executing asyncTaskRecognizingTest");
                serverStatus.set("connected");
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                serverStatus.set("disconnected. failed to run async task");
            }
        });

        webSocket.connectAsynchronously();
    }

    public void recognizeRecording() {

        if (new File(recordingFilepath).exists()) {
            Log.d("DVM", "file exists");
        } else {
            Log.d("DVM", "file does not exist");
        }

        Log.d("DVM", "ext dir state: " + Environment.getExternalStorageState());

        result.set(Environment.getExternalStorageState());
        Log.d("DVM", "recognizing file:" + recordingFilepath );
    }

    public void recognizeByFile() {

        String endpoint = ((MyApplication) getApplication()).getSpeechEndpoint();

        try {
            //webSocket.recreate();
            webSocket = new WebSocketFactory().createSocket(endpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Attempt attempt = new Attempt(chapter.get(), verse.get(), 123);
        final VerseRecognitionTask recognitionTask = new VerseRecognitionTask(webSocket);

        webSocket.addListener(new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                super.onConnected(websocket, headers);

                recognitionTask.execute(attempt);
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
            }
        });

        serverStatus.set("connecting...");
        webSocket.connectAsynchronously();

    }

    private String getFilePath(int chapter, int verse) {
        return quranVerseAudioDir + "/" + chapter + "-" + verse + ".wav";
    }

    private boolean isConnected() {
        return serverStatus.get().toString().equals("connected");
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

}

