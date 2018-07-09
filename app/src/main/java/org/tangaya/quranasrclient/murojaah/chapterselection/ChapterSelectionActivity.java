package org.tangaya.quranasrclient.murojaah.chapterselection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.murojaah.tutorial.TutorialActivity;


public class ChapterSelectionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_selection);

        SharedPreferences sharedPref = ((MyApplication) getApplication()).getPreferences();
        final SharedPreferences.Editor editor = sharedPref.edit();

        mRecyclerView = findViewById(R.id.verses_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                mRecyclerView, new RecyclerTouchListener.ClickListener() {

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