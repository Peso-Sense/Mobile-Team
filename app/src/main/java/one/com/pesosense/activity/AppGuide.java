package one.com.pesosense.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import one.com.pesosense.R;


public class AppGuide extends ActionBarActivity {

    ScrollView scroll;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);


        scroll = (ScrollView) findViewById(R.id.scroll);
        btn1 = (Button) findViewById(R.id.Button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AppGuide.this,Login.class);
                startActivity(intent);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appguide, menu);
        return true;




    }




}
