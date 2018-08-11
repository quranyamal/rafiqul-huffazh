package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import org.tangaya.rafiqulhuffazh.MyApplication;
import org.tangaya.rafiqulhuffazh.data.model.EvaluationOld;
import org.tangaya.rafiqulhuffazh.data.repository.EvaluationRepository;
import org.tangaya.rafiqulhuffazh.util.QuranFactory;

import java.util.ArrayList;

import timber.log.Timber;

public class ScoreDetailViewModel extends AndroidViewModel {

    //todo: should be one source in a session
    public final ObservableField<String> currentChapter = new ObservableField<>();

    public final ObservableField<String> verseNumber = new ObservableField<>();
    public final ObservableField<String> recognizedTranscript = new ObservableField<>();
    public final ObservableField<String> recognizedArabicTranscript = new ObservableField<>();
    public final ObservableField<String> refereceTranscript = new ObservableField<>();
    public final ObservableField<String> refereceArabicScript = new ObservableField<>();
    public final ObservableField<String> diff = new ObservableField<>();
    public final ObservableField<String> evalStr = new ObservableField<>();

    public final ObservableField<String> point = new ObservableField<>();
    public final ObservableInt maxPoint = new ObservableInt();
    public final ObservableInt earnedPoint = new ObservableInt();

    public final ObservableBoolean isCorrect = new ObservableBoolean();

    public MutableLiveData<ArrayList<ScoreDetailViewModel>> arrayListMutableLiveData = new MutableLiveData<>();

    private ArrayList<ScoreDetailViewModel> arrayList;

    public ScoreDetailViewModel(@NonNull Application application, EvaluationOld evaluation) {
        super(application);

        int currentChapterNum = ((MyApplication) getApplication()).getCurrentChapterNum();
        currentChapter.set(QuranFactory.getSurahName(currentChapterNum));

        verseNumber.set(evaluation.getVerseNum().get());
        recognizedTranscript.set(evaluation.getTranscription().get());
        recognizedArabicTranscript.set(evaluation.getArabicTranscription().get());
        refereceTranscript.set(evaluation.getVerseQScript().get());
        refereceArabicScript.set(evaluation.getVerseScript().get());
        diff.set(evaluation.getDiff().get());
        evalStr.set(evaluation.getEvalStr().get());

        // switch check color to green
        isCorrect.set(evaluation.isCorrect().get());
        point.set(evaluation.getScoreStr().get());
        maxPoint.set(evaluation.getMaxpoints().get());
        earnedPoint.set(evaluation.getEarnedPoints().get());
    }

    public MutableLiveData<ArrayList<ScoreDetailViewModel>> getArrayListMutableLiveData() {

        arrayList = new ArrayList<>();

        //ArrayList<EvaluationOld> evaluations = ((MyApplication) getApplication()).getEvaluations();

        ArrayList<EvaluationOld> evaluations = EvaluationRepository.getEvals();

        for (int i = 0; i< evaluations.size(); i++) {
            ScoreDetailViewModel mViewModel = new ScoreDetailViewModel(getApplication(), evaluations.get(i));
            arrayList.add(mViewModel);
        }

        arrayListMutableLiveData.setValue(arrayList);

        return arrayListMutableLiveData;
    }

    public void playReference() {
        Timber.d("playing reference");
    }
}
