package com.push.k.mybroadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.push.k.myWebService.APIs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ManagementWeb extends AppCompatActivity {

    private WebView view;
    private SslErrorHandler handler;
    private SslError error;
    public static String isAlarm = "0";

    //--
    private static final String TAG = ManagementWeb.class.getSimpleName();
    private Uri mCa = null;
    private Uri mCaV = null;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;

    String[] mPermission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //--
    APIs api = new APIs();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiver = new BingoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.parse.push.intent.RECEIVE");
        registerReceiver(receiver, filter);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();


        getConfigurationInfo();

        api.c = this;
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        myWebView.requestFocusFromTouch();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);

        //--20170320

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != MockPackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        myWebView.addJavascriptInterface(new JavaScriptInterface(ManagementWeb.this), "JSInterface");
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            CookieManager.getInstance().removeAllCookies(null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            CookieManager.getInstance().removeAllCookie();
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            CookieManager.getInstance().removeAllCookie();
        }
        myWebView.setWebViewClient(new Callback());
        //--



        myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

                callback.invoke(origin, true, false);

                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view,int newProgress){
                super.onProgressChanged(view, newProgress);if(newProgress >=100) {}
            }

            //--
            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                ManagementWeb.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                ManagementWeb.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUM = uploadMsg;
                mCa = null;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser");
                cameraDataDir.mkdirs();
                String mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                mCa = Uri.fromFile(new File(mCameraFilePath));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCa);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, i);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                ManagementWeb.this.startActivityForResult(chooserIntent, FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent takeVideoIntent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                if (takePictureIntent.resolveActivity(ManagementWeb.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntentVideo = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntentVideo.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntentVideo.setType("video/*");
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray = new Intent[1];

                if (takePictureIntent != null) {
                    intentArray[0] = takePictureIntent;
                } else {
                    intentArray[0] = null;
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);

                return true;
            }
            //--
        });

        myWebView.setWebViewClient(new MyWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                //handler.cancel(); //默认的处理方式，WebView变成空白页
//                handler.proceed();
//                // handleMessage(Message msg); //其他处理
//            }

            public void onPageFinished(WebView view, String url) {

                String s = ((WebView) findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView) findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length - 1)].replace(".aspx", "");
                if (((WebView) findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView) findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length - 1)].replace(".aspx", "").equalsIgnoreCase("Login")) {
                    String account = api.getName(WebSelectPage.selectedDB);
                    String password = api.getEmail(WebSelectPage.selectedDB);
                    view.loadUrl("javascript:mobileLogin('" + account + "','" + password + "','')");
                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d(TAG, "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                m1.invoke(webSettings, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                m2.invoke(webSettings, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(webSettings, "/data/data/" + getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(webSettings, 1024 * 1024 * 8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(webSettings, "/data/data/" + getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", new Class[]{Boolean.TYPE});
                m6.invoke(webSettings, Boolean.TRUE);

                Log.d(TAG, "Enabled HTML5-Features");
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Reflection fail", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "Reflection fail", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Reflection fail", e);
            }
        }

        myWebView.getSettings().setBuiltInZoomControls(true);
        webSettings.setGeolocationEnabled(true);
        String dbname = WebSelectPage.selectedDB.replace("DAIIO", "");
        //String s = "http://www.iotpush.com.tw/" + dbname + "/DeviceManagement/AllDeviceInstantStatus.aspx";
        // myWebView.loadUrl("http://www.iotpush.com.tw/"+dbname+"/DeviceManagement/AllDeviceInstantStatus.aspx");

        //myWebView.loadUrl("javascript: {document.getElementsById(\"login_i\")[0].value ='" + "ABC" + "';};");

        if (isAlarm.equals("0")) {
            myWebView.loadUrl("https://www.iotpush.com.tw/NewIotPush2/#/login");
            //myWebView.loadUrl("http://www.iotpush.com.tw/NewIotPush2/#/app/dashboard/index");
        } else if (isAlarm.equals("1")) {
            myWebView.loadUrl("https://www.iotpush.com.tw/NewIotPush2/#/app/devicemanagement/full-warning-log");
            isAlarm = "0";
        }
    }

    private void getConfigurationInfo(){
        Configuration configuration=getResources().getConfiguration();
        //获取屏幕方向
        int l=configuration.ORIENTATION_LANDSCAPE;
        int p=configuration.ORIENTATION_PORTRAIT;
        if (configuration.orientation==l) {
            //System.out.println("现在是横屏");

            LinearLayout bar = (LinearLayout)findViewById(R.id.footer);
            bar.setVisibility(View.GONE);

            WebView webview = (WebView)findViewById(R.id.webView);
            ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) webview.getLayoutParams();
            param.bottomMargin=0;

            webview.setLayoutParams(param);
        }
        if (configuration.orientation==p) {
            //System.out.println("现在是竖屏");

            LinearLayout bar = (LinearLayout)findViewById(R.id.footer);
            bar.setVisibility(View.VISIBLE);

            WebView webview = (WebView)findViewById(R.id.webView);
            ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) webview.getLayoutParams();
            param.bottomMargin=54;

            webview.setLayoutParams(param);
        }
    }

    public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }

        //--
        // Create an image file
        private File createImageFile() throws IOException {
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "img_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        if (mCM != null) {
                            String dataString = intent.getDataString();
                            if (dataString != null) {
                                results = new Uri[]{Uri.parse(dataString)};
                            } else {
                                results = new Uri[]{Uri.parse(mCM)};
                            }
                        } else {
                            String dataString = intent.getDataString();
                            if (dataString != null) {
                                results = new Uri[]{Uri.parse(dataString)};
                            }
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 3 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED) {

                // Success Stuff here

            }
        }

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getConfigurationInfo();
        } else {
            getConfigurationInfo();
        }
    }

        //--

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String s=((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length-1)].replace(".aspx","");
            if(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length-1)].replace(".aspx","").equalsIgnoreCase("index") || ((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length-1)].replace(".aspx","").equalsIgnoreCase("Login"))
            {
                //changeIntent(MainActivity3.class);
                show();
            }
            else
            {
                if(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length-1)].replace(".aspx","").equalsIgnoreCase("Login"))
                {
                    changeIntent(MainActivity3.class);
                }
                else {
                    if ((((WebView) findViewById(R.id.webView)).canGoBack())) {
                        ((WebView) findViewById(R.id.webView)).goBack();

                        if(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/")[(((WebView)findViewById(R.id.webView)).getUrl().split("\\\\s+|[?]")[0].split("/").length-1)].replace(".aspx","").equalsIgnoreCase("Login"))
                        {
                            changeIntent(MainActivity3.class);
                        }
                    }
                }
            }
        }
        return true;
    }

    public void show() {
        new AlertDialog.Builder(ManagementWeb.this)
                .setTitle(R.string.LeaveDiagTitle)
                .setMessage(R.string.LeaveDiagBody)
                .setPositiveButton(R.string.LeaveDiagConfirmText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ManagementWeb.this.finish();
                    }
                })
                .setNegativeButton(R.string.LeaveDiagCancelText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    private class MyWebViewClient extends WebViewClient
    { @Override public boolean shouldOverrideUrlLoading(WebView view, String url) { return super.shouldOverrideUrlLoading(view, url); } }

    private BingoReceiver receiver;
    class BingoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                String str = json.getString("alert");

                //彈出通知視窗
                CustomDialog.content = str;


                Intent i = new Intent(ManagementWeb.this, CustomDialog.class);
                startActivity(i);
            } catch (JSONException je) {

            } catch (Exception e) {
            }
        }
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
            //else if(item.getTitle().toString().equals("Repair"))
            //{
            //    changeIntent(repair_request.class);
            //}
            else if(item.getTitle().toString().equals("Settings"))
            {
                changeIntent(SettingPage.class);
            }
        }
        return  super.onOptionsItemSelected(item);
    }
    public void broadcastlistClick(View view) { changeIntent(MainActivity3.class); }

    //public void webpageClick(View view) { changeIntent(repair_request.class); }

    public void btnInfoClick(View view)    {changeIntent(SettingPage.class);    }

    private void changeIntent(Class _Intent)
    {
        if(_Intent.equals(repair_request.class) || _Intent.equals(ManagementWeb.class))
        {
            if(CheckProductKey.APPMode.equals("V"))
            {
                if(ManagementWeb.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(ManagementWeb.this, repair_request.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                if(ManagementWeb.class.equals(_Intent))
                    startActivity(getIntent());
                else {
                    Intent intent = new Intent();
                    intent.setClass(ManagementWeb.this, ManagementWeb.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            if(ManagementWeb.class.equals(_Intent))
                startActivity(getIntent());
            else {
                Intent intent = new Intent();
                intent.setClass(ManagementWeb.this, _Intent);
                startActivity(intent);
                finish();
            }
        }
    }
}
