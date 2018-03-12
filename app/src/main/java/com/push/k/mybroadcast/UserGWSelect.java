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

import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserGWSelect extends AppCompatActivity {

    private Context context;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> itemList;


    public static String selectedDB="";
    public static String selectedDBConnStr="";
    public static String DBType="";
    public static String WebList_rslt = "";
    public static String[] weblist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gwselect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        context = this;

        //Get User Infos
        APIs a = new APIs();
        a.c = this;
        String phone = a.getPhone();

        // Step 5 :註冊 OnItemClickListener
        ListView lv = (ListView)findViewById(R.id.weblist2);
        lv.setOnItemClickListener(new MyOnItemClickListener());
        String objectId = phone;
        getWebList(objectId);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            changeIntent(MainActivity3.class);
        }
        return super.onKeyDown(keyCode, event);
    }
    public void show() {
        new AlertDialog.Builder(UserGWSelect.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.LeaveDiagBody)
                .setPositiveButton(R.string.LeaveDiagConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserGWSelect.this.finish();
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
                intent.setClass(UserGWSelect.this, repair_request.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent();
                intent.setClass(UserGWSelect.this, ManagementWeb.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(UserGWSelect.this, _Intent);
            startActivity(intent);
            finish();
        }
    }

    private void getWebList(String Objectid) {
        WebList_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.FuntionType = "getUserGatewayList";
            c.objectId=Objectid;
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

                            String DBFULLNAME = weblist[i].split("&c&")[0].replace("[@datas]:", "");
                            String  DBNAME= weblist[i].split("&c&")[1];

                            // Step 1 :定義商品資料
                            HashMap<String, Object> msg = new HashMap<>(); // 漢堡

                            msg.put("DBFULLNAME", DBFULLNAME);
                            msg.put("DBNAME", DBNAME);

                            // Step 2 :放入到List集合容器中

                            itemList.add(msg);


                        }
                        // Step 3 :建立SimpleAdapter適配器
                        adapter = new SimpleAdapter(
                                this,         // 設定接口環境
                                itemList,     // 設定接口容器資料
                                R.layout.content_user_gwselect, // 資料顯示 UI XML
                                new String[]{"DBNAME"},   // 商品資料標題
                                new int[]{R.id.DBName2} // 資料 UI
                        );

                        // Step 4 :設定適配器
                        ListView lv = (ListView)findViewById(R.id.weblist2);
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

            selectedDB = item.get("DBNAME").toString();
            WebSelectPage.selectedDBConnStr=  "Data Source=192.168.1.88;Initial Catalog=DAIIO"+selectedDB+";Persist Security Info=True;User ID=sa;Password=p@ssWord";

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


                Intent i = new Intent(UserGWSelect.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
            }
        }
    }

}
