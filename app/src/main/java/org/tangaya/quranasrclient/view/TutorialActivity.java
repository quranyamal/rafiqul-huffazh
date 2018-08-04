package org.tangaya.quranasrclient.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.navigator.TutorialNavigator;
import org.tangaya.quranasrclient.viewmodel.TutorialViewModel;

public class TutorialActivity extends Activity implements TutorialNavigator {

    TutorialViewModel mViewModel;

    TextView skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        setTitle("Tutorial");

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
