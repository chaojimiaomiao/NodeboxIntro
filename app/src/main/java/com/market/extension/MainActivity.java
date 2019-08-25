package com.market.extension;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.market.extension.ui.SectionsPagerAdapter;
import com.market.extension.util.AppItem;
import com.market.extension.util.DownloadFinishReceiver;
import com.market.extension.util.NetworkUtil;
import com.market.extension.util.PackageUtil;

import java.io.File;
import java.util.List;

import static com.market.extension.util.DownloadFinishReceiver.setPermission;

public class MainActivity extends AppCompatActivity {
    protected List<AppItem> remoteAppList;
    private DownloadFinishReceiver finishReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //((SearchView)findViewById(R.id.main_search)).setIconified(false);

        IntentFilter intentFilter = new IntentFilter(DownloadFinishReceiver.DOWNLOAD_RECEIVER);
        finishReceiver = new DownloadFinishReceiver();
        registerReceiver(finishReceiver, intentFilter);

        /*String apkName = NetworkUtil.getNameFromUrl("browser_caidan.apk");
        setPermission(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + apkName);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            localIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            localIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            localIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            localIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(localIntent);*/
    }

    private void openAccess() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PackageUtil.hasServicePermission(this, MyAccessibilityService.class)) {
            findViewById(R.id.open_access).setVisibility(View.GONE);
        } else {
            findViewById(R.id.open_access).setVisibility(View.VISIBLE);
            findViewById(R.id.open_access).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccess();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(finishReceiver);
        super.onDestroy();
    }
}