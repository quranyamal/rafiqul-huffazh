package org.tangaya.rafiqulhuffazh.data.model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;

import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class Evaluation extends BaseObservable {

    private Attempt attempt;
    private String transcription, reference;
    private String evalDescription;

    boolean isCorrect;

    int ayah, surah;

    public ObservableInt earnedPoints = new ObservableInt(90);
    public ObservableInt maxPoints = new ObservableInt(100);

    public Evaluation() {}

    public Evaluation(Attempt attempt, String transcription_) {

        this.attempt = attempt;
        this.transcription = transcription_;

        surah = attempt.getSurahNum();
        ayah = attempt.getAyahNum();

        reference = QuranUtil.getAyah(surah, ayah);
        isCorrect = transcription.equals(reference);

        if (isCorrect) {
            evalDescription = "Correct";
        } else {
            evalDescription = "todo";
        }

    }

    public void setEvalDescription(String desc) {
        evalDescription = desc;
    }

    public ObservableInt getEarnedPoints() {
        return earnedPoints;
    }

    public ObservableInt getMaxpoints() {
        return maxPoints;
    }

}
