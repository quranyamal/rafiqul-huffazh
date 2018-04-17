package org.tangaya.quranasrclient.domain;

import android.util.Log;

import org.tangaya.quranasrclient.domain.model.Transcription;

/**
 * Created by Rahman Adianto on 22-Apr-17.
 */

public class UploadAudioInteractorImpl extends AbstractInteractor
        implements UploadAudioInteractor {


    private UploadAudioInteractor.Callback mCallback;
    private DecoderRepository mRepository;
    private Transcription mTranscription;

    public UploadAudioInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     Callback callback,
                                     DecoderRepository repository,
                                     Transcription transcription) {

        super(threadExecutor, mainThread);
        mCallback = callback;
        mRepository = repository;
        mTranscription = transcription;
    }

    private void notifyStartUploadSuccess() {

        Log.d("UAI", "notifyStartUploadSuccess");

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                Log.d("UAI", "inside run notifyStartUploadSuccess");
                mCallback.onUploadStart();
            }
        });
    }

    @Override
    public void run() {

        Log.d(toString(), "run() on upload audio interacotor");

        boolean uploadStart = mRepository.uploadAudio(mTranscription);

        if (uploadStart) {
            Log.d(toString(), "inside if uploadStart");
            notifyStartUploadSuccess();
        }
    }
}