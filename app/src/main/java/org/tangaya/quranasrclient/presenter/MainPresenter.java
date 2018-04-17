package org.tangaya.quranasrclient.presenter;

import android.view.MenuItem;

import org.tangaya.quranasrclient.view.BaseView;

public interface MainPresenter extends BasePresenter {

    /* Implemented by View, needed by Presenter */
    interface View extends BaseView {
        void changeScreen(Class fragmentClass);
        void checkedMenu(MenuItem menuItem);
        void showSignOutConfirmation();
        void onSignOut();
    }

    /* Presenter's methods, Implemented by Presenter, needed by View */
    boolean selectMenu(MenuItem menuItem);
    void signOut();
}
