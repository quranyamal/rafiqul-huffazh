package org.tangaya.quranasrclient.devspace;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.tangaya.quranasrclient.service.Transcriber;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DevspaceViewModel extends AndroidViewModel {

    Transcriber transcriber = new Transcriber();
    WebSocket webSocket; // todo: set simeout?

    private String extStorageDir = Environment.getExternalStorageDirectory()+"";
    private String quranVerseAudioDir = extStorageDir + "/quran-verse-audio";

    public final ObservableInt chapter = new ObservableInt();
    public final ObservableInt verse = new ObservableInt();
    public final ObservableField<String> result= new ObservableField<>();
    public final ObservableField<String> serverStatus = new ObservableField<>();


    public DevspaceViewModel(@NonNull Application application) {
        super(application);

        chapter.set(1);
        verse.set(1);
        serverStatus.set("disconnected");
    }

    public void recognizeByFile() {
        String filepath = getFilePath(chapter.get(), verse.get());

        if (new File(filepath).exists()) {
            Log.d("DVM", "file exists");
        } else {
            Log.d("DVM", "file does not exist");
        }

        Log.d("DVM", "ext dir state: " + Environment.getExternalStorageState());

        result.set(Environment.getExternalStorageState());
        Log.d("DVM", "recognizing file:" + filepath);

        RecognitionRequest recognitionRequest = new RecognitionRequest();
        recognitionRequest.execute(filepath);
    }

    private String getFilePath(int chapter, int verse) {
        return quranVerseAudioDir + "/" + chapter + "-" + verse + ".wav";
        //return Environment.getExternalStorageDirectory().toString() + "/1-1.wav";
    }

    class RecognitionRequest extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... filenames) {

            File file = new File(filenames[0]);
            int size = (int) file.length();
            byte[] bytes = new byte[size];

            Log.d("DVM", "Recognizing " + filenames[0]);
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

            webSocket.sendBinary(bytes);
            Log.d("TranscriberOld", "aftersendBinary");

            return null;
        }
    }

    public void connect() {
        String hostname = "192.168.1.217";
        String port = "8888";

        String endpoint = "ws://"+hostname+":"+port+"/client/ws/speech";

        serverStatus.set("connecting ...");
        try {
            webSocket = new WebSocketFactory().createSocket(endpoint);

            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);

                    serverStatus.set("connected");
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

                    serverStatus.set("disconnected");
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    super.onConnectError(websocket, exception);

                    serverStatus.set("error");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);

                    Log.d("DVM", "onTextMessage. message: " + text);
                    result.set(text);
                }
            });

            webSocket.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

