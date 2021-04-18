package com.architech.architech.customer;

import android.app.Application;

import com.onesignal.OneSignal;

public class Notification extends Application {
    private static final String ONESIGNAL_APP_ID = "bbcc3a17-6447-447d-8718-0a485dd91063";

    @Override
    public void onCreate() {
        super.onCreate();

        // Uncomment to enable verbose OneSignal logging to debug issues if needed.
        // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}