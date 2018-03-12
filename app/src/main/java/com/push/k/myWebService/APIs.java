package com.push.k.myWebService;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;

import com.push.k.mybroadcast.MyDBHelper;

import java.util.Locale;

/**
 * Created by Byron-NB on 2016/12/8.
 */
public class APIs {

    SQLiteDatabase db;
    //資料庫名
    public String db_name = "PUSHDB0";
    //表名
    public String table_name1 = "productkey";
    public String table_name2 = "userInfo";
    public String table_name3 = "Language";

    public Context c;
    //輔助類名

    //取得記憶資料
    public String getPhone(){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select PHONE from userInfo ", null);
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

                rslt = sNote[0];
            }

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public String getName(){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select NAME from userInfo ", null);
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

                rslt = sNote[0];
            }

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public boolean updateMyInfo(String key,String name,String email) {
        boolean rslt = false;
        try {
            MyDBHelper helper = new MyDBHelper(c, db_name);
            db = helper.getReadableDatabase();
            db.execSQL("UPDATE userInfo SET NAME='"+name+"',EMAIL='"+email+"' WHERE ProductKey = '"+key+"'");
            db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            helper.close();
        } catch (Exception ex) {
            rslt = false;
        }
        return rslt;
    }

    public String getName(String DBNAME){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select NAME from userInfo  WHERE ProductKey = '"+DBNAME+"'", null);
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

                rslt = sNote[0];
            }

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public String getEmail(){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select EMAIL from userInfo ", null);
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

                rslt = sNote[0];
            }

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public String getEmail(String DBNAME){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select EMAIL from userInfo WHERE ProductKey = '"+DBNAME+"'", null);
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

                rslt = sNote[0];
            }

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public String getLanguage(){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select Language from Language ", null);
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

                rslt = sNote[0];
            }
            else
                rslt="";

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public String delProductKey(){
        MyDBHelper helper = new MyDBHelper(c, db_name);
        String rslt="";
        try {

            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("DELETE Key from productkey ", null);
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

                rslt = sNote[0];
            }

            cursor.close(); //關閉Cursor
            helper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            db.close();

        }catch (Exception ex)
        {
            rslt = ex.getMessage();
        }
        return rslt;
    }

    public void getLanguage(Context _c)
    {
        c = _c;
        String language = getLanguage();

        if(language.equals(""))
            language = "0";

        Resources resources = _c.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        switch (language)
        {
            case "0":
                config.locale = Locale.ENGLISH;
                break;
            case "1":
                config.locale = Locale.TAIWAN;
                break;
            default:
                config.locale = Locale.ENGLISH;
                break;
        }
        resources.updateConfiguration(config, dm);
    }

    public void setLanguage(Context _c,String language)
    {
        c = _c;

        try {
            MyDBHelper helper = new MyDBHelper(_c, db_name);
            db = helper.getReadableDatabase();
            db.execSQL("DELETE From Language");
            db.execSQL("INSERT INTO Language (Language) VALUES ('" + language +"')");
            db.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
            helper.close();
        } catch (Exception ex) {
        }

        Resources resources = _c.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        switch (language)
        {
            case "0":
                config.locale = Locale.ENGLISH;
                break;
            case "1":
                config.locale = Locale.TAIWAN;
                break;
            default:
                config.locale = Locale.ENGLISH;
                break;
        }
        resources.updateConfiguration(config, dm);
    }
}

