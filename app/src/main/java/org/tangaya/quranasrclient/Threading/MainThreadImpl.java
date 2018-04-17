package org.tangaya.quranasrclient.Threading;

import android.os.Handler;
import android.os.Looper;

import org.tangaya.quranasrclient.domain.MainThread;

/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
public class MainThreadImpl implements MainThread {

    private static MainThread sMainThread;

    private Handler mHandler;

    public static MainThread getInstance() {
        if (sMainThread == null) {
            sMainThread = new MainThreadImpl();
        }

        return sMainThread;
    }

    private MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    @Override
    public void postDelayed(Runnable runnable, long delayMillis) {

        mHandler.postDelayed(runnable, delayMillis);
    }
}
