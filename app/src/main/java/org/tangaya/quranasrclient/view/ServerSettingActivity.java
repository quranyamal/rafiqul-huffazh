package org.tangaya.quranasrclient.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityServerSettingBinding;
import org.tangaya.quranasrclient.navigator.ServerSettingNavigator;
import org.tangaya.quranasrclient.viewmodel.ServerSettingViewModel;

public class ServerSettingActivity extends AppCompatActivity implements ServerSettingNavigator {

    public ServerSettingViewModel mViewModel;
    private ActivityServerSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ServerSettingViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_setting);
        binding.setViewmodel(mViewModel);
    }

    public void onClickSave(View view) {
        finish();
    }


    @Override
    public void onSettingSaved() {
        finish();
    }

    @Override
    public void onSettingCancelled() {
        finish();
    }
}
