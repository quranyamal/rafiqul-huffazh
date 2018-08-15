package org.tangaya.rafiqulhuffazh.data.service;

import android.os.AsyncTask;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.rafiqulhuffazh.data.model.Attempt;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;

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

    private String transcription;

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
        if (attempt.getMockType()== Attempt.MockType.MOCK_RESULT) {
            transcription = QuranUtil.getQScript(attempt.getSurahNum(), attempt.getAyahNum());
            EvaluationOld evaluation = new EvaluationOld(attempt, transcription);

            EvaluationRepository.addToEvalSet(evaluation);
        } else {
            webSocket.connectAsynchronously();
        }
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

        transcription = new JSONObject(text).getJSONObject("result").getJSONArray("hypotheses")
                .getJSONObject(0).getString("transcript");

        Timber.d("received transcription: " + transcription);

        if (isTranscriptionFinal(text)) {
            transcription = transcription.substring(0, transcription.length()-1);
            EvaluationOld evaluation = new EvaluationOld(attempt, transcription);
            EvaluationRepository.addToEvalSet(evaluation);
            Timber.d("eval added to eval set");
        }

        String[] words = transcription.split("");

        Timber.d(QuranScriptConverter.toArabic(transcription));
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
