package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepositoryOld;
import org.tangaya.rafiqulhuffazh.util.AudioFileHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class TranscriptionTask extends WebSocketAdapter {

    private WebSocket webSocket;

    private QuranAyahAudio inputAudio;
    private MutableLiveData<QuranAyahAudio> resultHolder;

    public TranscriptionTask(QuranAyahAudio inputAudio, MutableLiveData<QuranAyahAudio> resultHolder) {
        this.inputAudio = inputAudio;
        this.resultHolder = resultHolder;

        try {
            webSocket = new WebSocketFactory().createSocket(ServerSetting.getRecognitionEndpoint());
        } catch (IOException e) {
            e.printStackTrace();
        }
        webSocket.addListener(this);
    }

    private boolean isTranscriptionFinal(String text) {
        try {
            JSONObject json = new JSONObject(text);
            if (json.getInt("status")==0) {
                return json.getJSONObject("result").getBoolean("final");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void transcribe() {

        webSocket.connectAsynchronously();
    }

    private void sendAudio() {
        File file;

        if (inputAudio instanceof Recording) {
            file = new File(AudioFileHelper.getUserRecordingFilePath(inputAudio.getSurah(), inputAudio.getAyah()));
        } else {
            // since there is only two type of QuranAyahAudio
            file = new File(AudioFileHelper.getQari1AudioFilePath(inputAudio.getSurah(), inputAudio.getAyah()));
        }

        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        webSocket.sendBinary(bytes);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);

        Timber.d("onConnected()");
        sendAudio();
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                               WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

        Timber.d("onDisonnected()");
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        Timber.d("onTextMessage");
        Timber.d(text);

        if (isTranscriptionFinal(text)) {
            String transcription = new JSONObject(text).getJSONObject("result").getJSONArray("hypotheses")
                    .getJSONObject(0).getString("transcript");
            transcription = transcription.substring(0, transcription.length()-1);

            inputAudio.setTranscription(transcription);
            resultHolder.postValue(inputAudio);
        }
    }
}
