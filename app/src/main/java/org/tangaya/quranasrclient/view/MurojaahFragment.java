package org.tangaya.quranasrclient.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.tangaya.quranasrclient.AndroidApplication;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.Threading.MainThreadImpl;
import org.tangaya.quranasrclient.domain.DecoderWebSocket;
import org.tangaya.quranasrclient.domain.ThreadExecutor;
import org.tangaya.quranasrclient.domain.TranscriberService;
import org.tangaya.quranasrclient.domain.model.Transcription;
import org.tangaya.quranasrclient.presenter.MurojaahPresenter;
import org.tangaya.quranasrclient.presenter.MurojaahPresenterImpl;

import java.io.File;

import timber.log.Timber;

public class MurojaahFragment extends Fragment
     {




    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater,
                                          @Nullable ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {

        android.view.View view = inflater.inflate(R.layout.activity_murojaah,
                container, false);



        return view;
    }

}
