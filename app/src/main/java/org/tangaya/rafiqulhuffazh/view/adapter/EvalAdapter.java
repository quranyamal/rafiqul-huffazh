package org.tangaya.rafiqulhuffazh.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.databinding.CardEvaluationBinding;

import java.util.ArrayList;

import timber.log.Timber;

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Evaluation> evaluations;
    private LayoutInflater layoutInflater;
    private EvalAdapterListener listener;

    public EvalAdapter(Context context, ArrayList<Evaluation> evaluations, EvalAdapterListener listener) {
        this.context =context;
        this.evaluations = evaluations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CardEvaluationBinding mBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.card_evaluation, parent, false);
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mBinding.setEvaluation(evaluations.get(position));

        holder.mBinding.evalDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEvalDescriptionClicked(evaluations.get(position));
                Timber.d("evalCard onClick:" + position);
                Log.d("EvalAdapter", "onClick set " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return evaluations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardEvaluationBinding mBinding;

        public MyViewHolder(CardEvaluationBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface EvalAdapterListener {
        void onEvalDescriptionClicked(Evaluation eval);
    }
}
