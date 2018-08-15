package org.tangaya.rafiqulhuffazh;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.util.QuranScriptFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
@SmallTest
public class MurojaahEvaluatorTest {

    AssetManager assetManager = InstrumentationRegistry.getContext().getAssets();

    @Before
    public void initQuranScriptFactory() {
        QuranUtil.init(assetManager);
    }

    @Test
    public void murojaahEvaluator_CorrectAttempt_ReturnsCorrectMessage() {
        int chapter=103, verse=3;
        String qScriptRecognized = QuranUtil.getQScript(chapter, verse);

        String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);
        assertThat(evalResult, is(MurojaahEvaluator.CORRECT_MESSAGE));
    }

    @Test
    public void murojaahEvaluator_IncorrectAttemptBySkippingVerse_ReturnsSkippingVerseIncorrectMessage() {
        int chapter=91, verse=8;

        String qScriptRecognized = QuranUtil.getQScript(chapter, verse+1);
        String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluator.INCORRECT_MESSAGE_SKIPPING_ONE_VERSE));
    }

    @Test
    public void murojaahEvaluator_IncorrectAttemptBySkippingVerses_ReturnsSkippingVersesIncorrectMessage() {
        int chapter=91, verse=8;

        String qScriptRecognized = QuranUtil.getQScript(chapter, verse+2);
        String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluator.INCORRECT_MESSAGE_SKIPPING_SOME_VERSES));
    }

    @Test
    public void murojaahEvaluator_IncorrectAttemptByReturningToPrevVerse_ReturnsReturnToPrevVerseIncorrectMessage() {
        int chapter=91, verse=8;

        for (int i=1; i<verse; i++) {
            String qScriptRecognized = QuranUtil.getQScript(chapter, i);
            String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);
            assertThat(evalResult, is(MurojaahEvaluator.INCORRECT_MESSAGE_RETURNING_TO_PREV_VERSE));
        }
    }

    @Test
    public void murojaahEvaluator_IncorrectAttemptByInsertingPart_ReturnsInsertionPartIncorrectMessage() {
        int chapter=103, verse=3;
        String qScriptRecognized = "eillAl lacIna eAmanU waEamilUS SOliHAti falahum watawASC bilHaqqi watawASC biSSoBr";

        String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);
        assertThat(evalResult, is(MurojaahEvaluator.INCORRECT_MESSAGE_INSERTION_PART));
    }

    @Test
    public void murojaahEvaluator_IncorrectAttemptByMissingPart_ReturnsMissingPartIncorrectMessage() {
        int chapter=103, verse=3;

        String qScriptRecognized = "eillAl lacIna eAmanU watawASC bilHaqqi watawASC biSSoBr";
        String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluator.INCORRECT_MESSAGE_MISSING_PART));
    }

    @Test
    public void murojaahEvaluator_IncorrectAttemptByInsAndMissPart_ReturnsInsAndMissPartIncorrectMessage() {
        int chapter=103, verse=3;

        String qScriptRecognized = "eillAl lacIna eAmanU falahum bilHaqqi falahum biSSoBr";
        String evalResult = MurojaahEvaluator.evaluate(chapter, verse, qScriptRecognized);

        assertThat(evalResult, is(MurojaahEvaluator.INCORRECT_MESSAGE_INSERTION_AND_MISSING_PART));
    }

}
