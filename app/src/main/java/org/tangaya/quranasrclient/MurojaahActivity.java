package org.tangaya.quranasrclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MurojaahActivity extends AppCompatActivity {

    WebSocket ws;
    TextView resultTv;
    String transcript;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murojaah);

        resultTv = findViewById(R.id.result);

        initWS();
        ConnectToWSTask connectToWSTask = new ConnectToWSTask(ws, resultTv);
        connectToWSTask.execute();

        count = 0;

    }

    protected void initWS() {

        String hostname = "192.168.1.217";
        String port = "8888";


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


                transcript = "undef";
                int lastResultLength = 0;

                try {
                    JSONObject json = new JSONObject(message);

                    int status = json.getInt("status");

                    if (status == 0) {  // 0=success

                        JSONObject result = json.getJSONObject("result");
                        boolean isFinal = result.getBoolean("final");
                        JSONObject first_hypotheses = result.getJSONArray("hypotheses").getJSONObject(0);
                        transcript = first_hypotheses.getString("transcript");

                        //publishProgress(transcript);
                        updateTranscript(transcript);

                        String resultStr = transcript.substring(lastResultLength, transcript.length() - 1);
                        if (!isFinal) {
                            lastResultLength = transcript.length() - 1;
                        }
                        else  {
                            lastResultLength = 0;
                            resultStr += ".\n\n";
                        }

                    }
                    else if (status == 1) {     // 1 = no speech
                        // todo
                    }
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

    private void updateTranscript(String transcript) {
        resultTv.setText(transcript);
    }


    public void onClickNextButton(View view) {
        Log.d("MurojaahActivity", "Send button clicked");

        File file = new File("/storage/emulated/0/DCIM/100-1.wav");
        int size = (int) file.length();
        byte[] bytes = new byte[size];
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
        Log.d("MurojaahActivity", "sending binary...");
    }


}
