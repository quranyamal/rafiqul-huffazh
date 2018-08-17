package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.view.navigator.ServerSettingNavigator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ServerSettingViewModel extends AndroidViewModel {

    public static String CONNECTION_STATUS_CONNECTED = "connected";
    public static String CONNECTION_STATUS_DISCONNECTED = "disconnected";
    public static String CONNECTION_STATUS_ERROR = "error";
    public static String CONNECTION_STATUS_CONNECTING = "connecting...";

    ServerSettingNavigator mNavigator;

    public final ObservableField<String> hostname = new ObservableField<>();
    public final ObservableField<String> port = new ObservableField<>();
    public final ObservableField<String> connectionStatus = new ObservableField<>();

    public ServerSettingViewModel(@NonNull Application application) {
        super(application);

        hostname.set(ServerSetting.getHostname());
        port.set(ServerSetting.getPort());
    }

    public void onActivityCreated(ServerSettingNavigator navigator) {
        mNavigator = navigator;
    }

    public void testConnection() {
        ServerSetting.setHostname(hostname.get());
        ServerSetting.setPort(port.get());
        ServerSetting.applySetting();
        String endpoint = ServerSetting.getStatusEndpoint();

        try {
            WebSocket ws = new WebSocketFactory().createSocket(endpoint);
            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);

                    connectionStatus.set(CONNECTION_STATUS_CONNECTED);
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                    connectionStatus.set(CONNECTION_STATUS_DISCONNECTED);
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    super.onConnectError(websocket, exception);

                    connectionStatus.set(CONNECTION_STATUS_ERROR);
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Log.d("DVM", "onTextMessage. message: " + text);
                }
            });

            ws.connectAsynchronously();
            connectionStatus.set("connecting...");
            Log.d("SSVM", "connecting to " + endpoint);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSetting() {
        ServerSetting.setHostname(hostname.get());
        ServerSetting.setPort(port.get());
        ServerSetting.applySetting();

        mNavigator.onSaveSetting(hostname.get(), port.get());
    }

    public void cancelSetting() {
        mNavigator.onSettingCancelled();
    }
}
