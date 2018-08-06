package org.tangaya.quranasrclient;

import android.test.mock.MockContext;

import org.junit.Test;
import org.mockito.Mock;
import org.tangaya.quranasrclient.util.QuranScriptConverter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QuranScriptConverterTest {

    @Mock
    MockContext mMockContext;

    @Test
    public void loadDataFromAssets_ReturnsZero() {
        mMockContext = new MockContext();

        assertThat (QuranScriptConverter.init(mMockContext), is(0));
    }

}
