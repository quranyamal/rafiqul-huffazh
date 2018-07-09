package org.tangaya.quranasrclient.murojaah.scoreboard;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class ScoreboardViewModel extends AndroidViewModel {

    ScoreboardNavigation mNavigator;

    public ScoreboardViewModel(@NonNull Application application) {
        super(application);
    }

    public void onActivityCreated(ScoreboardNavigation navigator) {
        mNavigator = navigator;
    }

    public void showDetail() {
        mNavigator.showDetail();
    }

    public void retryChapter() {
        mNavigator.retryChapter();
    }

    public void nextChapter() {
        mNavigator.nextChapter();
    }

    public void selectAnotherChapter() {
        mNavigator.selectAnotherChapter();
    }

    public void exit() {
        mNavigator.exit();
    }

}
