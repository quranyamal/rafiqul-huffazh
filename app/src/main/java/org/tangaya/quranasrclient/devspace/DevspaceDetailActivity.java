package org.tangaya.quranasrclient.devspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.R;

public class DevspaceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devspace_detail);

        RecyclerView recyclerView = findViewById(R.id.devspace_detail_recycler);

        AttemptAdapter mAdapter = new AttemptAdapter(((MyApplication)getApplication()).getAttempts());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
}
