package org.tangaya.rafiqulhuffazh.data.service;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.tangaya.rafiqulhuffazh.data.model.QuranAyahAudio;
import org.tangaya.rafiqulhuffazh.data.model.Recording;
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.util.AudioFileHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class RecognitionTaskNew extends WebSocketAdapter {

    private QuranAyahAudio audio;
    private WebSocket webSocket;

    public RecognitionTaskNew(QuranAyahAudio audio) {
        this.audio = audio;

        try {
            webSocket = new WebSocketFactory().createSocket(ServerSetting.getRecognitionEndpoint());
        } catch (IOException e) {
            e.printStackTrace();
        }
        webSocket.addListener(this);
    }

    public void executeTask() {
        webSocket.connectAsynchronously();
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);

        Timber.d("onConnected()");
        sendAudio();
    }

    private void sendAudio() {
        File file = null;

        if (audio instanceof Recording) {
            file = new File(AudioFileHelper.getUserRecordingFilePath(audio.getSurah(), audio.getAyah()));
        } else {
            // since there is only two type of QuranAyahAudio
            file = new File(AudioFileHelper.getQari1AudioFilePath(audio.getSurah(), audio.getAyah()));
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

}
