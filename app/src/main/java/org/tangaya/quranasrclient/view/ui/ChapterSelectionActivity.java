package org.tangaya.quranasrclient.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.view.adapter.ChapterAdapter;
import org.tangaya.quranasrclient.view.listener.ChapterRecyclerTouchListener;


public class ChapterSelectionActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_selection);

        setTitle("Select Chapter");

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();
        final SharedPreferences.Editor editor = sharedPref.edit();

        mRecyclerView = findViewById(R.id.verses_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new ChapterRecyclerTouchListener(getApplicationContext(),
                mRecyclerView, new ChapterRecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                editor.putInt("CURRENT_CHAPTER_NUM", position);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                finish();
                startActivity(intent);
            }
        }));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChapterAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}