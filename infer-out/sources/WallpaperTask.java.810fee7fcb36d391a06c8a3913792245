package com.karthik.imager;

import com.karthik.imager.Fragments.DetailsFragment;

import java.io.InputStream;
import java.net.URL;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by karthikrk on 14/01/16.
 */
public class WallpaperTask extends AsyncTask<String,Void,Boolean> {
    private String url;
    private Context mContext;

    public WallpaperTask(String url,Context mContext){
        this.url = url;
        this.mContext = mContext;
    }
    @Override
    protected Boolean doInBackground(String... params) {

        try{
            InputStream ins;
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
            ins = new URL(url).openStream();
            wallpaperManager.setStream(ins);
            return true;
        }
        catch (Exception e){
            Log.e("TAG", "EXCEPTION SETTING WALLPAPER: " + e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();

        if(manager.getBackStackEntryCount()>1){
            DetailsFragment detailsFragment = (DetailsFragment) manager.getFragments().get(1);
            detailsFragment.wallpaperSetCallback(success);
        }
    }
}
