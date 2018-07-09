package org.tangaya.quranasrclient.murojaah.evaldetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.R;

public class EvalDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_detail);

        AttemptAdapter mAdapter = new AttemptAdapter(((MyApplication) getApplication()).getAttempts());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.evaluation_recycler);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
