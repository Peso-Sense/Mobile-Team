package one.com.pesosense.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.download.APIHandler;

public class Signup extends ActionBarActivity {


    Toolbar mToolbar;

    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;

    Button btnSignup;

    /****/
    String strEmail, strPass, strName;
    Map<String, String> passedData;
    String url = "http://search.onesupershop.com/api/register";
    APIHandler apiHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initToolbar();
        initValues();

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) mToolbar.findViewById(R.id.title);
        title.setText("SIGN UP");
        title.setTypeface(UtilsApp.opensansNormal());
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void initValues() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // (s) RUM 07/17/15
        apiHandler = new APIHandler();
        passedData = new HashMap<String, String>();
        // (e) RUM 07/17/15

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtUsername.setTypeface(UtilsApp.opensansNormal());

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(UtilsApp.opensansNormal());

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setTypeface(UtilsApp.opensansNormal());

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setTypeface(UtilsApp.opensansNormal());
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strEmail = txtEmail.getText().toString().trim();
                strPass = txtPassword.getText().toString().trim();
                strName = txtUsername.getText().toString().trim();

                if (strEmail.equals("") || strPass.equals("") || strName.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!parseUsername(strName)) {
                    txtUsername.setError("A~Z, a~z, 0~9, _");
                } else {
                    if (UtilsApp.isOnline()) {
                        addToPassedDataMap("email", strEmail);
                        addToPassedDataMap("password", strPass);
                        addToPassedDataMap("name", strName);
                        new RegisterUser().execute();
                    } else {
                        UtilsApp.toast("No Network Connection Available");
                    }
                }

            }
        });

    }

    private Boolean parseUsername(String uname) {
        int i;
        List<String> allowedCharsList = new ArrayList<String>();

        for (i = 65; i < 91; i++) {
            allowedCharsList.add(String.valueOf((char) i));
        }

        for (i = 97; i < 123; i++) {
            allowedCharsList.add(String.valueOf((char) i));
        }

        for (i = 0; i < 10; i++) {
            allowedCharsList.add(String.valueOf(i));
        }

        allowedCharsList.add("_");

        String[] allowedChars = new String[allowedCharsList.size()];
        allowedCharsList.toArray(allowedChars);

        for (i = 0; i < uname.length(); i++) {
            if (!Arrays.asList(allowedChars).contains(String.valueOf(uname.charAt(i)))) {
                return false;
            }
        }

        return true;
    }

    public class RegisterUser extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signup.this);
            pDialog.setMessage("Signing up...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = apiHandler.httpMakeRequest(url, passedData, "post");
            try {
                jsonObject = new JSONObject(response);
                Log.d("key", response);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (parseResponse(jsonObject)) {
                userInfo();
                //Toast.makeText(getApplicationContext(), "RESIGTER ", Toast.LENGTH_SHORT).show();
            } else
                showError();
            //Toast.makeText(getApplicationContext(), "Email Already used. ", Toast.LENGTH_SHORT).show();

        }
    }

    public void userInfo() {
        Bundle b = new Bundle();
        b.putString("root", "signup");
        b.putString("email", strEmail);
        Intent intent = new Intent(Signup.this, UserInformation.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void addToPassedDataMap(String key, String value) {
        passedData.put(key, value);
    }

    public boolean parseResponse(JSONObject jObject) {

        int status = 0;
        String token = "";

        if (jObject.has("status")) {
            try {
                token = jObject.getString("token");
                UtilsApp.putString("token_info", token);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
        builder.setTitle("Email Already used");
        builder.setMessage("Please select another Email Address");
        builder.setPositiveButton("ok", null);
        builder.create();
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
