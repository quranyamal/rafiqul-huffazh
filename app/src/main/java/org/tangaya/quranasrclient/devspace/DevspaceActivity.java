package org.tangaya.quranasrclient.devspace;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityDevspaceBinding;


public class DevspaceActivity extends AppCompatActivity {

//    private Handler mHandler = new Handler();

    public DevspaceViewModel mViewModel;
    private ActivityDevspaceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new DevspaceViewModel(this.getApplication());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_devspace);
        binding.setViewmodel(mViewModel);

        //setContentView(R.layout.activity_devspace);
    }

}
