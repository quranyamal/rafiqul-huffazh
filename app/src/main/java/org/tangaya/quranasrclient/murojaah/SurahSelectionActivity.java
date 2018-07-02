package org.tangaya.quranasrclient.murojaah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.Surah;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

import java.util.ArrayList;
import java.util.List;

public class SurahSelectionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<Surah> surahsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_selection);

        prepareSurahsData();

        mRecyclerView = findViewById(R.id.verses_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                mRecyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Surah surah = surahsList.get(position);
                Log.d("onClick", surah.getTitle()+" selected");

                Intent intent = new Intent(getApplicationContext(), MurojaahActivity.class);
                intent.putExtra("CHAPTER_NUM", surah.getId());
                startActivity(intent);
            }
        }));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SurahsAdapter(surahsList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void prepareSurahsData() {

        QuranScriptRepository.init(getApplication());

        for (int i=1; i<=114; i++) {
            surahsList.add(new Surah(i, QuranScriptRepository.getChapter(i).getTitle()));
        }

    }
}