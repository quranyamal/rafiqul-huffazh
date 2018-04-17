package org.tangaya.quranasrclient.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Environment;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.DatePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.DisconnectWSInteractorImpl;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.domain.ConnectWSInteractor;
import org.tangaya.quranasrclient.domain.ConnectWSInteractorImpl;
import org.tangaya.quranasrclient.domain.DecoderWSRepository;
import org.tangaya.quranasrclient.domain.DisconnectWSInteractor;
import org.tangaya.quranasrclient.domain.Executor;
import org.tangaya.quranasrclient.domain.GetTranscriptionIdInteractor;
import org.tangaya.quranasrclient.domain.GetTranscriptionIdInteractorImpl;
import org.tangaya.quranasrclient.domain.MainThread;
import org.tangaya.quranasrclient.domain.DecoderRepository;
import org.tangaya.quranasrclient.domain.UploadAudioInteractor;
import org.tangaya.quranasrclient.domain.UploadAudioInteractorImpl;
import org.tangaya.quranasrclient.domain.model.Transcription;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MurojaahPresenterImpl extends AbstractPresenter
    implements MurojaahPresenter, UploadAudioInteractor.Callback,
        GetTranscriptionIdInteractor.Callback, ConnectWSInteractor.Callback  {

    private DecoderRepository mDecoderRepository;
    private DecoderWSRepository mTranscriberWSRepository;
    private MurojaahPresenter.View mView;

    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    public MurojaahPresenterImpl(Executor executor, MainThread mainThread,
                                 DecoderRepository decoderRepository,
                                 DecoderWSRepository transcriberWSRepository,
                                 MurojaahPresenter.View view) {

        super(executor, mainThread);

        mDecoderRepository = decoderRepository;
        mTranscriberWSRepository = transcriberWSRepository;
        mView = view;
        mCalendar = Calendar.getInstance();
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                mView.onDateSet(sdf.format(mCalendar.getTime()));

                Timber.d(sdf.format(mCalendar.getTime()));

            }
        };
        Log.d("MPI", "MurojaahPresenterImpl constructed");
    }


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void start() {

        ConnectWSInteractor connect = new ConnectWSInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mTranscriberWSRepository
        );

        connect.execute();
    }

    @Override
    public void stop() {

        DisconnectWSInteractor interactor = new DisconnectWSInteractorImpl(
                mExecutor,
                mMainThread,
                mTranscriberWSRepository
        );

        interactor.execute();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void startOfflineDecoding(Transcription transcription) {

        Log.d(toString(), "startOfflineDecoding");

        UploadAudioInteractor interactor = new UploadAudioInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mDecoderRepository,
                transcription
        );

        interactor.execute();
    }

    @Override
    public void onUploadStart() {
        mView.notifyUploadStart();
    }

    @Override
    public void showDatePicker(Context ctx) {

        new DatePickerDialog(ctx, R.style.DatePickerTheme, mDateSetListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void prepareOnlineDecoding(Transcription transcription) {

        Log.d("POD", "inside prepareOnlineDecoding method");

        //Show progress
        mView.showProgress();
        //Prepare uri
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Transcriber");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            transcription.local_uri = folder.getPath();
        }
        else {
            transcription.local_uri =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .getPath();
        }
        transcription.local_uri +=
                File.separator + transcription.name.replace(" ", "\\ ") + ".wav";
        //Get transcription id
        Log.d("MurPres", "GetTranscriptionIdInteractor constructing");
        GetTranscriptionIdInteractor interactor = new GetTranscriptionIdInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mDecoderRepository,
                transcription
        );

        Log.d("POD", "before interactor.execute()");
        interactor.execute();
    }

    @Override
    public void onTranscriptionIdResult(String transcriptionId) {
        mView.hideProgress();
        mView.startOnlineDecodingActivity(transcriptionId);
    }

    @Override
    public void onTranscriptionIdError() {
        mView.hideProgress();
        mView.showMessage("Periksa koneksi internet anda");
    }

    @Override
    public void onConnected(Map<String, List<String>> headers) {

        Timber.d("Status worker websocket connected");
        Log.d("MPI", "onConnected");
    }

    @Override
    public void onDisconnected() {

        Timber.d("Status worker websocket disconnected");
        Log.d("MPI", "onDisconnected");

    }

    @Override
    public void onSocketError(String error) {

        mView.showMessage("Periksa koneksi internet anda");
        Log.d("MPI", "onSocketError");
    }

    @Override
    public void onTextMessage(String message) {

        Timber.d(message);

        try {
            JSONObject json = new JSONObject(message);
            int num_worker = json.getInt("num_workers_available");

            if (num_worker > 0) {
                mView.onWorkerAvailable();
            }
            else {
                mView.onWorkerNotAvailable();
            }
        }
        catch (JSONException e) {
            Timber.e(e.getMessage());
        }
        Log.d("MPI", "onTextMessage");
    }

    @Override
    public void onSendError(String error) {
        Log.d("MPI", "onSendError");
    }
}
