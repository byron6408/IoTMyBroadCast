package com.push.k.mybroadcast;

import android.animation.TimeAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.push.k.myWebService.CallWebService;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {

    public static String notificationList_rslt="";

    public static String[] notificationlist = new String[0];

    public Timer timer = new Timer(true);

    private Context context;
    private ArrayList<String> randomList;
    private ListAdapter adapter;
    private ListView listView;
    private TimeAnimator.TimeListener l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //listView = (ListView) findViewById(R.id.listView);
        //listView.setEmptyView(findViewById(R.id.emptyView));
       // randomList = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(context,
        //       android.R.layout.simple_expandable_list_item_1,
        //        randomList);
        //listView.setAdapter(adapter);// 資料接口
        //registerForContextMenu(listView);


        //宣告Timer
        Timer timer01 =new Timer();
        //設定Timer(task為執行內容，1000代表1秒後開始,間格1秒執行一次)
        timer01.schedule(task, 5000,5000);




        //Ge User Infos
        String objectId = ParseInstallation.getCurrentInstallation().getObjectId();

        getNoticeficationList(objectId);

    }

    //TimerTask無法直接改變元件因此要透過Handler來當橋樑
    private Handler handler = new Handler(){
        public  void  handleMessage(Message msg) {
            super.handleMessage(msg);
            //Ge User Infos
            String objectId = ParseInstallation.getCurrentInstallation().getObjectId();
            getNoticeficationList(objectId);

        }
    };

    private TimerTask task = new TimerTask(){

        @Override
        public void run() {
                Message message = new Message();

                //傳送訊息1
                message.what =1;
                handler.sendMessage(message);

        }

    };

    private void getNoticeficationList(String objectId) {
        notificationList_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.objectId = objectId;
            c.FuntionType = "getNotificationList";
            c.join();
            c.start();
            while (notificationList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (notificationList_rslt.equalsIgnoreCase("success")) {
                if(notificationlist !=null) {
                    if(notificationlist.length!=0) {
                        randomList.clear();
                        for (int i = 0; i < notificationlist.length; i++) {
                            String NO = notificationlist[i].split("&c&")[0].replace("[@datas]:", "");
                            String MsgType = notificationlist[i].split("&c&")[1];
                            String Channel = notificationlist[i].split("&c&")[2];
                            String Title = notificationlist[i].split("&c&")[3];
                            String Body = notificationlist[i].split("&c&")[4];
                            String Target = notificationlist[i].split("&c&")[5];
                            String GCMType = notificationlist[i].split("&c&")[6];
                            String DateTime = notificationlist[i].split("&c&")[7];
                            String Remark = notificationlist[i].split("&c&")[8];
                            String Source = notificationlist[i].split("&c&")[9];

                            String text = "內容:" + Body + " \n時間:" + DateTime;

                            addListItem(text);
                        }
                    }
                }
            }
            else
                showDialog_1(notificationList_rslt);
        } catch (InterruptedException e) {
            showDialog_1(e.toString());
        }
    }


    private void addListItem(String text)
    {
        // 增加資料
        randomList.add(text);
        // 通知 adapter 更新
        ((BaseAdapter) adapter).notifyDataSetChanged();
        // 自動將listView捲軸都移到最上面
        listView.setSelection(0);
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

}
