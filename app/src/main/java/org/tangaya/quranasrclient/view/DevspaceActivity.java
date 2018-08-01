package org.tangaya.quranasrclient.view;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.EvaluationOld;
import org.tangaya.quranasrclient.databinding.ActivityDevspaceBinding;
import org.tangaya.quranasrclient.navigator.DevspaceNavigator;
import org.tangaya.quranasrclient.viewmodel.DevspaceViewModel;

import java.util.ArrayList;

import timber.log.Timber;


public class DevspaceActivity extends AppCompatActivity  implements DevspaceNavigator {

//    private Handler mHandler = new Handler();

    public DevspaceViewModel mViewModel;
    private ActivityDevspaceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new DevspaceViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        final Observer<Integer> numWorkerObserver = new Observer<Integer>() {

            @Override
            public void onChanged(@Nullable Integer numAvailWorkers) {
                Timber.d("num worker has been changed ==> " + numAvailWorkers);
                mViewModel.numAvailableWorkers.set(numAvailWorkers);
                if (numAvailWorkers>0) {
                    mViewModel.dequeueRecognitionTasks();
                }
            }
        };

        mViewModel.getStatusListener().getNumWorkersAvailable().observe(this, numWorkerObserver);

        final Observer<ArrayList<EvaluationOld>> evalsObserver = new Observer<ArrayList<EvaluationOld>>() {
            @Override
            public void onChanged(@Nullable ArrayList<EvaluationOld> evaluations) {
                Timber.d("eval set has changed");

            }
        };

        mViewModel.getEvalsMutableLiveData().observe(this, evalsObserver);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_devspace);
        binding.setViewmodel(mViewModel);
     }

    @Override
    public void gotoEvalDetail() {
        Intent intent = new Intent(this, DevspaceDetailActivity.class);
        startActivity(intent);
    }

}
