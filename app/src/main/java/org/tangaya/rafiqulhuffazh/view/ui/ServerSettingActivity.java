package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.service.ServerStatusListener;
import org.tangaya.rafiqulhuffazh.databinding.ActivityServerSettingBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.ServerSettingNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.ServerSettingViewModel;

public class ServerSettingActivity extends Activity implements LifecycleOwner, ServerSettingNavigator {

    public ServerSettingViewModel mViewModel;
    private ActivityServerSettingBinding binding;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ServerSettingViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_setting);
        binding.setViewmodel(mViewModel);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

//        Toolbar toolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);

        setTitle("Server Setting");

        final Observer<String> connectionStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String connectionStatus) {

                mViewModel.connectionStatus.set(connectionStatus);

                if (connectionStatus.equals(ServerStatusListener.STATUS_ERROR)) {
                    mViewModel.errorInfo.set("connection refused");
                }
                //todo: set editable false in case of connected
            }
        };
        mViewModel.getServerStatusListener().getStatus().observe(this, connectionStatusObserver);
    }

    @Override
    public void onSaveSetting(String hostname, String port) {
        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SERVER_HOSTNAME", hostname);
        editor.putString("SERVER_PORT", port);
        editor.commit();
        finish();
    }

    @Override
    public void onSettingCancelled() {
        finish();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
