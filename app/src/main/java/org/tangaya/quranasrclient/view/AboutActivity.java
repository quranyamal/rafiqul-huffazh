package org.tangaya.quranasrclient.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.tangaya.quranasrclient.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle("About");
    }
}
