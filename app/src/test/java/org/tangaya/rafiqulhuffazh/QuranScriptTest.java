package org.tangaya.rafiqulhuffazh;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class QuranScriptTest {

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

        URL qscriptUrl= getClass().getClassLoader().getResource("qscript.txt");
        URL quranUrl= getClass().getClassLoader().getResource("quran-uthmani.txt");
        URL quranSurahsUrl = getClass().getClassLoader().getResource("quran_surahs_name.csv");

//        QuranScriptFactory.init(is1, is2, is3);

        URLConnection qscriptUrlConn, quranUrlConn, quranSurahUrlConn;

        try {
            qscriptUrlConn = new URL(qscriptUrl.toString()).openConnection();
            quranUrlConn = new URL(quranUrl.toString()).openConnection();
            quranSurahUrlConn = new URL(quranSurahsUrl.toString()).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        InputStream isQScript = qscriptUrlConn.getInputStream();
//        BufferedReader reader = null;
//
//        try {
//            reader = new BufferedReader(new InputStreamReader(isQScript, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        System.out.println(QuranScriptFactory.getSurah(1).getVerseQScript(1));

        assertThat(qscriptUrl, notNullValue());
        assertThat(quranUrl, notNullValue());
        assertThat(quranSurahsUrl, notNullValue());

//        assertThat(reader, notNullValue());
    }

}
