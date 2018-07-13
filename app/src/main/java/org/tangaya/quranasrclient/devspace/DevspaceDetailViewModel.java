package org.tangaya.quranasrclient.devspace;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.Attempt;

import java.util.ArrayList;

public class DevspaceDetailViewModel extends AndroidViewModel {
    // todo: edit layout binding

    public final ObservableField<String> verseNumber = new ObservableField<>();
    public final ObservableField<String> recognizedTranscript = new ObservableField<>();
    public final ObservableField<String> refereceTranscript = new ObservableField<>();
    public final ObservableField<String> diff = new ObservableField<>();
    public final ObservableField<String> evalStr = new ObservableField<>();
    public final ObservableBoolean isCorrect = new ObservableBoolean();


    public MutableLiveData<ArrayList<DevspaceDetailViewModel>> arrayListMutableLiveData = new MutableLiveData<>();

    private ArrayList<DevspaceDetailViewModel> arrayList;

    // todo: change to ArrayList<Attempt> ? add lifecycle owner
    public DevspaceDetailViewModel(@NonNull Application application, Attempt attempt) {
        super(application);

        verseNumber.set(attempt.getVerseNum().get());
        recognizedTranscript.set(attempt.getTranscription().get());
        refereceTranscript.set(attempt.getVerseQScript().get());
        //diff.set(attempt.getDiffStr().get().toString());
        evalStr.set(attempt.getEvalStr().get());
        isCorrect.set(attempt.isCorrect().get());
    }

    public MutableLiveData<ArrayList<DevspaceDetailViewModel>> getArrayListMutableLiveData() {

        arrayList = new ArrayList<>();
        //Attempt attempt = new Attempt(1,1,123);

        ArrayList<Attempt> attempts = ((MyApplication) getApplication()).getAttempts();

        //Attempt attempt = attemts.get(0);

        for (int i=0; i<attempts.size(); i++) {
            DevspaceDetailViewModel mViewModel = new DevspaceDetailViewModel(getApplication(), attempts.get(i));
            arrayList.add(mViewModel);
        }

        arrayListMutableLiveData.setValue(arrayList);

        return arrayListMutableLiveData;
    }
}
