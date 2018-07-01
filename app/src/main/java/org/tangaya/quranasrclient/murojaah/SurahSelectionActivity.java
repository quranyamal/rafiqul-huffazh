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

//        surahsList.add(new Surah(1, "Al-Fatihah"));
//        surahsList.add(new Surah(2, "Al-Baqarah"));
//        surahsList.add(new Surah(3, "Ali Imran"));
//        surahsList.add(new Surah(4, "An-Nisa"));
//        surahsList.add(new Surah(5, "Al-Maidah"));
//        surahsList.add(new Surah(6, "Al-An'am"));
//        surahsList.add(new Surah(7, "Al-A'raf"));
//        surahsList.add(new Surah(8, "Al-Anfal"));
//        surahsList.add(new Surah(9, "At-Taubah"));
//        surahsList.add(new Surah(10, "Taha"));
//        surahsList.add(new Surah(10, "Hud"));
//        surahsList.add(new Surah(10, "Yusuf"));
//        surahsList.add(new Surah(10, "Ar-Ra'd"));
//        surahsList.add(new Surah(10, "Ibrahim"));
//        surahsList.add(new Surah(10, "Ibrahim"));
//        surahsList.add(new Surah(10, "Ibrahim"));
//        surahsList.add(new Surah(10, "Ibrahim"));
    }
}