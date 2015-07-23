package one.com.pesosense.activity;

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


public class Login2 extends ActionBarActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Button btnLogin;

    EditText txtEmail;
    EditText txtPassword;

    Map<String, String> passedData;
    APIHandler apiHandler;

    //---- facebook //
    CallbackManager cb;
    LoginButton loginButton;
    String urlOfFBAuth = "http://search.onesupershop.com/api/auth/facebook";

    //
    ProgressDialog pDialog;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) mToolbar.findViewById(R.id.title);
        title.setText("Log in");
        title.setTypeface(UtilsApp.opensansNormal());
        setSupportActionBar(mToolbar);

        initFB();
        initAPI();
        initValues();
    }

    private void initFB() {

        cb = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        loginButton.registerCallback(cb, callback);
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

        pDialog = new ProgressDialog(Login2.this);
        pDialog.setCancelable(false);

//        btnSignup = (Button) findViewById(R.id.btnSignup);
//        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLogin) {
            UtilsApp.LOGIN_STATUS = 1;
            login();

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
            // Log.d("response", "End point for now");
            parseResponse(response);

        }
    }

    public void parseResponse(String response) {

        final String AUTH_SUCCESSFUL = "auth successful";

        JSONObject jsonObject;


        String message;
        String token;

        try {

            jsonObject = new JSONObject(response);

            if (jsonObject.has("message")) {
               message = jsonObject.getString("message");
                if(message.equalsIgnoreCase(AUTH_SUCCESSFUL)){
                    token = jsonObject.getString("token");

                    // TODO: GET USER DETAILS
                    addToDataMap("token", token);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class GetUserDetails extends AsyncTask<Void, Void, Void>{

        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

         //   response = apiHandler.httpMakeRequest(url)
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cb.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        addToDataMap("email", email);
        addToDataMap("password", password);

//        new LoginTask().execute();

    }

    public void addToDataMap(String key, String value) {
        passedData.put(key, value);
    }

    public void startActivity() {
        startActivity(new Intent(Login2.this, PesoActivity.class));
    }
}
