package org.tangaya.quranasrclient.murojaah.evaldetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Evaluation;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

import java.util.ArrayList;

public class EvalDetailViewModel extends AndroidViewModel {

    //todo: should be one source in a session
    public final ObservableField<String> currentChapter = new ObservableField<>();

    public final ObservableField<String> verseNumber = new ObservableField<>();
    public final ObservableField<String> recognizedTranscript = new ObservableField<>();
    public final ObservableField<String> refereceTranscript = new ObservableField<>();
    public final ObservableField<String> diff = new ObservableField<>();
    public final ObservableField<String> evalStr = new ObservableField<>();
    public final ObservableBoolean isCorrect = new ObservableBoolean();


    public MutableLiveData<ArrayList<EvalDetailViewModel>> arrayListMutableLiveData = new MutableLiveData<>();

    private ArrayList<EvalDetailViewModel> arrayList;

    public EvalDetailViewModel(@NonNull Application application, Evaluation evaluation) {
        super(application);

        int currentChapterNum = ((MyApplication) getApplication()).getCurrentChapterNum();
        currentChapter.set(QuranScriptRepository.getChapter(currentChapterNum).getTitle());

        verseNumber.set(evaluation.getVerseNum().get());
        recognizedTranscript.set(evaluation.getTranscription().get());
        refereceTranscript.set(evaluation.getVerseQScript().get());
        //diff.set(evaluation.getDiffStr().get().toString());
        evalStr.set(evaluation.getEvalStr().get());
        isCorrect.set(evaluation.isCorrect().get());

    }

    public MutableLiveData<ArrayList<EvalDetailViewModel>> getArrayListMutableLiveData() {

        arrayList = new ArrayList<>();

        ArrayList<Evaluation> evaluations = ((MyApplication) getApplication()).getEvaluations();

        for (int i = 0; i< evaluations.size(); i++) {
            EvalDetailViewModel mViewModel = new EvalDetailViewModel(getApplication(), evaluations.get(i));
            arrayList.add(mViewModel);
        }

        arrayListMutableLiveData.setValue(arrayList);

        return arrayListMutableLiveData;
    }
}
