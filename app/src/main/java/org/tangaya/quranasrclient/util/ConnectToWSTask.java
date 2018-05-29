package org.tangaya.quranasrclient.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ConnectToWSTask extends AsyncTask<WebSocket, String, Boolean> {

    private Context mContext;

    private WebSocket ws;
    private TextView resultTv;
    private TextView serverStatusTv;


    public ConnectToWSTask(WebSocket ws, TextView serverStatusTv, TextView resultTv) {
        this.ws = ws;
        this.resultTv = resultTv;
        this.serverStatusTv = serverStatusTv;
    }


    @Override
    protected Boolean doInBackground(WebSocket... webSockets) {

        try {
            ws.connect();
            Log.d("MANew", "connecting to ws");
        } catch (WebSocketException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(String... transcript) {
        super.onProgressUpdate(transcript);

        resultTv.setText(transcript[0]);

        Log.d("async", "resultTv: " + resultTv);
    }

    @Override
    protected void onPostExecute(Boolean isServerAvailable) {
        super.onPostExecute(isServerAvailable);

        if (isServerAvailable) {
            //statusTv.setText("server tersedia");
            serverStatusTv.setText("terhubung ke server");
            Log.d("MurojaahActivity", "server tersedia");

        } else {
            serverStatusTv.setText("server tidak tersedia");
            //statusTv.setText("server tidak tersedia");
            Log.d("MurojaahActivity", "server tidak tersedia");
        }
    }

}