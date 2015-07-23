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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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


public class Login extends ActionBarActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Button btnLogin;

    EditText txtEmail;
    EditText txtPassword;

    Map<String, String> passedData;
    APIHandler apiHandler;
    String url = "http://search.onesupershop.com/api/auth";
    String urlUserDetails = "http://search.onesupershop.com/api/me";

    String token = "";
    ProgressDialog pDialog;

    //---- facebook //
    CallbackManager cb;
    LoginButton loginButton;
    String urlOfFBAuth = "http://search.onesupershop.com/api/auth/facebook";

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
        loginButton.setOnClickListener(this);

        LoginManager.getInstance().registerCallback(cb, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new LoginWithFacebook(loginResult.getAccessToken().getToken()).execute();
            }

            @Override
            public void onCancel() {
                UtilsApp.toast("Cancel");
            }

            @Override
            public void onError(FacebookException e) {
                UtilsApp.toast("FAILED");
            }
        });

    }

    public class LoginWithFacebook extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        HttpEntity httpEntity;
        HttpResponse httpResponse;
        String response;
        List<NameValuePair> httpParams;
        JSONObject jsonObject;
        String token;

        public LoginWithFacebook(String token) {
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Loading... Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            httpParams = new ArrayList<NameValuePair>();
            httpParams.add(new BasicNameValuePair("access_token", token));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlOfFBAuth);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(httpParams));
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.d("response", response);
                jsonObject = new JSONObject(response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            parseFBResponse(jsonObject);
        }
    }

    private void parseFBResponse(JSONObject jObject) {

        final String RESULT_OK = "auth successful";
        String response;
        String token;
        try {

            response = jObject.getString("message");

            if (response.equalsIgnoreCase(RESULT_OK)) {
                token = jObject.getString("token");
                UtilsApp.putString("token", token);
                passedData.clear();
                addToDataMap("token", token);
                new GetUserDetails().execute();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        if (v.getId() == R.id.login_button) {
            facebookLogin();
        }


        if (v.getId() == R.id.btnLogin) {
            UtilsApp.LOGIN_STATUS = 1;
            login();

        }

    }

    private void facebookLogin() {

        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().logInWithPublishPermissions(Login.this, Arrays.asList("publish_actions"));

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

        new LoginTask().execute();

    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {


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
            loginResult();
            //pDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String response = apiHandler.httpMakeRequest(url, passedData, "post");
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getString("message").equalsIgnoreCase("authentication successful"))
                    token = jsonObject.getString("token");
                else
                    token = jsonObject.getString("message");
                passedData.clear();
                addToDataMap("token", token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void loginResult() {

        Log.d("token", token);
        if (token.equalsIgnoreCase("Invalid credentials")) {
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();

            // insert error message;
        } else {
            pDialog.dismiss();
            startActivity();
            //     new GetUserDetails().execute();
        }

    }

    public class GetUserDetails extends AsyncTask<Void, Void, Void> {
        String response;
        ProgressDialog pDialog;
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Loading... Please Wait...sx");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = apiHandler.httpMakeRequest(urlUserDetails, passedData, "get");

            try {

                jsonObject = new JSONObject(response);
                Log.d("response", response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();

            parseUserResponse(jsonObject);

        }
    }

    public void parseUserResponse(JSONObject jsonObject) {

        String response = "";
        int status = 0;
        try {
            if (jsonObject.has("status")) {

                if (jsonObject.isNull("profile")) {
                    Log.d("response", "Profile is null");
                    startActivity(new Intent(Login.this, UserInformation.class));
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addToDataMap(String key, String value) {
        passedData.put(key, value);
    }

    public void startActivity() {
        startActivity(new Intent(Login.this, PesoActivity.class));
    }
}
