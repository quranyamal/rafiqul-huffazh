package org.tangaya.rafiqulhuffazh.data.model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import org.tangaya.rafiqulhuffazh.util.QuranUtil;

public class Evaluation extends BaseObservable {

    public ObservableInt earnedPoints = new ObservableInt();
    public ObservableInt maxPoints = new ObservableInt();
    public ObservableField<String> qscript = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<QuranAyahAudio> audio = new ObservableField<>();

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
    }

    public void setMaxPoints(int maxPts) {
        maxPoints.set(maxPts);
    }

    public void setEarnedPoints(int pts) {
        earnedPoints.set(pts);
    }

}
