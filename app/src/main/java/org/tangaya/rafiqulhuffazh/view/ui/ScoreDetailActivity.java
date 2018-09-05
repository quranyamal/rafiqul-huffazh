package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.databinding.ActivityScoreDetailBinding;
import org.tangaya.rafiqulhuffazh.databinding.DialogEvalDetailBinding;
import org.tangaya.rafiqulhuffazh.view.adapter.EvalAdapter;
import org.tangaya.rafiqulhuffazh.viewmodel.ScoreDetailViewModel;

import java.util.ArrayList;

import timber.log.Timber;

public class ScoreDetailActivity extends Activity implements LifecycleOwner, EvalAdapter.EvalAdapterListener {

    private ScoreDetailViewModel mViewModel;
    private ActivityScoreDetailBinding mBinding;
    private LifecycleRegistry mLifecycleRegistry;
    private EvalAdapter.EvalAdapterListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score_detail);

        setTitle("Score Detail");

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        listener = this;

        final RecyclerView recyclerView = findViewById(R.id.score_detail_recycler);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mViewModel = new ScoreDetailViewModel(getApplication());
        mViewModel.getEvaluationsLiveData().observe(this, new Observer<ArrayList<Evaluation>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Evaluation> evaluations) {

                EvalAdapter mAdapter = new EvalAdapter(getApplicationContext(), evaluations, listener);
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

    @Override
    public void onEvalDescriptionClicked(Evaluation eval) {
        Timber.d("onEvalDescriptionClicked");
        //Toast.makeText(getApplicationContext(), "eval-"+eval.ayah.get()+ " clicked", Toast.LENGTH_SHORT).show();

        DialogEvalDetailBinding binding = DialogEvalDetailBinding
                .inflate(LayoutInflater.from(getApplicationContext()));

        binding.setEvaluation(eval);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
