package org.tangaya.quranasrclient.murojaah.scoreboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.murojaah.evaldetail.EvalDetailActivity;

public class ScoreboardActivity extends AppCompatActivity {

    TextView detailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        detailBtn = findViewById(R.id.detail);
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEvalDetail();
            }
        });
    }

    void gotoEvalDetail() {
        Intent intent = new Intent(this, EvalDetailActivity.class);
        startActivity(intent);
    }
}
