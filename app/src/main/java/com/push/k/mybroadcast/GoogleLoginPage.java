package com.push.k.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.content.IntentSender;

import com.androidquery.AQuery;
import com.push.k.myWebService.CallWebService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.parse.ParseInstallation;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GoogleLoginPage extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static String rslt="";                 //test
    public static String registered_rslt="";        //註冊狀況
    public static String register_rslt="";          //註冊

    public static String NAME;
    public static String EMAIL;
    public static String GENDER;
    public static String PLUSID;

    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;

    private AQuery mAQuery;

    private TextView mContentTv;
    private ImageView mPhotoIv;
    private SignInButton mSignInButton;
    private Button confirmButton;

    private BingoReceiver receiver;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContentTv = (TextView)findViewById(R.id.contentTv);
        mPhotoIv = (ImageView)findViewById(R.id.photoIv);
        mSignInButton = (SignInButton)findViewById(R.id.signInBtn);
        mSignInButton.setOnClickListener(this);
        confirmButton = (Button)findViewById(R.id.confirmButton);
        mAQuery = new AQuery(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();


        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        //Get User Infos
        String objectId = ParseInstallation.getCurrentInstallation().getObjectId();
        String GCMSenderId = "";
        String appIdentifier = "com.push.k.mybroadcast";
        String appName = "mybroadcast";
        String appVersion = "1.0";
        String badge = "";
        String channels = "[\"brocast\"]";
        String deviceToken = "";
        String deviceTokenLastModified = "";
        String DeviceType = "android";
        String installationId = ParseInstallation.getCurrentInstallation().getInstallationId();
        String localeIdentifier = "";
        String parseVersion = "1.9.2";
        String pushType = "gcm";
        String timeZone = "Asia/Taipei";
        String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String updateAt = createAt;
        String ACL = "";

        String DeviceID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        //CheckRegistered
        checkRegistered(objectId);
    }

    class BingoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                num = (json.getString("alert"));
                //TextView txt = (TextView)findViewById(R.id.showtext);
                //txt.setText("收到訊息:"+num);

            } catch (Exception e) {

            }
        }
    }

    public boolean checkRegistered(String objectId) {
        boolean exist = false;
        registered_rslt = "START";
        try {
            CallWebService c = new CallWebService();
            c.objectId = objectId;
            c.FuntionType = "checkExist";
            c.join();
            c.start();
            while (registered_rslt == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            if (registered_rslt.equalsIgnoreCase("true") || registered_rslt.equalsIgnoreCase("false")) {
                if(registered_rslt.equalsIgnoreCase("true"))
                {
                    changeToListPage();
                }

            } else
                showDialog_1(registered_rslt);


        } catch (InterruptedException e) {
            showDialog_1(e.toString());
        }


        return exist;
    }

    private void showDialog_1()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("參數錯誤");
        builder.setMessage("未輸入內容");

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
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
    private void showDialog_2(String s)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("伺服器回傳");
        builder.setMessage(s);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                changeToListPage();
            }
        });

        builder.create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signInBtn:
                mGoogleApiClient.connect();
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int gender = currentPerson.getGender();
                String plusId = currentPerson.getId();
                String personPhotoUrl = currentPerson.getImage().getUrl();

                String content = "姓名:"+personName+"\n";
                content += "Email:"+personEmail+"\n";
                //content += "Gender:"+gender+"\n";
                //content += "PlusId:"+plusId;

                NAME = personName;
                EMAIL=personEmail;
                GENDER = String.valueOf(gender);
                PLUSID = plusId;

                mContentTv.setText(content);
                mAQuery.id(mPhotoIv).image(personPhotoUrl, true, true, 0, android.R.drawable.ic_menu_gallery);



                confirmButton.setVisibility(View.VISIBLE);
                //mSignInButton.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void changeToListPage()
    {
        Intent intent = new Intent();
        intent.setClass(GoogleLoginPage.this,MainActivity3.class);
        startActivity(intent);
        finish();
    }

    public void confirmLogin(View view)
    {
            try
            {
                    //Get User Infos
                    String objectId = ParseInstallation.getCurrentInstallation().getObjectId();
                    String GCMSenderId = "";
                    String appIdentifier = "com.push.k.mybroadcast";
                    String appName = "mybroadcast";
                    String appVersion = "1.0";
                    String badge = "";
                    String channels = "[\"brocast\"]";
                    String deviceToken = "";
                    String deviceTokenLastModified = "";
                    String DeviceType = "android";
                    String installationId = ParseInstallation.getCurrentInstallation().getInstallationId();
                    String localeIdentifier = "";
                    String parseVersion = "1.9.2";
                    String pushType = "gcm";
                    String timeZone = "Asia/Taipei";
                    String createAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String updateAt = createAt;
                    String ACL = "";

                    String DeviceID = Settings.Secure.getString(this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    EditText ed1=(EditText)findViewById(R.id.register_name);
                    register_rslt="START";
                    CallWebService c=new CallWebService();
                    c.FuntionType = "register";
                    c.objectId = objectId;
                    c.GCMSenderId = GCMSenderId;
                    c.appIdentifier = appIdentifier;
                    c.appName = appName;
                    c.appVersion = appVersion;
                    c.badge = badge;
                    c.channels = channels;
                    c.deviceToken = deviceToken;
                    c.deviceTokenLastModified = deviceTokenLastModified;
                    c.DeviceType = DeviceType;
                    c.installationId = installationId;
                    c.localeIdentifier = localeIdentifier;
                    c.parseVersion = parseVersion;
                    c.pushType = pushType;
                    c.timeZone = timeZone;
                    c.createAt = createAt;
                    c.updateAt = updateAt;
                    c.ACL = ACL;
                    c.DeviceID = DeviceID;
                    c.Name = NAME;
                    c.Email = EMAIL;
                    c.Gender = String.valueOf(GENDER);
                    c.PlusId = PLUSID;

                    c.join(); c.start();
                    while(register_rslt=="START") {
                        try {
                            Thread.sleep(100);
                        }catch(Exception ex) {
                        }
                    }
                    if (register_rslt.equalsIgnoreCase("true") || register_rslt.equalsIgnoreCase("false")) {
                        if(register_rslt.equalsIgnoreCase("true"))
                        {
                            showDialog_1("註冊成功!");
                            changeToListPage();
                        }
                        else
                        {
                            showDialog_1("註冊失敗，請稍後重試");
                        }

                    } else
                        showDialog_1(register_rslt);

        }catch(Exception ex) {
                showDialog_1(ex.toString());
            }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            connectionResult.startResolutionForResult(this, RC_SIGN_IN);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

}
