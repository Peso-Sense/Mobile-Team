package one.com.pesosense.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import one.com.pesosense.C;
import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.download.APIHandler;
import one.com.pesosense.download.AndroidMultiPartEntity;
import one.com.pesosense.helper.DatabaseHelper;

/**
 * Created by mykelneds on 7/17/15.
 */
public class UserInformation extends ActionBarActivity implements View.OnClickListener {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 150;
    private static final int IMAGE_CROP = 110;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_DIR_NAME = "Peso Sense";

    Uri fileUri;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues values;

    ScrollView container;

    ImageView imgUser;

    TextView lblLName;
    TextView lblMName;
    TextView lblFName;
    TextView lblGender;
    TextView lblBirthday;
    TextView lblAddress;

    EditText txtLName;
    EditText txtMName;
    EditText txtFName;
    EditText txtBirthday;
    EditText txtAddress;

    Spinner spinnerGender;

    Button btnSave;
    Button btnEdit;

    Dialog dialog;

    Bundle b;

    String email, imgPath, lName, fName, mName, gender, birthday, address;

    // Responsible for register
    Map<String, String> param;
    APIHandler apiHandler;
    long totalSize = 0;
    SharedPreferences sp;
    String url = "http://search.onesupershop.com/api/me/photo";
    String urlPersonInfo = "http://search.onesupershop.com/api/me";

    String imageFilePath;

    String token;
    // for validating if there's an image uploaded or none
    int USER_IMAGE = 0; // indicates that no image was uploaded yet

    /**
     * **
     */
    String root;

    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        param = new HashMap<String, String>();

        apiHandler = new APIHandler();
        setContentView(R.layout.activity_user_information);

        initValues();
        getBundle();

    }

    private void initValues() {

        dbHelper = DatabaseHelper.getInstance(getApplicationContext());

        container = (ScrollView) findViewById(R.id.container);
        UtilsApp.hideSoftKeyboard(UserInformation.this, container);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        imgUser.setOnClickListener(this);

        lblLName = (TextView) findViewById(R.id.lblLName);
        lblLName.setTypeface(UtilsApp.opensansNormal());

        lblMName = (TextView) findViewById(R.id.lblMName);
        lblMName.setTypeface(UtilsApp.opensansNormal());

        lblFName = (TextView) findViewById(R.id.lblFName);
        lblFName.setTypeface(UtilsApp.opensansNormal());

        lblGender = (TextView) findViewById(R.id.lblGender);
        lblGender.setTypeface(UtilsApp.opensansNormal());

        lblBirthday = (TextView) findViewById(R.id.lblBirthday);
        lblBirthday.setTypeface(UtilsApp.opensansNormal());
        lblBirthday.setOnClickListener(this);

        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblAddress.setTypeface(UtilsApp.opensansNormal());

        txtLName = (EditText) findViewById(R.id.txtLName);
        txtLName.setTypeface(UtilsApp.opensansNormal());

        txtBirthday = (EditText) findViewById(R.id.txtBirthday);
        txtBirthday.setTypeface(UtilsApp.opensansNormal());
        txtBirthday.setOnClickListener(this);

        txtMName = (EditText) findViewById(R.id.txtMName);
        txtMName.setTypeface(UtilsApp.opensansNormal());

        txtFName = (EditText) findViewById(R.id.txtFName);
        txtFName.setTypeface(UtilsApp.opensansNormal());

        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtAddress.setTypeface(UtilsApp.opensansNormal());

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        genderItem();

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setTypeface(UtilsApp.opensansNormal());
        btnSave.setOnClickListener(this);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setTypeface(UtilsApp.opensansNormal());

        btnSave.setVisibility(View.VISIBLE);

        UtilsApp.initSharedPreferences(getApplicationContext());
        token = UtilsApp.getString("access_token");
    }

    public void genderItem() {
        List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        //dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerGender.setAdapter(dataAdapter);
    }

    private void getBundle() {

        b = getIntent().getExtras();
        if (b != null) {
            root = b.getString("root");

            if (root.equalsIgnoreCase("signup")) {
                btnSave.setVisibility(View.VISIBLE);
                email = b.getString("email");
            }
        }

    }

    @Override
    public void onClick(View view){

        if (view.getId() == R.id.imgUser) {
            openPhotoDialog();
        }

        if (view.getId() == R.id.txtBirthday) {
            showDatePicker();
        }

        if (view.getId() == R.id.btnSave) {

            if (UtilsApp.isOnline()) { // Check first if user is online

                saveInfo();

            } else {

                UtilsApp.toast("No Network Connection");

            }
        }
    }

    public void openPhotoDialog() {
        dialog = new Dialog(UserInformation.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_photo);
        dialog.show();

        TextView lblTitle = (TextView) dialog.findViewById(R.id.lbl_title);
        lblTitle.setTypeface(UtilsApp.opensansNormal());

        Button btnGallery = (Button) dialog.findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                dialog.dismiss();
            }
        });

        Button btnCamera = (Button) dialog.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                dialog.dismiss();
            }
        });
    }

    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_IMAGE_REQUEST_CODE);
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        imageFilePath = fileUri.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                Log.d("tag", "cursor: " + DatabaseUtils.dumpCursorToString(cursor));
                int column_index = cursor.getColumnIndexOrThrow("_data");

                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                Log.d("tag", "cursor: " + DatabaseUtils.dumpCursorToString(cursor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uri.getPath();
        }

        return null;
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIR_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIR_NAME, "Oops! Failed create "
                        + IMAGE_DIR_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void saveInfo(){

        lName = txtLName.getText().toString();
        mName = txtMName.getText().toString();
        fName = txtFName.getText().toString();
        gender = spinnerGender.getSelectedItem().toString();
        birthday = txtBirthday.getText().toString();
        address = txtAddress.getText().toString();

        // Validation part
        if (lName.equals("") || mName.equals("") || fName.equals("") || birthday.equals("") || address.equals("")) {

            UtilsApp.toast("All fields are required");

        } else {
            // will launch series of alert dialogs
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // final Dialog dialog = new Dialog(getApplicationContext());

            builder.setTitle("Save infromation?");
            if (USER_IMAGE == 0) {

                builder.setMessage("You haven't provided a profile image; the application default profile image will be used." +
                        "\n\nTap OK to proceed with the registration");

                imageFilePath = getDrawablePath();
                Log.d("Peso Sense", "Image file path: " + imageFilePath);

            } else {

                builder.setMessage("Tap OK to proceed in the registration.");

            }

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendInfo();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void sendInfo() {
        db = dbHelper.getWritableDatabase();
        db.delete("tbl_user_info", null, null);

        values = new ContentValues();

        values.put("photo", imageFilePath);
        values.put("last_name", lName);
        values.put("first_name", fName);
        values.put("middle_name", mName);
        values.put("gender", gender);
        values.put("birthday", birthday);
        values.put("address", address);
        values.put("email", email);

        db.insert("tbl_user_info", null, values);
        db.close();

        UtilsApp.putString("email", email);

        // send to API then server

        addParams("first_name", fName);
        addParams("last_name", lName);
        addParams("middle_name", mName);
        addParams("birthday", birthday);
        addParams("gender", gender);
        addParams("address", address);

        if (USER_IMAGE == 0) {
            new AddInformation().execute();
        } else {
            new UploadData().execute();
        }

    }

    public String getDrawablePath() {
        // this method shall return the filepath of the drawable/asset
        //return "drawable://" + R.drawable.pesosense_sqaure;
        Uri testUri = Uri.parse("file:///android_assets/pesosense_square.png");
        return testUri.getPath();
    }

    public String getAssetPath() {
        // this method decodes the image from the assets folder
        BitmapFactory.decodeFile("");
        return null;
    }

    public void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                txtBirthday.setText(formatMonth(month) + " " + day + ", " + year);
            }
        }, year, month, day);

        dpd.getDatePicker().setMaxDate(maxDate());
        dpd.getDatePicker().setMinDate(minDate());
        dpd.show();
    }

    private String formatMonth(int m) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        return months[m];
    }

    private Long minDate() {

        try {

            String time = "1900-01-01";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = df.parse(time);
            Long minDate = dt.getTime();

            return minDate;

        } catch (ParseException e) {

            UtilsApp.toast(e.getLocalizedMessage());
            return 0L;

        }

    }

    private Long maxDate() {

        Calendar now = Calendar.getInstance();

        String allowedYear = String.valueOf(now.get(Calendar.YEAR) - C.MIN_AGE);
        String currentMonth = String.valueOf(now.get(Calendar.MONTH) + 1);
        String currentDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH));

        Log.d("Peso Sense", "Current year: " + allowedYear); // track

        try {

            String time = allowedYear + "-" + currentMonth + "-" + currentDay;
            Log.d("Peso Sense", time);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = df.parse(time);
            Long maxDate = dt.getTime();

            Log.d("Peso Sense", "This is the long min date: " + String.valueOf(maxDate)); // track
            return maxDate;

        } catch (ParseException e) {

            UtilsApp.toast(e.getLocalizedMessage());
            return 0L;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Log.d("Peso Sense", "Cropping image from the GALLERY");
                fileUri = data.getData();
                getPickedImagePath(fileUri);
                cropImage(fileUri);
            }

        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Log.d("Peso Sense", "Cropping image from the CAMERA");
                cropImage(fileUri);
            }

        } else if (requestCode == IMAGE_CROP) {
            Log.d("Pese Sense", "Viewing cropped image");
            USER_IMAGE = 1; // indicate that the user has provided an image
            previewCroppedImage(data);
        }
    }

    private void getPickedImagePath(Uri pickedImage) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        imageFilePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
    }

    class UploadData extends AsyncTask<Void, Integer, String> {
        ProgressDialog pDialog;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(UserInformation.this);
            pDialog.setTitle("Uploading User Infromation");
            pDialog.setMax(100);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            pDialog.setProgress(progress[0]);
            pDialog.setMessage("Uploading " + progress[0] + "%");
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("Peso Sense", "In doInBackground already");

            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }

                    });

            File sourceFile = new File(imageFilePath); // imageFilePath is the path of the image
            Log.d("Peso Sense", "Upload image path  " + imageFilePath); // we need to know if there's a file path being passed

            try {
                // Adding file data to http body
                entity.addPart("file", new FileBody(sourceFile));

                entity.addPart("Content-Type", new StringBody("x-www-form-urlencoded"));
                totalSize = entity.getContentLength();
                Log.d("tag", "size: " + totalSize);

                response = apiHandler.httpMakeRequest(url, "post", entity, token);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("tag", response);
                addParams("photo", jsonObject.getJSONObject("data").getString("photo"));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            new AddInformation().execute();
        }
    }

    class AddInformation extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserInformation.this);
            pDialog.setMessage("Loading....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = apiHandler.httpMakeRequest(urlPersonInfo, param, "put", token);
            Log.d("tag", "response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (root.equalsIgnoreCase("signup")) {
                startActivity(new Intent(UserInformation.this, PesoActivity.class));
            }

            finish();
        }
    }

    public void addParams(String key, String value) {
        param.put(key, value);
    }

    // XLTC (START) most recently added: image cropping and validation

    // generic image cropping: works on image capture by the camera first
    private void cropImage(Uri imgUri) {

        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            imageFilePath = fileUri.getPath();

            cropIntent.setDataAndType(imgUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(cropIntent, IMAGE_CROP);

        } catch (ActivityNotFoundException e) {

            UtilsApp.toast("Your device does not support image cropping");

        }
    }

    private void previewCroppedImage(Intent data) {
        try {

            Bundle extras = data.getExtras();
            Bitmap imgPreview = extras.getParcelable("data");
            imgUser.setImageBitmap(imgPreview);

        } catch (NullPointerException e) {

            Log.d("Pese Sense", e.getLocalizedMessage());
            UtilsApp.toast("No image to be cropped");

        }
    }


    // XLTC (END)
}
