package org.tangaya.quranasrclient.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class HomeViewModel extends AndroidViewModel {

    HomeNavigator mNavigator;

    public final ObservableField<String> serverStatus = new ObservableField<>();

//    private MutableLiveData<String> cobaLiveData;
//
//    public MutableLiveData<String> getCobaLiveData() {
//        return cobaLiveData;
//    }


    public HomeViewModel(@NonNull Application application) {
        super(application);

        serverStatus.set("tes status");
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
