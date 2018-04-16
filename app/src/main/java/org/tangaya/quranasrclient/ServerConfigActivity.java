package org.tangaya.quranasrclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class ServerConfigActivity extends AppCompatActivity {

    Button connectBtn;
    TextView serverStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);

        connectBtn = findViewById(R.id.connect_server_button);
        serverStatusTextView = findViewById(R.id.server_status_text_view);
    }

    public void onClickConnectButton(View view) {
        CheckServerTask checkServerTask = new CheckServerTask();
        checkServerTask.execute();
        Toast.makeText(getApplicationContext(), "connecting...", Toast.LENGTH_SHORT).show();
    }

    public void onClickStartButton(View view) {
        Intent intent = new Intent(this, MurojaahActivity.class);
        startActivity(intent);
    }

    private class CheckServerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                WebSocketFactory wsFactory = new WebSocketFactory();
                WebSocket ws = wsFactory.createSocket("ws://192.168.1.217:8888/client/ws/status");
                ws.connect();

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (WebSocketException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isServerAvailable) {
            super.onPostExecute(isServerAvailable);

            if (isServerAvailable) {
                serverStatusTextView.setText("server tersedia");
                connectBtn.setEnabled(false);
            } else {
                serverStatusTextView.setText("server tidak tersedia");
            }
        }

    }

}
