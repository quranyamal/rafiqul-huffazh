package org.tangaya.quranasrclient.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class HomeViewModel extends AndroidViewModel {

    ObservableField<String> serverStatus = new ObservableField<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);

        serverStatus.set("tes status");
    }

}
