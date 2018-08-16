package org.tangaya.rafiqulhuffazh.view.navigator;

public interface DevspaceNavigator {

    void onStartRecording(int surah, int ayah);

    void onStopRecording();

    void onPlayRecording(int surah, int ayah);

    void onPlayTestFile(int surah, int ayah);

    void gotoScoreboard();
}
