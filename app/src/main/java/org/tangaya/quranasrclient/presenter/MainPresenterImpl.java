package org.tangaya.quranasrclient.presenter;

import android.view.MenuItem;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.domain.AuthRepository;
import org.tangaya.quranasrclient.domain.Executor;
import org.tangaya.quranasrclient.domain.MainThread;
import org.tangaya.quranasrclient.view.MurojaahFragment;

public class MainPresenterImpl extends AbstractPresenter
        implements MainPresenter {

    private MainPresenter.View mView;
    private AuthRepository mAuthRepository;

    public MainPresenterImpl(Executor executor, MainThread mainThread,
                             AuthRepository authRepository, View view) {

        super(executor, mainThread);

        mView = view;
        mAuthRepository = authRepository;
    }

    /* Methods needed by View */
    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean selectMenu(MenuItem menuItem) {

//        Class fragmentClass;
//
//        switch(menuItem.getItemId()) {
//            case R.id.nav_add_transcription:
//                fragmentClass = MurojaahFragment.class;
//                break;
//            default:
//                return false;
//        }
//
//        mView.changeScreen(fragmentClass);
//        mView.checkedMenu(menuItem);

        return true;
    }

    @Override
    public void signOut() {
//        SignOutInteractor interactor = new SignOutInteractorImpl(
//                mExecutor,
//                mMainThread,
//                this,
//                mAuthRepository
//        );
//
//        interactor.execute();
    }

    /* Methods needed by Interactor */

//    @Override
//    public void onSignedOut() {
//
//        mView.onSignOut();
//    }
}