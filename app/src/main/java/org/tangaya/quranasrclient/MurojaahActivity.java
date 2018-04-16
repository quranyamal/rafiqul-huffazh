package org.tangaya.quranasrclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.neovisionaries.ws.client.WebSocket;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MurojaahActivity extends AppCompatActivity {


    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murojaah);

        Timber.d("onCreate");
        nextButton = findViewById(R.id.murojaah_next_button);
    }

    public void onClickNextButton(View view) {

        Timber.d("onClickNextButton");
        Log.d("MurAct", "onClickNextBut");
        DecodeTask decodeTask = new DecodeTask();
        decodeTask.execute();
    }

    private class DecodeTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            Timber.d("doInBackground");

            DecoderWebSocket decoderWebSocket =
                    new DecoderWebSocket("decode_1",
                            "ws://192.168.1.217:8888/client/ws/speech");

            decoderWebSocket.connect(new DecoderWSRepository.Callback() {
                @Override
                public void onConnected(Map<String, List<String>> headers) {
                    Timber.d("onConnected");
                }

                @Override
                public void onDisconnected() {
                    Timber.d("onDisconnected");
                }

                @Override
                public void onError(String error) {
                    Timber.d("onError");
                }

                @Override
                public void onTextMessage(String message) {
                    Timber.d("onTextMessage");
                }

                @Override
                public void onSendError(String error) {
                    Timber.d("onSendError");
                }
            });

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            Timber.d("onPostExecute");
        }
    }

}
