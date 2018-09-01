package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;


import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.data.service.ServerStatusListener;
import org.tangaya.rafiqulhuffazh.view.navigator.ServerSettingNavigator;

import java.io.IOException;

public class ServerSettingViewModel extends AndroidViewModel {

    public static String CONNECTION_STATUS_CONNECTED = "connected";
    public static String CONNECTION_STATUS_ERROR = "error";

    public final ObservableField<String> hostname = new ObservableField<>();
    public final ObservableField<String> port = new ObservableField<>();
    public final ObservableField<String> connectionStatus = new ObservableField<>();
    public final ObservableField<String> errorInfo = new ObservableField<>("");

    private ServerStatusListener serverStatusListener;
    ServerSettingNavigator mNavigator;

    public ServerStatusListener getServerStatusListener() {
        return serverStatusListener;
    }

    public ServerSettingViewModel(@NonNull Application application) {
        super(application);

        hostname.set(ServerSetting.getHostname());
        port.set(ServerSetting.getPort());

        serverStatusListener = ServerStatusListener.getInstance();
    }

    public void onActivityCreated(ServerSettingNavigator navigator) {
        mNavigator = navigator;
    }

    public void connect() {
        ServerSetting.setHostname(hostname.get());
        ServerSetting.setPort(port.get());
        ServerSetting.applySetting();

        try {
            serverStatusListener.connect();
        } catch (IOException e) {
            errorInfo.set("invalid address");
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
