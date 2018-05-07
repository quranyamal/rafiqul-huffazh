package org.tangaya.quranasrclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;

public class ServerConfigActivity extends AppCompatActivity {

    String hostname;
    String port;
    TextView statusTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);
    }

    public void onClickConnect(View view) {

        TextView hostnameTv = findViewById(R.id.hostname);
        TextView portTv = findViewById(R.id.port);
        statusTv = findViewById(R.id.status);

        hostname = hostnameTv.getText().toString();
        port = portTv.getText().toString();

        //WebSocket ws;

        //ConnectToWSTask checkServerTask = new ConnectToWSTask(ws, statusTv);
        //checkServerTask.execute();
        //Toast.makeText(this,"connecting...", Toast.LENGTH_SHORT).show();
    }

    public void onClickSave(View view) {
        finish();
    }
}
