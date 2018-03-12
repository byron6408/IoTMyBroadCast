package com.push.k.mybroadcast;

/**
 * Created by Byron-NB on 2016/2/19.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    //資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
    private static final int VERSION = 2;//資料庫版本

    //建構子
    public MyDBHelper(Context context, String name, CursorFactory factory,int version) {
        super(context, name, factory, version);
    }

    public MyDBHelper(Context context,String name) {
        this(context, name, null, VERSION);
    }

    public MyDBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    //輔助類建立時運行該方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            String DATABASE_CREATE_TABLE =
                    "create table productkey("
                            + "Key VARCHAR PRIMARY KEY  NOT NULL," +
                            " isUsing  VARCHAR "
                            + ")";
            db.execSQL(DATABASE_CREATE_TABLE);

            String DATABASE_CREATE_TABLE2 =
                    "create table userInfo("
                            + "ProductKey VARCHAR PRIMARY KEY  NOT NULL," +
                            " NAME TEXT NOT NULL, " +
                            " PHONE TEXT NOT NULL, " +
                            " EMAIL  TEXT NOT NULL"
                            + ")";
            db.execSQL(DATABASE_CREATE_TABLE2);

            String DATABASE_CREATE_TABLE3 =
                    "create table Language("
                            + "Language VARCHAR PRIMARY KEY  NOT NULL"
                            + ")";
            db.execSQL(DATABASE_CREATE_TABLE3);
        } catch (Exception ex) {
            String s = "";
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        db.execSQL("DROP TABLE IF EXISTS productkey"); //刪除舊有的資料表
        db.execSQL("DROP TABLE IF EXISTS userInfo"); //刪除舊有的資料表
        db.execSQL("DROP TABLE IF EXISTS Language"); //刪除舊有的資料表
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打開數據庫後首先被執行
    }

    @Override
    public synchronized void close() {
        super.close();
    }

}