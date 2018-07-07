package org.tangaya.quranasrclient.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.tangaya.quranasrclient.about.AboutActivity;
import org.tangaya.quranasrclient.devspace.DevspaceActivity;
import org.tangaya.quranasrclient.murojaah.chapterselection.ChapterSelectionActivity;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.setting.ServerSettingActivity;

public class HomeActivity extends AppCompatActivity implements HomeNavigator {

    String defaultHost = "192.168.0.217";
    String defaultPort = "8888";

    private HomeViewModel mHomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setDecorView();

        //mHomeViewModel = DataBindingUtil.setContentView(this, R.layout.activity_home);

        //HomeActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        //checkServerConnection();

    }

    private void setDecorView() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // todo: fix pattern
    @Override
    public void gotoMurojaah(View view) {
        Intent intent = new Intent(this, ChapterSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoServerSetting(View view) {
        Intent intent = new Intent(this, ServerSettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoDevspace(View view) {
        Intent intent = new Intent(this, DevspaceActivity.class);
        startActivity(intent);
    }
}
