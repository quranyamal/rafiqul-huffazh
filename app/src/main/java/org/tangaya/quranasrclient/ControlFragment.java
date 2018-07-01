package org.tangaya.quranasrclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.tangaya.quranasrclient.data.source.RecordingRepository;
import org.tangaya.quranasrclient.databinding.FragmentMurojaahBinding;
import org.tangaya.quranasrclient.murojaah.MurojaahActivity;
import org.tangaya.quranasrclient.murojaah.MurojaahViewModel;


public class ControlFragment extends Fragment {

    private MurojaahViewModel mViewModel;
    private FragmentMurojaahBinding mMurojaahFragDataBinding;

    ImageView hintBtn, retryBtn;
    ImageView recordBtn;
    View rootView;

    private RecordingRepository mRepository;

    private static final int IDLE = MurojaahViewModel.STATE_IDLE;
    private static final int RECORDING = MurojaahViewModel.STATE_RECORDING;
    private static final int RECOGNIZING = MurojaahViewModel.STATE_RECOGNIZING;

    public ControlFragment() {
    }

    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mRecorder = WavAudioRecorder.getInstance();
//        mRecorder.setOutputFile(mRecordFilePath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_control, container, false);

        //mViewModel = MurojaahActivity.obtainViewModel(getActivity());
        mViewModel = ((MurojaahActivity) getActivity()).mViewModel;

        setupAttemptButton();
        setupHintButton();
        setupRetryButton();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setupAttemptButton() {
        recordBtn = rootView.findViewById(R.id.record);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.getAttemptState() == IDLE) {
                    mViewModel.createAttempt();
                    //recordBtn.setText("recording...");
                    recordBtn.setImageResource(R.drawable.stop_black_100);
                } else if (mViewModel.getAttemptState() == RECORDING) {
                    mViewModel.submitAttempt();
                    //recordBtn.setText("recognizing...");
                    recordBtn.setImageResource(R.drawable.arrow_black_100);
                } else {
                    mViewModel.cancelAttempt();
                    mViewModel.playAttemptRecording();
                    //recordBtn.setText("record");
                    recordBtn.setImageResource(R.drawable.mic_black_100);
                }
            }
        });
    }

    private void setupRetryButton() {
        retryBtn = rootView.findViewById(R.id.cancel_icon);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MurojaahActivity", "Retry button clicked");
                //recognize(mRecordFilePath);
            }
        });
    }

    private void setupHintButton() {
        hintBtn = rootView.findViewById(R.id.hint_icon);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.showHint();
            }
        });
    }

}
