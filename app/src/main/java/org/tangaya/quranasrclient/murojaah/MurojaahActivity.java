package org.tangaya.quranasrclient.murojaah;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.tangaya.quranasrclient.ControlFragment;
import org.tangaya.quranasrclient.ViewModelFactory;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.service.WavAudioRecorder;

import java.io.File;

public class MurojaahActivity extends AppCompatActivity implements MurojaahNavigator {

    String surah = "not-set";

    Button recordButton, clearButton;
    WavAudioRecorder mRecorder;

    String mRecordFilePath = "/storage/emulated/0/DCIM/bismillah.wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murojaah);

        surah = getIntent().getExtras().getString("surah");
        TextView surahTv = findViewById(R.id.surah_name);
        surahTv.setText(surah);

        setupView();

        ControlFragment controlFragment = obtainViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.murojaah_control_frame, controlFragment);
        transaction.commit();
    }

    private void setupView() {
        recordButton = (Button) findViewById(R.id.record);
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
        clearButton = (Button) findViewById(R.id.reset);
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
    }

    public static MurojaahViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TranscriptionsRepository repo = new TranscriptionsRepository();

        return new MurojaahViewModel(activity.getApplication(), repo);
        //return ViewModelProviders.of(activity, factory).get(MurojaahViewModel.class);
    }

    @NonNull
    private ControlFragment obtainViewFragment() {
        // View Fragment
        ControlFragment controlFragment = (ControlFragment) getSupportFragmentManager()
                .findFragmentById(R.id.murojaah_control_frame);

        if (controlFragment == null) {
            controlFragment = ControlFragment.newInstance();
        }
        return controlFragment;
    }

    @Override
    public void onStartRecord() {}
}
