package one.com.pesosense.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.download.DownloadHelper;


public class ResetPassword extends ActionBarActivity implements View.OnClickListener {

    private final String TAG = "RESET PASSWORD";

    Toolbar toolbar;
    LinearLayout container, container2;
    TextView lblReset;
    TextView text;
    EditText txtEmail, txtCode, txtPassword;
    Button btnSubmit, btnChange;
    Dialog dialog;
    ProgressDialog pDialog;

    Map<String, String> data;
    boolean response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("Reset Password");
        title.setTypeface(UtilsApp.opensansNormal());
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {

        container = (LinearLayout) findViewById(R.id.container1);
        container2 = (LinearLayout) findViewById(R.id.container2);
        container2.setVisibility(View.GONE);


        lblReset = (TextView) findViewById(R.id.lblReset);
        lblReset.setTypeface(UtilsApp.opensansNormal());

        text = (TextView) findViewById(R.id.text);
        text.setTypeface(UtilsApp.opensansNormal());

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(UtilsApp.opensansNormal());

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(UtilsApp.opensansNormal());
        btnSubmit.setOnClickListener(this);

        //  - - - - - - - - - - - - - - - - - - - -

        txtCode = (EditText) findViewById(R.id.txtCode);
        txtCode.setTypeface(UtilsApp.opensansNormal());

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setTypeface(UtilsApp.opensansNormal());

        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setTypeface(UtilsApp.opensansNormal());
        btnChange.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSubmit) {

            String email = txtEmail.getText().toString();
            new ResetEmailTask(email).execute();
            //submit();
        }

        if (view.getId() == R.id.btnOk) {
            dialog.dismiss();
            container2.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            //ready for next Activity;
        }

        if (view.getId() == R.id.btnChange) {

            String code = txtCode.getText().toString();
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            new ChangePasswordTask(code, email, password).execute();

        }
    }


    public class ResetEmailTask extends AsyncTask<Void, Void, Void> {


        public ResetEmailTask(String email) {

            data = new HashMap<>();
            data.put("email", email);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ResetPassword.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = new DownloadHelper().forgetPassword(data);
            //  Log.d(TAG, response);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            if (response)
                checkEmail();
            else
                errorDialog("Invalid Email", "Please make sure you entered a valid email");
        }
    }

    private void checkEmail() {

        TextView lblCheckEmail, text;
        Button btnOk;

        dialog = new Dialog(ResetPassword.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_check_email);

        lblCheckEmail = (TextView) dialog.findViewById(R.id.lblCheckEmail);
        lblCheckEmail.setTypeface(UtilsApp.opensansBold());

        text = (TextView) dialog.findViewById(R.id.text);
        text.setTypeface(UtilsApp.opensansNormal());

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setTypeface(UtilsApp.opensansNormal());
        btnOk.setOnClickListener(this);

        dialog.show();

    }

    private void errorDialog(String title, String message) {
        //AlertDialog.Builder dialog = new AlertDialog(ResetPassword.this);

        final AlertDialog.Builder aDialog = new AlertDialog.Builder(ResetPassword.this);
        aDialog.setTitle(title);
        aDialog.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        txtEmail.setText("");
                    }
                });

        aDialog.show();
    }

    public class ChangePasswordTask extends AsyncTask<Void, Void, Void> {

        public ChangePasswordTask(String code, String email, String password) {
            data = new HashMap<>();
            data.put("code", code);
            data.put("email", email);
            data.put("password", password);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ResetPassword.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = new DownloadHelper().changedPassword(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            if (response) {
                // PASSWORD CHANGE SUCESSFULL;
                startActivity(new Intent(ResetPassword.this, Login.class));
            } else {
                errorDialog("Validation Code", "Please make sure you entered a valid verification code");
            }

        }
    }

}