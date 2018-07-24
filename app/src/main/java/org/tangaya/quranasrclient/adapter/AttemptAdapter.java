package org.tangaya.quranasrclient.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.EvalItemBinding;
import org.tangaya.quranasrclient.viewmodel.DevspaceDetailViewModel;

import java.util.ArrayList;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.MyViewHolder> {

    private ArrayList<DevspaceDetailViewModel> mArrayList;

    private Context mContext;

    private LayoutInflater layoutInflater;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView verseNum, rec, ref, diff, eval, levScore;
        ImageView evaluationIcon;

        private EvalItemBinding mBinding;

        public MyViewHolder(EvalItemBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            verseNum = itemView.findViewById(R.id.verse_num_evaluation);
            rec = itemView.findViewById(R.id.verse_rec_evaluation);
            ref = itemView.findViewById(R.id.verse_ref_evaluation);
            diff = itemView.findViewById(R.id.verse_diff_evaluation);
            evaluationIcon = itemView.findViewById(R.id.evaluation_icon);
            eval = itemView.findViewById(R.id.eval);
            levScore = itemView.findViewById(R.id.levenshtein_score_dev);
        }

        public void bind(DevspaceDetailViewModel viewModel) {
            mBinding.setViewmodel(viewModel);
            mBinding.executePendingBindings();
        }

        public EvalItemBinding getBinding() {
            return mBinding;
        }
    }

    public AttemptAdapter(Context context, ArrayList<DevspaceDetailViewModel> arrayList) {
        mContext = context;
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        EvalItemBinding evalBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_evaluation, parent, false);

        return new MyViewHolder(evalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DevspaceDetailViewModel mViewModel = mArrayList.get(position);
        holder.bind(mViewModel);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


}
