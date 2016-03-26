package com.karthik.imager.Injection.Modules;

import javax.inject.Singleton;

import android.app.Application;
import dagger.Module;
import dagger.Provides;

/**
 * Created by karthikrk on 26/03/16.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }
}
