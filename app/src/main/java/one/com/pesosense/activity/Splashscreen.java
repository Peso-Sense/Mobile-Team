package one.com.pesosense.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import one.com.pesosense.R;


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


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "one.com.pesosense",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                UtilsApp.putInt("APP_LOGIN", 1);
//                UtilsApp.APP_LOGIN = UtilsApp.getInt("APP_LOGIN");
                startActivity(new Intent(Splashscreen.this, AppGuide.class));
//                startActivity(new Intent(Splashscreen.this, ResetPassword.class));
//                startActivity(new Intent(Splashscreen.this, PesoActivity.class));

                finish();
            }
        }, 2500);
    }
}
