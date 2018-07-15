package org.tangaya.quranasrclient.data;

import android.os.AsyncTask;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class VerseRecognitionTask extends AsyncTask<Evaluation, Void, Void>{

    WebSocket webSocket;
    String endpoint;

    public VerseRecognitionTask(WebSocket ws) {
        webSocket = ws;
    }

    @Override
    protected Void doInBackground(Evaluation... evaluations) {

        evaluations[0].setStatus(Evaluation.STATE_PROCESSING);

        File file = new File(evaluations[0].getAudioFilepath().get());
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        Log.d("DVM", "Recognizing (asynctask)" + evaluations[0].getAudioFilepath());
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("VerseRecognitionTask", "sending binary. size: " + bytes.length);
        webSocket.sendBinary(bytes);

        return null;
    }
}
