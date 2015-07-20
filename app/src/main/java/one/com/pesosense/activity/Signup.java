package one.com.pesosense.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

        initValues();

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

                strEmail = txtEmail.getText().toString();
                strPass = txtPassword.getText().toString();
                strName = txtUsername.getText().toString();
                addToPassedDataMap("email", strEmail);
                addToPassedDataMap("password", strPass);
                addToPassedDataMap("name", strName);

                new RegisterUser().execute();

            }
        });


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
}
