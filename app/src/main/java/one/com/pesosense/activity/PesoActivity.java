package one.com.pesosense.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.fragment.FeedsFragment;
import one.com.pesosense.fragment.FragmentDrawer;
import one.com.pesosense.fragment.PaymentFragment;
import one.com.pesosense.fragment.RemittanceFragment;
import one.com.pesosense.fragment.Shop;


public class PesoActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {


    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    TextView title;
    int lastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peso);
        UtilsApp.initSharedPreferences(getApplicationContext());
//        this.invalidateOptionsMenu();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) mToolbar.findViewById(R.id.title);

        title.setTypeface(UtilsApp.opensansNormal());
        setSupportActionBar(mToolbar);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        displayView(0);


    }


    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }

    private void displayView(int position) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment = null;
        String title = "";
        switch (position) {
            case 0:
                title = "Feeds";
                mFragment = new FeedsFragment();
                break;
            case 1:
                setTitle("Shop");
                title = "Shop";
                mFragment = new Shop();
                break;
            case 2:
                title = "Payment";
                mFragment = new PaymentFragment();
                break;
            case 3:
                title = "Remittance";
                mFragment = new RemittanceFragment();
                break;
            case 4:
                signOut();
                break;
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container_body, mFragment).commit();
        }
        if (position != 4)
            lastPosition = position;

        this.title.setText(title);

    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PesoActivity.this);
        builder.setTitle("Sign out").setMessage("Do you want to sign out?").setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(PesoActivity.this, Login.class));
            }
        }).setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                displayView(lastPosition);
                dialogInterface.dismiss();

            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_peso, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(PesoActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UtilsApp.putInt("display_tips", 0);
    }
}
