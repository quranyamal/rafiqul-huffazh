package org.tangaya.quranasrclient.view.ui;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityAboutBinding;
import org.tangaya.quranasrclient.view.navigator.AboutNavigator;
import org.tangaya.quranasrclient.viewmodel.AboutViewModel;

public class AboutActivity extends Activity implements AboutNavigator {

    private AboutViewModel mViewModel;
    private ActivityAboutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new AboutViewModel();
        mViewModel.onActivityCreated(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        mBinding.setViewmodel(mViewModel);

        setTitle("About");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void backToHome() {
        finish();
    }
}
