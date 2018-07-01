package org.tangaya.quranasrclient.murojaah;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import org.tangaya.quranasrclient.ViewModelFactory;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityMurojaahBinding;
import org.tangaya.quranasrclient.eval.EvalActivity;

public class MurojaahActivity extends AppCompatActivity implements MurojaahNavigator {

    private int CHAPTER_NUM;

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mMurojaahDataBinding;

    ImageView hintBtn, retryBtn, recordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMurojaahDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mMurojaahDataBinding.setLifecycleOwner(this);

        CHAPTER_NUM = getIntent().getExtras().getInt("CHAPTER_NUM");
        Log.d("MA", "chapter num:"+CHAPTER_NUM);

        setupRecordBtn();
        setupHintBtn();
        setupCancelBtn();

        mViewModel = obtainViewModel(this);
        mMurojaahDataBinding.setViewmodel(mViewModel);
        mViewModel.onActivityCreated(this, CHAPTER_NUM);
    }

    public static MurojaahViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TranscriptionsRepository transcriptionRepo = new TranscriptionsRepository();
        RecordingRepository recordingRepo = new RecordingRepository();

        return new MurojaahViewModel(activity.getApplication(), transcriptionRepo, recordingRepo);
        //return ViewModelProviders.of(activity, factory).get(MurojaahViewModel.class);
    }

    @Override
    public void gotoEvaluation() {
        Intent intent = new Intent(this, EvalActivity.class);
        finish();
        startActivity(intent);
    }

    private void setupRecordBtn() {
        recordBtn = findViewById(R.id.record);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.isRecording.get()==1) {
                    mViewModel.submitAttempt();
                } else {
                    mViewModel.createAttempt();
                }
            }
        });
    }

    private void setupCancelBtn() {
        retryBtn = findViewById(R.id.cancel_icon);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MurojaahActivity", "cancel button clicked");
                mViewModel.cancelAttempt();
            }
        });
    }

    private void setupHintBtn() {
        hintBtn = findViewById(R.id.hint_icon);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.showHint();
            }
        });
    }
}
