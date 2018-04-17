package org.tangaya.quranasrclient.domain;

import android.util.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by Rahman Adianto on 11-Apr-17.
 */

public class ConnectWSInteractorImpl extends AbstractInteractor implements ConnectWSInteractor {

    private ConnectWSInteractor.Callback mCallback;
    private DecoderWSRepository mRepository;

    public ConnectWSInteractorImpl(Executor threadExecutor,
                                   MainThread mainThread,
                                   Callback callback,
                                   DecoderWSRepository repository) {

        super(threadExecutor, mainThread);

        mCallback = callback;
        mRepository = repository;
    }

    private void notifySocketConnected(final Map<String, List<String>> headers) {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onConnected(headers);
            }
        });
    }

    private void notifySocketGotTextMessage(final String message) {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onTextMessage(message);
            }
        });
    }

    private void notifySocketDisconnected() {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onDisconnected();
            }
        });
    }

    private void notifySocketError(final String error) {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onSocketError(error);
            }
        });
    }

    private void notifySendError(final String error) {

        mMainThread.post(new Runnable() {

            @Override
            public void run() {
                mCallback.onSendError(error);
            }
        });
    }

    @Override
    public void run() {

        mRepository.connect(new DecoderWSRepository.Callback() {
            @Override
            public void onConnected(Map<String, List<String>> headers) {
                Log.d("SOCKET", "socket terhubung");
                notifySocketConnected(headers);
            }

            @Override
            public void onDisconnected() {
                Log.d("SOCKET", "socket terputus");
                notifySocketDisconnected();
            }

            @Override
            public void onError(String error) {
                Log.d("SOCKET", "socket error");
                notifySocketError(error);
            }

            @Override
            public void onTextMessage(String message) {
                Log.d("MESSAGE", "pesan: " + message);
                notifySocketGotTextMessage(message);
            }

            @Override
            public void onSendError(String error) {
                Log.d("ERROR", "send error: " + error);
                notifySendError(error);
            }
        });
    }
}
