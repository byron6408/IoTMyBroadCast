package com.push.k.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.push.k.myWebService.CallWebService;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageDetails extends AppCompatActivity {

    public static String NO ;
    public static String MsgType;
    public static String Channel;
    public static String Title ;
    public static String Body;
    public static String Target;
    public static String GCMType ;
    public static String DateTime ;
    public static String Remark;
    public static String Source;
    public static String ChannelName;
    public static String DetailPageRslt;
    public static String channelName_rslt = "";

    WebView mWebView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        TextView tvremark = (TextView) findViewById(R.id.DeviceId);
        tvremark.setText(Source);


        tvremark.setText(ChannelName);


        TextView tvtitle = (TextView) findViewById(R.id.Title);
        tvtitle.setText(Title);

        TextView tvbody = (TextView) findViewById(R.id.Body);
        tvbody.setText(Body);

        TextView tvdatetime = (TextView) findViewById(R.id.DateTime);
        tvdatetime.setText(DateTime);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();
        myWebView.setWebChromeClient(new WebChromeClient());

        DetailPageRslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.FuntionType = "getInsPage";
            c.DeviceID = Source.split(",")[0];
            c.CH_ID = Source.split(",")[1];
            c.join();
            c.start();
            while (DetailPageRslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (DetailPageRslt.substring(0, 4).equalsIgnoreCase(("HTTP"))) {

            }
        } catch (InterruptedException e) {
        }

        //myWebView.loadUrl("http://www.iotpush.com.tw/DeviceManagement/m.DeviceInstantStatusDetail.aspx?type=ch&deviceId=D001&CH_TYPE=AI_ONOFF&CH_ID=ch1");
        myWebView.loadUrl(DetailPageRslt);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            changeIntent(MainActivity3.class);
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient
    { @Override public boolean shouldOverrideUrlLoading(WebView view, String url) { return super.shouldOverrideUrlLoading(view, url); } }

    private BingoReceiver receiver;
    class BingoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                String str = json.getString("alert");

                //彈出通知視窗
                CustomDialog.content = str;


                Intent i = new Intent(MessageDetails.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
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
                intent.setClass(MessageDetails.this, repair_request.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent();
                intent.setClass(MessageDetails.this, ManagementWeb.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(MessageDetails.this, _Intent);
            startActivity(intent);
            finish();
        }
    }

}
