package org.tangaya.quranasrclient.devspace;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.util.diff_match_patch;

import java.util.ArrayList;
import java.util.LinkedList;

public class DevspaceDetailViewModel extends AndroidViewModel {
    // todo: edit layout binding

    public final ObservableField<String> verseNumber = new ObservableField<>();
    public final ObservableField<String> recognizedTranscript = new ObservableField<>();
    public final ObservableField<String> refereceTranscript = new ObservableField<>();
    public final ObservableField<String> evalStr = new ObservableField<>();
    public final ObservableBoolean isCorrect = new ObservableBoolean();

    //private ObservableField<LinkedList<diff_match_patch.Diff>> diff;

    public final ObservableField<String> diff = new ObservableField<>();
    public final ObservableInt levScore = new ObservableInt();

    public MutableLiveData<ArrayList<DevspaceDetailViewModel>> arrayListMutableLiveData = new MutableLiveData<>();

    private ArrayList<DevspaceDetailViewModel> arrayList;

    // todo: change to ArrayList<Evaluation> ? add lifecycle owner
    public DevspaceDetailViewModel(@NonNull Application application, Evaluation evaluation) {
        super(application);

        verseNumber.set(evaluation.getVerseNum().get());
        recognizedTranscript.set(evaluation.getTranscription().get());
        refereceTranscript.set(evaluation.getVerseQScript().get());
        diff.set(evaluation.getDiff().get());
        evalStr.set(evaluation.getEvalStr().get());
        isCorrect.set(evaluation.isCorrect().get());
        levScore.set(evaluation.getLevScore().get());
    }

    public MutableLiveData<ArrayList<DevspaceDetailViewModel>> getArrayListMutableLiveData() {

        arrayList = new ArrayList<>();

        ArrayList<Evaluation> evaluations = ((MyApplication) getApplication()).getEvaluations();

        for (int i = 0; i< evaluations.size(); i++) {
            DevspaceDetailViewModel mViewModel = new DevspaceDetailViewModel(getApplication(), evaluations.get(i));
            arrayList.add(mViewModel);
        }

        arrayListMutableLiveData.setValue(arrayList);

        return arrayListMutableLiveData;
    }
}
