package one.com.pesosense.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import one.com.pesosense.R;


public class AppGuide extends Activity implements View.OnClickListener {

    Button btnLogin;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        initValues();

    }

    public void initValues() {

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        if (view.getId() == R.id.btnLogin)
            intent = new Intent(AppGuide.this, PesoActivity.class);
            //intent = new Intent(AppGuide.this, Login.class);

        if (view.getId() == R.id.btnSignup)
            intent = new Intent(AppGuide.this, Signup.class);

        if (intent != null)
            startActivity(intent);

    }


}
