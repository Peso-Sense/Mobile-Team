package one.com.pesosense.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
import one.com.pesosense.download.DownloadHelper;

public class Signup extends ActionBarActivity implements View.OnClickListener {


    Toolbar toolbar;

    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;

    Button btnSignup;

    /****/
    String email, password, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        UtilsApp.initSharedPreferences(getApplicationContext());
        initToolbar();
        initValues();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("SIGN UP");
        title.setTypeface(UtilsApp.opensansNormal());
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void initValues() {

        // (s) RUM 07/17/15
//        apiHandler = new APIHandler();
//        data = new HashMap<String, String>();
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

                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                username = txtUsername.getText().toString().trim();

                if (email.equals("") || password.equals("") || username.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!parseUsername(username)) {
                    txtUsername.setError("A~Z, a~z, 0~9, _");
                } else {
                    if (UtilsApp.isOnline()) {

                        new RegisterUser(email, password, username).execute();
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

    @Override
    public void onClick(View view) {

    }

    public class RegisterUser extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        String response;

        Map<String, String> data;

        public RegisterUser(String email, String password, String username) {
            data = new HashMap<>();
            data.put("email", email);
            data.put("password", password);
            data.put("name", username);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signup.this);
            pDialog.setMessage("Signing up...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            response = new DownloadHelper().registerUser(data);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (parseResponse(response)) {
                userInfo();
            } else
                showError();

        }
    }

    public void userInfo() {
        Bundle b = new Bundle();
        b.putString("root", "signup");
        b.putString("email", email);
        Intent intent = new Intent(Signup.this, UserInformation.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public boolean parseResponse(String response) {

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int status = 0;
        String token = "";

        if (jObject.has("status")) {
            try {
                token = jObject.getString("token");
                UtilsApp.putString("access_token", token);
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
