package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.MutableLiveData;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ASRServerStatusListener {

    WebSocket serverStatusWebSocket;

    String STATUS_ENDPOINT;

    MutableLiveData<Integer> numWorkersAvailable;

    public ASRServerStatusListener(String hostname, String port) {

        STATUS_ENDPOINT = "ws://"+hostname+":"+port+"/client/ws/status";

        init();

        numWorkersAvailable = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getNumWorkersAvailable() {
        return numWorkersAvailable;
    }

    public void init() {


        try {
            serverStatusWebSocket = new WebSocketFactory().createSocket(STATUS_ENDPOINT);
            serverStatusWebSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);

                    Timber.d("onConnected serverStatusWebSocket");
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                    Timber.d("onDisonnected serverStatusWebSocket");
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    super.onConnectError(websocket, exception);

                    Timber.d("onConnectError serverStatusWebSocket");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Timber.d("onTextMessage: " + text);
                    JSONObject responseJSON = new JSONObject(text);
                    numWorkersAvailable.postValue(responseJSON.getInt("num_workers_available"));

                    Timber.d("num worker available: " + numWorkersAvailable);
                }
            });

            serverStatusWebSocket.connectAsynchronously();
            Timber.d("connecting to " + STATUS_ENDPOINT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
