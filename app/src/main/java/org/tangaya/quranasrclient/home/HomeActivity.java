package org.tangaya.quranasrclient.home;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.tangaya.quranasrclient.about.AboutActivity;
import org.tangaya.quranasrclient.databinding.ActivityHomeBinding;
import org.tangaya.quranasrclient.devspace.DevspaceActivity;
import org.tangaya.quranasrclient.murojaah.chapterselection.ChapterSelectionActivity;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.setting.ServerSettingActivity;

public class HomeActivity extends AppCompatActivity implements HomeNavigator {

    public HomeViewModel mViewModel;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new HomeViewModel(getApplication());
        mViewModel.onActivityCreated(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setViewmodel(mViewModel);

        setDecorView();
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

    @Override
    public void gotoMurojaah() {
        Intent intent = new Intent(this, ChapterSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoServerSetting() {
        Intent intent = new Intent(this, ServerSettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoDevspace() {
        Intent intent = new Intent(this, DevspaceActivity.class);
        startActivity(intent);
    }
}
