package org.tangaya.rafiqulhuffazh.view.navigator;

import org.tangaya.rafiqulhuffazh.data.model.Recording;

public interface DevspaceNavigator {

    void onStartRecording(Recording recording);

    void onStopRecording();

    void onPlayRecording(int surah, int ayah);

    void onPlayTestFile(int surah, int ayah);

    void gotoScoreboard();
}
