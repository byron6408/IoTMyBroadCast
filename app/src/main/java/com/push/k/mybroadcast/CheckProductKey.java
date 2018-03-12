package com.push.k.mybroadcast;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.push.k.myWebService.CallWebService;


public class CheckProductKey extends AppCompatActivity {
    public static String checkProductKey_rslt = "";        //確認DB狀態
    public static String checkCustomerType_rslt = "";        //確認DB類型(1.一般,2.醫院)

    SQLiteDatabase db;
    //資料庫名
    public String db_name = "PUSHDB0";
    //表名
    public String table_name1 = "productkey";
    public String table_name2 = "userInfo";
    //輔助類名
    MyDBHelper helper = new MyDBHelper(CheckProductKey.this, db_name);

    static String APPMode = "";

    static String UseMode="";
    static String AdminKey="";

    //檢測頁面狀態，若為改變序號，則停留在頁面
    public static String Type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_product_key);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //以輔助類獲得資料庫對象
        db = helper.getReadableDatabase();

        //取得SQL Lite 紀錄
        String[] key = myKey();

        if (!Type.equalsIgnoreCase("change")) {

            if (key[0] == null || key[0].equalsIgnoreCase("Attempt to invoke virtual method 'android.database.Cursor android.database.sqlite.SQLiteDatabase.rawQuery(java.lang.String, java.lang.String[])' on a null object reference")) {

            } else {
                //CheckProductKey();

                db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
                WebSelectPage.selectedDB = key[0];
                WebSelectPage.selectedDBConnStr = "Data Source=192.168.1.88;Initial Catalog=" + key[0] + ";Persist Security Info=True;User ID=sa;Password=p@ssWord";

                CheckProductKey2(key[0]);
            }
        }
        Type = "";

        if(UseMode.equals("Admin"))
        {
            CheckProductKey();
        }
    }

    public void confirmProductBtnClick(View view) {
        TextView tv_productkey = (TextView) findViewById(R.id.productkey);

        String productkey = tv_productkey.getText().toString().trim();

        if (productkey.equals("")) {
            showDialog_1("Please Fill The Data!");
        } else {
//            CallWebService service = new CallWebService();
//            String response = service.Call(text);
//            response = response;
            CheckProductKey();
        }
    }

    public void CheckProductKey()
    {
        String productkey="";
        if(UseMode.equals("Admin"))
        {
            productkey = AdminKey;
        }
        else {
            TextView tv_productkey = (TextView) findViewById(R.id.productkey);
            productkey = tv_productkey.getText().toString().trim();
        }

        try
        {
            EditText ed1=(EditText)findViewById(R.id.register_name);
            checkProductKey_rslt="START";
            CallWebService c=new CallWebService();
            c.FuntionType = "checkProductKey_new";
            c.ProductKey = productkey;

            c.join(); c.start();
            while(checkProductKey_rslt=="START") {
                try {
                    Thread.sleep(100);
                }catch(Exception ex) {
                }
            }
            if (checkProductKey_rslt.equalsIgnoreCase("true") || checkProductKey_rslt.equalsIgnoreCase("false") || checkProductKey_rslt.equalsIgnoreCase("V")) {
                if(checkProductKey_rslt.equalsIgnoreCase("true"))
                {
                    productkey = productkey.replace("DAIIO-","");
                    productkey = productkey.replace("DAIIO","");
                    productkey = productkey.toUpperCase();
                    if(productkey.equals("ZZZZ-ZZZZ-ZZZZ"))
                        productkey= "DAIIO-"+ productkey;
                    else
                        productkey= "DAIIO"+ productkey;
                    WebSelectPage.selectedDB=productkey;
                    WebSelectPage.selectedDBConnStr="Data Source=192.168.1.88;Initial Catalog="+productkey+";Persist Security Info=True;User ID=sa;Password=p@ssWord";

                    if(!checkMyKey(productkey))
                    {
                        setMyKey(productkey);
                    }

                    updateMyKey(productkey);


                    db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
                }
                else if(checkProductKey_rslt.equalsIgnoreCase("V"))
                {
                    APPMode = "V";
                    productkey = productkey.replace("DAIIO","");
                    productkey= "DAIIO"+ productkey;
                    WebSelectPage.selectedDB=productkey;
                    WebSelectPage.selectedDBConnStr="Data Source=192.168.1.88;Initial Catalog="+productkey+";Persist Security Info=True;User ID=sa;Password=p@ssWord";

                    if(!checkMyKey(productkey))
                    {
                        setMyKey(productkey);
                    }
                    updateMyKey(productkey);

                        changeToRegisterPage();
                    db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
                }
                else
                {
                    showDialog_1("Key Not Found!");
                }

            } else
                showDialog_1("ERROR!");
        }catch(Exception ex) {
            showDialog_1(ex.toString());
        }

        //Check DB Type
        try
        {
            checkCustomerType_rslt="START";
            CallWebService c=new CallWebService();
            c.FuntionType = "CheckCustomerType";
            c.ProductKey = productkey;

            c.join(); c.start();
            while(checkCustomerType_rslt=="START") {
                try {
                    Thread.sleep(100);
                }catch(Exception ex) {
                }
            }
            if (checkCustomerType_rslt.equalsIgnoreCase("1") || checkCustomerType_rslt.equalsIgnoreCase("2")) {
                    WebSelectPage.DBType=checkCustomerType_rslt;
            } else
                showDialog_1("Type ERROR!");
        }
        catch (Exception ex)
        {
            showDialog_1(ex.toString());
        }

        if(WebSelectPage.DBType.equals("1") || WebSelectPage.DBType.equals("")) {
            //ImageButton Ib = (ImageButton)findViewById(R.id.webpage);
            //Ib.setVisibility(View.VISIBLE);
            changeToRegisterPage();
        }
        else if(WebSelectPage.DBType.equals("2")) {
            //ImageButton Ib = (ImageButton)findViewById(R.id.webpage);
            //Ib.setVisibility(View.GONE);
            changeToRegisterPage();
        }
    }

    public void CheckProductKey2(String productkey)
    {
        try
        {
            EditText ed1=(EditText)findViewById(R.id.register_name);
            checkProductKey_rslt="START";
            CallWebService c=new CallWebService();
            c.FuntionType = "checkPruductKey2_new";
            c.ProductKey = productkey;

            c.join(); c.start();
            while(checkProductKey_rslt=="START") {
                try {
                    Thread.sleep(100);
                }catch(Exception ex) {
                }
            }
            if (checkProductKey_rslt.equalsIgnoreCase("true") || checkProductKey_rslt.equalsIgnoreCase("false") || checkProductKey_rslt.equalsIgnoreCase("V")) {
                if(checkProductKey_rslt.equalsIgnoreCase("true"))
                {
                    //productkey = productkey.replace("DAIIO","");
                    //productkey= "DAIIO"+ productkey;
                    WebSelectPage.selectedDB=productkey;
                    WebSelectPage.selectedDBConnStr="Data Source=192.168.1.88;Initial Catalog="+productkey+";Persist Security Info=True;User ID=sa;Password=p@ssWord";

                    db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
                }
                else if(checkProductKey_rslt.equalsIgnoreCase("V"))
                {
                    APPMode = "V";
                    productkey = productkey.replace("DAIIO","");
                    productkey= "DAIIO"+ productkey;
                    WebSelectPage.selectedDB=productkey;
                    WebSelectPage.selectedDBConnStr="Data Source=192.168.1.88;Initial Catalog="+productkey+";Persist Security Info=True;User ID=sa;Password=p@ssWord";

                        changeToRegisterPage();
                    db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
                }
                else
                {
                    showDialog_1("Key Not Found!");
                }
            } else
                showDialog_1("ERROR!");
        }catch(Exception ex) {
            showDialog_1(ex.toString());
        }

        //Check DB Type
        try
        {
            checkCustomerType_rslt="START";
            CallWebService c=new CallWebService();
            c.FuntionType = "CheckCustomerType";
            c.ProductKey = productkey;

            c.join(); c.start();
            while(checkCustomerType_rslt=="START") {
                try {
                    Thread.sleep(100);
                }catch(Exception ex) {
                }
            }
            if (checkCustomerType_rslt.equalsIgnoreCase("1") || checkCustomerType_rslt.equalsIgnoreCase("2")) {
                WebSelectPage.DBType=checkCustomerType_rslt;
            } else
                showDialog_1("Type ERROR");

        }
        catch (Exception ex)
        {
            showDialog_1(ex.toString());
        }

        if(WebSelectPage.DBType.equals("1") || WebSelectPage.DBType.equals("")) {
            changeToRegisterPage();
        }
        else if(WebSelectPage.DBType.equals("2")) {
            changeToRegisterPage();
        }
    }

    //設定記憶資料
    public boolean setMyKey(String key) {
        boolean rslt = false;
        try {
            //
            db.execSQL("DELETE FROM productkey WHERE  KEY ='" + key + "'");
            db.execSQL("DELETE FROM userInfo ");
            db.execSQL("INSERT INTO productkey (Key,isUsing) VALUES ('" + key + "','true')");
            //db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
        } catch (Exception ex) {
            rslt = false;
        }
        return rslt;
    }

    //設定記憶資料
    public boolean checkMyKey(String key) {
        boolean rslt = false;
        try {
            Cursor cursor = db.rawQuery("Select Key from productkey where key = '" + key + "'", null);
            //用陣列存資料
            int rows_num = cursor.getCount();//取得資料表列數
            String[] sNote = new String[0];
            if(rows_num != 0) {
                sNote = new String[cursor.getCount()];
                cursor.moveToFirst();   //將指標移至第一筆資料
                for (int i = 0; i < rows_num; i++) {
                    String strCr = cursor.getString(0);
                    sNote[i] = strCr;

                    rslt = true;
                    cursor.moveToNext();//將指標移至下一筆資料
                }
            }
            //db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
        } catch (Exception ex) {
        }
        return rslt;
    }

    //設定記憶資料
    public boolean updateMyKey(String key) {
        boolean rslt = false;
        try {
            db.execSQL("Update productkey Set isUsing = '" + "false" + "'");
            db.execSQL("DELETE FROM userInfo ");
            db.execSQL("Update productkey Set isUsing = '" + "true" + "' where key='"+key+"' ");

            //db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
        } catch (Exception ex) {
            rslt = false;
        }
        return rslt;
    }

    //取得記憶資料
    public String[] myKey(){
        String []rslt=new String[1];
        try {
            Cursor cursor = db.rawQuery("select key from productkey where isUsing='"+"true"+"'", null);
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
            //123
        }
        return rslt;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            show();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void show() {
        new android.app.AlertDialog.Builder(CheckProductKey.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.LeaveDiagBody)
                .setPositiveButton(R.string.LeaveDiagConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CheckProductKey.this.finish();
                    }
                })
                .setNegativeButton(R.string.LeaveDiagCancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
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

    private  void changeToRegisterPage()
    {
        Intent intent = new Intent();
        intent.setClass(CheckProductKey.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private  void changeToSettingPage()
    {
        Intent intent = new Intent();
        intent.setClass(CheckProductKey.this, SettingPage.class);
        startActivity(intent);
        finish();
    }



}
