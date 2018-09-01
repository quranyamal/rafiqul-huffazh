package org.tangaya.rafiqulhuffazh;


import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
@SmallTest
public class QuranScriptConverterTest {

    AssetManager assetManager = InstrumentationRegistry.getContext().getAssets();

    @Before
    public void init() {
        QuranScriptConverter.init(assetManager);
    }

    @Test
    public void basmalahTest() {
        String qScript = "bismil lAhir roHmAnir roHIm";
        String extectedResult = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيم";

        assertThat(QuranScriptConverter.toArabic(qScript), is(extectedResult));
    }

    @Test
    public void arrahmanirrahim() {
        String qScript = "earroHmAnir roHIm";
        String extectedResult = "ٱلرَّحْمَٰنِ ٱلرَّحِيم";

        assertThat(QuranScriptConverter.toArabic(qScript), is(extectedResult));
    }
}
