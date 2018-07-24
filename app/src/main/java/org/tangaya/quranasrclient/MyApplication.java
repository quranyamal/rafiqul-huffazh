package org.tangaya.quranasrclient;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

import java.util.ArrayList;

import timber.log.Timber;

public class MyApplication extends Application {

    private String STATUS_ENDPOINT;

    private SharedPreferences sharedPref;

    private String hostname, port;

    private ArrayList<Evaluation> evaluations = new ArrayList<>();

    private MutableLiveData<ArrayList<Evaluation>> evalsLiveData = new MutableLiveData<>();

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = getApplicationContext().getSharedPreferences("APPLICATION_PREFERENCES",
                Context.MODE_PRIVATE);

        STATUS_ENDPOINT = "ws://"+getServerHostname()+":"+getServerPort()+"/client/ws/status";

        //connectToServer();

        QuranScriptRepository.init(this);

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

    public ArrayList<Evaluation> getEvaluations() {
        return evaluations;
    }

    public MutableLiveData<ArrayList<Evaluation>> getEvalsLiveData() {
        return evalsLiveData;
    }

    public SharedPreferences getPreferences() {
        return sharedPref;
    }

    public String getSpeechEndpoint() {
        return "ws://"+getServerHostname()+":"+getServerPort()+"/client/ws/speech";
    }

    public int getCurrentChapterNum() {
        return getPreferences().getInt("CURRENT_CHAPTER_NUM", -1) + 1;
    }
}
