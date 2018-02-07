package com.relinns.viegram.controller;

import android.support.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        new Instabug.Builder(this, "9b00be6a6cc935318ee33e5626204ae6")
//                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
//                .build();
    }
}
