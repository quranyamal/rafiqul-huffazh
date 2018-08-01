package org.tangaya.quranasrclient.view;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.adapter.AttemptAdapter;
import org.tangaya.quranasrclient.data.EvaluationOld;
import org.tangaya.quranasrclient.viewmodel.DevspaceDetailViewModel;

import java.util.ArrayList;

import timber.log.Timber;

public class DevspaceDetailActivity extends AppCompatActivity {

    DevspaceDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devspace_detail);

        final RecyclerView recyclerView = findViewById(R.id.devspace_detail_recycler);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //EvaluationOld attempt = ((MyApplication) getApplication()).getEvaluations().get(0);

        Timber.d("debug versenum error 1");
        mViewModel = new DevspaceDetailViewModel(getApplication(), new EvaluationOld(2,2,1));
        Timber.d("debug versenum error 2");
        mViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<DevspaceDetailViewModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DevspaceDetailViewModel> devspaceDetailViewModels) {

                AttemptAdapter mAdapter = new AttemptAdapter(DevspaceDetailActivity.this, devspaceDetailViewModels);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
            }
        });
        Timber.d("debug versenum error 3");

    }
}
