package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.databinding.ActivitySurahSelectionBinding;
import org.tangaya.rafiqulhuffazh.view.listener.SurahRecyclerTouchListener;
import org.tangaya.rafiqulhuffazh.viewmodel.SurahSelectionViewModel;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


public class SurahSelectionActivity extends Activity {

    private SurahSelectionViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_selection);

        setTitle("Pilih Surat");

        View view = bind();
        initRecyclerView(view);
    }

    private View bind() {
        ActivitySurahSelectionBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_surah_selection);
        mViewModel = new SurahSelectionViewModel();
        binding.setViewModel(mViewModel);

        return binding.getRoot();
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.surahs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL));

        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(new SurahRecyclerTouchListener(getApplicationContext(),
                recyclerView, new SurahRecyclerTouchListener.ClickListener() {

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();
        final SharedPreferences.Editor editor = sharedPref.edit();
            @Override
            public void onClick(View view, int position) {
                editor.putInt("CURRENT_SURAH_NUM", position);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                finish();
                startActivity(intent);
            }
        }));

//        mAdapter = new SurahAdapter();
//        mRecyclerView.setAdapter(mAdapter);
    }
}