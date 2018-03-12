package com.push.k.mybroadcast;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Byron-NB on 2017/3/20.
 */

public class JavaScriptInterface {
    private Activity activity;
    public JavaScriptInterface(Activity activiy) {
        this.activity = activiy;
    }
    @JavascriptInterface
    public void showToast(){
        Toast.makeText(activity, "我被從WebView呼叫了", Toast.LENGTH_SHORT).show();
    }
}
