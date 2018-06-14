package org.tangaya.quranasrclient.imtihan;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.service.AudioRecorder;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.service.WavAudioRecorder;

import java.io.File;

public class ImtihanActivity extends AppCompatActivity {

    private Button btnControl, btnClear;
    private TextView textDisplay;
    private WavAudioRecorder mRecorder;
    private static final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/testwaveee.wav";

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_imtihan);

        btnControl = (Button) this.findViewById(R.id.btnControl);
        btnControl.setText("Start");
        mRecorder = WavAudioRecorder.getInstance();
        mRecorder.setOutputFile(mRcordFilePath);
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
                    mRecorder.prepare();
                    mRecorder.start();
                    btnControl.setText("Stop");
                } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                    mRecorder.release();
                    mRecorder = WavAudioRecorder.getInstance();
                    mRecorder.setOutputFile(mRcordFilePath);
                    btnControl.setText("Start");
                } else {
                    mRecorder.stop();
                    mRecorder.reset();
                    btnControl.setText("Start");
                }
            }
        });
        btnClear = (Button) this.findViewById(R.id.btnClear);
        btnClear.setText("Clear");
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pcmFile = new File(mRcordFilePath);
                if (pcmFile.exists()) {
                    pcmFile.delete();
                }
            }
        });
        textDisplay = (TextView) this.findViewById(R.id.Textdisplay);
        textDisplay.setText("recording saved to: " + mRcordFilePath);

        final AudioRecorder recorder = new AudioRecorder();
        Button recBtnNew = findViewById(R.id.record_new);
//        recBtnNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recorder.startRecording(new RecordingRepository.Callback() {
//                    @Override
//                    public void onReadResult(byte[] audioByte) {
//                        Log.d("recording", "onReadResult");
//                        notifyOnAudioData(audioByte);
//                    }
//                });
//            }
//        });

        Button stopBtnNew = findViewById(R.id.stop_new);
        stopBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stopRecording();
            }
        });

        Button playBtnNew = findViewById(R.id.play_new);

    }

//    private void notifyOnAudioData(byte[] audioByte) {
//        mHandler.postDelayed(mRunnable, 1000);
//        Log.d("IA", "notifyOnAudioData called");
//    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("IA", "delayed runnable");
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            mRecorder.release();
        }
    }

}
