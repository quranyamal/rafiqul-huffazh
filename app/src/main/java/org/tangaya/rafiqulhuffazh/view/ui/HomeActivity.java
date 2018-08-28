package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.tangaya.rafiqulhuffazh.view.navigator.HomeNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.HomeViewModel;
import org.tangaya.rafiqulhuffazh.databinding.ActivityHomeBinding;
import org.tangaya.rafiqulhuffazh.R;

import timber.log.Timber;

public class HomeActivity extends Activity implements LifecycleOwner, HomeNavigator {

    public HomeViewModel mViewModel;
    private ActivityHomeBinding binding;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new HomeViewModel(getApplication());
        mViewModel.onActivityCreated(this);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setViewmodel(mViewModel);

        final Observer<String> serverStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String status) {
                mViewModel.setServerStatus(status);
            }
        };
        mViewModel.getServerStatusListener().getStatus().observe(this, serverStatusObserver);
    }

    @Override
    public void gotoMurojaah() {
        Intent intent = new Intent(this, SurahSelectionActivity.class);
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

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
