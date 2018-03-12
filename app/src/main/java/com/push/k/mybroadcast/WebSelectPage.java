package com.push.k.mybroadcast;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WebSelectPage extends AppCompatActivity {

    private Context context;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> itemList;


    public static String selectedDB="";
    public static String DBType="";
    public static String selectedDBConnStr="";
    public static String WebList_rslt = "";
    public static String[] weblist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_select_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        context = this;

        // Step 5 :註冊 OnItemClickListener
        ListView lv = (ListView)findViewById(R.id.weblist);
        lv.setOnItemClickListener(new MyOnItemClickListener());

        getWebList();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            show();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void show() {
        new AlertDialog.Builder(WebSelectPage.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.LeaveDiagBody)
                .setPositiveButton(R.string.LeaveDiagConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WebSelectPage.this.finish();
                    }
                })
                .setNegativeButton(R.string.LeaveDiagCancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

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
                Intent intent = new Intent();
                intent.setClass(WebSelectPage.this, repair_request.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent();
                intent.setClass(WebSelectPage.this, ManagementWeb.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(WebSelectPage.this, _Intent);
            startActivity(intent);
            finish();
        }
    }

    private void getWebList() {
        WebList_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.FuntionType = "getWebList";
            c.join();
            c.start();
            while (WebList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (WebList_rslt.equalsIgnoreCase("success")) {
                if(weblist !=null) {
                    if(weblist.length!=0) {
                        itemList = new ArrayList<>();
                        for (int i = 0; i < weblist.length; i++) {

                            String DBName = weblist[i].split("&c&")[0].replace("[@datas]:", "");
                            String connectionString = weblist[i].split("&c&")[1];

                            // Step 1 :定義商品資料
                            HashMap<String, Object> msg = new HashMap<>(); // 漢堡

                            msg.put("DBName", DBName);
                            msg.put("connectionString", connectionString);

                            // Step 2 :放入到List集合容器中

                            itemList.add(msg);


                        }
                        // Step 3 :建立SimpleAdapter適配器
                        adapter = new SimpleAdapter(
                                this,         // 設定接口環境
                                itemList,     // 設定接口容器資料
                                R.layout.content_web_select_page, // 資料顯示 UI XML
                                new String[]{"DBName","connectionString"},   // 商品資料標題
                                new int[]{R.id.DBName} // 資料 UI
                        );

                        // Step 4 :設定適配器
                        ListView lv = (ListView)findViewById(R.id.weblist);
                        lv.setAdapter(adapter);
                        //setListAdapter(adapter);

                    }
                }
            }
            else
            {}//showDialog_1(notificationList_rslt);
        } catch (InterruptedException e) {
            //showDialog_1(e.toString());
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // 取得被點選之商品資料
            HashMap<String, Object> item =
                    (HashMap<String, Object>) parent.getItemAtPosition(position);
            // 取出商品名稱, 價格

            selectedDB = item.get("DBName").toString();
            selectedDBConnStr=item.get("connectionString").toString();

            CheckProductKey.Type = "change";
            CheckProductKey.UseMode="Admin";
            CheckProductKey.AdminKey=selectedDB;

            changeIntent(CheckProductKey.class);
            // Toast 顯示
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
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


                Intent i = new Intent(WebSelectPage.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
            }
        }
    }

}
