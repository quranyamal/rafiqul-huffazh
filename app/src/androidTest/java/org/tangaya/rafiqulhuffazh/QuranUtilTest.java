package org.tangaya.rafiqulhuffazh;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class QuranUtilTest {

    @Before
    public void loadJson() {
        AssetManager assetManager = InstrumentationRegistry.getContext().getAssets();
        QuranUtil.init(assetManager);
    }

    @Test
    public void quranUtil_GetFirstSurahName_ReturnsCorrectString() {

        MatcherAssert.assertThat(QuranUtil.getSurahName(1), is("Al Fatihah"));
    }

    @Test
    public void quranUtil_GetLastSurahName_ReturnsCorrectString() {

        MatcherAssert.assertThat(QuranUtil.getSurahName(114), is("An Nas"));
    }

    @Test
    public void quranUtil_GetFirstQScript_ReturnsCorrectString() {

        String fatihah1 = QuranUtil.getQScript(1,1);
        MatcherAssert.assertThat(fatihah1, is("bismil lAhir roHmAnir roHIm"));
    }

    @Test
    public void quranUtil_GetLastQScript_ReturnsCorrectString() {

        String annas6 = QuranUtil.getQScript(114,6);
        MatcherAssert.assertThat(annas6, is("minal jinnati wannAs"));
    }

    @Test
    public void quranUtil_GetFirstAyah_ReturnsCorrectString() {

        String fatihah1 = QuranUtil.getAyah(1,1);
        MatcherAssert.assertThat(fatihah1, is("بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"));
    }

    @Test
    public void quranUtil_GetLastAyah_ReturnsCorrectString() {

        String annas6 = QuranUtil.getAyah(114,6);
        MatcherAssert.assertThat(annas6, is("مِنَ ٱلْجِنَّةِ وَٱلنَّاسِ"));
    }

    @Test
    public void quranUtil_GetQScriptWithValidIndexes_NeverReturnsInvalidIndexMessage() {

        for (int chapter=1; chapter<=114; chapter++) {
            int numVerse = QuranUtil.getNumAyah(chapter);

            for (int verse=1; verse<=numVerse; verse++) {
                MatcherAssert.assertThat(QuranUtil.getAyah(chapter, verse),
                        not(QuranUtil.INVALID_INDEX_MESSAGE));
            }
        }
    }

    @Test
    public void quranUtil_GetQScriptWithMinimumIndex_ReturnsCorrectString() {

        String fatihah1 = QuranUtil.getQScript(1,1);
        MatcherAssert.assertThat(fatihah1, is("bismil lAhir roHmAnir roHIm"));
    }

    @Test
    public void quranUtil_GetQScriptWithMaximumIndex_ReturnsCorrectString() {

        String annas6 = QuranUtil.getQScript(114,6);
        MatcherAssert.assertThat(annas6, is("minal jinnati wannAs"));
    }

    @Test
    public void quranUtil_GetQScriptWithInvalidIndexes_ReturnsInvalidIndexMessage() {

        String invalidIdxMsg = QuranUtil.INVALID_INDEX_MESSAGE;
        MatcherAssert.assertThat(QuranUtil.getQScript(0,0), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranUtil.getQScript(0,1), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranUtil.getQScript(1,8), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranUtil.getQScript(114,0), is(invalidIdxMsg));
        MatcherAssert.assertThat(QuranUtil.getQScript(115,1), is(invalidIdxMsg));
    }
}
