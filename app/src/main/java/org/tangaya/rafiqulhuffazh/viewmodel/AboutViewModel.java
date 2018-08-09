package org.tangaya.rafiqulhuffazh.viewmodel;

import org.tangaya.rafiqulhuffazh.view.navigator.AboutNavigator;

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
