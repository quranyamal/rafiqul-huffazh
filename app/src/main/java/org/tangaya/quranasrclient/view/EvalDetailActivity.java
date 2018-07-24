package org.tangaya.quranasrclient.view;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.databinding.ActivityEvalDetailBinding;
import org.tangaya.quranasrclient.adapter.EvalAdapter;
import org.tangaya.quranasrclient.viewmodel.EvalDetailViewModel;

import java.util.ArrayList;

public class EvalDetailActivity extends AppCompatActivity {

    EvalDetailViewModel mViewModel;
    ActivityEvalDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_devspace_detail);

        final RecyclerView recyclerView = findViewById(R.id.devspace_detail_recycler);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Evaluation attempt = ((MyApplication) getApplication()).getEvaluations().get(0);

        mViewModel = new EvalDetailViewModel(getApplication(), new Evaluation(1,1,1));
        mViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<EvalDetailViewModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<EvalDetailViewModel> devspaceDetailViewModels) {

                EvalAdapter mAdapter = new EvalAdapter(EvalDetailActivity.this, devspaceDetailViewModels);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
}
