package com.market.extension.widget;

import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.market.extension.R;
import com.market.extension.util.AppItem;
import com.market.extension.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AppItem> mData = new ArrayList<>();

    public AppListAdapter(Context context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    public void setData(List<AppItem> data) {
        this.mData = data;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dapp, null);
            holder.img = (ImageView)convertView.findViewById(R.id.dapp_icon);
            holder.title = (TextView)convertView.findViewById(R.id.dapp_name);
            holder.info = (TextView)convertView.findViewById(R.id.dapp_desc);
            holder.progressBar = (CircularProgressBar)convertView.findViewById(R.id.dapp_progress);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        AppItem item = mData.get(position);
        holder.url = item.download;
        holder.title.setText(item.name);
        holder.info.setText(item.name);
        holder.initProgressBar();
        if (item.icon.contains("R.mipmap")) {
            if (convertStrToInt(item.icon) > 0) {
                holder.img.setImageResource(convertStrToInt(item.icon));
            }
        } else {
            //Glide.with(context).load(item.icon).into(holder.imageView);
        }
        /*holder.viewBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showInfo();
            }
        });*/


        return convertView;
    }

    public final class ViewHolder{
        public String url;
        public ImageView img;
        public TextView title;
        public TextView info;
        public CircularProgressBar progressBar;
        private io.reactivex.disposables.Disposable disposable;

        public void initProgressBar() {
//            mCircularProgressBar = findViewById(R.id.dapp_progress);
//        mCircularProgressBar.setTipsMessage("下载");
//        mCircularProgressBar.setDefColor(getResources().getColor(R.color.x_red));
//        mCircularProgressBar.setFontColor(getResources().getColor(R.color.x_yellow));
//        mCircularProgressBar.setTabColor(getResources().getColor(R.color.x_blue));
//        mCircularProgressBar.setProgressColor(getResources().getColor(R.color.colorPrimaryDark));
//        mCircularProgressBar.setProgressWidth(4);
//        mCircularProgressBar.setRectWidth(1);
//        mCircularProgressBar.setTipsFinish("你好");
            progressBar.setOnClickListener(v -> {
                if (disposable != null) {
                    disposable.dispose();
                }
                if (progressBar.isInProgress()) {
                    progressBar.doPauseProgress();
                } else {
                    doStartDownLoad();
                }
            });
        }

        private void doStartDownLoad() {
            long downloadId = NetworkUtil.downloadApk(url, mContext);
            progressBar.doStartProgress();
            //此处指的是过1s做一个操作
            disposable = io.reactivex.Observable.interval(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        int status = NetworkUtil.getDownloadStatus(downloadId, url, mContext);
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            //Toast.makeText(mContext, "下载成功！", Toast.LENGTH_LONG).show();
                            progressBar.doFinishProgress();
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            Toast.makeText(mContext, "下载出错", Toast.LENGTH_LONG).show();
                        }
                        if (aLong >= 9) {
                            //progressBar.doFinishProgress();
                        } else {
                            progressBar.setProgress((int) ((aLong + 1) * 10), 100);
                        }
                    });
            progressBar.setOnProgressListener(disposable::dispose);
        }
    }

    private int convertStrToInt(String iconName) {
        if (TextUtils.isEmpty(iconName)) {
            return -1;
        }
        return R.mipmap.icon;
    }
}
