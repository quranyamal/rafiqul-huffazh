package org.tangaya.quranasrclient.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChaptersViewHolder> {

    public ChapterAdapter() {}

    @Override
    public ChaptersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chapter_list, parent, false);

        return new ChaptersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersViewHolder holder, int position) {

        int chapterId = position+1;
        String chapterTitile = QuranScriptRepository.getChapter(chapterId).getTitle();
        holder.chapterId.setText(chapterId+" ");
        holder.chapterName.setText(chapterTitile);
    }

    @Override
    public int getItemCount() {
        return 114;
    }

    public class ChaptersViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterId, chapterName;

        public ChaptersViewHolder(View v) {
            super(v);
            chapterId = v.findViewById(R.id.chapter_id);
            chapterName = v.findViewById(R.id.chapter_name);
        }
    }
}
