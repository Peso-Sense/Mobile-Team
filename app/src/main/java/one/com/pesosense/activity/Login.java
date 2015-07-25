package one.com.pesosense.activity;

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
import one.com.pesosense.download.APIHandler;
import one.com.pesosense.download.LoginDownload;
import one.com.pesosense.helper.ProfileParser;


public class Login extends ActionBarActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Button btnLogin;

    EditText txtEmail;
    EditText txtPassword;

    Map<String, String> passedData;
    APIHandler apiHandler;

    //---- facebook //

    final String GET = "get";
    final String POST = "post";
    final String urlEmail = "http://search.onesupershop.com/api/auth";
    final String urlUserDetails = "http://search.onesupershop.com/api/me";
    CallbackManager cb;
    LoginButton loginButton;
    Map<String, String> data;
    //
    ProgressDialog pDialog;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        initToolbar();
        initFB();
        initAPI();
        initValues();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) mToolbar.findViewById(R.id.title);
        title.setText("Log in");
        title.setTypeface(UtilsApp.opensansNormal());
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initFB() {

        cb = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        loginButton.registerCallback(cb, callback);
        loginButton.setTypeface(UtilsApp.opensansNormal());
        //loginButton.setOnClickListener(this);


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

        pDialog = new ProgressDialog(Login.this);
        pDialog.setCancelable(false);

//        btnSignup = (Button) findViewById(R.id.btnSignup);
//        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLogin) {
            UtilsApp.LOGIN_STATUS = 1;
            //test();
            login();

        }

    }

    public void login() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        passedData.clear();
        addToDataMap("email", email);
        addToDataMap("password", password);

        new LoginTask().execute();
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {

        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Log in...");
            pDialog.show();
            Log.d("response", "Login using email");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = apiHandler.httpMakeRequest(urlEmail, passedData, POST);
            Log.d("response", "Login response: " + response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("response", "Parsing response...");
            parseResponse(response);
        }
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("response", "I WAS HERE");
            token = loginResult.getAccessToken().getToken();
            Log.d("response", "Token: " + token);
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

            response = new LoginDownload(token).login();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            // TODO: to be removed later

            parseResponse(response);

        }
    }

    public class GetUserDetails extends AsyncTask<Void, Void, Void> {

        String response;
        Map<String, String> data;

//        public GetUserDetails(Map<String, String> data) {
//           // this.data = data;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //   response = apiHandler.httpMakeRequest(url)
            //response = new LoginDownload(data).userDetails();
            response = apiHandler.httpMakeRequest(urlUserDetails, passedData, GET);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("response", "User details: " + response);
            parseUserResponse(response);

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
                    UtilsApp.putString("token_info", token);
                    // TODO: GET USER DETAILS
                    Log.d("response", "Fetching user details...");

                    //data.put("token", token);

                    passedData.clear();
                    addToDataMap("token", token);
                    new GetUserDetails().execute();

                } else if (message.equalsIgnoreCase(INVALID_CREDENTIALS)) {
                    // Invalid email or password;
                    pDialog.dismiss();
                    invalidLogin();

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void parseUserResponse(String response) {

        final String AUTHENTICATION = "authentication successful";

        JSONObject jsonObject;
        JSONObject profileObject;

        String email, name;

        Intent intent = null;

        try {
            jsonObject = new JSONObject(response).getJSONObject("data");
            Log.d("response", "Parsing user details");

            name = jsonObject.getString("name");
            email = jsonObject.getString("email");

            Log.d("response", "username: " + name + "| email: " + email);

            if (jsonObject.isNull("profile")) {

                Log.d("response", "Profile is null");
                intent = new Intent(Login.this, UserInformation.class);

            } else {
                profileObject = jsonObject.getJSONObject("profile");
                if (profileObject.has("user_id")) {

                    Log.d("response", "Profile has information");
                    new ProfileParser(getApplicationContext()).parse(profileObject, name, email);
                    intent = new Intent(Login.this, PesoActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cb.onActivityResult(requestCode, resultCode, data);
    }

    public void addToDataMap(String key, String value) {
        passedData.put(key, value);
    }

    public void invalidLogin() {

        txtEmail.setText("");
        txtPassword.setText("");

        //TODO: PAPALITAN PA TO.
        UtilsApp.toast("INVALID EMAIL OR PASSWORD!");
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
