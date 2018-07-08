package org.tangaya.quranasrclient.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.tangaya.quranasrclient.MyApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class VerseRecognitionTask extends AsyncTask<Attempt, Void, Void>{

    WebSocket webSocket;
    String endpoint;

    public VerseRecognitionTask(WebSocket ws) {
        webSocket = ws;
    }

    @Override
    protected Void doInBackground(Attempt... attempts) {

        attempts[0].setStatus(Attempt.STATE_PROCESSING);

        File file = new File(attempts[0].getAudioFilepath());
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        Log.d("DVM", "Recognizing (asynctask)" + attempts[0].getAudioFilepath());
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("TranscriberOld", "before sendBinary");
        Log.d("TranscriberOld", "binary size: " + bytes.length);

        //if (!isConnected());
        webSocket.sendBinary(bytes);

        return null;
    }
}
