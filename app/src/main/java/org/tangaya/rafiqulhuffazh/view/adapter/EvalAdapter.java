package org.tangaya.rafiqulhuffazh.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.data.model.Evaluation;
import org.tangaya.rafiqulhuffazh.databinding.CardEvaluationBinding;

import java.util.ArrayList;

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Evaluation> evaluations;
    private LayoutInflater layoutInflater;

    public EvalAdapter(Context context, ArrayList<Evaluation> evaluations) {
        this.context =context;
        this.evaluations = evaluations;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mBinding.setEvaluation(evaluations.get(position));
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
}
