package org.tangaya.quranasrclient;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tangaya.quranasrclient.view.ui.ChapterSelectionActivity;
import org.tangaya.quranasrclient.view.ui.HomeActivity;
import org.tangaya.quranasrclient.view.ui.MurojaahActivity;
import org.tangaya.quranasrclient.view.ui.TutorialActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.support.test.espresso.contrib.RecyclerViewActions;


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
    public void murojaahActivityTest_SimulateFromHome() {
        onView(withId(R.id.murojaah_button_home)).perform(click());

        intended(hasComponent(ChapterSelectionActivity.class.getName()));

        onView(withId(R.id.chapters_recycler_view)).perform(RecyclerViewActions.scrollToPosition(105));

        onView(withText("Al 'Asr")).check(matches(isDisplayed()));

        try {
            Thread.sleep(1000);        // wait for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView((withText("Al 'Asr"))).perform(click());

        intended(hasComponent(TutorialActivity.class.getName()));

        onView((withText("skip"))).perform(click());

        intended(hasComponent(MurojaahActivity.class.getName()));
    }

    @Test
    public void murojaahActivity_SimulateAlAsr() {
        try {
            Thread.sleep(1000);        // wait for second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.attempt_button_murojaah)).perform(click());

        onView(withId(R.id.instruction_murojaah)).check(matches(withText(R.string.recording_true)));

        onView(withId(R.id.verse_num_text)).perform(click());

    }

}
