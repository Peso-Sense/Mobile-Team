package one.com.pesosense.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.fragment.FeedsFragment;
import one.com.pesosense.fragment.FragmentDrawer;
import one.com.pesosense.fragment.PaymentFragment;
import one.com.pesosense.fragment.RemittanceFragment;
import one.com.pesosense.fragment.ShopFragment;


public class PesoActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {


    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    int status = UtilsApp.LOGIN_STATUS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peso);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        if (status == 1)
            displayViewRich(1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_peso, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {

        if (status == 1)
            displayViewRich(position);
    }

    private void displayViewRich(int position) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment = null;

        switch (position) {
            case 0:

                mFragment = new FeedsFragment();
                break;
            case 1:
                setTitle("Shop");
                mFragment = new ShopFragment();
                break;
            case 2:
                setTitle("Payment");
                mFragment = new PaymentFragment();
                break;
            case 3:
                setTitle("Remittance");
                mFragment = new RemittanceFragment();
                break;
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container_body, mFragment).commit();
        }


    }


}
