package org.tangaya.quranasrclient;

import android.app.Application;

import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        QuranScriptRepository.init(this);
    }
}
