package org.tangaya.quranasrclient.devspace;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.ActivityDevspaceBinding;


public class DevspaceActivity extends AppCompatActivity  implements DevspaceNavigator {

//    private Handler mHandler = new Handler();

    public DevspaceViewModel mViewModel;
    private ActivityDevspaceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new DevspaceViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_devspace);
        binding.setViewmodel(mViewModel);
     }

    @Override
    public void gotoEvalDetail() {
        Intent intent = new Intent(this, DevspaceDetailActivity.class);
        startActivity(intent);
    }

}
