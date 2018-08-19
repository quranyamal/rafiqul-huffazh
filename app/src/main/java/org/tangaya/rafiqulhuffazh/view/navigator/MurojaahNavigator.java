package org.tangaya.rafiqulhuffazh.view.navigator;

import org.tangaya.rafiqulhuffazh.data.model.Recording;

public interface MurojaahNavigator {

    void onMurojaahFinished();

    void onStartRecording(Recording recording);

    void onStopRecording();

    void gotoResult();

}
