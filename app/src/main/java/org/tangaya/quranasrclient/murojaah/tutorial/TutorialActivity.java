package org.tangaya.quranasrclient.murojaah.tutorial;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.murojaah.main.MurojaahActivity;

public class TutorialActivity extends AppCompatActivity implements TutorialNavigator {

    TutorialViewModel mViewModel;

    TextView skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //mViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);
        mViewModel = new TutorialViewModel(this.getApplication());
        mViewModel.onActivityCreated(this);

        // todo: change to viewmodel
        skipBtn = findViewById(R.id.skip_tutorial);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipTutorial();
            }
        });
    }

    @Override
    public void skipTutorial() {
        Intent intent = new Intent(this, MurojaahActivity.class);
        finish();
        startActivity(intent);
    }

}
