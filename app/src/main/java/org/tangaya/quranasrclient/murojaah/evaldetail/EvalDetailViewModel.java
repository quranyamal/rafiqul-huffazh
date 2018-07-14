package org.tangaya.quranasrclient.murojaah.evaldetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.tangaya.quranasrclient.MyApplication;
import org.tangaya.quranasrclient.data.source.QuranScriptRepository;

public class EvalDetailViewModel extends AndroidViewModel {

    //todo: should be one source in a session
    public final ObservableField<String> currentChapter = new ObservableField<>();

    public EvalDetailViewModel(@NonNull Application application) {
        super(application);

        int currentChapterNum = ((MyApplication) getApplication()).getCurrentChapterNum();

        currentChapter.set(QuranScriptRepository.getChapter(currentChapterNum).getTitle());

    }
}
