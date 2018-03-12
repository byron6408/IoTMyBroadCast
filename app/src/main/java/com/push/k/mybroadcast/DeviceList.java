package com.push.k.mybroadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;

import com.push.k.myWebService.CallWebService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceList extends AppCompatActivity {

    public static String getDeviceList_rslt="";
    public static String [] deviceList;
    private ExpandableListView expListView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> itemList;
    private List<Map<String, String>> group = new ArrayList<Map<String, String>>();
    private static Context context;

    private List<List<Map<String, String>>> childrenList = new ArrayList<List<Map<String, String>>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        expListView = (ExpandableListView)findViewById(R.id.list);

        getDeviceList();

        // Step 5 :註冊 OnItemClickListener
        ListView lv = (ListView)findViewById(R.id.list);
        lv.setOnItemClickListener(new MyOnItemClickListener());
    }

    private  void getDeviceList()
    {
        getDeviceList_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.FuntionType = "getDeviceList";
            c.join();
            c.start();
            while (getDeviceList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }

            if (getDeviceList_rslt.equalsIgnoreCase("success")) {
                if (deviceList != null) {
                    if (deviceList.length != 0) {
                        itemList = new ArrayList<>();

                        final String DeviceGroup[] = getDeviceidGroup();

                        for (int i = 0; i < DeviceGroup.length; i++) {
                            // Group
                            Map<String, String> DeviceIdGroup = new HashMap<String, String>();
                            DeviceIdGroup.put("groupKey", DeviceGroup[i]);
                            DeviceIdGroup.put("Msg_groupKey", "裝置:"+DeviceGroup[i]);
                            group.add(DeviceIdGroup);

                            // Sub
                            List<Map<String, String>> children =
                                    new ArrayList<Map<String, String>>();

                            for (int j = 0; j < deviceList.length; j++) {
                                String DEV_ID = deviceList[j].split("&c&")[0].replace("[@datas]:", "");
                                String DEV_NAME = deviceList[j].split("&c&")[1];
                                String TYPE = deviceList[j].split("&c&")[2];
                                String DEV_IP = deviceList[j].split("&c&")[3];
                                String DEV_PORT = deviceList[j].split("&c&")[4];
                                String AI_ONOFF = deviceList[j].split("&c&")[5];
                                String DI_ONOFF = deviceList[j].split("&c&")[6];
                                String AO_ONOFF = deviceList[j].split("&c&")[7];
                                String DO_ONOFF = deviceList[j].split("&c&")[8];
                                String CH_ID = deviceList[j].split("&c&")[9];
                                String CH_NAME = deviceList[j].split("&c&")[10];

                                String Push_PUSH_TYPE = "";
                                String Push_MAX = "";
                                String Push_MIN = "";
                                String Push_VALUE = "";
                                String Push_AlarmTime = "";

                                if(deviceList[j].split("&c&").length>11)
                                {
                                    Push_PUSH_TYPE = deviceList[j].split("&c&")[11];

                                    if(Push_PUSH_TYPE.equalsIgnoreCase("A"))
                                    {
                                        Push_MAX = deviceList[j].split("&c&")[12];
                                        Push_MIN = deviceList[j].split("&c&")[13];
                                        Push_AlarmTime = deviceList[j].split("&c&")[14];
                                    }
                                    else if(Push_PUSH_TYPE.equalsIgnoreCase("C"))
                                    {
                                        Push_VALUE = deviceList[j].split("&c&")[12];
                                        Push_AlarmTime = deviceList[j].split("&c&")[13];
                                    }
                                }


                                HashMap<String, Object> msg = new HashMap<>();

                                msg.put("DEV_ID", DEV_ID);
                                msg.put("DEV_NAME", DEV_NAME);
                                msg.put("TYPE", TYPE);
                                msg.put("DEV_IP", DEV_IP);
                                msg.put("DEV_PORT", DEV_PORT);
                                msg.put("AI_ONOFF", AI_ONOFF);
                                msg.put("DI_ONOFF", DI_ONOFF);
                                msg.put("AO_ONOFF", AO_ONOFF);
                                msg.put("DO_ONOFF", DO_ONOFF);
                                msg.put("CH_ID", CH_ID);
                                msg.put("CH_NAME", CH_NAME);

                                Map<String, String> childMap =
                                        new HashMap<String, String>();

                                childMap.put("DEV_ID", DEV_ID);
                                childMap.put("Msg_DEV_ID", "裝置" +DEV_ID);
                                childMap.put("DEV_NAME", DEV_NAME);
                                childMap.put("TYPE", TYPE);
                                childMap.put("Msg_TYPE", "設備類型:"+TYPE);
                                childMap.put("DEV_IP", DEV_IP);
                                childMap.put("DEV_PORT", DEV_PORT);
                                childMap.put("AI_ONOFF", AI_ONOFF);
                                childMap.put("DI_ONOFF", DI_ONOFF);
                                childMap.put("AO_ONOFF", AO_ONOFF);
                                childMap.put("DO_ONOFF", DO_ONOFF);
                                childMap.put("CH_ID", CH_ID);
                                childMap.put("CH_NAME", CH_NAME);
                                childMap.put("Msg_CH_NAME", "Channel名稱:" +CH_NAME);

                                childMap.put("Push_PUSH_TYPE", Push_PUSH_TYPE);
                                childMap.put("Push_MAX", Push_MAX);
                                childMap.put("Push_MIN", Push_MIN);
                                childMap.put("Push_VALUE", Push_VALUE);
                                childMap.put("Push_AlarmTime", Push_AlarmTime);

                                children.add(childMap);

                                // Step 2 :放入到List集合容器中

                                //itemList.add(msg);
                            }
                            childrenList.add(children);
                        }

                        // 建立 SimpleExpandableListAdapter

                        SimpleExpandableListAdapter mAdapter =
                                new SimpleExpandableListAdapter(
                                        context,
                                        group,
                                        android.R.layout.simple_expandable_list_item_1,
                                        new String[]{"Msg_groupKey"},
                                        new int[]{android.R.id.text1},
                                        childrenList,
                                        R.layout.content_device_list,
                                        new String[]{"Msg_TYPE","Msg_CH_NAME"},
                                        new int[]{R.id.TYPE,R.id.CH_NAME}
                                );

                        // 設定Adapter
                        expListView.setAdapter(mAdapter);

                        // 設定監聽器
                        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                            @Override
                            public boolean onChildClick(ExpandableListView parent,
                                                        View view, int groupPosition, int childPosition, long id) {

                                // 取得被點選之商品資料
                                HashMap<String, Object> item =
                                        (HashMap<String, Object>) parent.getItemAtPosition(groupPosition);

                                // 取出DeviceName
                                String DeviceName = item.get("groupKey").toString();

                                //取得該裝置所有CH
                                String []temp = new String[0];
                                for(int i=0;i<deviceList.length;i++)
                                {
                                    if(deviceList[i].split("&c&")[1].equalsIgnoreCase(DeviceName))
                                    {
                                        if(temp.length ==0)
                                        {
                                            temp = new String[1];
                                            temp[0] = deviceList[i];
                                        }
                                        else {
                                            temp = Arrays.copyOf(temp, temp.length + 1);
                                            temp[temp.length-1] = deviceList[i];
                                        }
                                    }
                                }

                                String DEV_ID = temp[childPosition].split("&c&")[0].replace("[@datas]:", "");
                                String DEV_NAME = temp[childPosition].split("&c&")[1];
                                String TYPE = temp[childPosition].split("&c&")[2];
                                String DEV_IP = temp[childPosition].split("&c&")[3];
                                String DEV_PORT = temp[childPosition].split("&c&")[4];
                                String AI_ONOFF = temp[childPosition].split("&c&")[5];
                                String DI_ONOFF = temp[childPosition].split("&c&")[6];
                                String AO_ONOFF = temp[childPosition].split("&c&")[7];
                                String DO_ONOFF = temp[childPosition].split("&c&")[8];
                                String CH_ID = temp[childPosition].split("&c&")[9];
                                String CH_NAME = temp[childPosition].split("&c&")[10];

                                String Push_PUSH_TYPE = "";
                                String Push_MAX = "";
                                String Push_MIN = "";
                                String Push_VALUE = "";
                                String Push_AlarmTime = "";

                                if(temp[childPosition].split("&c&").length>11)
                                {
                                    Push_PUSH_TYPE = temp[childPosition].split("&c&")[11];

                                    if(Push_PUSH_TYPE.equalsIgnoreCase("A"))
                                    {
                                        Push_MAX = temp[childPosition].split("&c&")[12];
                                        Push_MIN = temp[childPosition].split("&c&")[13];
                                        Push_AlarmTime = temp[childPosition].split("&c&")[14];
                                    }
                                    else if(Push_PUSH_TYPE.equalsIgnoreCase("C"))
                                    {
                                        Push_VALUE = temp[childPosition].split("&c&")[12];
                                        Push_AlarmTime = temp[childPosition].split("&c&")[13];
                                    }
                                }

                                DeviceDetail.DEV_ID = DEV_ID;
                                DeviceDetail.DEV_NAME = DEV_NAME;
                                DeviceDetail.TYPE = TYPE;
                                DeviceDetail.DEV_IP = DEV_IP;
                                DeviceDetail.DEV_PORT = DEV_PORT;
                                DeviceDetail.AI_ONOFF = AI_ONOFF;
                                DeviceDetail.DI_ONOFF = DI_ONOFF;
                                DeviceDetail.AO_ONOFF = AO_ONOFF;
                                DeviceDetail.DO_ONOFF = DO_ONOFF;
                                DeviceDetail.CH_ID = CH_ID;
                                DeviceDetail.CH_NAME = CH_NAME;

                                DeviceDetail.Push_PUSH_TYPE = Push_PUSH_TYPE;
                                DeviceDetail.Push_MAX = Push_MAX;
                                DeviceDetail.Push_MIN = Push_MIN;
                                DeviceDetail.Push_VALUE = Push_VALUE;
                                DeviceDetail.Push_AlarmTime = Push_AlarmTime;

                                changeToDetailPage();
                                return false;
                            }

                        });

//                        for (int i = 0; i < deviceList.length; i++)
//                        {
//                            String DEV_ID = deviceList[i].split("&c&")[0].replace("[@datas]:", "");
//                            String DEV_NAME = deviceList[i].split("&c&")[1];
//                            String TYPE = deviceList[i].split("&c&")[2];
//                            String DEV_IP = deviceList[i].split("&c&")[3];
//                            String DEV_PORT = deviceList[i].split("&c&")[4];
//                            String AI_ONOFF = deviceList[i].split("&c&")[5];
//                            String DI_ONOFF = deviceList[i].split("&c&")[6];
//                            String AO_ONOFF = deviceList[i].split("&c&")[7];
//                            String DO_ONOFF = deviceList[i].split("&c&")[8];
//                            String CH_NAME = deviceList[i].split("&c&")[9];
//
//
//
//
//                            HashMap<String, Object> msg = new HashMap<>();
//
//                            msg.put("DEV_ID", DEV_ID);
//                            msg.put("DEV_NAME", DEV_NAME);
//                            msg.put("TYPE", TYPE);
//                            msg.put("DEV_IP", DEV_IP);
//                            msg.put("DEV_PORT", DEV_PORT);
//                            msg.put("AI_ONOFF", AI_ONOFF);
//                            msg.put("DI_ONOFF", DI_ONOFF);
//                            msg.put("AO_ONOFF", AO_ONOFF);
//                            msg.put("DO_ONOFF", DO_ONOFF);
//                            msg.put("CH_NAME", CH_NAME);
//
//                            // Step 2 :放入到List集合容器中
//
//                            itemList.add(msg);
//                        }
//
//                        // Step 3 :建立SimpleAdapter適配器
//                        adapter = new SimpleAdapter(
//                                this,         // 設定接口環境
//                                itemList,     // 設定接口容器資料
//                                R.layout.content_device_list, // 資料顯示 UI XML
//                                new String[]{"DEV_ID", "TYPE", "CH_NAME"},   // 商品資料標題
//                                new int[]{R.id.DEV_ID, R.id.TYPE, R.id.CH_NAME} // 資料 UI
//                        );
//
//                        // Step 4 :設定適配器
//                        ListView lv = (ListView)findViewById(R.id.list);
//                        lv.setAdapter(adapter);
//                    }
                    }
                }
            }
        }catch (Exception ex)
        {}
    }

    public String[] getDeviceidGroup()
    {
        String []DeviceGroup = new String[0];

        for(int i=0;i<deviceList.length;i++)
        {
            String DEV_ID = deviceList[i].split("&c&")[0].replace("[@datas]:", "");
            String DEV_NAME = deviceList[i].split("&c&")[1];
            String TYPE = deviceList[i].split("&c&")[2];
            String DEV_IP = deviceList[i].split("&c&")[3];
            String DEV_PORT = deviceList[i].split("&c&")[4];
            String AI_ONOFF = deviceList[i].split("&c&")[5];
            String DI_ONOFF = deviceList[i].split("&c&")[6];
            String AO_ONOFF = deviceList[i].split("&c&")[7];
            String DO_ONOFF = deviceList[i].split("&c&")[8];
            String CH_NAME = deviceList[i].split("&c&")[9];

            String thisName = DEV_NAME;

            if(DeviceGroup.length==0)
            {
                DeviceGroup = new String[1];
                DeviceGroup[0] = thisName;
            }
            else
            {
                for (int j=0;j<DeviceGroup.length;j++)
                {
                    if(DeviceGroup[j].equals(thisName))
                        continue;
                    else if(!DeviceGroup[j].equals(thisName) && j==DeviceGroup.length-1) {
                        DeviceGroup = Arrays.copyOf(DeviceGroup,DeviceGroup.length+1);
                        DeviceGroup[DeviceGroup.length-1] = thisName;
                    }
                }
            }
        }

        return  DeviceGroup;
    }

    // 列表項目點選之事件處理
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // 取得被點選之商品資料
            HashMap<String, Object> item =
                    (HashMap<String, Object>) parent.getItemAtPosition(position);

            // 取出商品名稱, 價格
            DeviceDetail.DEV_ID = item.get("DEV_ID").toString();
            DeviceDetail.DEV_NAME = item.get("DEV_NAME").toString();
            DeviceDetail.TYPE = item.get("TYPE").toString();
            DeviceDetail.DEV_IP = item.get("DEV_IP").toString();
            DeviceDetail.DEV_PORT = item.get("DEV_PORT").toString();
            DeviceDetail.AI_ONOFF = item.get("AI_ONOFF").toString();
            DeviceDetail.DI_ONOFF = item.get("DI_ONOFF").toString();
            DeviceDetail.AO_ONOFF = item.get("AO_ONOFF").toString();
            DeviceDetail.DO_ONOFF = item.get("DO_ONOFF").toString();
            DeviceDetail.CH_NAME = item.get("CH_NAME").toString();

            changeToDetailPage();
            // Toast 顯示
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private  void changeToDetailPage()
    {
        Intent intent = new Intent();
        intent.setClass(DeviceList.this, DeviceDetail.class);
        startActivity(intent);
        finish();
    }
    @Override
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

            }
            else if(item.getTitle().toString().equals("即時資訊"))
            {
                changeToInstantStatusPage();
            }
        }
        return  super.onOptionsItemSelected(item);
    }

    private  void changeToMessageList()
    {
        Intent intent = new Intent();
        intent.setClass(DeviceList.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    private  void changeToDeviceList()
    {
        Intent intent = new Intent();
        intent.setClass(DeviceList.this, DeviceList.class);
        startActivity(intent);
        finish();
    }

    private  void changeToInstantStatusPage()
    {
        Intent intent = new Intent();
        intent.setClass(DeviceList.this,InstantDeviceStatus.class);
        startActivity(intent);
        finish();
    }

}
