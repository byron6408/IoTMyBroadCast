package com.push.k.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class repair_request extends AppCompatActivity {

    private Context context;
    public static SimpleAdapter adapter;
    public static ArrayList<HashMap<String, Object>> itemList;

    public static String repairList_rslt = "";
    public static String[] repairlist;

    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        //宣告Timer
        Timer timer01 =new Timer();
        //設定Timer(task為執行內容，1000代表1秒後開始,間格1秒執行一次)
        //timer01.schedule(task, 1000,1000);

        //Get User Infos
        APIs a = new APIs();
        a.c = this;
        String phone = a.getPhone();


        //Ge User Infos
        String objectId = phone;
        getRepairList(objectId);

        // Step 5 :註冊 OnItemClickListener
        ListView lv = (ListView)findViewById(R.id.repairlist);
        lv.setOnItemClickListener(new MyOnItemClickListener());

    }

    public void getRepairList(String objectId) {
        repairList_rslt = "START";
        try {

            CallWebService c = new CallWebService();
            c.objectId = objectId;
            c.FuntionType = "getRepairList";
            c.join();
            c.start();
            while (repairList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (repairList_rslt.equalsIgnoreCase("success")) {
                if(repairlist !=null) {
                    if(repairlist.length!=0) {
                        itemList = new ArrayList<>();
                        for (int i = 0; i < repairlist.length; i++) {
                            String NO = repairlist[i].split("&c&")[0].replace("[@datas]:", "");
                            String DEV_ID = repairlist[i].split("&c&")[1];
                            String DEV_NAME = repairlist[i].split("&c&")[2];
                            String NAME = repairlist[i].split("&c&")[3];
                            String PHONE = repairlist[i].split("&c&")[4];
                            String STARTTIME = repairlist[i].split("&c&")[5];
                            String ENDTIME = repairlist[i].split("&c&")[6];

                            // Step 1 :定義商品資料
                            HashMap<String, Object> msg = new HashMap<>();

                            msg.put("NO", NO);
                            msg.put("DEV_ID",  DEV_ID);
                            msg.put("DEV_NAME","Device:"+ DEV_NAME);
                            msg.put("NAME", NAME);
                            msg.put("PHONE", PHONE);
                            msg.put("STARTTIME", "Start Time:"+STARTTIME);
                            msg.put("ENDTIME", "End Time:"+ENDTIME);
                            msg.put("Empty", "");


                            // Step 2 :放入到List集合容器中
                            itemList.add(msg);
                        }

                        // Step 3 :建立SimpleAdapter適配器
                        adapter = new SimpleAdapter(
                                this,         // 設定接口環境
                                itemList,     // 設定接口容器資料
                                R.layout.content_repair_request, // 資料顯示 UI XML
                                new String[]{"DEV_NAME", "STARTTIME", "ENDTIME"},   // 商品資料標題
                                new int[]{R.id.RepairDeviceId, R.id.RepairTitle, R.id.RepairDateTime} // 資料 UI
                        );

                        // Step 4 :設定適配器
                        ListView lv = (ListView)findViewById(R.id.repairlist);
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

    // 列表項目點選之事件處理
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // 取得被點選之商品資料


            HashMap<String, Object> item =
                    (HashMap<String, Object>) parent.getItemAtPosition(position);
            // 取出商品名稱, 價格
            RepairRequestDetails.NO=item.get("NO").toString();
            RepairRequestDetails.DevName=item.get("DEV_NAME").toString().replace("Device:","");
            RepairRequestDetails.Name=item.get("NAME").toString();
            RepairRequestDetails.Phone=item.get("PHONE").toString();
            RepairRequestDetails.StartTime=item.get("STARTTIME").toString();
            RepairRequestDetails.EndTime=item.get("ENDTIME").toString();

            changeIntent(RepairRequestDetails.class);

            // Toast 顯示
            //Toast.makeText(context, item.get("NO").toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void createRepairRequestClick(View view) {
        changeIntent(CreateRepairRequest.class);
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


                Intent i = new Intent(repair_request.this, CustomDialog.class);
                startActivity(i);
            }
            catch (JSONException je)
            {

            }
            catch (Exception e) {
            }
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
//        return  true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean success = false;
        if(item.getGroupId() == 0)
        {
            if(item.getTitle().toString().equals("BrocastList")){ changeIntent(MainActivity3.class); }
            else if(item.getTitle().toString().equals("Repair")){ changeIntent(repair_request.class); }
            else if(item.getTitle().toString().equals("Settings")){ changeIntent(SettingPage.class); }
        }
        return  super.onOptionsItemSelected(item);
    }

    public void broadcastlistClick(View view)
    {
        changeIntent(MainActivity3.class);
    }

    public void webpageClick(View view) { changeIntent(repair_request.class); }

    public void btnInfoClick(View view)    {changeIntent(SettingPage.class);    }

    private void changeIntent(Class _Intent)
    {
        if(_Intent.equals(repair_request.class) || _Intent.equals(ManagementWeb.class))
        {
            if(CheckProductKey.APPMode.equals("V"))
            {
                if(repair_request.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(repair_request.this, repair_request.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                if(repair_request.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(repair_request.this, ManagementWeb.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            if(repair_request.class.equals(_Intent))
                startActivity(getIntent());
            else {
                Intent intent = new Intent();
                intent.setClass(repair_request.this, _Intent);
                startActivity(intent);
                finish();
            }
        }
    }
}
