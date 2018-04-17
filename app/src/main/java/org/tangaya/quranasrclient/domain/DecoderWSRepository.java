package org.tangaya.quranasrclient.domain;

import java.util.List;
import java.util.Map;

public interface DecoderWSRepository {

    interface Callback {

        void onConnected(Map<String, List<String>> headers);
        void onDisconnected();
        void onError(String error);
        void onTextMessage(String message);
        void onSendError(String error);
    }

    void connect(Callback callback);
    void disconnect();
    void sendBinary(byte[] binary);
    void sendText(String text);
}
