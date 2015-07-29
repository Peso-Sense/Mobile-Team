package one.com.pesosense.activity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;


public class ResetPassword extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView lblReset;
    TextView text;

    EditText txtEmail;

    Button btnSubmit;

    Dialog dialog;

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
        lblReset = (TextView) findViewById(R.id.lblReset);
        lblReset.setTypeface(UtilsApp.opensansNormal());

        text = (TextView) findViewById(R.id.text);
        text.setTypeface(UtilsApp.opensansNormal());

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(UtilsApp.opensansNormal());

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(UtilsApp.opensansNormal());
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSubmit) {
            submit();
        }

        if (view.getId() == R.id.btnOk) {
            dialog.dismiss();
            //ready for next Activity;
        }
    }


    private void submit() {
        new ResetEmailTask().execute();
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


    public class ResetEmailTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            checkEmail();
        }
    }


}
