package com.market.extension.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;

public class DownloadFinishReceiver extends BroadcastReceiver {
    public static final String DOWNLOAD_RECEIVER="com.DownloadFinishReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //下载完成的广播接收者
        if (intent.getAction().equals(DOWNLOAD_RECEIVER)) {
            Toast.makeText(context, "接收到广播", Toast.LENGTH_LONG).show();
            String apkUrl = intent.getStringExtra("url");
            //long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            setPermission(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "");
            PackageUtil.smartInstall(context, apkUrl);
        }
    }

    public static void setPermission(String filePath)  {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
