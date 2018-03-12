package com.push.k.mybroadcast;

/**
 * Created by K on 2015/11/19.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PushMessageAdapter extends BaseAdapter {
    // 定義 LayoutInflater
    private LayoutInflater myInflater;
    // 定義 Adapter 內藴藏的資料容器
    private ArrayList<PushMessages> list;

    public PushMessageAdapter(Context context, ArrayList<PushMessages> list){
        //預先取得 LayoutInflater 物件實體
        myInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() { // 公定寫法(取得List資料筆數)
        return list.size();
    }

    @Override
    public Object getItem(int position) { // 公定寫法(取得該筆資料)
        return list.get(position);
    }

    @Override
    public long getItemId(int position) { // 公定寫法(取得該筆資料的position)
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            // 1:將 R.layout.row 實例化
            convertView = myInflater.inflate(R.layout.activity_main3, null);

            // 2:建立 UI 標籤結構並存放到 holder
            holder = new ViewHolder();
            //holder.deviceId = (TextView)convertView.findViewById(R.id.DeviceId);
            //holder.msg = (TextView)convertView.findViewById(R.id.Msg);
            //holder.time = (TextView)convertView.findViewById(R.id.Time);
            //holder.icon = (ImageView)convertView.findViewById(R.id.icon);

            // 3:注入 UI 標籤結構 --> convertView
            convertView.setTag(holder);

        } else {
            // 取得  UI 標籤結構
            holder = (ViewHolder)convertView.getTag();
        }

        // 4:取得Fastfood物件資料
        PushMessages pushMessages = list.get(position);


        // 5:設定顯示資料
        holder.deviceId.setText(pushMessages.getDeviceId());
        holder.msg.setText(pushMessages.getMsg());
        holder.time.setText(pushMessages.gettime());
        //if(pushMessages.getIcon().split(",")[1].toLowerCase().equalsIgnoreCase("ai"))
        holder.icon.setImageResource(pushMessages.getIcon());
        //else if(pushMessages.getIcon().split(",")[1].toLowerCase().equalsIgnoreCase("di"))
        //holder.icon.setImageResource(drawable.di);

        return convertView;
    }

    // UI 標籤結構
    static class ViewHolder {
        TextView deviceId;
        TextView msg;
        TextView time;
        ImageView icon;
    }
}
