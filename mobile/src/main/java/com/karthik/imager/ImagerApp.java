package com.karthik.imager;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import android.app.Application;
import android.content.Context;
import okhttp3.OkHttpClient;

/**
 * Created by karthikrk on 15/02/16.
 */
public class ImagerApp extends Application {
    private Context mContext;
    private OkHttpClient client;
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //basic stetho intialization
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    //using singleton pattern,so that stetho is enabled across the app.
    public OkHttpClient getOkHttpInstance(){
        if(client==null){
           client = new OkHttpClient.Builder()
                   .addNetworkInterceptor(new StethoInterceptor())
                   .build();
        }
        return client;
    }
}
