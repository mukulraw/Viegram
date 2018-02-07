package com.relinns.viegram.uploadservice.events;

/*
 * Created by admin on 10-01-2018.
 */

import android.app.Activity;

import com.relinns.viegram.Activity.Timeline;

import java.lang.ref.WeakReference;

public class WeakInstanceClass {

    private static WeakInstanceClass mInstance = null;
    private WeakReference<Activity> mWeakReference;

    private WeakInstanceClass(){}

    public static WeakInstanceClass getInstance(){
        if (mInstance == null)
            mInstance = new WeakInstanceClass();
        return mInstance;
    }

    public void updateReference(Activity  mActivity){
        mWeakReference = new WeakReference<>(mActivity);
    }

    public WeakReference<Activity> getReference(){
        return mWeakReference;
    }
}
