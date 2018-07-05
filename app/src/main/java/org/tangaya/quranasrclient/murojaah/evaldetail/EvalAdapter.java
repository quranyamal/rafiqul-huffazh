package org.tangaya.quranasrclient.murojaah.evaldetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.EvalsViewHolder> {

    public EvalAdapter() {}

    @NonNull
    @Override
    public EvalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evaluation, parent, false);

        return new EvalsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EvalsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EvalsViewHolder extends RecyclerView.ViewHolder {
        public TextView verseNum;
        public TextView refText;
        public TextView recText;

        public EvalsViewHolder(View view) {
            super(view);

            verseNum = view.findViewById(R.id.verse_num_evaluation);
            refText = view.findViewById(R.id.verse_ref_evaluation);
            recText = view.findViewById(R.id.verse_rec_evaluation);
        }
    }

}
