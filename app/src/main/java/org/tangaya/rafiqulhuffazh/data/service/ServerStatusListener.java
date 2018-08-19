package org.tangaya.rafiqulhuffazh.data.service;

import android.arch.lifecycle.MutableLiveData;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import org.json.JSONObject;
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ServerStatusListener extends WebSocketAdapter {

    private static ServerStatusListener INSTANCE = null;

    private WebSocket serverStatusWebSocket;
    private static String statusEndpoint;

    public static String STATUS_CONNECTED = "connected";
    public static String STATUS_CONNECTING = "connecting...";
    public static String STATUS_DISCONNECTED = "disconnected";
    public static String STATUS_ERROR = "connection error";
    public static String STATUS_UNKNOWN = "unknown server status";

    MutableLiveData<Integer> numWorkersAvailable;
    MutableLiveData<String> status;
    MutableLiveData<String> errorMessage;

    public ServerStatusListener() {

        numWorkersAvailable = new MutableLiveData<>();
        status = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        status.setValue(STATUS_UNKNOWN);

        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerStatusListener getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerStatusListener();
        }
        return INSTANCE;
    }

    public MutableLiveData<Integer> getNumWorkersAvailable() {
        return numWorkersAvailable;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void connect() throws IOException {

        statusEndpoint = ServerSetting.getStatusEndpoint();

        if (!status.getValue().equals(STATUS_CONNECTED)) {
            serverStatusWebSocket = new WebSocketFactory().createSocket(statusEndpoint);
            serverStatusWebSocket.addListener(this);
            serverStatusWebSocket.connectAsynchronously();
            status.setValue(STATUS_CONNECTING);
            Timber.d("connecting to " + statusEndpoint);
        } else {
            Timber.d("connected to the server, do noting");
        }
    }

    @Override
    public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
        super.onStateChanged(websocket, newState);
        Timber.d("onStateChanged");
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);

        status.postValue(STATUS_CONNECTED);
        Timber.d("onConnected");
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
        super.onConnectError(websocket, cause);

        status.postValue(STATUS_ERROR);

        Timber.d("onConnectError");
        Timber.d(cause);
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

        status.postValue(STATUS_DISCONNECTED);
        numWorkersAvailable.postValue(0);
        Timber.d("onDisconnected");
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);

        Timber.d("onTextMessage: " + text);
        JSONObject responseJSON = new JSONObject(text);
        numWorkersAvailable.postValue(responseJSON.getInt("num_workers_available"));

        Timber.d("num worker available: " + numWorkersAvailable);
    }
}
