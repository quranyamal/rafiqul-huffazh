package org.tangaya.quranasrclient.murojaah.evaldetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.data.Attempt;

import java.util.ArrayList;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.MyViewHolder> {

    private ArrayList<Attempt> attempts;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView rec, ref;

        public MyViewHolder(View itemView) {
            super(itemView);

            rec = itemView.findViewById(R.id.verse_rec_evaluation);
            ref = itemView.findViewById(R.id.verse_ref_evaluation);
        }
    }

    public AttemptAdapter(ArrayList<Attempt> attempts_) {
        attempts = attempts_;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evaluation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Attempt attempt = attempts.get(position);
        holder.rec.setText(attempt.getTranscription());
        holder.ref.setText(attempt.getVerseScript());
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }


}
