package org.tangaya.quranasrclient.data.service;

import android.os.AsyncTask;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.util.ConnectToWSTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class Transcriber {

    WebSocket ws;
    String hostname = "192.168.1.217";
    String port = "8888";

    public Transcriber() {}

    public void startRecognize(String filename) {

        initWS();
        //ConnectToWSTask connectToWSTask = new ConnectToWSTask(ws, serverStatusTv, resultTv);
        ConnectToWSTask connectToWSTask = new ConnectToWSTask(ws);
        connectToWSTask.execute();

        File file = new File(filename);
        int size = (int) file.length();
        byte[] bytes = new byte[size];


        Log.d("MurojaahActivity", "Recognizing " + filename);
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ws.sendBinary(bytes);
    }

    protected void initWS() {

        String endpoint = "ws://"+hostname+":"+port+"/client/ws/speech";
        int timeout = 5000;

        WebSocketFactory factory = new WebSocketFactory();
        try {
            ws = factory.createSocket(endpoint, timeout);
        } catch (IOException e) {
            Log.d("MorojaahActivity", "Creating socket error");
            e.printStackTrace();
        }

        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket webSocket,
                                    Map<String,List<String>> headers) throws Exception {

                Log.d("ConnectToWSTask", "onConnected");
            }

            @Override
            public void onTextMessage(WebSocket webSocket, String message) throws Exception {

                Log.d("ConnectToWSTask", "onTextMessage: " + message);

                //transcript = "undef";
                int lastResultLength = 0;

                try {
                    JSONObject json = new JSONObject(message);

                    int status = json.getInt("status");

                    if (status == 0) {  // 0=success

                        JSONObject result = json.getJSONObject("result");
                        boolean isFinal = result.getBoolean("final");
                        JSONObject first_hypotheses = result.getJSONArray("hypotheses").getJSONObject(0);
                        //transcript = first_hypotheses.getString("transcript");

                                //publishProgress(transcript);
                        //updateTranscript(transcript);

                        //String resultStr = transcript.substring(lastResultLength, transcript.length() - 1);
                        //if (!isFinal) {
                        //    lastResultLength = transcript.length() - 1;
                        //}
                        //else  {
                        //    lastResultLength = 0;
                        //    resultStr += ".\n\n";
                        //}

                    }
                    //else if (status == 1) {     // 1 = no speech
                        // todo
                    //}
                }
                catch (JSONException e) {
                    Timber.e(e.getMessage());
                }

            }

            @Override
            public void onBinaryMessage(WebSocket websocket,
                                        byte[] binary) throws Exception {

                Log.d("MANew", "onBinaryMessage: " + binary.toString());
            }
        });
    }

}
