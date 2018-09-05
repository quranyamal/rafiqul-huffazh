package org.tangaya.rafiqulhuffazh;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.data.service.ServerStatusListener;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;

import timber.log.Timber;

public class MyApplication extends Application {

    private SharedPreferences sharedPref;
    private ServerStatusListener serverStatusListener;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.d("Starting application");

        sharedPref = getApplicationContext().getSharedPreferences("APPLICATION_PREFERENCES",
                Context.MODE_PRIVATE);

        ServerSetting.setHostname(getServerHostname());
        ServerSetting.setPort(getServerPort());
        ServerSetting.applySetting();

        QuranUtil.init(getApplicationContext().getAssets());
        QuranScriptConverter.init(getApplicationContext().getAssets());

        serverStatusListener = ServerStatusListener.getInstance();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public ServerStatusListener getServerStatusListener() {
        return serverStatusListener;
    }

    public String getServerHostname() {
        return sharedPref.getString("SERVER_HOSTNAME", "0.0.0.0");
    }

    public String getServerPort() {
        return sharedPref.getString("SERVER_PORT", "0");
    }

    public SharedPreferences getPreferences() {
        return sharedPref;
    }

    public int getCurrentSurahNum() {
        return getPreferences().getInt("CURRENT_SURAH_NUM", -1) + 1;
    }
}
