package com.push.k.mybroadcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InstantDeviceStatus extends AppCompatActivity {

    public static String type;
    public static String deviceId;
    public static String CH_TYPE;
    public static String CH_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_device_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("http://www.iotpush.com.tw/DeviceManagement/m.AllDeviceInstantStatus.aspx");
    }

    private class MyWebViewClient extends WebViewClient
    { @Override public boolean shouldOverrideUrlLoading(WebView view, String url) { return super.shouldOverrideUrlLoading(view, url); } }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.add(0, 0, Menu.NONE, "推播清單");
        menu.add(0, 1, Menu.NONE, "設備清單");
        menu.add(0, 2, Menu.NONE, "即時資訊");
        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean success = false;
        if(item.getGroupId() == 0)
        {
            if(item.getTitle().toString().equals("推播清單"))
            {
                changeToMessageList();
            }
            else if(item.getTitle().toString().equals("設備清單"))
            {
                changeToDeviceList();
            }
            else if(item.getTitle().toString().equals("即時資訊"))
            {

            }

        }
        return  super.onOptionsItemSelected(item);
    }

    private  void changeToMessageList()
    {
        Intent intent = new Intent();
        intent.setClass(InstantDeviceStatus.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    private  void changeToDeviceList()
    {
        Intent intent = new Intent();
        intent.setClass(InstantDeviceStatus.this, DeviceList.class);
        startActivity(intent);
        finish();
    }

    private  void changeToInstantStatusPage()
    {
        Intent intent = new Intent();
        intent.setClass(InstantDeviceStatus.this,InstantDeviceStatus.class);
        startActivity(intent);
        finish();
    }

}
