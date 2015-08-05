package one.com.pesosense.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.download.DownloadHelper;
import one.com.pesosense.download.DownloadHelper2;
import one.com.pesosense.helper.ProfileParser;


public class Login2 extends ActionBarActivity implements View.OnClickListener {

    private final String TAG = "Login";

    Toolbar toolbar;
    Button btnLogin;

    EditText txtEmail;
    EditText txtPassword;

    ProgressDialog pDialog;

    LoginButton loginButton;
    CallbackManager cb;

    Map<String, String> data;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        UtilsApp.initSharedPreferences(getApplicationContext());
        initToolbar();
        initFB();
        initValues();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("Log in");
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

    private void initValues() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setTypeface(UtilsApp.opensansNormal());
        btnLogin.setOnClickListener(this);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(UtilsApp.opensansNormal());

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setTypeface(UtilsApp.opensansNormal());

        pDialog = new ProgressDialog(Login2.this);
        pDialog.setCancelable(false);

//        btnSignup = (Button) findViewById(R.id.btnSignup);
//        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLogin) {
            loginEmail();
        }

        if (v.getId() == R.id.btnReset) {
            UtilsApp.toast("RESET");
            startActivity(new Intent(Login2.this, ResetPassword.class));
        }

        if (v.getId() == R.id.btnRegister) {
            startActivity(new Intent(Login2.this, Signup.class));
        }
    }

    public void loginEmail() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (!email.trim().equals("") && !password.trim().equals(""))
            new LoginEmailTask(email, password).execute();
        else {
            if (email.trim().equals("")) {
                txtEmail.setError("Please enter your email");
                txtEmail.requestFocus();
            }
            if (password.trim().equals("")) {
                txtPassword.requestFocus();
                txtPassword.setError("Please enter your password");
            }
        }
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

            parseResponse(response);

        }
    }

    public class LoginEmailTask extends AsyncTask<Void, Void, Void> {

        String response;
        String email, password;

        public LoginEmailTask(String email, String password) {
            this.email = email;
            this.password = password;

            data = new HashMap<>();
            data.put("email", email);
            data.put("password", password);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Log in...");
            pDialog.show();
            Log.d(TAG, "Login using email");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = new DownloadHelper2().emailLogin(data);
            Log.d(TAG, "Login response: " + response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "Parsing response...");
            parseResponse(response);
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
            parseUserResponse(response, token);

        }
    }

    public void parseResponse(String response) {

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

                } else if (message.equalsIgnoreCase(INVALID_CREDENTIALS)) {
                    // Invalid email or password;
                    invalidLogin();

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void parseUserResponse(String response, String token) {

        JSONObject jsonObject;
        JSONObject profileObject;

        int id;
        String email, name;

        Intent intent = null;

        try {
            jsonObject = new JSONObject(response).getJSONObject("data");
            Log.d(TAG, "Parsing user details");

            id = jsonObject.getInt("id");
            UtilsApp.putInt("user_id", id);
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");

            Log.d(TAG, "username: " + name + "| email: " + email);

            if (jsonObject.isNull("profile")) {

                Log.d(TAG, "Profile is null");
                intent = new Intent(Login2.this, UserInformation.class);

            } else {
                profileObject = jsonObject.getJSONObject("profile");
                if (profileObject.has("user_id")) {

                    Log.d(TAG, "Profile has information");
                    new ProfileParser(getApplicationContext()).parse(profileObject, name, email);
                    intent = new Intent(Login2.this, PesoActivity.class);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (intent != null) {
            pDialog.dismiss();
            startActivity(intent);
            setResult(1);
            finish();
        }

    }

    public void invalidLogin() {

        TextView lblFailed, text;
        Button btnReset, btnRegister;

        pDialog.dismiss();

        txtPassword.setText("");

        Dialog dialog = new Dialog(Login2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_failed);

        lblFailed = (TextView) dialog.findViewById(R.id.lblLFailed);
        lblFailed.setTypeface(UtilsApp.opensansNormal());

        text = (TextView) dialog.findViewById(R.id.text);
        text.setTypeface(UtilsApp.opensansNormal());

        btnReset = (Button) dialog.findViewById(R.id.btnReset);
        btnReset.setTypeface(UtilsApp.opensansNormal());
        btnReset.setOnClickListener(this);

        btnRegister = (Button) dialog.findViewById(R.id.btnRegister);
        btnRegister.setTypeface(UtilsApp.opensansNormal());
        btnRegister.setOnClickListener(this);

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cb.onActivityResult(requestCode, resultCode, data);
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
