package org.tangaya.quranasrclient.viewmodel;

import org.tangaya.quranasrclient.view.navigator.AboutNavigator;

public class AboutViewModel {

    private AboutNavigator mNavigator;

    public AboutViewModel() {}

    public void onActivityCreated(AboutNavigator navigator) {
        mNavigator = navigator;
    }

    public void backToHome() {
        mNavigator.backToHome();
    }
}
