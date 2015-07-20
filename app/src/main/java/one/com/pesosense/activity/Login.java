package one.com.pesosense.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.download.APIHandler;


public class Login extends ActionBarActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Button btnLogin;

    EditText txtEmail;
    EditText txtPassword;

    Map<String, String> passedData;
    APIHandler apiHandler;
    String url = "http://search.onesupershop.com/api/auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initAPI();
        initValues();
    }

    private void initAPI() {
        apiHandler = new APIHandler();
        passedData = new HashMap<String, String>();
    }

    private void initValues() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setTypeface(UtilsApp.opensansNormal());
        btnLogin.setOnClickListener(this);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(UtilsApp.opensansNormal());

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setTypeface(UtilsApp.opensansNormal());

//        btnSignup = (Button) findViewById(R.id.btnSignup);
//        btnSignup.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            UtilsApp.LOGIN_STATUS = 1;
            login();

        }

//        startActivity(new Intent(Login.this, PesoActivity.class));
//        finish();
    }

    public void login() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        addToDataMap("email", email);
        addToDataMap("password", password);

        new LoginTask().execute();

    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logging in...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }

    public void addToDataMap(String key, String value) {
        passedData.put(key, value);
    }

}
