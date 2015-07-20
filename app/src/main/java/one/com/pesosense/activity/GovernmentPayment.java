package one.com.pesosense.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;

public class GovernmentPayment extends ActionBarActivity {

    Toolbar toolbar;

    TextView lblGovernment;
    TextView lblName;
    TextView lblRef;
    TextView lblAmount;

    Button btnConfirm;

    EditText txtName;
    EditText txtRefNum;
    EditText txtAmount;

    int root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_payment);

        initValues();
    }

    private void initValues() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        root = getIntent().getIntExtra("root", 0);

        lblGovernment = (TextView) findViewById(R.id.lblGovernment);
        lblGovernment.setTypeface(UtilsApp.opensansNormal());

        lblName = (TextView) findViewById(R.id.lblName);
        lblName.setTypeface(UtilsApp.opensansNormal());

        lblRef = (TextView) findViewById(R.id.lblRef);
        lblRef.setTypeface(UtilsApp.opensansNormal());

        lblAmount = (TextView) findViewById(R.id.lblAmount);
        lblAmount.setTypeface(UtilsApp.opensansNormal());

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setTypeface(UtilsApp.opensansNormal());

        txtName = (EditText) findViewById(R.id.txtName);
        txtName.setTypeface(UtilsApp.opensansNormal());

        txtRefNum = (EditText) findViewById(R.id.txtRefNum);
        txtRefNum.setTypeface(UtilsApp.opensansNormal());

        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(UtilsApp.opensansNormal());

        setGovernmentName(root);
    }

    private void setGovernmentName(int root) {

        String governmentName = "";

        switch (root) {

            case 0:
                governmentName = "Social Security System";
                break;
            case 1:
                governmentName = "PhilHealth";
                break;
            case 2:
                governmentName = "Pag ibig Fund";
                break;

        }

        lblGovernment.setText(governmentName);
        setTitle(governmentName);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_government_payment, menu);
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

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
