package org.tangaya.quranasrclient;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tangaya.quranasrclient.util.QuranScriptFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class QuranScriptFactoryTest {

    AssetManager assetManager = InstrumentationRegistry.getContext().getAssets();

    @Before
    public void loadQuranScriptData() {

        QuranScriptFactory.init(assetManager);
    }

    @Test
    public void quranScriptFactory_GetVerseQScript() {

        BufferedReader verseBr = null;

        try {
            verseBr = new BufferedReader(new InputStreamReader(assetManager.open("qscript.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numVerse;
        for (int chapter=1; chapter<=114; chapter++) {

            numVerse = QuranScriptFactory.getChapter(chapter).getNumVerse();

            for (int verse=1; verse<=numVerse; verse++) {

                String qScriptRef = "";
                try {
                    qScriptRef = verseBr.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String qScriptInApp = QuranScriptFactory.getVerseQScript(chapter, verse);

                Log.d("QScript Test", "isEqual ("+qScriptInApp+", "+qScriptRef + ") = " + qScriptInApp.equals(qScriptRef));
                assertThat(qScriptInApp, is(qScriptRef));
            }
        }
    }

}
