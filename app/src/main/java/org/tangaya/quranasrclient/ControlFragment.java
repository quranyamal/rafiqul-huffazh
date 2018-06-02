package org.tangaya.quranasrclient;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tangaya.quranasrclient.databinding.FragmentMurojaahBinding;
import org.tangaya.quranasrclient.murojaah.MurojaahActivity;
import org.tangaya.quranasrclient.murojaah.MurojaahNavigator;
import org.tangaya.quranasrclient.murojaah.MurojaahViewModel;
import org.tangaya.quranasrclient.service.WavAudioRecorder;


public class ControlFragment extends Fragment implements MurojaahNavigator {

    private MurojaahViewModel mViewModel;
    private FragmentMurojaahBinding mMurojaahFragDataBinding;

    WavAudioRecorder mRecorder;

    Button hintBtn, recordBtn, retryBtn;
    View rootView;

    String mRecordFilePath = Environment.getExternalStorageDirectory() + "/testwaveee.wav";

    public ControlFragment() {}

    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRecordFilePath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_control, container, false);

        //mViewModel = MurojaahActivity.obtainViewModel(getActivity());
        mViewModel = ((MurojaahActivity)getActivity()).mViewModel;

        setupRecordButton();
        setupHintButton();
        setupRetryButton();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setupRecordButton() {
        recordBtn = rootView.findViewById(R.id.record);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRecording();
            }
        });
    }

    private void setupRetryButton() {
        retryBtn = rootView.findViewById(R.id.retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MurojaahActivity", "Retry button clicked");
                //recognize(mRecordFilePath);
            }
        });
    }

    private void setupHintButton() {
        hintBtn = rootView.findViewById(R.id.hint);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHint();
            }
        });
    }

    @Override
    public void startRecording() {
        if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
            mRecorder.prepare();
            mRecorder.start();
            recordBtn.setText("Stop");
        } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
            mRecorder.release();
            mRecorder = WavAudioRecorder.getInstanse();
            mRecorder.setOutputFile(mRecordFilePath);
            recordBtn.setText("Start");
        } else {
            mRecorder.stop();
            mRecorder.reset();
            recordBtn.setText("Start");
        }
    }

    @Override
    public void retryRecording() {

    }

    @Override
    public void showHint() {
        mViewModel.showHint();
    }

}
