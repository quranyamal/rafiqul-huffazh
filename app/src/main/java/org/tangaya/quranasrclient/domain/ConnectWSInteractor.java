package org.tangaya.quranasrclient.domain;

import java.util.List;
import java.util.Map;

public interface ConnectWSInteractor extends Interactor {

    interface Callback {
        void onConnected(Map<String, List<String>> headers);
        void onDisconnected();
        void onSocketError(String error);
        void onTextMessage(String message);
        void onSendError(String error);
    }
}
