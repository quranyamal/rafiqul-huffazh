package org.tangaya.quranasrclient;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tangaya.quranasrclient.util.MurojaahEvaluatorNew;
import org.tangaya.quranasrclient.util.QuranScriptFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
@SmallTest
public class MurojaahEvaluatorTest {

    AssetManager assetManager = InstrumentationRegistry.getContext().getAssets();

    @Before
    public void initQuranScriptFactory() {
        QuranScriptFactory.init(assetManager);
    }

    @Test
    public void murojaahEvaluator_CorrectAttempt_ReturnsCorrectMessage() {
        int chapter=103, verse=3;
        String qScriptRecognized = QuranScriptFactory.getVerseQScript(chapter, verse);

        String evalResult = MurojaahEvaluatorNew.evaluate(chapter, verse, qScriptRecognized);
        assertThat(evalResult, is(MurojaahEvaluatorNew.CORRECT_MESSAGE));
    }

    @Test
    public void murojaahEvaluator_AttemptWithInsertionPart_ReturnsInsertionPartIncorrectMessage() {
        int chapter=103, verse=3;
        String qScriptRecognized = "eillAl lacIna eAmanU waEamilUS SOliHAti falahum watawASC bilHaqqi watawASC biSSoBr";

        String evalResult = MurojaahEvaluatorNew.evaluate(chapter, verse, qScriptRecognized);
        assertThat(evalResult, is(MurojaahEvaluatorNew.INCORRECT_MESSAGE_INSERTION_PART));
    }

    @Test
    public void murojaahEvaluator_AttemptWithMissingPart_ReturnsMissingPartIncorrectMessage() {
        int chapter=103, verse=3;

        String qScriptRecognized = "eillAl lacIna eAmanU watawASC bilHaqqi watawASC biSSoBr";
        String evalResult = MurojaahEvaluatorNew.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluatorNew.INCORRECT_MESSAGE_MISSING_PART));
    }

    @Test
    public void murojaahEvaluator_AttemptWithInsAndMissPart_ReturnsInsAndMissPartIncorrectMessage() {
        int chapter=103, verse=3;

        String qScriptRecognized = "eillAl lacIna eAmanU falahum bilHaqqi falahum biSSoBr";
        String evalResult = MurojaahEvaluatorNew.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluatorNew.INCORRECT_MESSAGE_INSERTION_AND_MISSING_PART));
    }

    @Test
    public void murojaahEvaluator_SkippingVerseAttempt_ReturnsSkippingVerseIncorrectMessage() {
        int chapter=91, verse=8;

        String qScriptRecognized = QuranScriptFactory.getVerseQScript(chapter, verse+1);
        String evalResult = MurojaahEvaluatorNew.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluatorNew.INCORRECT_MESSAGE_SKIPPING_ONE_VERSE));
    }

}
