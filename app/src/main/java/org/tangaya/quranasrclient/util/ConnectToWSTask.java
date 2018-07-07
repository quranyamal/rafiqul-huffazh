package org.tangaya.quranasrclient.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;

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

    public ConnectToWSTask(WebSocket ws) {
        this.ws = ws;
    }

    @Override
    protected Boolean doInBackground(WebSocket... webSockets) {

        try {
            ws.connect();
            Log.d("ConnectToWSTask", "connecting to ws");
        } catch (WebSocketException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(String... transcript) {
        super.onProgressUpdate(transcript);

//        resultTv.setText(transcript[0]);
//
//        Log.d("async", "resultTv: " + resultTv);
    }

    @Override
    protected void onPostExecute(Boolean isServerAvailable) {
        super.onPostExecute(isServerAvailable);

//        if (isServerAvailable) {
//            //statusTv.setText("server tersedia");
//            serverStatusTv.setText("terhubung ke server");
//            Log.d("MurojaahActivity", "server tersedia");
//
//        } else {
//            serverStatusTv.setText("server tidak tersedia");
//            //statusTv.setText("server tidak tersedia");
//            Log.d("MurojaahActivity", "server tidak tersedia");
//        }
    }

}