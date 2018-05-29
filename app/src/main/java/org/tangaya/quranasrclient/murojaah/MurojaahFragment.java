package org.tangaya.quranasrclient.murojaah;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.service.WavAudioRecorder;
import org.tangaya.quranasrclient.util.ConnectToWSTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MurojaahFragment extends Fragment {

    WebSocket ws;
    TextView resultTv;
    TextView serverStatusTv;

    String transcript;

    boolean mStartRecording;
    boolean mStartPlaying;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mRecordFilePath = null;

    private WavAudioRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private Button recordButton, playButton, clearButton;
    private Button retryBtn, nextBtn, showBtn;

    TextView statusTv;

    String hostname = "192.168.1.217";
    String port = "8888";

    // NEW WKWKWK

    private MurojaahViewModel mViewModel;

    public MurojaahFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setupRecordButton();
    }

    public static MurojaahFragment newInstance() {
        return new MurojaahFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_murojaah, container, false);

        resultTv = view.findViewById(R.id.result);
        serverStatusTv = view.findViewById(R.id.server_status);

        initWS();
        ConnectToWSTask connectToWSTask = new ConnectToWSTask(ws, serverStatusTv, resultTv);
        connectToWSTask.execute();

        mRecordFilePath = "/storage/emulated/0/DCIM/bismillah.wav";

        retryBtn = view.findViewById(R.id.retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MurojaahActivity", "Recognize button clicked");
                recognize(mRecordFilePath);
            }
        });

        nextBtn = view.findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MurojaahActivity", "Send button clicked");
                //recognize("/storage/emulated/0/DCIM/100-1.wav");
                recognize(mRecordFilePath);
                Log.d("MurojaahActivity", "sending binary...");
            }
        });

        recordButton = (Button) view.findViewById(R.id.record);
        recordButton.setText("Start");
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRecordFilePath);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
                    mRecorder.prepare();
                    mRecorder.start();
                    recordButton.setText("Stop");
                } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                    mRecorder.release();
                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(mRecordFilePath);
                    recordButton.setText("Start");
                } else {
                    mRecorder.stop();
                    mRecorder.reset();
                    recordButton.setText("Start");
                }
            }
        });
        clearButton = (Button) view.findViewById(R.id.reset);
        clearButton.setText("Clear");
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pcmFile = new File(mRecordFilePath);
                if (pcmFile.exists()) {
                    pcmFile.delete();
                }
            }
        });

        return view;
    }


    private void checkServerStatus() {
        hostname = "192.168.1.217";
        port = "8888";

        String status_endpoint = "ws://"+hostname+":"+port+"/client/ws/status";

        ConnectToWsStatus connectToWsStatus = new ConnectToWsStatus(status_endpoint);
        connectToWsStatus.execute();
        Toast.makeText(getActivity(),"connecting...", Toast.LENGTH_SHORT).show();
    }

    private class ConnectToWsStatus extends AsyncTask<Void, Void, Boolean> {

        String endpoint;
        int timeout = 5000;

        ConnectToWsStatus(String endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                WebSocketFactory factory = new WebSocketFactory();
                ws = factory.createSocket(endpoint, timeout);
            } catch (IOException e) {
                Log.d("MorojaahActivity", "Creating socket error");
                e.printStackTrace();
            }
            return ws!=null;
        }

        @Override
        protected void onPostExecute(Boolean isWsCreated) {
            if (isWsCreated) {
                statusTv.setText("terhubung");
            } else {
                statusTv.setText("tidak terhubung");
            }
        }
    }

    private void recognize(String filename) {
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


}