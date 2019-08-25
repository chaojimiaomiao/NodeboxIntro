package com.market.extension.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.market.extension.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtil {

    public static long downloadApk(String apkUrl, Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        String apkName = getNameFromUrl(apkUrl);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
        request.setDestinationUri(Uri.fromFile(file));
        long downloadId = MyApplication.downloadManager.enqueue(request);
        return downloadId;
    }

    public static int getDownloadStatus(long downloadId, String apkUrl, Context context) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        int status = -1;
        try {
            cursor = MyApplication.downloadManager.query(query);//获得游标
            if (cursor != null && cursor.moveToFirst()) {
                status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_RUNNING:
                        //下载中
                        break;
                    case DownloadManager.STATUS_FAILED:
                        //下载失败
                        break;
                    case DownloadManager.STATUS_PAUSED:
                        //下载暂停
                        break;
                    case DownloadManager.STATUS_PENDING:
                        //下载延迟
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        //下载成功,发送广播
                        Intent intent = new Intent(DownloadFinishReceiver.DOWNLOAD_RECEIVER);
                        intent.putExtra("url", apkUrl);
                        context.sendBroadcast(intent);
                        break;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return status;
    }

    public static String getNameFromUrl(String apkUrl) {
        String[] names = apkUrl.split("/");
        String apkName = names[names.length - 1];
        return apkName;
    }

    public int getProgress(long downloadId, Context context) {
        //查询进度
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        int progress = 0;
        try {
            cursor = MyApplication.downloadManager.query(query);//获得游标
            if (cursor != null && cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_RUNNING:
                        //下载中
                        break;
                    case DownloadManager.STATUS_FAILED:
                        //下载失败
                        break;
                    case DownloadManager.STATUS_PAUSED:
                        //下载暂停
                        break;
                    case DownloadManager.STATUS_PENDING:
                        //下载延迟
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        //下载成功,发送广播
                        Intent intent = new Intent(DownloadFinishReceiver.DOWNLOAD_RECEIVER);
                        intent.putExtra("url", "http://www.baidu.com/pic/qqq.png");
                        context.sendBroadcast(intent);
                        break;
                }
                //当前的下载量
                int downloadSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //文件总大小
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                progress = (int) (downloadSoFar * 1.0f / totalBytes * 100);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return progress;
    }

    public static List<AppItem> parseApps(String jsonStr) throws JSONException {
        JSONArray jsonArray = null;
        List<AppItem> list = new ArrayList<AppItem>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            AppItem item = new AppItem();
            item.name = jsonObject.getString("name");
            item.icon = jsonObject.getString("icon");
            item.packageName = jsonObject.getString("package");
            item.start = jsonObject.getString("start");
            item.download = jsonObject.getString("download");
            item.isInstalled = false;
            list.add(item);
        }
        return list;
    }

    public static List<String> parseDeviceList(String jsonStr) throws JSONException {
        JSONArray jsonArray = null;
        List<String> list = new ArrayList<String>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    /**
     * 从指定的URL中获取数组
     * @param urlPath
     * @return
     */
    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());
    }

    public interface NetworkListener {
        public void onFinish();
    }
}
