package com.push.k.mybroadcast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountManagement extends AppCompatActivity {

    public static String logon_rslt="";          //註冊
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Step 5 :註冊 OnItemClickListener
        Button login = (Button)findViewById(R.id.AccountManagement_LoginButton);
        login.setOnClickListener(new MyOnItemClickListener() {
            @Override
            public void onClick(View v) {
                changeAccountBtn_Click();
            }
        });
    }
    private abstract class MyOnItemClickListener implements View.OnClickListener {

    }

    public void changeAccountBtn_Click()
    {
        APIs api = new APIs();
        api.c = this;
        //Get Old account/password
        //String oldAccount = api.getName(WebSelectPage.selectedDB);
        //String oldPassword = api.getName(WebSelectPage.selectedDB);

        TextView tv_name = (TextView)findViewById(R.id.AccountManagement_Account);
        TextView tv_email = (TextView)findViewById(R.id.AccountManagement_Password);

        String account = tv_name.getText().toString();
        String password = tv_email.getText().toString();
        api.updateMyInfo(WebSelectPage.selectedDB,account,password);

        updateToken();

        changeIntent(SettingPage.class);

    }

    private void changeIntent(Class _Intent)
    {
        if(_Intent.equals(repair_request.class) || _Intent.equals(ManagementWeb.class))
        {
            if(CheckProductKey.APPMode.equals("V"))
            {
                if(SettingPage.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(AccountManagement.this, repair_request.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                if(SettingPage.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(AccountManagement.this, ManagementWeb.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            if(AccountManagement.class.equals(_Intent))
                startActivity(getIntent());
            else {
                Intent intent = new Intent();
                intent.setClass(AccountManagement.this, _Intent);
                startActivity(intent);
                finish();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
          changeIntent(SettingPage.class);
        }
        return super.onKeyDown(keyCode, event);
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

    public void webpageClick(View view) { changeIntent(repair_request.class); }

    public void btnInfoClick(View view)    {changeIntent(SettingPage.class);    }

    public void updateToken()
    {
        String token = RegistrationIntentService.thistoken;

        TextView tv_name = (TextView)findViewById(R.id.AccountManagement_Account);
        TextView tv_email = (TextView)findViewById(R.id.AccountManagement_Password);

        String name = tv_name.getText().toString();
        String email = tv_email.getText().toString();

        if( name.equals("") || email.equals(""))
        {
            showDialog_1(getString(R.string.RegisterPage_DataNotComplete));
        }
        else
        {

            try
            {
                //Get User Infos
                APIs a = new APIs();
                a.c = this;
                String _phone = a.getPhone();
                //Get User Infos
                String objectId = name;
                String GCMSenderId = "";
                String appIdentifier = "com.push.k.mybroadcast";
                String appName = "mybroadcast";
                String appVersion = "1.0";
                String badge = "";
                String channels = "[\"brocast\"]";
                String deviceToken = "";
                String deviceTokenLastModified = "";
                String DeviceType = "android";
                String installationId = name;
                String localeIdentifier = "";
                String parseVersion = "1.9.2";
                String pushType = "gcm";
                String timeZone = "Asia/Taipei";
                String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String updateAt = createAt;
                String ACL = "";

                String DeviceID = Settings.Secure.getString(this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                EditText ed1=(EditText)findViewById(R.id.AccountManagement_Account);
                logon_rslt="START";
                CallWebService c=new CallWebService();
                c.FuntionType = "Login2";
                c.GCMToken = token;
                c.objectId = objectId;
                c.GCMSenderId = GCMSenderId;
                c.appIdentifier = appIdentifier;
                c.appName = appName;
                c.appVersion = appVersion;
                c.badge = badge;
                c.channels = channels;
                c.deviceToken = deviceToken;
                c.deviceTokenLastModified = deviceTokenLastModified;
                c.DeviceType = DeviceType;
                c.installationId = installationId;
                c.localeIdentifier = localeIdentifier;
                c.parseVersion = parseVersion;
                c.pushType = pushType;
                c.timeZone = timeZone;
                c.createAt = createAt;
                c.updateAt = updateAt;
                c.ACL = ACL;
                c.DeviceID = DeviceID;
                c.Name = name;
                c.Email=email;
                c.PlusId="";

                c.join(); c.start();
                while(logon_rslt=="START") {
                    try {
                        Thread.sleep(100);
                    }catch(Exception ex) {
                    }
                }
                if (logon_rslt.equalsIgnoreCase("true") || logon_rslt.equalsIgnoreCase("false")) {
                    if(logon_rslt.equalsIgnoreCase("true"))
                    {
                        showDialog_2(getString(R.string.Meeage_RegisterComplete));
                    }
                    else
                    {
                        showDialog_1("Fail!");
                    }
                } else
                    showDialog_1(logon_rslt);
            }catch(Exception ex) {
                showDialog_1(ex.toString());
            }
        }
    }

    private void showDialog_1(String s)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.MeeageTitle));
        builder.setMessage(s);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showDialog_2(String s)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.MeeageTitle));
        builder.setMessage(s);

        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
