package com.karthik.imager;

import com.facebook.stetho.Stetho;

import android.app.Application;
import android.content.Context;

/**
 * Created by karthikrk on 15/02/16.
 */
public class ImagerApp extends Application {
    private Context mContext;
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
