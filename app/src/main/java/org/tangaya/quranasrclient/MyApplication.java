package org.tangaya.quranasrclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application {

    private String STATUS_ENDPOINT;

    private SharedPreferences sharedPref;

    private int numWorkerAvailable;

    WebSocket serverStatusWebSocket;

    private String hostname, port;

    @Override
    public void onCreate() {
        super.onCreate();

        QuranScriptRepository.init(this);

        sharedPref = getApplicationContext().getSharedPreferences("APPLICATION_PREFERENCES",
                Context.MODE_PRIVATE);

        STATUS_ENDPOINT = "ws://"+getServerHostname()+":"+getServerPort()+"/client/ws/status";

        try {
            serverStatusWebSocket = new WebSocketFactory().createSocket(STATUS_ENDPOINT);
            serverStatusWebSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);

                    Log.d("MyApplication", "onConnected serverStatusWebSocket");
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                    Log.d("MyApplication", "onDisonnected serverStatusWebSocket");
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    super.onConnectError(websocket, exception);

                    Log.d("MyApplication", "onConnectError serverStatusWebSocket");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Log.d("MyApplication", "onTextMessage: " + text);
                }
            });

            serverStatusWebSocket.connectAsynchronously();
            Log.d("MyApplication", "connecting...");
            Log.d("MyApplication", "STATUS_ENDPOINT: " + STATUS_ENDPOINT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerHostname() {
        return sharedPref.getString("SERVER_HOSTNAME", "0.0.0.0");
    }

    public String getServerPort() {
        return sharedPref.getString("SERVER_PORT", "0");
    }

    public void connectToServer() {

    }

    public SharedPreferences getPreferences() {
        return sharedPref;
    }

    public String getSpeechEndpoint() {
        return "ws://"+getServerHostname()+":"+getServerPort()+"/client/ws/speech";
    }
}
