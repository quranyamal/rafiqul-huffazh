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

public class MurojaahActivityNew extends AppCompatActivity {

    WebSocketFactory factory;
    WebSocket ws;

    String hostname;
    String port;
    TextView statusTv;
    TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murojaah_new);

        statusTv = findViewById(R.id.status);
        resultTv = findViewById(R.id.result);
        factory = new WebSocketFactory();

    }


    public void onClickConnect(View view) {

        TextView hostnameTv = findViewById(R.id.hostname);
        TextView portTv = findViewById(R.id.port);

        hostname = hostnameTv.getText().toString();
        port = portTv.getText().toString();


        Connect2WSTask checkServerTask = new Connect2WSTask();
        checkServerTask.execute();
        Toast.makeText(this,"connecting...", Toast.LENGTH_SHORT).show();
    }

    public void onClickSendButton(View view) {
        Log.d("MANew", "Send button clicked");

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
        Log.d("MANew", "sending binary...");
    }


    private class Connect2WSTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                String endpoint = "ws://"+hostname+":"+port+"/client/ws/speech";
                int timeout = 5000;
                ws = factory.createSocket(endpoint, timeout);

                ws.addListener(new WebSocketAdapter() {
                    @Override
                    public void onConnected(WebSocket webSocket,
                                            Map<String,List<String>> headers) throws Exception {

                        Toast.makeText(getApplicationContext(),
                                "connected to the socket", Toast.LENGTH_SHORT).show();
                        Log.d("MANew", "onConnected");
                    }

                    @Override
                    public void onTextMessage(WebSocket webSocket, String message) throws Exception {

                        Log.d("MANew", "onTextMessage: " + message);

                        String transcript = "undef";
                        int lastResultLength = 0;

                        try {
                            JSONObject json = new JSONObject(message);

                            int status = json.getInt("status");

                            if (status == 0) {  // 0=success

                                JSONObject result = json.getJSONObject("result");
                                boolean isFinal = result.getBoolean("final");
                                JSONObject first_hypotheses = result.getJSONArray("hypotheses").getJSONObject(0);
                                transcript = first_hypotheses.getString("transcript");

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
//                                stopRecording();
//                                Timber.d("Silent timeout from server");
//                                mView.showMessage("Tidak mendeteksi masukan suara");
//                                mView.onFinishProcess();
                            }
                        }
                        catch (JSONException e) {
                            Timber.e(e.getMessage());
                        }

                        resultTv.setText(transcript);
                    }

                    @Override
                    public void onBinaryMessage(WebSocket websocket,
                                                byte[] binary) throws Exception {

                        Log.d("MANew", "onBinaryMessage: " + binary.toString());
                    }
                });

                ws.connect();
                Log.d("MANew", "connecting to ws");
            } catch (WebSocketException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isServerAvailable) {
            super.onPostExecute(isServerAvailable);

            if (isServerAvailable) {
                statusTv.setText("server tersedia");
            } else {
                statusTv.setText("server tidak tersedia");
            }
        }

    }
}
