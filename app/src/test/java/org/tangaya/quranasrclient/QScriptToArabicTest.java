package org.tangaya.quranasrclient;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Test;
import org.mockito.Mock;
import org.tangaya.quranasrclient.util.QScriptToArabic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QScriptToArabicTest {

    @Mock
    MockContext mMockContext;

    @Test
    public void loadDataFromAssets_ReturnsZero() {
        mMockContext = new MockContext();

        assertThat (QScriptToArabic.init(mMockContext), is(0));
    }

}
