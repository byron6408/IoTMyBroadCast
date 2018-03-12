package com.push.k.mybroadcast;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity3 extends AppCompatActivity {

    private Context context;
    public static SimpleAdapter adapter;
    public static ArrayList<HashMap<String, Object>> itemList;

    public static String notificationList_rslt = "";
    public static String channelName_rslt = "";
    public static String[] notificationlist;

    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);


        //Get User Infos
        APIs a = new APIs();
        a.c = this;
        String phone = a.getPhone();

        context = this;

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        //宣告Timer
        Timer timer01 =new Timer();
        //設定Timer(task為執行內容，1000代表1秒後開始,間格1秒執行一次)
        //timer01.schedule(task, 1000,1000);

        //Ge User Infos
        String objectId = phone;
        getNoticeficationList(objectId);

        // Step 5 :註冊 OnItemClickListener
        ListView lv = (ListView)findViewById(R.id.list);
        lv.setOnItemClickListener(new MyOnItemClickListener());
    }

    private BingoReceiver receiver;
    class BingoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //Get User Infos
                APIs a = new APIs();
                a.c = context;
                String phone = a.getPhone();

                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                String str = json.getString("alert");

                //彈出通知視窗
                CustomDialog.content = str;

                Intent i = new Intent(MainActivity3.this, CustomDialog.class);
                startActivity(i);

                String objectId = phone;
                getNoticeficationList(objectId);
            }
            catch (JSONException je)
            {

            }
            catch (Exception e) {
            }
        }
    }

    //TimerTask無法直接改變元件因此要透過Handler來當橋樑
    private Handler handler = new Handler(){
        public  void  handleMessage(Message msg) {
            super.handleMessage(msg);

            //Get User Infos
            APIs a = new APIs();
            a.c = context;
            String phone = a.getPhone();

            //Ge User Infos
            String objectId = phone;
            getNoticeficationList(objectId);

        }
    };

    private TimerTask task = new TimerTask(){

        @Override
        public void run() {
            Message message = new Message();

            //傳送訊息1
            message.what =1;
            handler.sendMessage(message);

        }

    };

    public void getNoticeficationList(String objectId) {
        notificationList_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.objectId = objectId;
            c.FuntionType = "getNotificationList";
            c.join();
            c.start();
            while (notificationList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (notificationList_rslt.equalsIgnoreCase("success")) {
                if(notificationlist !=null) {
                    if(notificationlist.length!=0) {
                        itemList = new ArrayList<>();
                        for (int i = 0; i < notificationlist.length; i++) {
                            String NO = notificationlist[i].split("&c&")[0].replace("[@datas]:", "");
                            String MsgType = notificationlist[i].split("&c&")[1];
                            String Channel = notificationlist[i].split("&c&")[2];
                            String Title = notificationlist[i].split("&c&")[3];
                            String Body = notificationlist[i].split("&c&")[4];
                            String Target = notificationlist[i].split("&c&")[5];
                            String GCMType = notificationlist[i].split("&c&")[6];
                            String DateTime = notificationlist[i].split("&c&")[7];
                            String Remark = notificationlist[i].split("&c&")[8];
                            String Source = notificationlist[i].split("&c&")[9];


                            // Step 1 :定義商品資料
                            HashMap<String, Object> msg = new HashMap<>();

                            if(Title.equalsIgnoreCase("Connection TimeOut"))
                            {
                                msg.put("NO", NO);
                                msg.put("MsgType", MsgType);
                                msg.put("Channel", Channel);
                                msg.put("Title", Title);
                                msg.put("Msg_Title", Title);
                                msg.put("Body", Body);
                                msg.put("Target", Target);
                                msg.put("GCMType", GCMType);
                                msg.put("DateTime", DateTime);
                                msg.put("Msg_DateTime", "DateTime:" + DateTime);
                                msg.put("Remark", Remark);
                                msg.put("Source", Source);
                                msg.put("Msg_Source", "Device:" + Source.split(",")[0]);
                            }
                            else {

                                String ChannelName = notificationlist[i].split("&c&")[10];

                                String text = "Message:" + Body + " \nDatetime:" + DateTime;

                                msg.put("NO", NO);
                                msg.put("MsgType", MsgType);
                                msg.put("Channel", Channel);
                                msg.put("Title", Title);
                                msg.put("Msg_Title", Title);
                                msg.put("Body", Body);
                                msg.put("Target", Target);
                                msg.put("GCMType", GCMType);
                                msg.put("DateTime", DateTime);
                                msg.put("Msg_DateTime", "DateTime:" + DateTime);
                                msg.put("Remark", Remark);
                                msg.put("Source", Source);
                                msg.put("Msg_Source", "Device:" + ChannelName);
                                msg.put("ChannelName", ChannelName);
                                if (Source.split(",")[2].equalsIgnoreCase("ai"))
                                    msg.put("icon", R.drawable.ai);
                                else if (Source.split(",")[2].equalsIgnoreCase("di"))
                                    msg.put("icon", R.drawable.di);
                            }

                            // Step 2 :放入到List集合容器中
                             itemList.add(msg);
                        }

                        // Step 3 :建立SimpleAdapter適配器
                        adapter = new SimpleAdapter(
                                this,         // 設定接口環境
                                itemList,     // 設定接口容器資料
                                R.layout.content_main3, // 資料顯示 UI XML
                                new String[]{"Msg_Source", "Msg_Title", "Msg_DateTime","icon"},   // 商品資料標題
                                new int[]{R.id.DeviceId, R.id.Title, R.id.DateTime,R.id.icon} // 資料 UI
                        );

                        // Step 4 :設定適配器
                        ListView lv = (ListView)findViewById(R.id.list);
                        lv.setAdapter(adapter);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            show();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void show() {
        new AlertDialog.Builder(MainActivity3.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.LeaveDiagBody)
                .setPositiveButton(R.string.LeaveDiagConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity3.this.finish();
                    }
                })
                .setNegativeButton(R.string.LeaveDiagCancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    // 列表項目點選之事件處理
    private class MyOnItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // 取得被點選之商品資料


                HashMap<String, Object> item =
                        (HashMap<String, Object>) parent.getItemAtPosition(position);
                // 取出商品名稱, 價格
            if(!item.get("Title").toString().equalsIgnoreCase("Connection TimeOut")) {
                MessageDetails.NO = item.get("NO").toString();
                MessageDetails.MsgType = item.get("MsgType").toString();
                MessageDetails.Channel = item.get("Channel").toString();
                MessageDetails.Title = item.get("Title").toString();
                MessageDetails.Body = item.get("Body").toString();
                MessageDetails.Target = item.get("Target").toString();
                MessageDetails.GCMType = item.get("GCMType").toString();
                MessageDetails.DateTime = item.get("DateTime").toString();
                MessageDetails.Remark = item.get("Remark").toString();
                MessageDetails.Source = item.get("Source").toString();
                MessageDetails.ChannelName = item.get("ChannelName").toString();
                changeIntent(MessageDetails.class);
            }
            // Toast 顯示
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        menu.add(0, 0, Menu.NONE, "BrocastList");
//        menu.add(0, 0, Menu.NONE, "Repair");
//        menu.add(0, 2, Menu.NONE, "Settings");
////        menu.add(0, 1, Menu.NONE, "設備清單");
////        menu.add(0, 2, Menu.NONE, "即時資訊");
//        return  true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean success = false;
        if(item.getGroupId() == 0)
        {
            if(item.getTitle().toString().equals("BrocastList"))
            {
                changeIntent(MainActivity3.class);
            }
            else if(item.getTitle().toString().equals("Repair"))
            {
                changeIntent(repair_request.class);
            }
            else if(item.getTitle().toString().equals("Settings"))
            {
                changeIntent(SettingPage.class);
            }
        }
        return  super.onOptionsItemSelected(item);
    }
    public void broadcastlistClick(View view) { changeIntent(MainActivity3.class); }

    public void webpageClick(View view) { changeIntent(repair_request.class); }

    public void btnInfoClick(View view)    {changeIntent(SettingPage.class);    }

    private void changeIntent(Class _Intent)
    {

        if(_Intent.equals(repair_request.class) || _Intent.equals(ManagementWeb.class))
        {
            if(CheckProductKey.APPMode.equals("V"))
            {
                if(MainActivity3.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity3.this, repair_request.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                if(MainActivity3.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity3.this, ManagementWeb.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            if(MainActivity3.class.equals(_Intent))
                startActivity(getIntent());
            else {
                Intent intent = new Intent();
                intent.setClass(MainActivity3.this, _Intent);
                startActivity(intent);
                finish();
            }
        }
    }
}

