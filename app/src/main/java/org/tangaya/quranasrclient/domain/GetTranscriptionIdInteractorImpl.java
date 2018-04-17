package org.tangaya.quranasrclient.domain;

import android.util.Log;

import org.tangaya.quranasrclient.domain.model.Transcription;

/**
 * Created by Rahman Adianto on 23-Apr-17.
 */

public class GetTranscriptionIdInteractorImpl extends AbstractInteractor
        implements GetTranscriptionIdInteractor {

    GetTranscriptionIdInteractor.Callback mCallback;
    DecoderRepository mRepository;
    Transcription mData;

    public GetTranscriptionIdInteractorImpl(Executor threadExecutor,
                                            MainThread mainThread,
                                            Callback callback,
                                            DecoderRepository repository,
                                            Transcription data) {

        super(threadExecutor, mainThread);


        mCallback = callback;
        mRepository = repository;
        mData = data;
        Log.d(toString(), "GetTranscriptionIdInteractorImpl constructed");
    }

    private void notifyOnResult(final String result) {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onTranscriptionIdResult(result);
            }
        });
    }

    private void notifyOnError() {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onTranscriptionIdError();
            }
        });
    }

    @Override
    public void run() {

        Log.d(toString(), "run() on GetTranscriptionIdInteractorImpl");

        //String transcriptionId = mRepository.requestTranscriptionId(mData);
        String transcriptionId = "transc_id_123";

        Log.d("GTransIdInteractorImpl", "transcriptionId: " + transcriptionId );

        if (transcriptionId != null) {
            notifyOnResult(transcriptionId);
            Log.d(toString(), "transcription id nya tidak null");
        }
        else {
            notifyOnError();
            Log.d(toString(), "transcription id nya null");
        }
    }
}