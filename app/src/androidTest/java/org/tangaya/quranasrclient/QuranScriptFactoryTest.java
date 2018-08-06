package org.tangaya.quranasrclient;

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
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
    public void quranScriptFactory_GetQScriptWithValidIndexes_NeverReturnsInvalidIndexMessage() {

        for (int chapter=1; chapter<=114; chapter++) {
            int numVerse = QuranScriptFactory.getChapter(chapter).getNumVerse();

            for (int verse=1; verse<=numVerse; verse++) {
                assertThat(QuranScriptFactory.getArabicVerseScript(chapter, verse),
                        not(QuranScriptFactory.INVALID_INDEX_MESSAGE));
            }
        }
    }

    @Test
    public void quranScriptFactory_GetQScriptWithMinimumIndex_ReturnsCorrectString() {

        String fatihah1 = QuranScriptFactory.getVerseQScript(1,1);
        assertThat(fatihah1, is("bismil lAhir roHmAnir roHIm"));
    }

    @Test
    public void quranScriptFactory_GetQScriptWithMaximumIndex_ReturnsCorrectString() {

        String annas6 = QuranScriptFactory.getVerseQScript(114,6);
        assertThat(annas6, is("minal jinnati wannAs"));
    }

    @Test
    public void quranScriptFactory_GetQScriptWithInvalidIndexes_ReturnsInvalidIndexMessage() {

        String invalidIdxMsg = QuranScriptFactory.INVALID_INDEX_MESSAGE;
        assertThat(QuranScriptFactory.getVerseQScript(0,0), is(invalidIdxMsg));
        assertThat(QuranScriptFactory.getVerseQScript(0,1), is(invalidIdxMsg));
        assertThat(QuranScriptFactory.getVerseQScript(1,8), is(invalidIdxMsg));
        assertThat(QuranScriptFactory.getVerseQScript(114,0), is(invalidIdxMsg));
        assertThat(QuranScriptFactory.getVerseQScript(115,1), is(invalidIdxMsg));
    }

    @Test
    public void quranScriptFactory_GetFirstQScript() {

        String fatihah1 = QuranScriptFactory.getVerseQScript(1,1);
        assertThat(fatihah1, is("bismil lAhir roHmAnir roHIm"));
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

                //Log.d("QScript Test", "isEqual ("+qScriptInApp+", "+qScriptRef + ") = " + qScriptInApp.equals(qScriptRef));
                assertThat(qScriptInApp, is(qScriptRef));
            }
        }
    }

    @Test
    public void quranScriptFactory_GetArabicVerseScript() {
        BufferedReader verseBr = null;

        try {
            verseBr = new BufferedReader(new InputStreamReader(assetManager.open("quran-uthmani.txt")));
            //verseBr.readLine(); // skipping first line
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numVerse;
        for (int chapter = 1; chapter <= 114; chapter++) {

            numVerse = QuranScriptFactory.getChapter(chapter).getNumVerse();

            for (int verse = 1; verse <= numVerse; verse++) {

                String verseScriptRef = "";
                try {
                    String lineVerse = verseBr.readLine();
                    String[] lineVersePart = lineVerse.split("\\|");

                    verseScriptRef = lineVersePart[2];

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (chapter>1 && verse==1) {
                    // remove basmalah from first verse of every chapter (except Al-Fatiahah)
                    verseScriptRef = verseScriptRef.substring(37, verseScriptRef.length());
                }

                String verseScriptInApp = QuranScriptFactory.getArabicVerseScript(chapter, verse);

                Log.d("Quran Verse Script Test", "isEqual (" + verseScriptInApp + ", " + verseScriptRef + ") = " + verseScriptInApp.equals(verseScriptRef));
                assertThat(verseScriptInApp, is(verseScriptRef));
            }
        }
    }

}
