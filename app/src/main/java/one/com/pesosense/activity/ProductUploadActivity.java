package one.com.pesosense.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import one.com.pesosense.C;
import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.download.APIHandler;
import one.com.pesosense.download.AndroidMultiPartEntity;


public class ProductUploadActivity extends ActionBarActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 150;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_DIR_NAME = "Peso Sense";

    private Uri fileUri;
    private ImageView imgPreview;
    private Dialog dialog;
    private EditText prodName;
    private TextView lblProdName;
    private EditText prodQnty;
    private TextView lblProdQnty;
    private EditText prodDesc;
    private TextView lblProdDesc;
    private EditText prodBrand;
    private TextView lblProdBrand;
    private EditText prodPrice;
    private TextView lblProdPrice;
    private EditText prodAvail;
    private TextView lblProdAvail;

    private Boolean uploadImageIsEmpty;

    // for sending product to API
    // TODO: Seek for API endpoints and URL's
    private String imageFilePath;
    private String token;
    private APIHandler apiHandler;
    private Map<String, String> param;
    private String userId = "23";
    private String productId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_product_upload);

        param = new HashMap<String, String>();
        apiHandler = new APIHandler();
        UtilsApp.initSharedPreferences(getApplicationContext());
        token = UtilsApp.getString("access_token");

        Button btnUpload = (Button) findViewById(R.id.btn_upload);
        imgPreview = (ImageView) findViewById(R.id.upload_thumbnail);
        imgPreview.setPadding(72, 144, 72, 144);
        uploadImageIsEmpty = true;
        initFields();
        initLabels();
        initSpinners();

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPhotoDialog();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToAPI();
            }
        });


    }

    public void initFields() {
        prodName = (EditText) findViewById(R.id.prod_name);
        prodQnty = (EditText) findViewById(R.id.prod_qnty);
        prodDesc = (EditText) findViewById(R.id.prod_desc);
        prodBrand = (EditText) findViewById(R.id.prod_brand);
        prodPrice = (EditText) findViewById(R.id.prod_price);
        prodAvail = (EditText) findViewById(R.id.prod_avail);

        prodName.setTypeface(UtilsApp.opensansNormal());
        prodQnty.setTypeface(UtilsApp.opensansNormal());
        prodDesc.setTypeface(UtilsApp.opensansNormal());
        prodBrand.setTypeface(UtilsApp.opensansNormal());
        prodPrice.setTypeface(UtilsApp.opensansNormal());
        prodAvail.setTypeface(UtilsApp.opensansNormal());

        prodName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                validView(prodName);
            }
        });

        prodQnty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                validView(prodQnty);
            }
        });

        prodDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                validView(prodDesc);
            }
        });
    }

    private void initLabels() {
        lblProdName = (TextView) findViewById(R.id.lbl_prod_name);
        lblProdQnty = (TextView) findViewById(R.id.lbl_prod_qnty);
        lblProdDesc = (TextView) findViewById(R.id.lbl_prod_desc);
        lblProdBrand = (TextView) findViewById(R.id.lbl_prod_brand);
        lblProdPrice = (TextView) findViewById(R.id.lbl_prod_price);
        lblProdAvail = (TextView) findViewById(R.id.lbl_prod_avail);

        lblProdName.setTypeface(UtilsApp.opensansNormal());
        lblProdQnty.setTypeface(UtilsApp.opensansNormal());
        lblProdDesc.setTypeface(UtilsApp.opensansNormal());
        lblProdBrand.setTypeface(UtilsApp.opensansNormal());
        lblProdPrice.setTypeface(UtilsApp.opensansNormal());
        lblProdAvail.setTypeface(UtilsApp.opensansNormal());
    }

    public void initSpinners() {
        Spinner spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
        String[] currency = {"PHP", "USD"};
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(ProductUploadActivity.this, R.layout.my_spinner, currency);
        spinnerCurrency.setAdapter(adapterCurrency);

    }

    private void openAddPhotoDialog() {
        dialog = new Dialog(ProductUploadActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_photo);
        dialog.show();

        Button btnGallery = (Button) dialog.findViewById(R.id.btn_gallery);
        Button btnCamera = (Button) dialog.findViewById(R.id.btn_camera);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                dialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                dialog.dismiss();
            }
        });

    }

    private boolean isDeviceSupportCamera() {
        // this function is for checking camera availability through java code
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        imageFilePath = fileUri.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void pickImage() {
        Log.d("Peso Sense", "in picking Image");
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_IMAGE_REQUEST_CODE);//one can be replaced with any action code
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(ProductUploadActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                imageFilePath = getPickedImagePath(data);
                previewPickedImage(data);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void previewCapturedImage() {

        try {

            imgPreview.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            // database
            imgPreview.setImageBitmap(bitmap);
            imgPreview.setPadding(0, 0, 0, 0);
            uploadImageIsEmpty = false;

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }

    private String getPickedImagePath(Intent file) {
        Uri imageUri = file.getData();
        String[] imagePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(imageUri, imagePath, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(imagePath[0]));
    }

    private void previewPickedImage(Intent data) {

        try {

            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(pickedImage, filePath, null, null, null);

            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            final Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            imgPreview.setVisibility(View.VISIBLE);
            imgPreview.setImageBitmap(bitmap);
            imgPreview.setPadding(0, 0, 0, 0);
            uploadImageIsEmpty = false;

            cursor.close();

        } catch (NullPointerException e) {

            e.printStackTrace();

        }

    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
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
                    + "VID_" + timeStamp + ".3gp");
        } else {
            return null;
        }

        return mediaFile;
    }


    // API uploading part

    public void uploadToAPI() {
        String pName = prodName.getText().toString();
        String pQnty = prodQnty.getText().toString();
        String pDesc = prodDesc.getText().toString();
        String pBrand = prodBrand.getText().toString();
        String pPrice = prodPrice.getText().toString();
        String pAvail = prodAvail.getText().toString();

        if (uploadImageIsEmpty) {
            Toast.makeText(this, "Please provide thumbnail", Toast.LENGTH_SHORT).show();
            animateOnError(imgPreview);
        }

        // indicate errors
        if (isEmpty(pName)) {
            showError(prodName);
        } else if (isEmpty(pQnty)) {
            showError(prodQnty);
        } else if (isEmpty(pDesc)) {
            showError(prodDesc);
        } else if (isEmpty(pBrand)) {
            showError(prodBrand);
        } else if (isEmpty(pPrice)) {
            showError(prodPrice);
        } else if (isEmpty(pAvail)) {
            showError(prodAvail);
        }

        if (pName.length() != 0 && pQnty.length() != 0 && pDesc.length() != 0 && !uploadImageIsEmpty) {
            Toast.makeText(this, "Uploading product to API...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please provide enough information", Toast.LENGTH_SHORT).show();
        }

        addParams("name", pName);
        addParams("quantity", pQnty);
        addParams("description", pDesc);
        addParams("brand", pBrand);
        addParams("price", pPrice);
        //addParams("avail", pAvail);

        new CreateNewProduct().execute();
    }

    public boolean isEmpty(String s) {
        return s.length() == 0;
    }

    public void showError(TextView v) {
        v.setError("Field required");
        v.setBackgroundColor(0xFFFFEB8E);
    }

    public void validView(TextView v) {
        if (v.getText().toString().length() != 0) {
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.fields_bg));
        }
    }

    public void animateOnError(final View v) {
        AnimationSet errorAnimation = new AnimationSet(true);
        RotateAnimation anim2 = new RotateAnimation(10.0f, -10.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setRepeatCount(0);
        anim2.setDuration(100);
        anim2.setStartOffset(100);
        errorAnimation.addAnimation(anim2);
        v.startAnimation(errorAnimation);
    }

    /* MUST PUT ASYNC TASKS HERE
     * Process of uploading a new product/item:
     * 1. Create
     * 2. Update info
     * 3. Upload photo
     *
     * 3 different end points (see C.java)
     */

    // CREATE ITEM/PRODUCT

    // TODO: userid from the sharedpreferences upon log in

    class CreateNewProduct extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ProductUploadActivity.this);
            pDialog.setTitle("Uploading product");
            pDialog.setMessage("Please wait");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String requestUrl = C.URL_USER + "/" + userId + "/items";
            response = apiHandler.httpMakeRequest(requestUrl, param, "post", token);

            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("Peso Sense", String.valueOf(jsonObject.getJSONObject("data")));
                productId = jsonObject.getJSONObject("data").getString("id");
                addParams("item_id", productId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            // call next async tasks and add more necessary information
            new UpdateProduct().execute();
        }
    }

    // update product for the product id

    class UpdateProduct extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ProductUploadActivity.this);
            pDialog.setTitle("Uploading product");
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String requestUrl = C.URL_USER + "/" + userId + "/items/" + productId;
            response = apiHandler.httpMakeRequest(requestUrl, param, "put", token);

            try {
                JSONObject jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            // call async task for uploading the photo
            new UploadImage().execute();
        }
    }

    // task for uploading the profile image
    class UploadImage extends AsyncTask<Void, Integer, String> {

        ProgressDialog pDialog;
        String response;
        float totalSize;

        @Override
        protected void onPreExecute() {
            // initialize necessary values of the async tasks
            super.onPreExecute();

            pDialog = new ProgressDialog(ProductUploadActivity.this);
            pDialog.setTitle("Uploading Product Image");
            pDialog.setMax(100);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            File sourceFile = new File(imageFilePath);
            String requestUrl = C.URL_API + "/items/" + productId + "/photos";

            try {

                entity.addPart("file", new FileBody(sourceFile));
                entity.addPart("Content-Type", new StringBody("x-www-form-urlencoded"));
                totalSize = entity.getContentLength();

                response = apiHandler.httpMakeRequest(requestUrl, "post", entity, token);
                JSONObject jsonObject = new JSONObject(response);
                //addParams("Photo", jsonObject.getJSONObject("datA").getString("photo"));

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pDialog.setProgress(progress[0]);
            pDialog.setMessage("Uploading " + progress[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            // point to next activity or page
            startActivity(new Intent(ProductUploadActivity.this, PesoActivity.class));
            finish();
        }

    }

    public void addParams(String key, String value) {
        param.put(key, value);
    }

}
