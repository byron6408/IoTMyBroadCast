package com.push.k.mybroadcast;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;


/**
 * Created by K on 2015/11/16.
 */
public class ParseBrocast extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this,
                "ABcXhb35mcSPTyEr8CjtDQBV0anh8FT7sGv9QsQp",
                "vdZ0ZLExodvKetriRUa8d4n49rbjMNtJ7wEiYSAl");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("bingo");
    }


}


