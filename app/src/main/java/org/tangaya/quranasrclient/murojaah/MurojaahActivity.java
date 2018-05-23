package org.tangaya.quranasrclient.murojaah;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.tangaya.quranasrclient.util.ConnectToWSTask;
import org.tangaya.quranasrclient.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MurojaahActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_murojaah);

        MurojaahFragment murojaahFragment = obtainViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.murojaahFrame, murojaahFragment);
        transaction.commit();

    }

    @NonNull
    private MurojaahFragment obtainViewFragment() {
        // View Fragment
        MurojaahFragment murojaahFragment = (MurojaahFragment) getSupportFragmentManager()
                .findFragmentById(R.id.murojaahFrame);

        if (murojaahFragment == null) {
            murojaahFragment = MurojaahFragment.newInstance();

            // Send the task ID to the fragment
            // Bundle bundle = new Bundle();
            // bundle.putString(MurojaahFragment.ARGUMENT_EDIT_TASK_ID,
               //     getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID));
            // addEditTaskFragment.setArguments(bundle);
        }
        return murojaahFragment;
    }

}
