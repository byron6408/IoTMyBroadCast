package com.push.k.myWebService;

import com.push.k.mybroadcast.AccountManagement;
import com.push.k.mybroadcast.CheckProductKey;
import com.push.k.mybroadcast.CreateRepairRequest;
import com.push.k.mybroadcast.DeviceDetail;
import com.push.k.mybroadcast.DeviceList;
import com.push.k.mybroadcast.FindBackAccount;
import com.push.k.mybroadcast.FirstPage;
import com.push.k.mybroadcast.MainActivity;
import com.push.k.mybroadcast.MainActivity3;
import com.push.k.mybroadcast.MessageDetails;
import com.push.k.mybroadcast.RepairRequestDetails;
import com.push.k.mybroadcast.UserGWSelect;
import com.push.k.mybroadcast.WebSelectPage;
import com.push.k.mybroadcast.repair_request;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by K on 2015/11/17.
 */
public class CallWebService extends Thread   {
    public String SOAP_ACTION = "http://tempuri.org/HelloWorld";

    public  String OPERATION_NAME = "HelloWorld";

    public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

//    public  final String SOAP_ADDRESS = "http://192.168.1.110:8008/Service1.asmx";
    public  final String SOAP_ADDRESS = "https://www.iotpush.com.tw//Service1.asmx";

    public CallWebService cs;
    public String FuntionType;
    public String text;

    //register parameter
    public String objectId;
    public String GCMSenderId;
    public String appIdentifier;
    public String appName;
    public String appVersion;
    public String badge;
    public String channels;
    public String deviceToken;
    public String deviceTokenLastModified;
    public String DeviceType;
    public String installationId;
    public String localeIdentifier;
    public String parseVersion;
    public String pushType;
    public String timeZone;
    public String createAt;
    public String updateAt;
    public String ACL;
    public String DeviceID;
    public String CH_ID;
    public String CH_TYPE;
    public String CH_INDEX;
    public String ID;
    public String Name;
    public String Email;
    public String Phone;
    public String Gender;
    public String PlusId;
    public String TableName;
    public String CellName;
    public String CellValue;
    public String WhereString;
    public String AppVersion;
    public String ProductKey;
    public String GCMToken;
    public String RepairRequestNO;

    public String RepairRequestDev_ID;
    public String RepairRequestStartTime;
    public String RepairRequestEndTime;

    public void run() {

        cs = new CallWebService();

        if (FuntionType.equals("test")) {

            try {
                String resp = cs.Call(text);
                MainActivity.rslt = resp;
            } catch (Exception ex) {
                MainActivity.rslt = ex.toString();
            }
        } else if (FuntionType.equals("checkExist")) {
            try {
                String exist = "";
                cs = new CallWebService();
                exist = cs.checkExist(objectId, WebSelectPage.selectedDB);
                MainActivity.registered_rslt = String.valueOf(exist);
            } catch (Exception ex) {
                MainActivity.registered_rslt = ex.toString();
            }
        } else if (FuntionType.equals("register")) {
            try {
                String exist = "";
                cs = new CallWebService();
                exist = cs.register(objectId, GCMSenderId, appIdentifier, appName, appVersion, badge, channels, deviceToken, deviceTokenLastModified, DeviceType, installationId, localeIdentifier, parseVersion, pushType, timeZone, createAt, updateAt, ACL, DeviceID, ID, Name, Email, Phone, Gender, PlusId,WebSelectPage.selectedDB,GCMToken);
                MainActivity.register_rslt = String.valueOf(exist);
            } catch (Exception ex) {
                MainActivity.register_rslt = ex.toString();
            }
        } else if (FuntionType.equals("getNotificationList")) {
            try {
                cs = new CallWebService();
                String datas[] = cs.getNoticeficationList(objectId,WebSelectPage.selectedDB);

                if (datas != null) {
                    String datatype = "";
                    datatype = datas[0].split(":")[0];
                    datatype = datatype.replace("[@", "");
                    datatype = datatype.replace("]", "");
                    if (datatype.equalsIgnoreCase("excption")) {
                        MainActivity3.notificationList_rslt = datas[0];
                    } else if (datatype.equalsIgnoreCase("datas")) {
                        MainActivity3.notificationlist = datas;
                        MainActivity3.notificationList_rslt = "success";
                    }
                } else {
                    MainActivity3.notificationList_rslt = "查無資料";
                }

            } catch (Exception ex) {
                MainActivity3.notificationList_rslt = ex.toString();
            }
        } else if (FuntionType.equals("getDeviceList")) {
            try {
                cs = new CallWebService();
                String datas[] = cs.getDeviceList();

                if (datas != null) {
                    String datatype = "";
                    datatype = datas[0].split(":")[0];
                    datatype = datatype.replace("[@", "");
                    datatype = datatype.replace("]", "");
                    if (datatype.equalsIgnoreCase("excption")) {
                        DeviceList.getDeviceList_rslt = datas[0];
                    } else if (datatype.equalsIgnoreCase("datas")) {
                        DeviceList.deviceList = datas;
                        DeviceList.getDeviceList_rslt = "success";
                    }
                } else {
                    DeviceList.getDeviceList_rslt = "查無資料";
                }

            } catch (Exception ex) {
                DeviceList.getDeviceList_rslt = ex.toString();
            }
        } else if (FuntionType.equals("UpdateCommand")) {
            try {
                String exist = "";
                cs = new CallWebService();
                exist = cs.excuteUpdateCommand(TableName, CellName, CellValue, WhereString);
                DeviceDetail.updateComm_rslt = String.valueOf(exist);
            } catch (Exception ex) {
                DeviceDetail.updateComm_rslt = ex.toString();
            }
        } else if (FuntionType.equals("findbackaccount")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                rslt = cs.findBackAccount(Email, objectId,WebSelectPage.selectedDB);
                FindBackAccount.findbackaccount_rslt = String.valueOf(rslt);
            } catch (Exception ex) {
                FindBackAccount.findbackaccount_rslt = ex.toString();
            }
        }

        else if (FuntionType.equals("getInsPage")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                rslt = cs.getInsPage(DeviceID, CH_ID,WebSelectPage.selectedDB,WebSelectPage.selectedDB);
                MessageDetails.DetailPageRslt = String.valueOf(rslt);
            } catch (Exception ex) {
                MessageDetails.DetailPageRslt = ex.toString();
            }
        }

        else if (FuntionType.equals("getChName")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                rslt = cs.getChName(DeviceID, CH_TYPE, CH_INDEX, WebSelectPage.selectedDB);
                MainActivity3.channelName_rslt = String.valueOf(rslt);
            } catch (Exception ex) {
                MainActivity3.channelName_rslt = ex.toString();
            }
        }

        else if (FuntionType.equals("getChName2")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                rslt = cs.getChName(DeviceID, CH_TYPE, CH_INDEX, WebSelectPage.selectedDB);
                MessageDetails.channelName_rslt = String.valueOf(rslt);
            } catch (Exception ex) {
                MessageDetails.channelName_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("getRepairList")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                String datas[]  = cs.getRepairRequestList(objectId, WebSelectPage.selectedDB);


                if (datas != null) {
                    String datatype = "";
                    datatype = datas[0].split(":")[0];
                    datatype = datatype.replace("[@", "");
                    datatype = datatype.replace("]", "");
                    if (datatype.equalsIgnoreCase("excption")) {
                        repair_request.repairList_rslt = datas[0];
                    } else if (datatype.equalsIgnoreCase("datas")) {
                        repair_request.repairlist = datas;
                        repair_request.repairList_rslt = "success";
                    }
                } else {
                    repair_request.repairList_rslt = "Null Data";
                }

            } catch (Exception ex) {
                repair_request.repairList_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("getRepairDeviceList")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                String datas[]  = cs.getRepairDeviceList(objectId, WebSelectPage.selectedDB);

                if (datas != null) {
                    String datatype = "";
                    datatype = datas[0].split(":")[0];
                    datatype = datatype.replace("[@", "");
                    datatype = datatype.replace("]", "");
                    if (datatype.equalsIgnoreCase("excption")) {
                        CreateRepairRequest.deviceList_rslt = datas[0];
                    } else if (datatype.equalsIgnoreCase("datas")) {
                        CreateRepairRequest.device_list = datas;
                        CreateRepairRequest.deviceList_rslt = "success";
                    }
                } else {
                    CreateRepairRequest.deviceList_rslt = "Null Data";
                }

            } catch (Exception ex) {
                CreateRepairRequest.deviceList_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("delRepairRequest")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                rslt = cs.delRepairRequest(RepairRequestNO, WebSelectPage.selectedDB);
                RepairRequestDetails.repairRequestDetails_rslt = String.valueOf(rslt);
            } catch (Exception ex) {
                RepairRequestDetails.repairRequestDetails_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("createRepairRequest")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                rslt = cs.createRepairRequest(RepairRequestDev_ID, RepairRequestStartTime, RepairRequestEndTime, objectId, WebSelectPage.selectedDB);
                CreateRepairRequest.create_rslt = String.valueOf(rslt);
            } catch (Exception ex) {
                CreateRepairRequest.create_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("getUserGatewayList")) {
            try {
                String rslt = "";
                cs = new CallWebService();
                String datas[]  = cs.getUserGatewayList(objectId);

                if (datas != null) {
                    String datatype = "";
                    datatype = datas[0].split(":")[0];
                    datatype = datatype.replace("[@", "");
                    datatype = datatype.replace("]", "");
                    if (datatype.equalsIgnoreCase("excption")) {
                        UserGWSelect.WebList_rslt = datas[0];
                    } else if (datatype.equalsIgnoreCase("datas")) {
                        UserGWSelect.weblist = datas;
                        UserGWSelect.WebList_rslt = "success";
                    }
                } else {
                    UserGWSelect.WebList_rslt = "Null Data";
                }

            } catch (Exception ex) {
                UserGWSelect.WebList_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("getWebList")) {
            try {
                cs = new CallWebService();
                String datas[] = cs.getWebList();

                if (datas != null) {
                    String datatype = "";
                    datatype = datas[0].split(":")[0];
                    datatype = datatype.replace("[@", "");
                    datatype = datatype.replace("]", "");
                    if (datatype.equalsIgnoreCase("excption")) {
                        WebSelectPage.WebList_rslt = datas[0];
                    } else if (datatype.equalsIgnoreCase("datas")) {
                        WebSelectPage.weblist = datas;
                        WebSelectPage.WebList_rslt = "success";
                    }
                } else {
                    WebSelectPage.WebList_rslt = "查無資料";
                }

            } catch (Exception ex) {
                WebSelectPage.WebList_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("checkAppVersion")) {
            try {
                String exist = "";
                cs = new CallWebService();
                exist = cs.checkAppVersion(AppVersion, WebSelectPage.selectedDB);
                FirstPage.appVersion_rslt = String.valueOf(exist);
            } catch (Exception ex) {
                FirstPage.appVersion_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("chechAbleToRegister")) {
            try {
                String exist = "";
                cs = new CallWebService();
                exist = cs.checkAbleToRegister(WebSelectPage.selectedDB);
                MainActivity.abletoregister_rslt = String.valueOf(exist);
            } catch (Exception ex) {
                MainActivity.abletoregister_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("checkProductKey")) {
            try {
                String exist = "false";
                cs = new CallWebService();
                exist = cs.checkPruductKey(ProductKey);
                CheckProductKey.checkProductKey_rslt = exist;
            } catch (Exception ex) {
                CheckProductKey.checkProductKey_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("checkProductKey_new")) {
            try {
                String exist = "false";
                cs = new CallWebService();
                exist = cs.checkPruductKey_new(ProductKey);
                CheckProductKey.checkProductKey_rslt = exist;
            } catch (Exception ex) {
                CheckProductKey.checkProductKey_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("Login")) {
            try {
                String exist = "false";
                cs = new CallWebService();
                exist = cs.Login(WebSelectPage.selectedDB,Name,Email,GCMToken);MainActivity.register_rslt = exist;
            } catch (Exception ex) {
                MainActivity.register_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("Login2")) {
            try {
                String exist = "false";
                cs = new CallWebService();
                exist = cs.Login(WebSelectPage.selectedDB,Name,Email,GCMToken);
                AccountManagement.logon_rslt = exist;
            } catch (Exception ex) {
                AccountManagement.logon_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("checkPruductKey2_new")) {
            try {
                String exist = "false";
                cs = new CallWebService();
                exist = cs.checkPruductKey2_new(ProductKey);
                CheckProductKey.checkProductKey_rslt = exist;
            } catch (Exception ex) {
                CheckProductKey.checkProductKey_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("checkExist_new")) {
            try {
                String exist = "";
                cs = new CallWebService();
                exist = cs.checkExist_new(objectId, WebSelectPage.selectedDB);
                MainActivity.registered_rslt = String.valueOf(exist);
            } catch (Exception ex) {
                MainActivity.registered_rslt = ex.toString();
            }
        }
        else if (FuntionType.equals("CheckCustomerType")) {
            try {
                String exist = "false";
                cs = new CallWebService();
                exist = cs.CheckCustomerType(ProductKey);
                CheckProductKey.checkCustomerType_rslt = exist;
            } catch (Exception ex) {
                CheckProductKey.checkCustomerType_rslt = ex.toString();
            }
        }
    }

    public String CheckCustomerType(String SN)
    {
        SOAP_ACTION="http://tempuri.org/CheckCustomerType";
        OPERATION_NAME="CheckCustomerType";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("SN");
        t.setValue(SN);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response="false";
        }
        return response;
    }

    public  String delRepairRequest(String NO,String connString)
    {
        SOAP_ACTION="http://tempuri.org/delRepairRequest";
        OPERATION_NAME="delRepairRequest";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("NO");
        t.setValue(NO);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connectionString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response;
    }

    public  String createRepairRequest(String Dev_ID,String StartTime,String EndTime,String objectId,String connString)
    {
        SOAP_ACTION="http://tempuri.org/createRepairRequest";
        OPERATION_NAME="createRepairRequest";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("DEV_ID");
        t.setValue(Dev_ID);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("StartTime");
        t.setValue(StartTime);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("EndTime");
        t.setValue(EndTime);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connectionString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response;
    }

    public String checkPruductKey(String DBName)
    {
        SOAP_ACTION="http://tempuri.org/checkProductDB";
        OPERATION_NAME="checkProductDB";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("DBName");
        t.setValue(DBName);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response="false";
        }
        return response;
    }

    public String checkPruductKey_new(String DBName)
    {
        SOAP_ACTION="http://tempuri.org/checkProductDB_new";
        OPERATION_NAME="checkProductDB_new";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("DBName");
        t.setValue(DBName);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response="false";
        }
        return response;
    }

    public String checkPruductKey2_new(String DBName)
    {
        SOAP_ACTION="http://tempuri.org/checkProductDB_new";
        OPERATION_NAME="checkProductDB_new";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("DBName");
        t.setValue(DBName);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response="false";
        }
        return response;
    }

    public  String checkAbleToRegister(String connString)
    {
        SOAP_ACTION="http://tempuri.org/chechAbleToRegister";
        OPERATION_NAME="chechAbleToRegister";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("connStr");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String checkAppVersion(String appVersion,String connString)
    {

        SOAP_ACTION="http://tempuri.org/checkAppVersion";
        OPERATION_NAME="checkAppVersion";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("nowVersion");
        t.setValue(appVersion);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connStr");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public  String[] getWebList()
    {
        SOAP_ACTION="http://tempuri.org/getWebList";
        OPERATION_NAME="getWebList";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object[] response=new Object[1];
        String[] strings = new String[1];
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response[0] = envelope.getResponse();

            ArrayList<String> arraylist = new ArrayList<String>();
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            SoapObject obj2 =(SoapObject) obj1.getProperty(0);

            strings = new String[obj2.getPropertyCount()];
            for (int i = 0; i< obj2.getPropertyCount(); i++)
            {
                // int id1 = Integer.parseInt(obj2.getProperty(0).toString());
                strings[i] = obj2.getProperty(i).toString().replace("string : ","");
            }
        }
        catch (Exception exception)
        {
            String exc[]=new String[1];
            exc[0] = exception.toString();
            response[0]=exc;
            strings = exc;
        }
        return strings;
    }

    public String[] getNoticeficationList(String objectId,String connString)
    {
        SOAP_ACTION="http://tempuri.org/getNoticeData_Target";
        OPERATION_NAME="getNoticeData_Target";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object[] response=new Object[1];
        String[] strings = new String[1];
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response[0] = envelope.getResponse();

            ArrayList<String> arraylist = new ArrayList<String>();
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            SoapObject obj2 =(SoapObject) obj1.getProperty(0);

            strings = new String[obj2.getPropertyCount()];
            for (int i = 0; i< obj2.getPropertyCount(); i++)
            {
                // int id1 = Integer.parseInt(obj2.getProperty(0).toString());
                strings[i] = obj2.getProperty(i).toString().replace("string : ","");
            }
        }
        catch (Exception exception)
        {
            String exc[]=new String[1];
            exc[0] = exception.toString();
            response[0]=exc;
            strings = exc;
        }

        return strings;
    }

    public String[] getRepairRequestList(String objectId,String connString)
    {
        SOAP_ACTION="http://tempuri.org/getRepairList";
        OPERATION_NAME="getRepairList";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connectionString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object[] response=new Object[1];
        String[] strings = new String[1];
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response[0] = envelope.getResponse();

            ArrayList<String> arraylist = new ArrayList<String>();
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            SoapObject obj2 =(SoapObject) obj1.getProperty(0);

            strings = new String[obj2.getPropertyCount()];
            for (int i = 0; i< obj2.getPropertyCount(); i++)
            {
                // int id1 = Integer.parseInt(obj2.getProperty(0).toString());
                strings[i] = obj2.getProperty(i).toString().replace("string : ","");
            }
        }
        catch (Exception exception)
        {
            String exc[]=new String[1];
            exc[0] = exception.toString();
            response[0]=exc;
            strings = exc;
        }

        return strings;
    }

    public String[] getRepairDeviceList(String objectId,String connString)
    {
        SOAP_ACTION="http://tempuri.org/getRepairDeviceList";
        OPERATION_NAME="getRepairDeviceList";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connectionString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object[] response=new Object[1];
        String[] strings = new String[1];
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response[0] = envelope.getResponse();

            ArrayList<String> arraylist = new ArrayList<String>();
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            SoapObject obj2 =(SoapObject) obj1.getProperty(0);

            strings = new String[obj2.getPropertyCount()];
            for (int i = 0; i< obj2.getPropertyCount(); i++)
            {
                // int id1 = Integer.parseInt(obj2.getProperty(0).toString());
                strings[i] = obj2.getProperty(i).toString().replace("string : ","");
            }
        }
        catch (Exception exception)
        {
            String exc[]=new String[1];
            exc[0] = exception.toString();
            response[0]=exc;
            strings = exc;
        }

        return strings;
    }

    public String[] getUserGatewayList(String objectId)
    {
        SOAP_ACTION="http://tempuri.org/getUserGatewayList";
        OPERATION_NAME="getUserGatewayList";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object[] response=new Object[1];
        String[] strings = new String[1];
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response[0] = envelope.getResponse();

            ArrayList<String> arraylist = new ArrayList<String>();
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            SoapObject obj2 =(SoapObject) obj1.getProperty(0);

            strings = new String[obj2.getPropertyCount()];
            for (int i = 0; i< obj2.getPropertyCount(); i++)
            {
                // int id1 = Integer.parseInt(obj2.getProperty(0).toString());
                strings[i] = obj2.getProperty(i).toString().replace("string : ","");
            }
        }
        catch (Exception exception)
        {
            String exc[]=new String[1];
            exc[0] = exception.toString();
            response[0]=exc;
            strings = exc;
        }

        return strings;
    }

    public String[] getDeviceList()
    {

        SOAP_ACTION="http://tempuri.org/getDeviceList";
        OPERATION_NAME="getDeviceList";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object[] response=new Object[1];
        String[] strings = new String[1];
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response[0] = envelope.getResponse();

            ArrayList<String> arraylist = new ArrayList<String>();
            SoapObject obj1 = (SoapObject) envelope.bodyIn;

            SoapObject obj2 =(SoapObject) obj1.getProperty(0);

            strings = new String[obj2.getPropertyCount()];
            for (int i = 0; i< obj2.getPropertyCount(); i++)
            {
                // int id1 = Integer.parseInt(obj2.getProperty(0).toString());
                strings[i] = obj2.getProperty(i).toString().replace("string : ","");
            }
        }
        catch (Exception exception)
        {
            String exc[]=new String[1];
            exc[0] = exception.toString();
            response[0]=exc;
            strings = exc;
        }

        return strings;
    }

    public  String findBackAccount(String Email,String objectId,String connString)
    {
        SOAP_ACTION="http://tempuri.org/findBackAccount";
        OPERATION_NAME="findBackAccount";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();

        t=new PropertyInfo();
        t.setName("Email");
        t.setValue(Email);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String register(String objectId,String GCMSenderId,String appIdentifier,String appName,String appVersion,String badge,String channels,String deviceToken,String deviceTokenLastModified,String deviceType,String installationId,String localeIdentifier,String parseVersion,String pushType,String timeZone,String createAt,String updateAt,String ACL,String deviceID,String ID,String Name,String Email,String Phone,String Gender,String PlusId,String connString,String GCMToken)
    {
        SOAP_ACTION="http://tempuri.org/register";
        OPERATION_NAME="register";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("GCMSenderId");
        t.setValue(GCMSenderId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("appIdentifier");
        t.setValue(appIdentifier);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("appName");
        t.setValue(appName);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("appVersion");
        t.setValue(appVersion);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("badge");
        t.setValue(badge);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("channels");
        t.setValue(channels);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("deviceToken");
        t.setValue(deviceToken);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("deviceTokenLastModified");
        t.setValue(deviceTokenLastModified);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("DeviceType");
        t.setValue(DeviceType);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("installationId");
        t.setValue(installationId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("localeIdentifier");
        t.setValue(localeIdentifier);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("parseVersion");
        t.setValue(parseVersion);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("pushType");
        t.setValue(pushType);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("timeZone");
        t.setValue(timeZone);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("createAt");
        t.setValue(createAt);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("updateAt");
        t.setValue(updateAt);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("ACL");
        t.setValue(ACL);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("DeviceID");
        t.setValue(deviceID);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("ID");
        t.setValue("null");
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("Name");
        t.setValue(Name);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("Email");
        t.setValue(Email);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("Phone");
        t.setValue(Phone);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("Gender");
        t.setValue(Gender);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("PlusId");
        t.setValue(PlusId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("GCMToken");
        t.setValue(GCMToken);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String checkExist(String objectId,String connString)
    {
        SOAP_ACTION="http://tempuri.org/checkUserRegistered";
        OPERATION_NAME="checkUserRegistered";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("objectId");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String Call(String text)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("s");
        t.setValue(text);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String excuteUpdateCommand(String TableName,String CellName, String CellValue,String WhereString )
    {
        SOAP_ACTION="http://tempuri.org/executeUpdateCommand";
        OPERATION_NAME="executeUpdateCommand";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("TableName");
        t.setValue(TableName);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("CellName");
        t.setValue(CellName);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("CellValue");
        t.setValue(CellValue);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("WhereString");
        t.setValue(WhereString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String getChName(String deviceId,String chType,String chIndex,String connString)
    {
        String URL ="";
        SOAP_ACTION="http://tempuri.org/getChName";
        OPERATION_NAME="getChName";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("deviceId");
        t.setValue(deviceId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("chType");
        t.setValue(chType);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("chIndex");
        t.setValue(chIndex);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }

        return response.toString();
    }

    public String getInsPage(String deviceId,String chid,String dbname,String connString)
    {
        String URL ="";
        SOAP_ACTION="http://tempuri.org/getInsPage";
        OPERATION_NAME="getInsPage";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("deviceId");
        t.setValue(deviceId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("chid");
        t.setValue(chid);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("dbname");
        t.setValue(dbname);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("connString");
        t.setValue(connString);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }

        return response.toString();
    }

    public  String Login(String SN, String ac,String pw,String GCMToken)
    {
        SOAP_ACTION="http://tempuri.org/Login";
        OPERATION_NAME="Login";

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("SN");
        t.setValue(SN);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("AC");
        t.setValue(ac);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("PW");
        t.setValue(pw);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("token");
        t.setValue(GCMToken);
        t.setType(Integer.class);
        request.addProperty(t);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response="false";
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response;
    }

    public String checkExist_new(String objectId,String dbname)
    {
        SOAP_ACTION="http://tempuri.org/checkUserRegistered_new";
        OPERATION_NAME="checkUserRegistered_new";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        //Properties(example:http://www.codeproject.com/Articles/304302/Calling-Asp-Net-Webservice-ASMX-From-an-Android-Ap)
        PropertyInfo t=new PropertyInfo();
        t.setName("Account");
        t.setValue(objectId);
        t.setType(Integer.class);
        request.addProperty(t);

        t=new PropertyInfo();
        t.setName("DBName");
        t.setValue(dbname);
        t.setType(Integer.class);
        request.addProperty(t);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
}
