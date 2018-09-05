package org.tangaya.rafiqulhuffazh.view.navigator;

public interface ServerSettingNavigator {

    void onClickConnect();

    void onSaveSetting(String hostname, String port);

    void onSettingCancelled();

}
