package com.push.k.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.push.k.myWebService.APIs;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingPage extends AppCompatActivity {

    SQLiteDatabase db;
    //資料庫名
    public String db_name = "PUSHDB0";
    //表名
    public String table_name = "productkey";
    //輔助類名
    MyDBHelper helper = new MyDBHelper(SettingPage.this, db_name);

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(WebSelectPage.DBType.equals("1")) {
            ImageButton Ib = (ImageButton)findViewById(R.id.webpage);
            Ib.setVisibility(View.VISIBLE);
        }
        else if(WebSelectPage.DBType.equals("2")) {
            ImageButton Ib = (ImageButton)findViewById(R.id.webpage);
            Ib.setVisibility(View.GONE);
        }

        boolean Oncreate = true;

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        //以輔助類獲得資料庫對象
        db = helper.getReadableDatabase();

        //取得SQL Lite 紀錄
        String []key = myKey();

        if(key[0] == null || key[0].equalsIgnoreCase("Attempt to invoke virtual method 'android.database.Cursor android.database.sqlite.SQLiteDatabase.rawQuery(java.lang.String, java.lang.String[])' on a null object reference"))
        {

        }
        else
        {
            TextView pk = (TextView)findViewById(R.id.productNo);
            pk.setText(key[0].replace("DAIIO",""));
        }

        //取得用戶資訊
        String []s = myInfo(myKey()[0]);
        TextView nameTv =(TextView)findViewById(R.id.userName);
        nameTv.setText(s[0]);
        TextView phonenTv =(TextView)findViewById(R.id.userPhone);
        phonenTv.setText(s[1]);
        TextView emailTv =(TextView)findViewById(R.id.userEmail);
        emailTv.setText(s[2]);

        //取得當前版本
        TextView versionTv =(TextView)findViewById(R.id.nowVersion);
        versionTv.setText("code:" + BuildConfig.VERSION_CODE + " version:" + BuildConfig.VERSION_NAME);

        APIs a = new APIs();
        a.c = this;
        String L = a.getLanguage();

        spinner = (Spinner)findViewById(R.id.spinner2);

        if(L.equals(""))
            L="0";
        spinner.setSelection(Integer.valueOf(L));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    APIs a = new APIs();
                    a.setLanguage(SettingPage.this, String.valueOf(spinner.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            changeIntent(ManagementWeb.class);
        }
        return true;
    }

    //取得記憶資料
    public String[] myInfo(String key){
        String []rslt=new String[3];
        try {
            Cursor cursor = db.rawQuery("select NAME,PHONE,EMAIL from userInfo where Lower(userInfo.ProductKey) ='"+key.toLowerCase()+"'", null);
            //用陣列存資料
            int rows_num = cursor.getCount();//取得資料表列數
            String[] sNote = new String[0];
            if(rows_num != 0) {
                sNote = new String[3];
                cursor.moveToFirst();   //將指標移至第一筆資料
                for(int i=0; i<rows_num; i++) {
                    sNote[0]= cursor.getString(0);
                    sNote[1]= cursor.getString(1);
                    sNote[2]= cursor.getString(2);
                    cursor.moveToNext();//將指標移至下一筆資料
                }
                rslt = sNote;
            }

            cursor.close(); //關閉Cursor
            //dbHelper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉

        }catch (Exception ex)
        {
            rslt[0] = ex.getMessage();
        }
        return rslt;
    }

    public String[] myKey(){
        String []rslt=new String[1];
        try {
            Cursor cursor = db.rawQuery("select key from productkey where isUsing='true'", null);
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
            //dbHelper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉

        }catch (Exception ex)
        {
            rslt[0] = ex.getMessage();
        }
        return rslt;
    }

    public void changeKeyBtn_Click(View view)
    {
        CheckProductKey.Type="change";
        CheckProductKey.UseMode="";
        changeIntent(CheckProductKey.class);
    }

    public void changeDeviceBtn_Click(View view)
    {
        MainActivity.Type = "change";
        changeIntent(UserGWSelect.class);
    }

    public void changeAccountBtn_Click(View view)
    {
        MainActivity.Type = "change";
        changeIntent(AccountManagement.class);
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


                Intent i = new Intent(SettingPage.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
            }
        }
    }

//    @Override
//     public boolean onCreateOptionsMenu(Menu menu) {
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

    public void webpageClick(View view) { changeIntent(ManagementWeb.class); }

    public void btnInfoClick(View view)    {changeIntent(SettingPage.class);    }

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
                    intent.setClass(SettingPage.this, repair_request.class);
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
                    intent.setClass(SettingPage.this, ManagementWeb.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            if(SettingPage.class.equals(_Intent))
                startActivity(getIntent());
            else {
                Intent intent = new Intent();
                intent.setClass(SettingPage.this, _Intent);
                startActivity(intent);
                finish();
            }
        }
    }
}
