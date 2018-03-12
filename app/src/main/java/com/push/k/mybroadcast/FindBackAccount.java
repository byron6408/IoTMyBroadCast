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
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.push.k.myWebService.CallWebService;
import com.parse.ParseInstallation;

import org.json.JSONException;
import org.json.JSONObject;

public class FindBackAccount extends AppCompatActivity {

    public static String objectId = ParseInstallation.getCurrentInstallation().getObjectId();
    public static String findbackaccount_rslt="";          //註冊

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_back_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

    }

    public void findback_cancel_btn_Click(View view) {

        changeToRegisterPage();
    }

    public void findback_confirm_Click(View view) {

        //TextView tv_id = (TextView) findViewById(R.id.findback_id);
        TextView tv_email = (TextView) findViewById(R.id.findback_email);

        //String id = tv_id.getText().toString();
        String email = tv_email.getText().toString();

        if (email.equals("")) {
            showDialog_1("Imformation Not Complete!");
        } else {

            try {
                {

                    findbackaccount_rslt = "START";
                    CallWebService c = new CallWebService();
                    c.FuntionType = "findbackaccount";
                    c.objectId = objectId;
                    //c.ID = id;
                    c.Email = email;

                    c.join();
                    c.start();
                    while (findbackaccount_rslt == "START") {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                        }
                    }
                    if (findbackaccount_rslt.equalsIgnoreCase("已重新連結帳號")) {
                        showDialog_1("已重新連結帳號!");
                        changeToListPage();
                    } else {
                        showDialog_1(findbackaccount_rslt);
                    }
                }
            } catch (Exception ex) {
                showDialog_1(ex.getMessage());
            }

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            changeToFirstPage();
        }
        return super.onKeyDown(keyCode, event);
    }

    private  void changeToRegisterPage()
    {
        Intent intent = new Intent();
        intent.setClass(FindBackAccount.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private  void changeToFirstPage()
    {
        Intent intent = new Intent();
        intent.setClass(FindBackAccount.this,FirstPage.class);
        startActivity(intent);
        finish();
    }

    private  void changeToListPage()
    {
        Intent intent = new Intent();
        intent.setClass(FindBackAccount.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    private void showDialog_1(String s)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("伺服器回傳");
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


                Intent i = new Intent(FindBackAccount.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
            }
        }
    }

}
