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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.push.k.myWebService.CallWebService;

public class DeviceDetail extends AppCompatActivity {


    public static String DEV_ID ="";
    public static String DEV_NAME = "";
    public static String TYPE = "";
    public static String DEV_IP = "";
    public static String DEV_PORT = "";
    public static String AI_ONOFF = "";
    public static  String DI_ONOFF = "";
    public static String AO_ONOFF = "";
    public static String DO_ONOFF = "";
    public static String CH_ID = "";
    public static String CH_NAME = "";

    public static String Push_PUSH_TYPE = "";
    public static String Push_MAX = "";
    public static String Push_MIN = "";
    public static String Push_VALUE = "";
    public static String Push_AlarmTime = "";

    public static String updateComm_rslt="";

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        TextView tvno = (TextView)findViewById(R.id.DEV_ID);
        tvno.setText(DEV_ID);

        TextView tvremark = (TextView)findViewById(R.id.DEV_NAME);
        tvremark.setText(DEV_NAME);

        TextView tvtitle = (TextView)findViewById(R.id.TYPE);
        tvtitle.setText(TYPE);

        TextView tvbody = (TextView)findViewById(R.id.DEV_IP);
        tvbody.setText(DEV_IP);

        TextView tvdatetime = (TextView)findViewById(R.id.DEV_PORT);
        tvdatetime.setText(DEV_PORT);

        TextView tvchname = (TextView)findViewById(R.id.CH_NAME);
        tvchname.setText(CH_NAME);

        if(Push_PUSH_TYPE.equalsIgnoreCase("A"))
        {
            TextView htv = (TextView)findViewById(R.id.maxtemparature);
            htv.setText(Push_MAX);

            TextView ltv = (TextView)findViewById(R.id.mintemparature);
            ltv.setText(Push_MIN);

            TextView ttv = (TextView)findViewById(R.id.DuringTime);
            ttv.setText(Push_AlarmTime);

            // maxTempurature
            TextView htvl = (TextView)findViewById(R.id.maxtemparature_l);
            htvl.setVisibility(View.VISIBLE);
            htv.setVisibility(View.VISIBLE);

            //minTempurature
            TextView ltvl = (TextView)findViewById(R.id.mintemparature_l);
            ltvl.setVisibility(View.VISIBLE);
            ltv.setVisibility(View.VISIBLE);

            //DuringTime
            TextView ttvl = (TextView)findViewById(R.id.DuringTime_l);
            ttvl.setVisibility(View.VISIBLE);
            ttv.setVisibility(View.VISIBLE);

        }
        else if(Push_PUSH_TYPE.equalsIgnoreCase("C"))
        {
            TextView vtv = (TextView)findViewById(R.id.value);
            vtv.setText(Push_VALUE);

            TextView ttv = (TextView)findViewById(R.id.DuringTime);
            ttv.setText(Push_AlarmTime);

            //Value
            TextView vtvl = (TextView)findViewById(R.id.value_l);
            vtvl.setVisibility(View.VISIBLE);
            vtv.setVisibility(View.VISIBLE);

            //DuringTime
            TextView ttvl = (TextView)findViewById(R.id.DuringTime_l);
            ttvl.setVisibility(View.VISIBLE);
            ttv.setVisibility(View.VISIBLE);
        }

        if(AI_ONOFF.equalsIgnoreCase("Y"))
        {
            Switch s = (Switch)findViewById(R.id.AI_ONOFF);
            s.setChecked(true);
        }
        else if (AI_ONOFF.equalsIgnoreCase("N"))
        {
            Switch s = (Switch)findViewById(R.id.AI_ONOFF);
            s.setChecked(false);
        }

        if(DI_ONOFF.equalsIgnoreCase("Y"))
        {
            Switch s = (Switch)findViewById(R.id.DI_ONOFF);
            s.setChecked(true);
        }
        else if (DI_ONOFF.equalsIgnoreCase("N"))
        {
            Switch s = (Switch)findViewById(R.id.DI_ONOFF);
            s.setChecked(false);
        }

        if(AO_ONOFF.equalsIgnoreCase("Y"))
        {
            Switch s = (Switch)findViewById(R.id.AO_ONOFF);
            s.setChecked(true);
        }
        else if (AO_ONOFF.equalsIgnoreCase("N"))
        {
            Switch s = (Switch)findViewById(R.id.AO_ONOFF);
            s.setChecked(false);
        }

        if(DO_ONOFF.equalsIgnoreCase("Y"))
        {
            Switch s = (Switch)findViewById(R.id.DO_ONOFF);
            s.setChecked(true);
        }
        else if (DO_ONOFF.equalsIgnoreCase("N"))
        {
            Switch s = (Switch)findViewById(R.id.DO_ONOFF);
            s.setChecked(false);
        }

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
                changeToDeviceList();
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
        intent.setClass(DeviceDetail.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    private  void changeToInstantStatusPage()
    {
        Intent intent = new Intent();
        intent.setClass(DeviceDetail.this,InstantDeviceStatus.class);
        startActivity(intent);
        finish();
    }

    public  void changeToDeviceList(View view)
    {
        Intent intent = new Intent();
        intent.setClass(DeviceDetail.this,DeviceList.class);
        startActivity(intent);
        finish();
    }

    public  void changeToDeviceList()
    {
        Intent intent = new Intent();
        intent.setClass(DeviceDetail.this,DeviceList.class);
        startActivity(intent);
        finish();
    }


    public  void apply_setting_click(View view)
    {
        excuceUpdate();
    }

    private void excuceUpdate()
    {

        if(Push_PUSH_TYPE.equalsIgnoreCase("A"))
        {
            String MAXrslt="";
            String MINrslt="";
            String AlarmTimerslt="";

            TextView htv = (TextView)findViewById(R.id.maxtemparature);
            TextView ltv = (TextView)findViewById(R.id.mintemparature);
            TextView ttv = (TextView)findViewById(R.id.DuringTime);

            //DO MAX
            updateComm_rslt = "START";
            try {
                CallWebService c = new CallWebService();
                c.TableName = "PUSH_SET";
                c.CellName="MAX";
                c.CellValue=htv.getText().toString();
                c.WhereString="WHERE DEV_ID='"+DEV_ID+"' AND CH_TYPE='"+TYPE+"' AND CH_ID='"+CH_ID+"'";
                c.FuntionType = "UpdateCommand";
                c.join();
                c.start();
                while (updateComm_rslt == "START") {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }
                if(updateComm_rslt.equalsIgnoreCase("success")) {
                    MAXrslt = "success";
                }
                else {
                    MAXrslt = "MAX:"+updateComm_rslt;
                }

            }catch (Exception ex)
            {}

            //DO MIN
            updateComm_rslt = "START";
            try {
                CallWebService c = new CallWebService();
                c.TableName = "PUSH_SET";
                c.CellName = "MIN";
                c.CellValue = ltv.getText().toString();
                c.WhereString = "WHERE DEV_ID='" + DEV_ID + "' AND CH_TYPE='" + TYPE + "' AND CH_ID='" + CH_ID + "'";
                c.FuntionType = "UpdateCommand";
                c.join();
                c.start();
                while (updateComm_rslt == "START") {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }
                if (updateComm_rslt.equalsIgnoreCase("success")) {
                    MINrslt = "success";
                } else {
                    MINrslt = "MIN:" + updateComm_rslt;
                }

            }catch (Exception ex)
            {}

            //DO AlarmTime
            updateComm_rslt = "START";
            try {
                CallWebService c = new CallWebService();
                c.TableName = "PUSH_SET";
                c.CellName = "AlarmTime";
                c.CellValue = ttv.getText().toString();
                c.WhereString = "WHERE DEV_ID='" + DEV_ID + "' AND CH_TYPE='" + TYPE + "' AND CH_ID='" + CH_ID + "'";
                c.FuntionType = "UpdateCommand";
                c.join();
                c.start();
                while (updateComm_rslt == "START") {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }
                if (updateComm_rslt.equalsIgnoreCase("success")) {
                    AlarmTimerslt = "success";
                } else {
                    AlarmTimerslt = "AlarmTime:" + updateComm_rslt;
                }

            }catch (Exception ex)
            {}

            //Show rslt
            String rsltString ="";
            if(MAXrslt.equalsIgnoreCase("success") && MINrslt.equalsIgnoreCase("success") && AlarmTimerslt.equalsIgnoreCase("success"))
            {
                rsltString="變更成功";
            }
            else
            {
                if(!MAXrslt.equalsIgnoreCase("success"))
                    rsltString +=MAXrslt+"\n";
                if(!MINrslt.equalsIgnoreCase("success"))
                    rsltString +=MINrslt+"\n";
                if(!AlarmTimerslt.equalsIgnoreCase("success"))
                    rsltString +=AlarmTimerslt+"\n";
            }

            Toast.makeText(context, rsltString, Toast.LENGTH_SHORT).show();
        }
        else if(Push_PUSH_TYPE.equalsIgnoreCase("C"))
        {
            String Valuerslt="";
            String AlarmTimerslt="";

            TextView vtv = (TextView)findViewById(R.id.value);
            TextView ttv = (TextView)findViewById(R.id.DuringTime);

            //DO Value
            updateComm_rslt = "START";
            try {
                CallWebService c = new CallWebService();
                c.TableName = "PUSH_SET";
                c.CellName = "Value";
                c.CellValue = vtv.getText().toString();
                c.WhereString = "WHERE DEV_ID='" + DEV_ID + "' AND CH_TYPE='" + TYPE + "' AND CH_ID='" + CH_ID + "'";
                c.FuntionType = "UpdateCommand";
                c.join();
                c.start();
                while (updateComm_rslt == "START") {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }
                if (updateComm_rslt.equalsIgnoreCase("success")) {
                    Valuerslt = "success";
                } else {
                    Valuerslt = "Value:" + updateComm_rslt;
                }

            }catch (Exception ex)
            {}

            //DO AlarmTime
            updateComm_rslt = "START";
            try {
                CallWebService c = new CallWebService();
                c.TableName = "PUSH_SET";
                c.CellName = "AlarmTime";
                c.CellValue = ttv.getText().toString();
                c.WhereString = "WHERE DEV_ID='" + DEV_ID + "' AND CH_TYPE='" + TYPE + "' AND CH_ID='" + CH_ID + "'";
                c.FuntionType = "UpdateCommand";
                c.join();
                c.start();
                while (updateComm_rslt == "START") {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }
                if (updateComm_rslt.equalsIgnoreCase("success")) {
                    AlarmTimerslt = "success";
                } else {
                    AlarmTimerslt = "AlarmTime:" + updateComm_rslt;
                }

            }catch (Exception ex)
            {}

            //Show rslt
            String rsltString ="";
            if(Valuerslt.equalsIgnoreCase("success") && AlarmTimerslt.equalsIgnoreCase("success"))
            {
                rsltString="變更成功";
            }
            else
            {
                if(!Valuerslt.equalsIgnoreCase("success"))
                    rsltString +=Valuerslt+"\n";
                if(!AlarmTimerslt.equalsIgnoreCase("success"))
                    rsltString +=AlarmTimerslt+"\n";
            }
            Toast.makeText(context, rsltString, Toast.LENGTH_SHORT).show();
        }
    }

}
