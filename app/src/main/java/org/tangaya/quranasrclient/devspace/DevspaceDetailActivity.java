package org.tangaya.quranasrclient.devspace;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.Attempt;

import java.util.ArrayList;

public class DevspaceDetailActivity extends AppCompatActivity {

    DevspaceDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devspace_detail);

        final RecyclerView recyclerView = findViewById(R.id.devspace_detail_recycler);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Attempt attempt = ((MyApplication) getApplication()).getAttempts().get(0);

        mViewModel = new DevspaceDetailViewModel(getApplication(), new Attempt(1,1,1));
        mViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<DevspaceDetailViewModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DevspaceDetailViewModel> devspaceDetailViewModels) {

                AttemptAdapter mAdapter = new AttemptAdapter(DevspaceDetailActivity.this, devspaceDetailViewModels);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);

            }
        });

    }
}
