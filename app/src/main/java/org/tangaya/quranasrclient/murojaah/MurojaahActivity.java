package org.tangaya.quranasrclient.murojaah;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import org.tangaya.quranasrclient.data.Quran;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityMurojaahBinding;
import org.tangaya.quranasrclient.eval.EvalActivity;

public class MurojaahActivity extends AppCompatActivity implements MurojaahNavigator {

    private int SURAH_NUM = 0;
    private String SURAH_NAME = "not-set";

    Button recordBtn, playBtn, clearBtn;

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mMurojaahDataBinding;

    //String mRecordFilePath = "/storage/emulated/0/DCIM/bismillah.wav";
//    String mRecordFilePath = Environment.getExternalStorageDirectory() + "/testwaveee.wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_murojaah);
        mMurojaahDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mMurojaahDataBinding.setLifecycleOwner(this);

        SURAH_NUM = getIntent().getExtras().getInt("SURAH_NUM");
        SURAH_NAME = getIntent().getExtras().getString("SURAH_NAME");
        TextView surahTv = findViewById(R.id.surah_name);
        surahTv.setText(SURAH_NUM+"."+SURAH_NAME);

        setupRecordButton();
        setupPlayButton();
        setupDecodeTestButton();
        setupSkipButton();

        ControlFragment controlFragment = obtainViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.murojaah_control_frame, controlFragment);
        transaction.commit();

        mViewModel = obtainViewModel(this);

        mMurojaahDataBinding.setViewmodel(mViewModel);

        Quran.init(getApplicationContext());
        mViewModel.currentSurahNum.set(SURAH_NUM);
        mViewModel.currentAyahNum.set(1);

        mViewModel.onActivityCreated(this);
    }

    private void setupRecordButton() {
        recordBtn = (Button) findViewById(R.id.record_old);
        recordBtn.setText("Start");
//        mRecorder = WavAudioRecorder.getInstance();
//        mRecorder.setOutputFile(mRecordFilePath);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //performRecording();
            }
        });
    }

    private void setupPlayButton() {
        playBtn = findViewById(R.id.play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.playAttemptRecording();
            }
        });
    }

    private void setupDecodeTestButton() {
        Button decodeTestBtn = findViewById(R.id.test_decode);
        decodeTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.testDecode();
            }
        });
    }

    private void setupSkipButton() {
        Button skipAyahBtn = findViewById(R.id.skip);
        skipAyahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.isEndOfSurah()) {
                    gotoEvaluation();
                } else {
                    mViewModel.incrementAyah();
                }
            }
        });
    }

    public static MurojaahViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TranscriptionsRepository transcriptionRepo = new TranscriptionsRepository();
        RecordingRepository recordingRepo = new RecordingRepository();

        return new MurojaahViewModel(activity.getApplication(), transcriptionRepo, recordingRepo);
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
    public void gotoEvaluation() {
        Intent intent = new Intent(this, EvalActivity.class);
        finish();
        startActivity(intent);
    }
}
