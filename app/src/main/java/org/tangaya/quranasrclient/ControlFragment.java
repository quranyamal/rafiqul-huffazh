package org.tangaya.quranasrclient;

import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tangaya.quranasrclient.data.Quran;
import org.tangaya.quranasrclient.databinding.FragmentMurojaahBinding;
import org.tangaya.quranasrclient.murojaah.MurojaahActivity;
import org.tangaya.quranasrclient.murojaah.MurojaahViewModel;


public class ControlFragment extends Fragment {

    private MurojaahViewModel mViewModel;
    private FragmentMurojaahBinding mMurojaahFragDataBinding;

    Button hintBtn, nextBtn, retryBtn;
    View rootView;

    public ControlFragment() {}

    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_control, container, false);

        mViewModel = MurojaahActivity.obtainViewModel(getActivity());

        setupHintButton();
        setupRetryButton();

        return rootView;
    }

    private void setupHintButtonOld() {
        hintBtn = rootView.findViewById(R.id.hint);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bismillah = Quran.getSurah(1).getAyah(1);
                hintBtn.setText(bismillah);
                Log.d("test Quran", Quran.getSurah(18).getAyah(1));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        Button hintBtn = rootView.findViewById(R.id.hint);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MurojaahActivity) getActivity()).onClickHint();

            }
        });
    }

}
