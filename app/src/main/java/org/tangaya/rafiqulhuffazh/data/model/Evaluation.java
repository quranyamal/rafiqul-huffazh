package org.tangaya.rafiqulhuffazh.data.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.tangaya.rafiqulhuffazh.R;
import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.util.diff.diff_match_patch;

import java.util.LinkedList;

import timber.log.Timber;

public class Evaluation extends BaseObservable {

    enum ResultType {
        CORRECT,
        SMALL_MISTAKE,  // pts > 0.5
        LOT_MISTAKE,    // pts <= 0.5
        JUMPING
    }

    public ObservableInt earnedPoints = new ObservableInt();
    public ObservableInt maxPoints = new ObservableInt();
    public ObservableField<String> reference = new ObservableField<>();
    public ObservableField<String> recognized = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<QuranAyahAudio> audio = new ObservableField<>();
    public ObservableInt surah = new ObservableInt();
    public ObservableInt ayah = new ObservableInt();
    public ObservableField<String> surahName = new ObservableField<>();

    public ObservableField<Void> clickListener = new ObservableField<>();

    public ObservableField<Spanned> coloredEvalText = new ObservableField<>();

    // debugging stuff
    public ObservableField<LinkedList<diff_match_patch.Diff>> diffs = new ObservableField<>();

    public ObservableBoolean isCorrect = new ObservableBoolean();
    public ObservableInt cardColor = new ObservableInt();

    private ResultType resultType;

    public Evaluation(QuranAyahAudio audio) {
        this.audio.set(audio);
        surah.set(audio.surah);
        ayah.set(audio.ayah);
        surahName.set(QuranUtil.getSurahName(audio.surah));

        reference.set(QuranScriptConverter.toArabic(audio.qscript));
        recognized.set(QuranScriptConverter.toArabic(audio.transcription));

        clickListener.set(onClickEval());
    }

    public int getEarnedPoints() {
        return earnedPoints.get();
    }

    public int getMaxPoints() {
        return maxPoints.get();
    }

    public void setEvalDescription(String desc) {
        // todo: add jump to detail

         description.set(desc);
         isCorrect.set(desc.equals(MurojaahEvaluator.CORRECT_MESSAGE));
    }

    public void setMaxPoints(int maxPts) {
        maxPoints.set(maxPts);
    }

    public void setDiffs(LinkedList<diff_match_patch.Diff> diffs) {
        this.diffs.set(diffs);
    }

    public void setColoredEvalText() {

        String str = "";
        for (diff_match_patch.Diff diff : diffs.get()) {

            diff.text = QuranScriptConverter.toArabic(diff.text);

            if (diff.operation.equals(diff_match_patch.Operation.EQUAL)) {
                str = str + "<font color=#29a600> " + diff.text + "</font>";
            } else if (diff.operation.equals(diff_match_patch.Operation.DELETE)) {
                str = str + "<font color=#7f0000> " + diff.text + "</font>";
            }
        }

        coloredEvalText.set(Html.fromHtml(str));
    }

    public void setEarnedPoints(int pts) {
        earnedPoints.set(pts);
    }

    public void setResultType() {
        float pts = (float) earnedPoints.get() / maxPoints.get();
        if (pts==1) {
            resultType = ResultType.CORRECT;
            cardColor.set(R.color.green);
            Timber.d("setResultType cardColor green");
        } else if (pts>0.5) {
            resultType = ResultType.SMALL_MISTAKE;
            cardColor.set(R.color.yellow);
            Timber.d("setResultType cardColor yellow");
        } else {
            resultType = ResultType.LOT_MISTAKE;
            cardColor.set(R.color.red);
            Timber.d("setResultType cardColor red");
        }
        // todo: jumping case
    }

    public Void onClickEval() {
        Log.d("Evaluation", "onClickEval");
        return null;
    }

}
