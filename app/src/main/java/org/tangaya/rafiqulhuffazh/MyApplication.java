package org.tangaya.rafiqulhuffazh;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;

import java.util.ArrayList;

import timber.log.Timber;

public class MyApplication extends Application {

    private SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.d("Starting application");

        sharedPref = getApplicationContext().getSharedPreferences("APPLICATION_PREFERENCES",
                Context.MODE_PRIVATE);

        ServerSetting.setHostname(getServerHostname());
        ServerSetting.setPort(getServerPort());
        ServerSetting.applySetting();

        //connectToServer();
        QuranUtil.init(getApplicationContext().getAssets());
        QuranScriptConverter.init(getApplicationContext());

        Timber.d("after quran script repo init");

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
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

//    public void connectToServer() {
//
//
//        try {
//            serverStatusWebSocket = new WebSocketFactory().createSocket(STATUS_ENDPOINT);
//            serverStatusWebSocket.addListener(new WebSocketAdapter() {
//                @Override
//                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
//                    super.onConnected(websocket, headers);
//
//                    Log.d("MyApplication", "onConnected serverStatusWebSocket");
//                }
//
//                @Override
//                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
//                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
//
//                    Log.d("MyApplication", "onDisonnected serverStatusWebSocket");
//                }
//
//                @Override
//                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
//                    super.onConnectError(websocket, exception);
//
//                    Log.d("MyApplication", "onConnectError serverStatusWebSocket");
//                }
//
//                @Override
//                public void onTextMessage(WebSocket websocket, String text) throws Exception {
//                    super.onTextMessage(websocket, text);
//
//                    Log.d("MyApplication", "onTextMessage: " + text);
//                }
//            });
//
//            serverStatusWebSocket.connectAsynchronously();
//            Log.d("MyApplication", "connecting...");
//            Log.d("MyApplication", "STATUS_ENDPOINT: " + STATUS_ENDPOINT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
