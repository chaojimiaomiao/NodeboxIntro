package com.market.extension.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.market.extension.BuildConfig;

import java.io.File;

public class PackageUtil {
    public static boolean hasServicePermission(Context ct, Class serviceClass) {
        int ok = 0;
        try {
            ok = Settings.Secure.getInt(ct.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }

        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (ok == 1) {
            String settingValue = Settings.Secure.getString(ct.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    if (accessibilityService.contains(serviceClass.getSimpleName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void smartInstall(Context context, String apkUrl) {
        String apkName = NetworkUtil.getNameFromUrl(apkUrl);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            localIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            localIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            localIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(localIntent);
    }

/*    private void installApk(downloadId: Long) {
        val uri = manager.getUriForDownloadedFile(downloadId);
        val intent = Intent(Intent.ACTION_VIEW)
        if (uri != null) {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            if ((Build.VERSION.SDK_INT >= 24)) {//版本是否在7.0以上
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //对目标apk的uri临时授权 使得有权限打开该Uri指向的Apk
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            } else {
                Log.e("DownloadManager","自动安装失败");
            }
        }else{
            Log.e("DownloadManager","下载失败");
        }
    }*/

    public static void jumpAnotherApp(Context context, String packageName, String start) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            if (packageName.equals("com.android.browser")) {
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(TextUtils.isEmpty(start) ? "https://jiediantech.github.io/jiedian/" : start);
                intent.setData(content_url);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
