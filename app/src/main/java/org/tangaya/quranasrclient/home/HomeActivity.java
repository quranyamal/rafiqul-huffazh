package org.tangaya.quranasrclient.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.tangaya.quranasrclient.util.AudioRecordTest;
import org.tangaya.quranasrclient.murojaah.MurojaahActivity;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.config.ServerConfigActivity;


public class HomeActivity extends AppCompatActivity {

    String defaultHost = "192.168.0.217";
    String defailtPort = "8888";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //checkServerConnection();

    }

    public void gotoMorojaah(View view) {
        Intent intent = new Intent(this, MurojaahActivity.class);
        startActivity(intent);
    }

    public void gotoRecordTest(View view) {
        Intent intent = new Intent(this, AudioRecordTest.class);
        startActivity(intent);
    }

    public void gotoServerConfig(View view) {
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }
}
