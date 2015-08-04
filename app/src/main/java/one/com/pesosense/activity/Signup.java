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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

    private final String TAG = "Login";

    Toolbar toolbar;

    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;

    Button btnSignup;
    ProgressDialog pDialog;

    LoginButton loginButton;
    CallbackManager cb;


    Map<String, String> data;

    String token, email, password, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);

        UtilsApp.initSharedPreferences(getApplicationContext());
        initToolbar();
        initFB();
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

    private void initFB() {
        cb = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        loginButton.registerCallback(cb, callback);
        loginButton.setTypeface(UtilsApp.opensansNormal());
    }

    public void initValues() {

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtUsername.setTypeface(UtilsApp.opensansNormal());

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(UtilsApp.opensansNormal());

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setTypeface(UtilsApp.opensansNormal());

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setTypeface(UtilsApp.opensansNormal());
        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSignup) {
            signup();
        }

    }

    private void signup() {
        email = txtEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();
        username = txtUsername.getText().toString().trim();

        if (email.equals("") || password.equals("") || username.equals("")) {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
        } else if (!validateUsername(username)) {
            txtUsername.setError("A~Z, a~z, 0~9, _");
        } else {
            if (UtilsApp.isOnline()) {

                new RegisterUser(email, password, username).execute();
            } else {
                UtilsApp.toast("No Network Connection Available");
            }
        }
    }

    private Boolean validateUsername(String uname) {
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

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "Facebook Login");
            token = loginResult.getAccessToken().getToken();
            Log.d(TAG, "Token: " + token);
            new LoginFacebookTask(token).execute();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public class LoginFacebookTask extends AsyncTask<Void, Void, Void> {


        String token;
        String response;

        public LoginFacebookTask(String token) {
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //TODO: Papalitan ko pa to
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = new DownloadHelper().fbLogin(token);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            parseResponse2(response);

        }
    }

    public void parseResponse2(String response) {

        final String AUTHFB_SUCCESSFUL = "auth successful";
        final String AUTHEMAIL_SUCCESSFUL = "authentication successful";
        final String INVALID_CREDENTIALS = "Invalid credentials";

        JSONObject jsonObject;

        String message;
        String token;

        data = new HashMap<String, String>();

        try {

            jsonObject = new JSONObject(response);

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
                if (message.equalsIgnoreCase(AUTHFB_SUCCESSFUL) || message.equalsIgnoreCase(AUTHEMAIL_SUCCESSFUL)) {
                    token = jsonObject.getString("token");
                    // TODO: STORE TOKEN in SHARED PREF;
                    UtilsApp.putString("access_token", token);
                    UtilsApp.putInt("app_login", 1);
                    // TODO: GET USER DETAILS
                    Log.d(TAG, "Fetching user details...");

                    new GetUserDetails(token).execute();

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetUserDetails extends AsyncTask<Void, Void, Void> {

        String response;
        String token;

        public GetUserDetails(String token) {
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = new HashMap<>();
            data.put("token", token);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = new DownloadHelper().userDetails(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "User details: " + response);
            //   parseUserResponse(response, token);

        }
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
