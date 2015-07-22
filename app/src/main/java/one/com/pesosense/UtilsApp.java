package one.com.pesosense;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by mykelneds on 6/20/15.
 */

//TESING KO LANG
public class UtilsApp extends Application {

    private static Context mContext;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static int LOGIN_STATUS = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    public static boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Typeface opensansLight() {

        return Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans_Light.ttf");
    }

    public static Typeface opensansNormal() {

        return Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans_Regular.ttf");
    }

    public static Typeface opensansBold() {

        return Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans_Bold.ttf");
    }

    public static String getString(String title) {
        String str = preferences.getString(title, null);
        return str;
    }


    public static int getInt(String title) {
        int i = preferences.getInt(title, 0);
        return i;
    }

    public static void putString(String title, String value) {
        editor.putString(title, value);
        editor.apply();
    }

    public static void putInt(String title, int value) {
        editor.putInt(title, value);
        editor.apply();
    }


    public static void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }


    public static void hideSoftKeyboard(final Activity activity, View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                hideSoftKeyboard(activity, innerView);
            }
        }
    }
}
