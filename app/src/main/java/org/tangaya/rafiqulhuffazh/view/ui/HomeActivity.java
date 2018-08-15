package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import org.tangaya.rafiqulhuffazh.view.navigator.HomeNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.HomeViewModel;
import org.tangaya.rafiqulhuffazh.databinding.ActivityHomeBinding;
import org.tangaya.rafiqulhuffazh.R;

public class HomeActivity extends Activity implements HomeNavigator {

    public HomeViewModel mViewModel;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new HomeViewModel(getApplication());
        mViewModel.onActivityCreated(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setViewmodel(mViewModel);

//        final Observer<String> testObserver = new Observer<String>() {
//
//            @Override
//            public void onChanged(@Nullable String s) {
//                // do something here
//            }
//
//        };
//
//        mViewModel.getCobaLiveData().observe(this, testObserver);

        //setDecorView();
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
}
