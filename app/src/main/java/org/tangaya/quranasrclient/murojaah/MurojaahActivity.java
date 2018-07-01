package org.tangaya.quranasrclient.murojaah;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.tangaya.quranasrclient.ControlFragment;
import org.tangaya.quranasrclient.ViewModelFactory;
import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.data.source.TranscriptionsRepository;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityMurojaahBinding;
import org.tangaya.quranasrclient.eval.EvalActivity;

public class MurojaahActivity extends AppCompatActivity implements MurojaahNavigator {

    private int CHAPTER_NUM = 0;
    private String CHAPTER_NAME = "not-set";

    public MurojaahViewModel mViewModel;
    private ActivityMurojaahBinding mMurojaahDataBinding;

    //String mRecordFilePath = "/storage/emulated/0/DCIM/bismillah.wav";
//    String mRecordFilePath = Environment.getExternalStorageDirectory() + "/testwaveee.wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMurojaahDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_murojaah);
        mMurojaahDataBinding.setLifecycleOwner(this);

        CHAPTER_NUM = getIntent().getExtras().getInt("CHAPTER_NUM") + 1;
        CHAPTER_NAME = getIntent().getExtras().getString("CHAPTER_NAME");

        ControlFragment controlFragment = obtainViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.murojaah_control_frame, controlFragment);
        transaction.commit();

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
