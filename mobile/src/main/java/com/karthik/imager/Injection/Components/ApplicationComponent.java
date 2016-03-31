package com.karthik.imager.Injection.Components;

import com.karthik.imager.Injection.Modules.ApplicationModule;
import com.karthik.imager.MainActivity;

import javax.inject.Singleton;

import android.app.Application;
import dagger.Component;

/**
 * Created by karthikrk on 26/03/16.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    Application application();
    
}
