package org.tangaya.quranasrclient;

import android.os.Environment;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tangaya.quranasrclient.util.QuranScriptFactory;
import org.tangaya.quranasrclient.view.ui.ChapterSelectionActivity;
import org.tangaya.quranasrclient.view.ui.HomeActivity;
import org.tangaya.quranasrclient.view.ui.MurojaahActivity;
import org.tangaya.quranasrclient.view.ui.ScoreDetailActivity;
import org.tangaya.quranasrclient.view.ui.ScoreboardActivity;
import org.tangaya.quranasrclient.view.ui.TutorialActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MurojaahActivityTest {

    @Rule
    public ActivityTestRule<MurojaahActivity> mActivityRule = new ActivityTestRule<>(MurojaahActivity.class);

    @Before
    public void init() {
        Intents.init();
    }

    @Test
    public void murojaahActivityTest_ShowArabicVerseScript() {
        String hintText = QuranScriptFactory.getArabicVerseScript(103,1);

        onView(withId(R.id.hint_icon)).perform(click());
        onView(withId(R.id.hint_text)).check(matches(withText(hintText )));
    }

    @Test
    public void murojaahActivityTest_RecordingTest() {

        //check if recording file does not exist
        String filepath = "/rafiqul-huffazh/recording/103_1.wav";
        File recording = new File(Environment.getExternalStorageDirectory() + filepath);

        assert !recording.exists();

        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        onView(withId(R.id.verse_num_text)).perform(click());           // used to play verse

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.attempt_button_murojaah)).perform(click());

        //check if recording file exist and not empty
        assert recording.exists();
        assert recording.length()!=0;
    }

    @Test
    public void murojaahActivityTest_UITestFromHome() {
        onView(withId(R.id.murojaah_button_home)).perform(click());
        intended(hasComponent(ChapterSelectionActivity.class.getName()));

        onView(withId(R.id.chapters_recycler_view)).perform(RecyclerViewActions.scrollToPosition(105));
        onView(withText("Al 'Asr")).check(matches(isDisplayed()));

        onView((withText("Al 'Asr"))).perform(click());
        intended(hasComponent(TutorialActivity.class.getName()));

        onView((withText("skip"))).perform(click());
        intended(hasComponent(MurojaahActivity.class.getName()));

        // verse 1
        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        onView(withId(R.id.instruction_murojaah)).check(matches(withText(R.string.recording_true)));

        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        onView(withId(R.id.instruction_murojaah)).check(matches(withText(R.string.recording_false)));

        // verse 2
        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        onView(withId(R.id.instruction_murojaah)).check(matches(withText(R.string.recording_true)));

        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        onView(withId(R.id.instruction_murojaah)).check(matches(withText(R.string.recording_false)));

        //verse 3
        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        onView(withId(R.id.instruction_murojaah)).check(matches(withText(R.string.recording_true)));

        // wait for recognition process
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.attempt_button_murojaah)).perform(click());
        intended(hasComponent(ScoreboardActivity.class.getName()));

        onView(withId(R.id.detail_button_scoreboard)).perform(click());
        intended(hasComponent(ScoreDetailActivity.class.getName()));

        onView(withId(R.id.score_detail_recycler)).check(new RecyclerViewItemCountAssertion(3));
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

}
