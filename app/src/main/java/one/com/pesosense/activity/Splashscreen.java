package one.com.pesosense.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;


public class Splashscreen extends Activity {

    ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        img2 = (ImageView) findViewById(R.id.imageView);
        img2.setBackgroundResource(R.drawable.peso_gif);
        AnimationDrawable frameAnimation = (AnimationDrawable) img2.getBackground();
        frameAnimation.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilsApp.putInt("LOGIN_STATUS", 1);
                UtilsApp.LOGIN_STATUS = UtilsApp.getInt("LOGIN_STATUS");
                startActivity(new Intent(Splashscreen.this, AppGuide.class));
                //startActivity(new Intent(Splashscreen.this, PesoActivity.class));

                finish();


            }
        }, 2500);
    }


}
