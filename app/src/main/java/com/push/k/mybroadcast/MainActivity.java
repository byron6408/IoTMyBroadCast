package com.push.k.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //WebService Result
    public static String rslt="";                 //test
    public static String registered_rslt="";        //註冊狀況
    public static String register_rslt="";          //註冊
    public static String abletoregister_rslt="";     //註冊個數

    SQLiteDatabase db;
    //資料庫名
    public String db_name = "PUSHDB0";
    //表名
    public String table_name1 = "productkey";
    public String table_name2 = "userInfo";
    //輔助類名
    //MyDBHelper helper = new MyDBHelper(MainActivity.this, db_name);

    public static String Type = "";

//    private BingoReceiver receiver;
    private String num;
    private String Gender ="Male";
    private Spinner spinner;
    private Context mContext;

    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    ArrayAdapter<String> genderAtapder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        APIs a = new APIs();

        mContext = this.getApplicationContext();

        //Get User Infos
        a.c = this;

        String name = a.getName(WebSelectPage.selectedDB);

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

        String DeviceID = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

            //以輔助類獲得資料庫對象


        boolean hasInfo = checkUserInfo();

        //do nothing and try google login
        //changeToGoogleLoginPage();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                } else {
                }
            }
        };


        if (!Type.equalsIgnoreCase("change")) {
            // Registering BroadcastReceiver
            registerReceiver();
        }

        //CheckRegistered
        if(hasInfo) {
            objectId = a.getName(WebSelectPage.selectedDB);

            checkRegistered(objectId);
        }

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        //GCM

    }

    //GCM

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    //GCM

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            show();

        }
        return super.onKeyDown(keyCode, event);
    }
    public void show() {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.LeaveDiagBody)
                .setPositiveButton(R.string.LeaveDiagConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.LeaveDiagCancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

//    class BingoReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
//                num = (json.getString("alert"));
//                //TextView txt = (TextView)findViewById(R.id.showtext);
//                //txt.setText("收到訊息:"+num);
//
//
//            } catch (Exception e) {
//
//            }
//        }
//    }
    //--------------------------------Register-------------------------------------

    public boolean checkAbleToRegister() {
        boolean exist = false;
        abletoregister_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.FuntionType = "chechAbleToRegister";
            c.join();
            c.start();
            while (abletoregister_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (abletoregister_rslt.equalsIgnoreCase("true") || abletoregister_rslt.equalsIgnoreCase("false")) {
                if(abletoregister_rslt.equalsIgnoreCase("true"))
                {
                    //register GCM
                    //GCM

                    mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            SharedPreferences sharedPreferences =
                                    PreferenceManager.getDefaultSharedPreferences(context);
                            boolean sentToken = sharedPreferences
                                    .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                            if (sentToken) {
                                //
                            } else {
                                //
                            }
                        }
                    };


                }
                else
                    changeToMaxRegisterNumPage();

            } else {
                showDialog_1(abletoregister_rslt);
                changeToConnectErrorPage();
            }


        } catch (InterruptedException e) {
//            showDialog_1(e.toString());
            changeToConnectErrorPage();
        }


        return exist;
    }

    //取得記憶資料
    public boolean checkUserInfo(){
        boolean rslt=false;
        try {

            //以輔助類獲得資料庫對象
            String []pruductkey = myKey();
            MyDBHelper helper = new MyDBHelper(MainActivity.this, db_name);
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select ProductKey from userInfo where Lower(userInfo.ProductKey)='" + pruductkey[0].toLowerCase() + "'", null);
            //用陣列存資料
            int rows_num = cursor.getCount();//取得資料表列數
            if(rows_num != 0) {
                cursor.moveToFirst();   //將指標移至第一筆資料
                for(int i=0; i<rows_num; i++) {
                    String strCr = cursor.getString(0);
                    rslt = true;
                    cursor.moveToNext();//將指標移至下一筆資料
                }
            }

            cursor.close(); //關閉Cursor
            db.close();
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉

        }catch (Exception ex)
        {
            String s = "";
        }
        return rslt;
    }

    //取得記憶資料
    public String[] myKey(){
        String []rslt=new String[1];
        try {
            MyDBHelper helper = new MyDBHelper(MainActivity.this, db_name);
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select Key from productkey where isUsing='"+true+"'", null);
            //用陣列存資料
            int rows_num = cursor.getCount();//取得資料表列數
            String[] sNote = new String[0];
            if(rows_num != 0) {
                sNote = new String[cursor.getCount()];
                cursor.moveToFirst();   //將指標移至第一筆資料
                for(int i=0; i<rows_num; i++) {
                    String strCr = cursor.getString(0);
                    sNote[i]=strCr;

                    cursor.moveToNext();//將指標移至下一筆資料
                }
                rslt = sNote;
            }

            cursor.close(); //關閉Cursor
            db.close();
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉

        }catch (Exception ex)
        {
            rslt[0] = ex.getMessage();
        }
        return rslt;
    }

    public boolean checkRegistered(String objectId) {
        boolean exist = false;
        registered_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.objectId = objectId;
            c.FuntionType = "checkExist_new";
            c.join();
            c.start();
            while (registered_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (registered_rslt.equalsIgnoreCase("true") || registered_rslt.equalsIgnoreCase("false")) {
                if(registered_rslt.equalsIgnoreCase("true"))
                {
                    changeToManagementWeb();
                }
                else {
                    //確認廠商註冊個數
                    //checkAbleToRegister();
                }

            } else {
                showDialog_1(registered_rslt);
                changeToConnectErrorPage();
            }
        } catch (InterruptedException e) {
//            showDialog_1(e.toString());
            changeToConnectErrorPage();
        }


        return exist;
    }

    //--------------------------------Register-------------------------------------


    public void testButtonClick(View view) {

        String token = RegistrationIntentService.thistoken;

        TextView tv_name = (TextView)findViewById(R.id.register_name);
        TextView tv_email = (TextView)findViewById(R.id.register_email);

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

                String DeviceID = Secure.getString(this.getContentResolver(),
                        Secure.ANDROID_ID);

                EditText ed1=(EditText)findViewById(R.id.register_name);
                register_rslt="START";
                CallWebService c=new CallWebService();
                c.FuntionType = "Login";
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
                while(register_rslt=="START") {
                    try {
                        Thread.sleep(100);
                    }catch(Exception ex) {
                    }
                }
                if (register_rslt.equalsIgnoreCase("true") || register_rslt.equalsIgnoreCase("false")) {
                    if(register_rslt.equalsIgnoreCase("true"))
                    {
                        showDialog_2(getString(R.string.Meeage_RegisterComplete));

                        if(WebSelectPage.DBType.equals(("2"))) {
                            //Insert userInfo Data
                            setMyInfo(myKey()[0], name, email);
                            finish();
                            System.exit(0);
                        }
                        else
                        {
                            //Insert userInfo Data
                            setMyInfo(myKey()[0], name, email);
                        }
                    }
                    else
                    {
                        showDialog_1("Fail!");
                    }
                } else
                    showDialog_1(register_rslt);
            }catch(Exception ex) {
                showDialog_1(ex.toString());
            }
        }
    }

    public void backButtonClick(View view) {

        CheckProductKey.Type="change";
        changeToCheckProductKey();
    }

    //設定記憶資料
    public boolean setMyInfo(String key,String name,String email) {
        boolean rslt = false;
        try {
            MyDBHelper helper = new MyDBHelper(MainActivity.this, db_name);
            db = helper.getReadableDatabase();
            db.execSQL("DELETE FROM userInfo");
            db.execSQL("INSERT INTO userInfo (ProductKey,NAME,PHONE,EMAIL) VALUES ('" + key + "','"+name+"','','"+email+"')");
            db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            helper.close();
        } catch (Exception ex) {
            rslt = false;
        }
        return rslt;
    }

    public void findback_btn_Click(View view) {
        changeToFindBackAccountPage();
    }

    private  void changeToManagementWeb()
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,ManagementWeb.class);
        //intent.setClass(MainActivity.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    private  void changeToMaxRegisterNumPage()
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,MaxRegisterNumPage.class);
        //intent.setClass(MainActivity.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    private  void changeToFindBackAccountPage()
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,FindBackAccount.class);
        startActivity(intent);
        finish();
    }

    private  void changeToConnectErrorPage()
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,ConnectionErrorPage.class);
        startActivity(intent);
        finish();
    }

    private  void changeToCheckProductKey()
    {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,CheckProductKey.class);
        startActivity(intent);
        finish();
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
                changeToManagementWeb();
            }
        });

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
