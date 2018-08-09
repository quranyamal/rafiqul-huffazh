package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.databinding.ActivityServerSettingBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.ServerSettingNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.ServerSettingViewModel;

public class ServerSettingActivity extends Activity implements ServerSettingNavigator {

    public ServerSettingViewModel mViewModel;
    private ActivityServerSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mViewModel = new ServerSettingViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_setting);
        binding.setViewmodel(mViewModel);

//        Toolbar toolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);

        setTitle("Server Setting");
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
