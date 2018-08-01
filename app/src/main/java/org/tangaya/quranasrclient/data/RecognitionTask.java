package org.tangaya.quranasrclient.data;

import android.os.AsyncTask;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.data.source.EvaluationRepository;
import org.tangaya.quranasrclient.util.QScriptToArabic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class RecognitionTask extends WebSocketAdapter {

    public static String ENDPOINT;

    private WebSocket webSocket;
    private Attempt attempt;
    private EvaluationOld eval;

    private RecognitionAsyncTask recognitionAsyncTask;

    public RecognitionTask(Attempt attempt) {
        this.attempt = attempt;

        try {
            webSocket = new WebSocketFactory().createSocket(ENDPOINT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        webSocket.addListener(this);
        recognitionAsyncTask = new RecognitionAsyncTask();
    }

    public void execute() {
        webSocket.connectAsynchronously();
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);

        eval = recognitionAsyncTask.doInBackground();

        Timber.d("onConnected()");
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

        Timber.d("onDisconnected()");
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);

        Timber.d("onConnectError()");
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);

        Timber.d("onTextMessage(); message: " + text);

        if (isTranscriptionFinal(text)) {
            EvaluationOld evaluation = new EvaluationOld(attempt.getChapterNum(), attempt.getVerseNum(), 123);
            evaluation.setFilepath(attempt.getAudioFilePath());
            evaluation.setResponse(text);
            EvaluationRepository.addToEvalSet(evaluation);
            Timber.d("eval added to eval set");
        }

        String transcription = new JSONObject(text).getJSONObject("result").getJSONArray("hypotheses")
                .getJSONObject(0).getString("transcript");

        transcription = transcription.substring(0, transcription.length()-1);

        Timber.d("received transcription: " + transcription);

        String[] words = transcription.split("");

        Timber.d(QScriptToArabic.getArabic(transcription));
    }

    private boolean isTranscriptionFinal(String text) {
        JSONObject json;
        boolean isFinal = false;

        try {
            json = new JSONObject(text);
            if (json.getInt("status")==0) {
                isFinal = json.getJSONObject("result").getBoolean("final");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Timber.d("isTranscriptionFinal: " + isFinal);

        return isFinal;
    }

    private class RecognitionAsyncTask extends AsyncTask<Void, Void, EvaluationOld> {

        @Override
        protected EvaluationOld doInBackground(Void... voids) {

            File file = new File(attempt.getAudioFilePath());
            int size = (int) file.length();
            byte[] bytes = new byte[size];

            Timber.d("Recognizing (asynctask)" + attempt.getAudioFilePath());
            Timber.d("endpoint:" + ENDPOINT);
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Timber.d( "sending binary. size: " + bytes.length);
            webSocket.sendBinary(bytes);
            Timber.d( "after send statement");

            return null;
        }

        @Override
        protected void onPostExecute(EvaluationOld eval) {
            Timber.d("eval added to eval set. task execution finish");
        }
    }
}
