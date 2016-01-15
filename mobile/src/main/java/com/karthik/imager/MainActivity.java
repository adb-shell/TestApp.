package com.karthik.imager;

import com.karthik.imager.Fragments.DashboardFragment;
import com.karthik.imager.Fragments.DetailsFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
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
