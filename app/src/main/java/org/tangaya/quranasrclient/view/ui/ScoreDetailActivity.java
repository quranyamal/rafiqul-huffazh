package org.tangaya.quranasrclient.view.ui;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.model.EvaluationOld;
import org.tangaya.quranasrclient.databinding.ActivityScoreDetailBinding;
import org.tangaya.quranasrclient.view.adapter.EvalAdapter;
import org.tangaya.quranasrclient.viewmodel.EvalDetailViewModel;

import java.util.ArrayList;

public class ScoreDetailActivity extends Activity implements LifecycleOwner {

    private EvalDetailViewModel mViewModel;
    private ActivityScoreDetailBinding mBinding;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score_detail);

        setTitle("Score Detail");

        //setSupportActionBar(findViewById(R.id.my_toolbar));


        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        final RecyclerView recyclerView = findViewById(R.id.score_detail_recycler);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //EvaluationOld attempt = ((MyApplication) getApplication()).getEvaluations().get(0);

        mViewModel = new EvalDetailViewModel(getApplication(), new EvaluationOld(1,1,1));
        mViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<EvalDetailViewModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<EvalDetailViewModel> devspaceDetailViewModels) {

                EvalAdapter mAdapter = new EvalAdapter(ScoreDetailActivity.this, devspaceDetailViewModels);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
