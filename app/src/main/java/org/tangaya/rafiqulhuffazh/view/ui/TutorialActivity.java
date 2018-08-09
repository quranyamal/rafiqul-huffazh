package org.tangaya.rafiqulhuffazh.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.view.adapter.TutorialPagerAdapter;
import org.tangaya.rafiqulhuffazh.view.navigator.TutorialNavigator;
import org.tangaya.rafiqulhuffazh.viewmodel.TutorialViewModel;

import timber.log.Timber;

public class TutorialActivity extends Activity implements TutorialNavigator {

    TutorialViewModel mViewModel;
    TextView skipBtn;

    private ViewPager mViewPager;
    private int[] layouts = {R.layout.slide_tutorial_one,
            R.layout.slide_tutorial_two,
            R.layout.slide_tutorial_three,
            R.layout.slide_tutorial_four};
    private TutorialPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        setTitle("Tutorial");

        mViewPager = findViewById(R.id.tutorial_view_pager);
        mPagerAdapter = new TutorialPagerAdapter(layouts, this);
        mViewPager.setAdapter(mPagerAdapter);

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

        View nextBtn = findViewById(R.id.next_tutorial);
        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Timber.d("OnClick next");
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
            }
        });

        View prevBtn = findViewById(R.id.previous_tutorial);
        prevBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Timber.d("OnClick prev");
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1, true);
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
