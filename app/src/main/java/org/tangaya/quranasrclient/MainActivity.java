package org.tangaya.quranasrclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.tangaya.quranasrclient.view.ServerConfigActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClickMurojaahBtn(View view) {
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }

    public void gotoMorojaahNew(View view) {
        Intent intent = new Intent(this, MurojaahActivityNew.class);
        startActivity(intent);
    }

}
