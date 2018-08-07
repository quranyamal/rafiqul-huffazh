package org.tangaya.quranasrclient;


import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tangaya.quranasrclient.view.ui.HomeActivity;
import org.tangaya.quranasrclient.viewmodel.ServerSettingViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ServerSettingActivityTest {

    private String ipAddress, port;

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void initServerAddress() {

    }

    @Test
    public void checkServer_InputValidAddress_ShowsConnectedStatus() {

        ipAddress = "10.1.3.31";
        port = "8888";

        onView(withId(R.id.setting_button_home)).perform(click());

        onView(withId(R.id.hostname_input)).perform(click(), clearText(), typeText(ipAddress));

        onView(withId(R.id.port_input)).perform(click(), clearText(), typeText(port));

        onView(withId(R.id.connect_button)).perform(click());

        try {
            Thread.sleep(5000);        // maximum waiting time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.connection_status)).check(matches(withText(ServerSettingViewModel.CONNECTION_STATUS_CONNECTED)));
    }

    @Test
    public void checkServer_InputInvalidAddress_ShowsConnectedStatus() {

        ipAddress = "10.1.3.123";
        port = "8888";

        onView(withId(R.id.setting_button_home)).perform(click());

        onView(withId(R.id.hostname_input)).perform(click(), clearText(), typeText(ipAddress));

        onView(withId(R.id.port_input)).perform(click(), clearText(), typeText(port), closeSoftKeyboard());

        onView(withId(R.id.connect_button)).perform(click());

        try {
            Thread.sleep(5000);        // maximum waiting time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.connection_status)).check(matches(withText(ServerSettingViewModel.CONNECTION_STATUS_ERROR)));
    }

}
