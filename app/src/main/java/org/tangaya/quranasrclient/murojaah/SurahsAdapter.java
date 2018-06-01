package org.tangaya.quranasrclient.murojaah;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.Surah;

import java.util.List;

public class SurahsAdapter extends RecyclerView.Adapter<SurahsAdapter.SurahsViewHolder> {
    private List<Surah> surahsList;

    public class SurahsViewHolder extends RecyclerView.ViewHolder {
        public TextView surahId, surahName;

        public SurahsViewHolder(View v) {
            super(v);
            surahId = v.findViewById(R.id.surah_id);
            surahName = v.findViewById(R.id.surah_name);
        }
    }

    public SurahsAdapter(List<Surah> surahsDataset) {
        surahsList= surahsDataset;
    }

    @Override
    public SurahsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_surah_list, parent, false);

        return new SurahsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahsViewHolder holder, int position) {
        Surah surah = surahsList.get(position);
        Log.d("onBindViewHolder", surah.getId()+"."+surah.getTitle());
        holder.surahId.setText(surah.getId()+" ");
        holder.surahName.setText(surah.getTitle());
    }

    @Override
    public int getItemCount() {
        return surahsList.size();
    }
}
