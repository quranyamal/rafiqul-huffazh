package org.tangaya.quranasrclient.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.tangaya.quranasrclient.AndroidApplication;
import org.tangaya.quranasrclient.Threading.MainThreadImpl;
import org.tangaya.quranasrclient.domain.DecoderWSRepository;
import org.tangaya.quranasrclient.domain.DecoderWebSocket;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.domain.ThreadExecutor;
import org.tangaya.quranasrclient.domain.TranscriberService;
import org.tangaya.quranasrclient.domain.model.Transcription;
import org.tangaya.quranasrclient.presenter.MurojaahPresenter;
import org.tangaya.quranasrclient.presenter.MurojaahPresenterImpl;

import java.io.File;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MurojaahActivity extends AppCompatActivity
        implements MurojaahPresenter.View, android.view.View.OnClickListener{


    private Button nextButton;
    private FilePickerDialog filePickerDialog;
    private MurojaahPresenter mPresenter;
    private Transcription formInput;
    private Button mFileModeBtn;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murojaah);

        Timber.e("onCreate");
        Log.d(toString(), "onCreate Murojaah activity called");

        TranscriberService service = new TranscriberService(getApplication());

        String endpoint = "ws://192.168.1.217:8888/client/ws/status";
        mPresenter = new MurojaahPresenterImpl(ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                new TranscriberService(getApplication()),
                new DecoderWebSocket(endpoint), this);

//        mValidator = new Validator(this);
//        mValidator.setValidationListener(this);


        mFileModeBtn = findViewById(R.id.btn_mode_file);
        mFileModeBtn.setOnClickListener(this);

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"wav"};
        filePickerDialog = new FilePickerDialog(this, properties);
        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                    Log.d("MA", "onSelectedFilePaths");
                if (files.length > 0)
                    onGetFile(files[0]);
            }
        });

        resultTextView = findViewById(R.id.result_text_view);

    }




    public void onClickNextButton(View view) {
        TranscriberService service = new TranscriberService(getApplication());
        Transcription transcription = new Transcription("trans-name", "trans-about",
                "trans-date",
                1,
                "",
                false,
                "ab,cd");
        service.uploadAudio(transcription);
    }


    @Override
    public void onClick(android.view.View view) {

        Log.d(toString(), "onClickListener mod file called");

        onValidationSucceeded(view);
    }

    public void onValidationSucceeded(View view) {

        formInput = new Transcription("trans-name", "trans-about",
                "trans-date",
                1,
                "",
                false,
                "ab,cd"
        );


        filePickerDialog.show();

    }

    private void onGetFile(String uri) {

        Log.d(toString(), "onGetFile");

//        formInput.local_uri = uri.replace("/sdcard",
//                Environment.getExternalStorageDirectory().getPath())
//                .replace(" ", "\\ ");


        formInput.local_uri = "file:///storage/emulated/0/Download/100-1.wav";
        //formInput.local_uri = uri;
        Log.d("MAct", "local_uri:" + formInput.local_uri);

        Timber.d(formInput.local_uri);

        mPresenter.prepareOnlineDecoding(formInput);
        mPresenter.startOfflineDecoding(formInput);

    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.start();
        Log.d("MAct", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();

        mPresenter.stop();
        Log.d("MAct", "onStop");
    }

    @Override
    public void onWorkerAvailable() {
        Log.d("MurojaahAct", "onWorkerAvailable");
    }

    @Override
    public void onWorkerNotAvailable() {
        Log.d("MurojaahAct", "onWorkerNotAvailable");
    }

    @Override
    public void onDateSet(String date) {
        Log.d("MurojaahAct", "onDateSet");
    }

    @Override
    public void startOnlineDecodingActivity(String transcriptionId) {
        Log.d("MurojaahAct", "startOnlineDecodingActivity");
    }

    @Override
    public void notifyUploadStart() {
        Log.d("MurojaahAct", "notifyUploadStart");
    }

    @Override
    public void showProgress() {
        Log.d("MurojaahAct", "showProgress");
    }

    @Override
    public void hideProgress() {
        Log.d("MurojaahAct", "hideProgress");
    }

    @Override
    public void showMessage(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Timber.e(e.getMessage());
        }
        Log.d("MurojaahAct", "showMessage. message="+message);
    }

    public void showResult(View view) {
        resultTextView.setText("");
    }
}
