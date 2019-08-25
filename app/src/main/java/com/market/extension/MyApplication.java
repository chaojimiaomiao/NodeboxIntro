package com.market.extension;

import android.app.Application;
import android.app.DownloadManager;

public class MyApplication extends Application {
    public static DownloadManager downloadManager;
    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    }
}
