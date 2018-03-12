package com.push.k.mybroadcast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.push.k.myWebService.APIs;

public class FirstPage extends AppCompatActivity {

    public static String appVersion_rslt="";  //APP 版本
    public static String APPURL="";

    SQLiteDatabase db;
    //資料庫名
    public String db_name = "PUSHDB0";
    //表名
    public String table_name1 = "productkey";
    public String table_name2 = "userInfo";

    //輔助類名
    MyDBHelper helper = new MyDBHelper(FirstPage.this, db_name);

    //檢測頁面狀態，若為改變序號，則停留在頁面
    public static String Type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //取得語言
        APIs a  = new APIs();
        a.getLanguage(this);

        //以輔助類獲得資料庫對象
        db = helper.getReadableDatabase();
        Context context = this;

        //取得SQL Lite 紀錄
        String []key = myKey();
        if(!Type.equalsIgnoreCase("change")) {
            if (key[0] == null || key[0].equalsIgnoreCase("Attempt to invoke virtual method 'android.database.Cursor android.database.sqlite.SQLiteDatabase.rawQuery(java.lang.String, java.lang.String[])' on a null object reference")) {
                changeToCheckProductKeyPage();
            } else {
                db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
                WebSelectPage.selectedDB = key[0];
                WebSelectPage.selectedDBConnStr = "Data Source=192.168.1.88;Initial Catalog=" + key[0] + ";Persist Security Info=True;User ID=sa;Password=p@ssWord";

                checkAppVersion();
            }
        }

    }

    public String[] myKey(){
        String []rslt=new String[1];
        try {
            Cursor cursor = db.rawQuery("select key from productkey where isUsing='" + "true" + "'", null);
            //用陣列存資料
            int rows_num = cursor.getCount();//取得資料表列數
            String[] sNote = new String[0];
            if (rows_num != 0) {
                sNote = new String[cursor.getCount()];
                cursor.moveToFirst();   //將指標移至第一筆資料
                for (int i = 0; i < rows_num; i++) {
                    String strCr = cursor.getString(0);
                    sNote[i] = strCr;

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

    private  void checkAppVersion()
    {
//        Context context =this;
//        String thisVersion = context.getString(R.string.Version);
//        appVersion_rslt = "START";
//        try {
//            CallWebService c = new CallWebService();
//            c.FuntionType = "checkAppVersion";
//            c.AppVersion=thisVersion;
//            c.join();
//            c.start();
//            while (appVersion_rslt == "START") {
//                try {
//                    Thread.sleep(100);
//                } catch (Exception ex) {
//                }
//            }
//            if (appVersion_rslt.split(":")[0].equalsIgnoreCase("[true]") || appVersion_rslt.split(":")[0].equalsIgnoreCase("[false]")) {
//                if(appVersion_rslt.split(":")[0].equalsIgnoreCase("[true]"))
//                {
//                    APPURL = appVersion_rslt.replace("[true]:","");
//                    show();
//                    //changeToListPage();
//                }
//                else
//                    changeToCheckProductKeyPage();
//
//
//            } else {
                changeToCheckProductKeyPage();
//            }


//        } catch (Exception e) {
//            showDialog_1(e.toString());
//            checkAppVersion();
//        }
    }

    public void show() {
        new AlertDialog.Builder(FirstPage.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.UpdateAppBody)
                .setPositiveButton(R.string.UpdateAppConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(APPURL));
                        startActivity(intent);

                        FirstPage.this.finish();

                    }
                })
                .setNegativeButton(R.string.UpdateAppCancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirstPage.this.finish();
                    }

                })
                .show();
    }

    private  void changeToCheckProductKeyPage()
    {
        Intent intent = new Intent();
        intent.setClass(FirstPage.this,CheckProductKey.class);
        startActivity(intent);
        finish();
    }



}
