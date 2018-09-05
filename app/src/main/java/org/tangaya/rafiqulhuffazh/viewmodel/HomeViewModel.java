package org.tangaya.rafiqulhuffazh.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import org.tangaya.rafiqulhuffazh.view.navigator.HomeNavigator;

public class HomeViewModel extends AndroidViewModel {

    HomeNavigator mNavigator;

    public final ObservableField<String> serverStatus = new ObservableField<>();
    public final ObservableBoolean isConnected = new ObservableBoolean();

    public HomeViewModel(@NonNull Application application) {
        super(application);

        serverStatus.set("connecting...");
    }

    public ObservableField<String> getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String status) {
        serverStatus.set(status);
        isConnected.set(serverStatus.get().equals("connected"));
    }

    public void onActivityCreated(HomeNavigator navigator) {
        mNavigator = navigator;
    }

    public void gotoMurojaah() {
        mNavigator.gotoMurojaah();
    }

    public void gotoServerSetting() {
        mNavigator.gotoServerSetting();
    }

    public void gotoDevspace() {
        mNavigator.gotoDevspace();
    }

    public void gotoAbout() {
        mNavigator.gotoAbout();
    }

}
