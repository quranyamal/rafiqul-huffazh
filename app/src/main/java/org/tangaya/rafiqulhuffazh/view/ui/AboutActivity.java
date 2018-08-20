package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.databinding.ActivityAboutBinding;
import org.tangaya.rafiqulhuffazh.view.navigator.AboutNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.AboutViewModel;

import java.util.Locale;

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
