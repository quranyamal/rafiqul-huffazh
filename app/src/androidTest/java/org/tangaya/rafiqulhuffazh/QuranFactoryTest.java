package org.tangaya.rafiqulhuffazh;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tangaya.rafiqulhuffazh.util.QuranFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class QuranFactoryTest {

    @Before
    public void loadJson() {
        AssetManager assetManager = InstrumentationRegistry.getContext().getAssets();
        QuranFactory.init(assetManager);
    }

    @Test
    public void quranFactory_GetFirstSurahName_ReturnsCorrectString() {

        MatcherAssert.assertThat(QuranFactory.getSurahName(1), is("Al Fatihah"));
    }

    @Test
    public void quranFactory_GetLastSurahName_ReturnsCorrectString() {

        MatcherAssert.assertThat(QuranFactory.getSurahName(114), is("An Nas"));
    }

    @Test
    public void quranFactory_GetFirstQScript_ReturnsCorrectString() {

        String fatihah1 = QuranFactory.getQScript(1,1);
        MatcherAssert.assertThat(fatihah1, is("bismil lAhir roHmAnir roHIm"));
    }

    @Test
    public void quranFactory_GetLastQScript_ReturnsCorrectString() {

        String annas6 = QuranFactory.getQScript(114,6);
        MatcherAssert.assertThat(annas6, is("minal jinnati wannAs"));
    }

    @Test
    public void quranFactory_GetFirstAyah_ReturnsCorrectString() {

        String fatihah1 = QuranFactory.getAyah(1,1);
        MatcherAssert.assertThat(fatihah1, is("بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"));
    }

    @Test
    public void quranFactory_GetLastAyah_ReturnsCorrectString() {

        String annas6 = QuranFactory.getAyah(114,6);
        MatcherAssert.assertThat(annas6, is("مِنَ ٱلْجِنَّةِ وَٱلنَّاسِ"));
    }

    @Test
    public void quranFactory_GetQScriptWithValidIndexes_NeverReturnsInvalidIndexMessage() {

        for (int chapter=1; chapter<=114; chapter++) {
            int numVerse = QuranFactory.getNumAyah(chapter);

            for (int verse=1; verse<=numVerse; verse++) {
                MatcherAssert.assertThat(QuranFactory.getAyah(chapter, verse),
                        not(QuranFactory.INVALID_INDEX_MESSAGE));
            }
        }
    }

    @Test
    public void quranFactory_GetQScriptWithMinimumIndex_ReturnsCorrectString() {

        String fatihah1 = QuranFactory.getQScript(1,1);
        MatcherAssert.assertThat(fatihah1, is("bismil lAhir roHmAnir roHIm"));
    }

    @Test
    public void quranFactory_GetQScriptWithMaximumIndex_ReturnsCorrectString() {

        String annas6 = QuranFactory.getQScript(114,6);
        MatcherAssert.assertThat(annas6, is("minal jinnati wannAs"));
    }

    @Test
    public void quranFactory_GetQScriptWithInvalidIndexes_ReturnsInvalidIndexMessage() {

        String invalidIdxMsg = QuranFactory.INVALID_INDEX_MESSAGE;
        MatcherAssert.assertThat(QuranFactory.getQScript(0,0), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranFactory.getQScript(0,1), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranFactory.getQScript(1,8), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranFactory.getQScript(114,0), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranFactory.getQScript(115,1), is(invalidIdxMsg));
    }
}
