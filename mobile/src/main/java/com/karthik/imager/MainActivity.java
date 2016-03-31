package com.karthik.imager;

import com.karthik.imager.Fragments.DashboardFragment;
import com.karthik.imager.Fragments.DetailsFragment;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        if(savedInstanceState==null){
            //dummy method to fill the value to shared preferences.
            fillDummyValuesInSharedPreferences();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,new DashboardFragment())
                    .addToBackStack("")
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    /**
     This method is used to demonstrate particular stetho functionality.
     such that we view values in the local storage of chrome inspector
     as such it has no significance.
     **/
    private void fillDummyValuesInSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] strings = new String[]{"one","two","three","four","five","six","seven","eight","nine","ten"};
        for(int i=0;i<strings.length;i++){
            editor.putInt(strings[i], i);
        }
        editor.putString("String","This is awesome");
        //writes to the persistant storage quickly faster than commit
        editor.apply();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (fragmentManager.getBackStackEntryCount()){
            case 1:
                finish();
                break;
            case 2:
                //callback to the detail fragment on back pressed.
                ((DetailsFragment)fragmentManager.getFragments().get(1)).onBackPressed();
                break;
        }
    }

}
