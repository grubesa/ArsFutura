package grubi.arsfutura.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

public class SearchLiveData extends MediatorLiveData<Pair<String, Integer>> {
    public SearchLiveData(LiveData<String> query, LiveData<Integer> page) {
        addSource(query, new Observer<String>() {
            public void onChanged(@Nullable String query) {
                setValue(Pair.create(query, page.getValue()));
            }
        });
        addSource(page, new Observer<Integer>() {
            public void onChanged(@Nullable Integer page) {
                setValue(Pair.create(query.getValue(), page));
            }
        });
    }
}