package com.market.extension.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.market.extension.util.AppItem;
import com.market.extension.util.NetworkUtil;

import java.util.List;

public class ListViewModel extends ViewModel {
    protected String APPS_URL = "https://gitee.com/chaojimaomao/codes/nmatlrf2q541gdcix3ekw49/raw?blob_name=applist";
    private MutableLiveData<List<AppItem>> chosenApps;

    public LiveData<List<AppItem>> getApps() {
        if (chosenApps == null) {
            chosenApps = new MutableLiveData<>();
            getRemoteAppList();
        }
        return chosenApps;
    }

    public void loadApps() {
    }

    private void getRemoteAppList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonStr = NetworkUtil.readParse(APPS_URL);
                    chosenApps.postValue(NetworkUtil.parseApps(jsonStr));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}