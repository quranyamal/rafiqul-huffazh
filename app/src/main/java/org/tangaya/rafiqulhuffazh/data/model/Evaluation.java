package org.tangaya.rafiqulhuffazh.data.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Html;
import android.text.Spanned;

import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;
import org.tangaya.rafiqulhuffazh.util.diff.diff_match_patch;

import java.util.LinkedList;

public class Evaluation extends BaseObservable {

    public ObservableInt earnedPoints = new ObservableInt();
    public ObservableInt maxPoints = new ObservableInt();
    public ObservableField<String> qscript = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<QuranAyahAudio> audio = new ObservableField<>();

    public ObservableField<Spanned> coloredEvalText = new ObservableField<>();

    // debugging stuff
    public ObservableField<LinkedList<diff_match_patch.Diff>> diffs = new ObservableField<>();

    public ObservableBoolean isCorrect = new ObservableBoolean();

    public Evaluation(QuranAyahAudio audio) {
        this.audio.set(audio);

    }

    public int getEarnedPoints() {
        return earnedPoints.get();
    }

    public int getMaxPoints() {
        return maxPoints.get();
    }

    public void setEvalDescription(String desc) {
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

            if (diff.operation.equals(diff_match_patch.Operation.EQUAL)) {
                str = str + "<font color=#29a600> " + QuranScriptConverter.toArabic(diff.text) + "</font>";
            } else if (diff.operation.equals(diff_match_patch.Operation.DELETE)) {
                str = str + "<font color=#7f0000> " + QuranScriptConverter.toArabic(diff.text) + "</font>";
            }
        }

        coloredEvalText.set(Html.fromHtml(str));
    }

    public void setEarnedPoints(int pts) {
        earnedPoints.set(pts);
    }

}
