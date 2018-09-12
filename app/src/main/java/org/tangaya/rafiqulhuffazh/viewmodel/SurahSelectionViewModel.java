package org.tangaya.rafiqulhuffazh.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import org.tangaya.rafiqulhuffazh.data.model.SurahModel;
import org.tangaya.rafiqulhuffazh.util.QuranUtil;
import org.tangaya.rafiqulhuffazh.view.adapter.SurahAdapter;

import java.util.ArrayList;
import java.util.List;

public class SurahSelectionViewModel extends BaseObservable {

//    public ObservableField<List<String>> surahListObservable;

    private List<SurahModel> data;
    private SurahAdapter adapter;

    public SurahSelectionViewModel() {
        data = new ArrayList<>();
        adapter = new SurahAdapter();

//        surahListObservable = new ObservableField<>();
//        surahListObservable.set(data);
    }

    public void setUp() {
        populateData();
    }

    public void tearDown() {
        // perform tear down tasks, such as removing listeners
    }

    private void populateData() {

        for (int i=1; i<=114; i++) {
            SurahModel dataModel = new SurahModel();
            dataModel.setTitle(QuranUtil.getSurahName(i));
            data.add(dataModel);
        }
//        notifyPropertyChanged(BR.data);

//        for (int i=1; i<=114; i++) {
//            data.add(QuranUtil.getSurahName(i));
//        }
    }

    @Bindable
    public List<SurahModel> getData() {
        return data;
    }

//    @Bindable
//    public void setData(List<SurahModel> data) {
//        this.data = data;
//    }

    @Bindable
    public SurahAdapter getAdapter() {
        return adapter;
    }

}
