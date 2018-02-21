package com.immigration.controller.application;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.immigration.utils.ConnectivityReceiver;

import io.fabric.sdk.android.Fabric;

public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Fabric.with(this, new Crashlytics());


    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.Companion.setConnectivityReceiverListener(listener);
    }
}