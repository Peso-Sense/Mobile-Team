package one.com.pesosense.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;

public class GovernmentPayment extends ActionBarActivity {

    TextView lblGovernment;
    TextView lblName;
    TextView lblRef;
    TextView lblAmount;

    Button btnConfirm;

    EditText txtName;
    EditText txtRefNum;
    EditText txtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_payment);

        initValues();
    }

    private void initValues() {

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

        return super.onOptionsItemSelected(item);
    }
}
