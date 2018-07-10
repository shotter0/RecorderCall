package com.ys.recorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabeijianxi.smallvideo.R;
import com.ys.recorder.entity.AppInfomation;

import java.util.List;

public class AppInfosAdapter extends BaseAdapter {
    List<AppInfomation> appInfos;
    Context context;

    public AppInfosAdapter(Context paramContext, List<AppInfomation> paramList) {
        this.context = paramContext;
        this.appInfos = paramList;
    }

    public List<AppInfomation> getAppInfos() {
        return this.appInfos;
    }

    public Context getContext() {
        return this.context;
    }

    public int getCount() {
        int i = 0;
        if (this.appInfos != null) {
            i = this.appInfos.size();
        }
        return i;
    }

    public Object getItem(int paramInt) {
        return this.appInfos.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int position, View convertView, ViewGroup paramViewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.app_info_item, null);
            viewHolder.appIconImg = ((ImageView) convertView.findViewById(R.id.appIconImg));
            viewHolder.appNameText = ((TextView) convertView.findViewById(R.id.appNameText));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfomation appInfomation = appInfos.get(position);
        viewHolder.appIconImg.setImageDrawable(appInfomation.getDrawable());
        viewHolder.appNameText.setText(appInfomation.getAppName());
        return convertView;
    }

    public void setAppInfos(List<AppInfomation> paramList) {
        this.appInfos = paramList;
    }

    public void setContext(Context paramContext) {
        this.context = paramContext;
    }

    public void setList(List<AppInfomation> paramList) {
        this.appInfos = paramList;
    }

    private class ViewHolder {
        ImageView appIconImg;
        TextView appNameText;
    }
}
