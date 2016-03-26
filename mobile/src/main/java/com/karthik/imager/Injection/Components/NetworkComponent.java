package com.karthik.imager.Injection.Components;

import com.karthik.imager.Fragments.DashboardFragment;
import com.karthik.imager.Injection.Modules.NetworkModule;
import com.karthik.imager.Injection.PerActivity;
import com.karthik.imager.MainActivity;

import dagger.Component;

/**
 * Created by karthikrk on 26/03/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = NetworkModule.class)
public interface NetworkComponent {
    void inject(DashboardFragment dashboardFragment);
}
