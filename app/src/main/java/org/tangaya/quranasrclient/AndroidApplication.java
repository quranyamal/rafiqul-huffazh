package org.tangaya.quranasrclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import net.gotev.uploadservice.UploadService;

import timber.log.Timber;

public class AndroidApplication extends Application {

    private static String KEY_PREF =
            "com.rahmanadianto.transcriber.BASE_ENDPOINT_PREFERENCE";

    private String baseEndpoint;

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup Firebase database
        //-- FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // initiate Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Setup Upload service
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.rahmanadianto.transcriber";

        // Setup base endpoint
        SharedPreferences sharedPreferences =
                getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        baseEndpoint = sharedPreferences.getString("endpoint", "192.168.1.217:8888");

        Timber.d(String.format("Starting Application with endpoint %s", baseEndpoint));
    }

    public String getWSSpeechEndpoint() {
        return String.format("ws://%s/client/ws/speech", baseEndpoint);
    }

    public String getWSStatusEndpoint() {
        return String.format("ws://%s/client/ws/status", baseEndpoint);
    }

    public String getRESTEndpoint() {
        return String.format("http://%s/client/dynamic/", baseEndpoint);
    }

    public void setBaseEndpoint(String endpoint) {
        baseEndpoint = endpoint;

        SharedPreferences sharedPreferences =
                getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("endpoint", endpoint).apply();

        Timber.d(endpoint);
    }
}
