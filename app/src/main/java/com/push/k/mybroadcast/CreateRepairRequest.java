package com.push.k.mybroadcast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.push.k.myWebService.APIs;
import com.push.k.myWebService.CallWebService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CreateRepairRequest extends AppCompatActivity {

    public static String deviceList_rslt = "";
    public static String create_rslt = "";
    public static String[] device_list;
    public static SimpleAdapter adapter;
    public static ArrayList<HashMap<String, Object>> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_repair_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get User Infos
        APIs a = new APIs();
        a.c = this;
        String phone = a.getPhone();

        //Ge User Infos
        String objectId = phone;
        getRepairList(objectId);
        loadDDL();

    }

    public void loadDDL()
    {
        //Start Time
        Spinner sy=(Spinner)findViewById(R.id.rrStartYear);
        Spinner sM=(Spinner)findViewById(R.id.rrStartMonth);
        Spinner sd=(Spinner)findViewById(R.id.rrStartDay);
        Spinner sh=(Spinner)findViewById(R.id.rrStartHour);
        Spinner sm=(Spinner)findViewById(R.id.rrStartMin);

        //End Time
        Spinner ey=(Spinner)findViewById(R.id.rrEndYear);
        Spinner eM=(Spinner)findViewById(R.id.rrEndMonth);
        Spinner ed=(Spinner)findViewById(R.id.rrEndDay);
        Spinner eh=(Spinner)findViewById(R.id.rrEndHour);
        Spinner em=(Spinner)findViewById(R.id.rrEndMin);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);

        String[] year_items = new String[]{Integer.toString(year),Integer.toString(year+1)};
        String[] month_items = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"};
        String[] day_items = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        String[] hour_items = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String[] minute_items = new String[]{"0","5","10","15","20","25","30","35","40","45","50","55"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, year_items);
        sy.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, month_items);
        sM.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day_items);
        sd.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hour_items);
        sh.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minute_items);
        sm.setAdapter(adapter5);

        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, year_items);
        ey.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, month_items);
        eM.setAdapter(adapter7);

        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day_items);
        ed.setAdapter(adapter8);

        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hour_items);
        eh.setAdapter(adapter9);

        ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minute_items);
        em.setAdapter(adapter10);
    }

    public void createCreateRequestClick(View view) {
        try {

            //Start Time
            Spinner sy=(Spinner)findViewById(R.id.rrStartYear);
            Spinner sM=(Spinner)findViewById(R.id.rrStartMonth);
            Spinner sd=(Spinner)findViewById(R.id.rrStartDay);
            Spinner sh=(Spinner)findViewById(R.id.rrStartHour);
            Spinner sm=(Spinner)findViewById(R.id.rrStartMin);

            //End Time
            Spinner ey=(Spinner)findViewById(R.id.rrEndYear);
            Spinner eM=(Spinner)findViewById(R.id.rrEndMonth);
            Spinner ed=(Spinner)findViewById(R.id.rrEndDay);
            Spinner eh=(Spinner)findViewById(R.id.rrEndHour);
            Spinner em=(Spinner)findViewById(R.id.rrEndMin);

            Spinner devid = (Spinner)findViewById(R.id.rrDevList);

            HashMap<String, Object> item =
                    (HashMap<String, Object>) itemList.get(devid.getSelectedItemPosition());

            String DEV_ID= item.get("DEV_ID").toString();;
            String StartTime = sy.getSelectedItem().toString() +"-"+ sM.getSelectedItem().toString() +"-"+ sd.getSelectedItem().toString() +" "+ sh.getSelectedItem().toString() +":"+ sm.getSelectedItem().toString()+ ":00";
            String EndTime = ey.getSelectedItem().toString() +"-"+ eM.getSelectedItem().toString() +"-"+ ed.getSelectedItem().toString() +" "+ eh.getSelectedItem().toString() +":"+ em.getSelectedItem().toString()+ ":00";

            create_rslt = "START";
            CallWebService c = new CallWebService();


            //Get User Infos
            APIs a = new APIs();
            a.c = this;
            String phone = a.getPhone();

            //Ge User Infos
            String objectId = phone;
            getRepairList(objectId);
            c.objectId = objectId;
            c.FuntionType = "createRepairRequest";
            c.RepairRequestDev_ID = DEV_ID;
            c.RepairRequestStartTime = StartTime;
            c.RepairRequestEndTime = EndTime;
            c.join();
            c.start();
            while (create_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (create_rslt.equalsIgnoreCase("true") || create_rslt.equalsIgnoreCase("false")) {
                if (create_rslt.equalsIgnoreCase("true")) {
                    showDialog_1("Create Complete!");
                    changeIntent(repair_request.class);
                } else {
                    showDialog_1("Create Fail!");
                }
            } else
                showDialog_1(create_rslt);
        }catch (Exception ex)
        {changeIntent(ConnectionErrorPage.class);}
    }

    public void getRepairList(String objectId) {
        deviceList_rslt = "START";
        try {

            CallWebService c = new CallWebService();
            c.objectId=objectId;
            c.FuntionType = "getRepairDeviceList";
            c.join();
            c.start();
            while (deviceList_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (deviceList_rslt.equalsIgnoreCase("success")) {
                if(device_list !=null) {
                    if(device_list.length!=0) {
                        itemList = new ArrayList<>();
                        for (int i = 0; i < device_list.length; i++) {
                            String DEV_ID = device_list[i].split("&c&")[0].replace("[@datas]:", "");
                            String DEV_NAME = device_list[i].split("&c&")[1];

                            // Step 1 :定義商品資料
                            HashMap<String, Object> msg = new HashMap<>();

                            msg.put("DEV_ID",  DEV_ID);
                            msg.put("DEV_NAME",DEV_NAME);
                            // Step 2 :放入到List集合容器中
                            itemList.add(msg);
                        }

                        Spinner dropdown = (Spinner)findViewById(R.id.rrDevList);
                        String[] items = new String[]{"1", "2", "three"};


                        ArrayList<Device> countryList = new ArrayList<>();
                        for (int i = 0; i < device_list.length; i++) {

                            String DEV_ID = device_list[i].split("&c&")[0].replace("[@datas]:", "");
                            String DEV_NAME = device_list[i].split("&c&")[1];
                            countryList.add(new Device(DEV_ID, DEV_NAME));
                        }

                        ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(this, android.R.layout.simple_spinner_dropdown_item, countryList);
                        dropdown.setAdapter(adapter);
                    }
                }
            }
            else
            {
                String s;
            }//showDialog_1(notificationList_rslt);
        } catch (Exception e) {
            String s;
            //showDialog_1(e.toString());
        }
    }

    public class Device {

        private String id;
        private String name;

        public Device(String id, String name) {
            this.id = id;
            this.name = name;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        //to display object as a string in spinner
        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Device){
                Device c = (Device )obj;
                if(c.getName().equals(name) && c.getId()==id ) return true;
            }

            return false;
        }

    }

    private void showDialog_1(String s)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server Return");
        builder.setMessage(s);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
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

    public void webpageClick(View view) { changeIntent(repair_request.class); }

    public void btnInfoClick(View view)    {changeIntent(SettingPage.class);    }

    private void changeIntent(Class _Intent)
    {
        if(_Intent.equals(repair_request.class) || _Intent.equals(ManagementWeb.class))
        {
            if(CheckProductKey.APPMode.equals("V"))
            {
                Intent intent = new Intent();
                intent.setClass(CreateRepairRequest.this, repair_request.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent();
                intent.setClass(CreateRepairRequest.this, ManagementWeb.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(CreateRepairRequest.this, _Intent);
            startActivity(intent);
            finish();
        }
    }
}



