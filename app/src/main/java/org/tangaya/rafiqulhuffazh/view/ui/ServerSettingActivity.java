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

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.ServerSetting;
import org.tangaya.rafiqulhuffazh.databinding.ActivityServerSettingBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.ServerSettingNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.ServerSettingViewModel;

import java.io.IOException;

public class ServerSettingActivity extends Activity implements LifecycleOwner, ServerSettingNavigator {

    private ServerSettingViewModel mViewModel;
    private ActivityServerSettingBinding mBinding;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ServerSettingViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_server_setting);
        mBinding.setViewmodel(mViewModel);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        setTitle("Server Setting");

        final Observer<String> connectionStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String connectionStatus) {

                mViewModel.connectionStatus.set(connectionStatus);
                if (connectionStatus.equals("connection error")) {
                    mViewModel.errorInfo.set("connection refused");
                }
                //todo: set editable false in case of connected
            }
        };
        ((MyApplication)getApplication()).getServerStatusListener()
                .getStatus().observe(this, connectionStatusObserver);
    }

    @Override
    public void onClickConnect() {
        ServerSetting.setHostname(mViewModel.hostname.get());
        ServerSetting.setPort(mViewModel.port.get());
        ServerSetting.applySetting();

        try {
            ((MyApplication)getApplication()).getServerStatusListener().connect();
        } catch (IOException e) {
            mViewModel.errorInfo.set("invalid address");
            e.printStackTrace();
        }
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
