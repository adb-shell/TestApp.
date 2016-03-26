package com.karthik.imager;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.karthik.imager.Injection.Components.ApplicationComponent;
import com.karthik.imager.Injection.Components.DaggerApplicationComponent;
import com.karthik.imager.Injection.Modules.ApplicationModule;

import android.app.Application;
import android.content.Context;
import okhttp3.OkHttpClient;

/**
 * Created by karthikrk on 15/02/16.
 */
public class ImagerApp extends Application {
    private Context mContext;
    ApplicationComponent mApplicationComponent;


    public void onCreate() {
        super.onCreate();
        mContext = this;
        //basic stetho intialization
        Stetho.initialize(
                Stetho.newInitializerBuilder(mContext)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(mContext))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(mContext))
                        .build());

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }


    public static ImagerApp get(Context context) {
        return (ImagerApp) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
