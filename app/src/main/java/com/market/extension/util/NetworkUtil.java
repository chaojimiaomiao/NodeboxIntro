package com.market.extension.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtil {
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
