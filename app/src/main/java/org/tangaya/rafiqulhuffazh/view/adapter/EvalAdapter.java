package org.tangaya.rafiqulhuffazh.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.databinding.EvalCardBinding;
import org.tangaya.rafiqulhuffazh.viewmodel.ScoreDetailViewModel;

import java.util.ArrayList;

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.MyViewHolder> {

    private ArrayList<ScoreDetailViewModel> mArrayList;

    private Context mContext;

    private LayoutInflater layoutInflater;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ayahNum, rec, ref, eval, levScore;
        ImageView evaluationIcon;

        private EvalCardBinding mBinding;

        public MyViewHolder(EvalCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            ayahNum = itemView.findViewById(R.id.ayah_num_evaluation);
            rec = itemView.findViewById(R.id.ayah_rec_evaluation);
            ref = itemView.findViewById(R.id.ayah_ref_evaluation);
            eval = itemView.findViewById(R.id.eval_result);
        }

        public void bind(ScoreDetailViewModel viewModel) {
            mBinding.setViewmodel(viewModel);
            mBinding.executePendingBindings();
        }

        public EvalCardBinding getBinding() {
            return mBinding;
        }
    }

    public EvalAdapter(Context context, ArrayList<ScoreDetailViewModel> arrayList) {
        mContext = context;
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        EvalCardBinding evalBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.card_evaluation, parent, false);

        return new MyViewHolder(evalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ScoreDetailViewModel mViewModel = mArrayList.get(position);
        holder.bind(mViewModel);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


}
