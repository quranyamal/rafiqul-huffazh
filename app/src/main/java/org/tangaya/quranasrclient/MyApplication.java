package org.tangaya.quranasrclient;

import android.app.Application;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

import java.io.IOException;
import java.net.Socket;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        QuranScriptRepository.init(this);
    }

}
