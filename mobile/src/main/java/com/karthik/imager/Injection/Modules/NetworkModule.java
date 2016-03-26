package com.karthik.imager.Injection.Modules;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.karthik.imager.Injection.PerActivity;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by karthikrk on 26/03/16.
 */
@Module
public class NetworkModule {
    private Context mContext;

    public NetworkModule(Context mContext){
        this.mContext = mContext;
    }

    @Provides @PerActivity
    public OkHttpClient getOkhttp(){
        //adding stetho network interceptors
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
