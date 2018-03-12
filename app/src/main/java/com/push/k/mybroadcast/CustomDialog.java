package com.push.k.mybroadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomDialog extends Activity {

    private Button btn_open;
    private Button btn_delay_open;

    public static String content="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog);
        Button btn_confirm;
        Button btn_cancel;
        TextView text_content;
        TextView text_title;
        TextView text_close;

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);



        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        text_title  = (TextView)findViewById(R.id.text_title);
        text_content = (TextView)findViewById(R.id.text_content);

        text_content.setText(content);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                changeToMessageList();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                finish();
            }
        });

    }

    private  void changeToMessageList()
    {
        ManagementWeb.isAlarm="1";

        Intent intent = new Intent();
        intent.setClass(CustomDialog.this, ManagementWeb.class);
        startActivity(intent);

        //Get User Infos
        APIs a = new APIs();
        a.c = this;
        String phone = a.getPhone();

        String objectId = phone;
        getNoticeficationList(objectId);

        finish();
    }

    private BingoReceiver receiver;
    class BingoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                String str = json.getString("alert");

                //彈出通知視窗
                CustomDialog.content = str;


                Intent i = new Intent(CustomDialog.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
            }
        }
    }

    public void getNoticeficationList(String objectId) {
        MainActivity3.notificationList_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.objectId = objectId;
            c.FuntionType = "getNotificationList";
            c.join();
            c.start();
            while (MainActivity3.notificationList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (MainActivity3.notificationList_rslt.equalsIgnoreCase("success")) {
                if(MainActivity3.notificationlist !=null) {
                    if(MainActivity3.notificationlist.length!=0) {
                        MainActivity3.itemList = new ArrayList<>();
                        for (int i = 0; i < MainActivity3.notificationlist.length; i++) {
                            String NO = MainActivity3.notificationlist[i].split("&c&")[0].replace("[@datas]:", "");
                            String MsgType = MainActivity3.notificationlist[i].split("&c&")[1];
                            String Channel = MainActivity3.notificationlist[i].split("&c&")[2];
                            String Title = MainActivity3.notificationlist[i].split("&c&")[3];
                            String Body = MainActivity3.notificationlist[i].split("&c&")[4];
                            String Target = MainActivity3.notificationlist[i].split("&c&")[5];
                            String GCMType = MainActivity3.notificationlist[i].split("&c&")[6];
                            String DateTime = MainActivity3.notificationlist[i].split("&c&")[7];
                            String Remark = MainActivity3.notificationlist[i].split("&c&")[8];
                            String Source = MainActivity3.notificationlist[i].split("&c&")[9];
                            String ChannelName = MainActivity3.notificationlist[i].split("&c&")[10];

                            String text = "內容:" + Body + " \n時間:" + DateTime;

                            // Step 1 :定義商品資料
                            HashMap<String, Object> msg = new HashMap<>();

                            msg.put("NO", NO);
                            msg.put("MsgType", MsgType);
                            msg.put("Channel", Channel);
                            msg.put("Title", Title);
                            msg.put("Msg_Title", Title);
                            msg.put("Body", Body);
                            msg.put("Target", Target);
                            msg.put("GCMType", GCMType);
                            msg.put("DateTime", DateTime);
                            msg.put("Msg_DateTime", "時間:"+DateTime);
                            msg.put("Remark", Remark);
                            msg.put("Source", Source);
                            msg.put("Msg_Source", "設備:"+ChannelName);
                            msg.put("ChannelName", ChannelName);
                            if(Source.split(",")[1].equalsIgnoreCase("ai"))
                                msg.put("icon",R.drawable.ai);
                            else if(Source.split(",")[1].equalsIgnoreCase("di"))
                                msg.put("icon",R.drawable.di);

                            // Step 2 :放入到List集合容器中
                            MainActivity3.itemList.add(msg);
                        }

                        // Step 3 :建立SimpleAdapter適配器
                        MainActivity3.adapter = new SimpleAdapter(
                                this,         // 設定接口環境
                                MainActivity3.itemList,     // 設定接口容器資料
                                R.layout.content_main3, // 資料顯示 UI XML
                                new String[]{"Msg_Source", "Msg_Title", "Msg_DateTime","icon"},   // 商品資料標題
                                new int[]{R.id.DeviceId, R.id.Title, R.id.DateTime,R.id.icon} // 資料 UI
                        );

                        // Step 4 :設定適配器
                        ListView lv = (ListView)findViewById(R.id.list);
                        lv.setAdapter(MainActivity3.adapter);
                        //setListAdapter(adapter);
                    }
                }
            }
            else
            {
                String s;
            }//showDialog_1(notificationList_rslt);
        } catch (Exception e) {
            String s;
            //showDialog_1(e.toString());
        }
    }
}
