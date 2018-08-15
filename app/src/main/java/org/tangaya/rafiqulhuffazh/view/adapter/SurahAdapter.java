package org.tangaya.rafiqulhuffazh.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.SurahsViewHolder> {

    public SurahAdapter() {}

    @Override
    public SurahsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_surah_list, parent, false);

        return new SurahsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahsViewHolder holder, int position) {

        int surah = position+1;
        String surahName = QuranUtil.getSurahName(surah);
        holder.surahId.setText(surah+" ");
        holder.surahName.setText(surahName);
    }

    @Override
    public int getItemCount() {
        return 114;
    }

    public class SurahsViewHolder extends RecyclerView.ViewHolder {
        public TextView surahId, surahName;

        public SurahsViewHolder(View v) {
            super(v);
            surahId = v.findViewById(R.id.surah_id);
            surahName = v.findViewById(R.id.surah_name);
        }
    }
}
