package org.tangaya.quranasrclient;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Test;
import org.mockito.Mock;
import org.tangaya.quranasrclient.data.source.QuranScriptRepositoryOld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class QuranScriptRepositoryOldTest {

    @Mock
    Context mMockContext;


    @Test
    public void mockAssets_NotNull() {
        mMockContext = new MockContext();

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("quran-simple.txt");
        File file = new File(resource.getPath());

        FileReader fl = null;

        try {
            fl = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(fl);

        assertThat(file, is(notNullValue()));
        assertThat(br, is(notNullValue()));
//        assertThat(mMockContext, is(notNullValue()));
//        assertThat(mMockContext.getAssets(), is(notNullValue()));

        assertThat("1", is("1"));
    }

    @Test
    public void loadDataFromAssets_ReturnsZero() {
        mMockContext = new MockContext();

        assertThat (QuranScriptRepositoryOld.init(mMockContext), is(0));
    }

}
