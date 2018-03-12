package com.push.k.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class RepairRequestDetails extends AppCompatActivity {

    private Context context;
    public static SimpleAdapter adapter;
    public static ArrayList<HashMap<String, Object>> itemList;

    public static String repairRequestDetails_rslt = "";
    public static String NO = "";
    public static String DevName = "";
    public static String Name = "";
    public static String Phone = "";
    public static String StartTime = "";
    public static String EndTime = "";

    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_request_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
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

        TextView tNO = (TextView)findViewById(R.id.rrNO);
        tNO.setText(NO);

        TextView tDevid = (TextView)findViewById(R.id.rrDevName);
        tDevid.setText(DevName);

        TextView tName = (TextView)findViewById(R.id.rrRequester);
        tName.setText(Name);

        TextView tPhone = (TextView)findViewById(R.id.rrPhone);
        tPhone.setText(Phone);

        TextView tStartTime = (TextView)findViewById(R.id.rrStartTime);
        tStartTime.setText(StartTime.replace("Start Time:",""));

        TextView tEndTime = (TextView)findViewById(R.id.rrEndTime);
        tEndTime.setText(EndTime.replace("End Time:",""));
    }

    public void rrDeleteButtonClick(View view) {
        try {
            repairRequestDetails_rslt = "START";
            CallWebService c = new CallWebService();
            c.FuntionType = "delRepairRequest";
            c.RepairRequestNO = NO;
            c.join();
            c.start();
            while (repairRequestDetails_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (repairRequestDetails_rslt.equalsIgnoreCase("true") || repairRequestDetails_rslt.equalsIgnoreCase("false")) {
                if (repairRequestDetails_rslt.equalsIgnoreCase("true")) {
                    showDialog_1("Delete Complete!");
                    changeIntent(repair_request.class);
                } else {
                    showDialog_1("Delete Fail!");
                }
            } else
                showDialog_1(repairRequestDetails_rslt);
        }catch (Exception ex)
        {changeIntent(ConnectionErrorPage.class);}
    }

    private void showDialog_1(String s)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server Return");
        builder.setMessage(s);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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


                Intent i = new Intent(RepairRequestDetails.this, CustomDialog.class);
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
                Intent intent = new Intent();
                intent.setClass(RepairRequestDetails.this, repair_request.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent();
                intent.setClass(RepairRequestDetails.this, ManagementWeb.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(RepairRequestDetails.this, _Intent);
            startActivity(intent);
            finish();
        }
    }

}
