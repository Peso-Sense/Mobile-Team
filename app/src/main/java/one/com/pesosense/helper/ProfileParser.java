package one.com.pesosense.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import one.com.pesosense.UtilsApp;

/**
 * Created by mykelneds on 7/23/15.
 */
public class ProfileParser {

    private static String imageUrl = "https://search.onesupershop.com/api/photos/";
    private String url = "http://4hdwallpapers.com/wp-content/uploads/2013/05/Chopper-One-Piece-Photo-728x546.jpg";
    Context context;


    public ProfileParser(Context context) {
        this.context = context;
    }

    public void parse(JSONObject jsonObject, String name, String email) {

        JSONObject jsonInner;

        int userID = 0;
        String username = "";
        String emailA = "";
        String photo = "";
        String firstName = "";
        String middleName = "";
        String lastName = "";
        String birthday = "";
        String gender = "";
        String address = "";

        try {
            userID = jsonObject.getInt("user_id");

            Log.d("response", "Put user_id to SharedPref: " + userID);
            UtilsApp.putInt("user_id", userID);

            username = name;
            emailA = email;

            jsonInner = jsonObject.getJSONObject("info");

            if (!jsonInner.isNull("photo"))
                photo = jsonInner.getString("photo");
            firstName = jsonInner.getString("first_name");
            middleName = jsonInner.getString("middle_name");
            lastName = jsonInner.getString("last_name");

            birthday = jsonInner.getString("birthday");
            gender = jsonInner.getString("gender");

            address = jsonInner.getString("address");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        populateDB(userID, username, emailA, photo, firstName, middleName, lastName, birthday, gender, address);
    }

    public void populateDB(int userID, String username, String emailA, String photo, String firstName, String middleName, String lastName, String birthday, String gender, String address) {

        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        ContentValues values;

        dbHelper = DatabaseHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        values = new ContentValues();

        values.put("user_id", userID);
        values.put("username", username);
        values.put("email", emailA);
        values.put("photo", photo);
        values.put("first_name", firstName);
        values.put("middle_name", middleName);
        values.put("last_name", lastName);
        values.put("birthday", birthday);
        values.put("gender", gender);
        values.put("address", address);

        db.insert("tbl_user_info", null, values);
        getPhoto(photo);
        Log.d("response", "Inserting userInformation in database");

    }

    private void getPhoto(final String photo) {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File file = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_PICTURES), "Peso Testing");

                if (!file.exists()) {
                    // do something
                    file.mkdir();
                }

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);

                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context).load(url).into(target);
    }

    //(user_id integer, username varchar, email varchar, photo varchar, first_name varchar, middle_name varchar, last_name varchar, birthday varchar, gender varchar, address varchar)");
}
