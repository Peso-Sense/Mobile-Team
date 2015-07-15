package one.com.pesosense.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;


public class Login extends ActionBarActivity implements View.OnClickListener {

    Button btnLogin;
    Button btnSignup;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initValues();
    }

    private void initValues() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

//        btnSignup = (Button) findViewById(R.id.btnSignup);
//        btnSignup.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            UtilsApp.LOGIN_STATUS = 1;

        }
        if (v.getId() == R.id.btnSignup) {
            UtilsApp.LOGIN_STATUS = 0;

        }

        startActivity(new Intent(Login.this, PesoActivity.class));
        finish();
    }
}
