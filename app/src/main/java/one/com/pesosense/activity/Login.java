package one.com.pesosense.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;


public class Login extends Activity implements View.OnClickListener {

    Button pabida;

    Button btnLogin;
    Button btnSkip;

    // Add comment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initValues();
    }

    private void initValues() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            UtilsApp.LOGIN_STATUS = 1;

        }
        if (v.getId() == R.id.btnSkip) {
            UtilsApp.LOGIN_STATUS = 0;

        }

        startActivity(new Intent(Login.this, PesoActivity.class));
        finish();
    }
}
