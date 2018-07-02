package org.tangaya.quranasrclient.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;

public class ServerSettingActivity extends AppCompatActivity implements ServerSettingNavigator {

    String hostname;
    String port;
    TextView statusTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_setting);
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

    // todo: fix pattern
    @Override
    public void onSettingSaved(View view) {
        finish();
    }

    @Override
    public void onSettingCancelled(View view) {
        finish();
    }
}
